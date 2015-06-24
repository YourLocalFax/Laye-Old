/**
 * Copyright (C) 2015 Sekai Kyoretsuna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.fudev.laye.internal.compile.laye;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.FunctionBuilder;
import net.fudev.laye.internal.lexical.Operator;
import net.fudev.laye.internal.values.LayeFloat;
import net.fudev.laye.internal.values.LayeInt;
import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeValue;
import net.fudev.laye.util.Logger;
import net.fudev.laye.util.Util;

public final class LayeFunctionBuilder implements FunctionBuilder
{
   private final class Scope
   {
      public final Scope previous;
      
      public int initialLocalsSize;
      
      // TODO use numUpValues
      // public int numUpValues;
      
      public Scope(final Scope previous)
      {
         this.previous = previous;
         // this.numUpValues = FunctionBuilder.this.getNumUpVals();
         initialLocalsSize = getLocalsSize();
      }
   }
   
   private final class Block
   {
      public final Block previous;
      
      public int startPosition;
      
      public Block(final Block previous)
      {
         this.previous = previous;
         startPosition = getCurrentPos();
      }
   }
   
   private static final boolean LINE_INFOS = true;
   private static final boolean DEBUG_STACK_SIZE = false;
   
   private final List<LayeValue> k = new Vector<>();
   private final List<Map<LayeValue, Integer>> jumpTables = new Vector<>();
   private final Stack<Integer> code = new Stack<>();
   private final List<FunctionPrototype> nested = new Vector<>();
   
   /** Variables referenced that are outside of this function. */
   private final List<UpValueInfo> upvals = new Vector<>();
   /** Variables declared inside this function. */
   private final List<LocalValueInfo> locals = new Vector<>();
   
   // TODO include line info, we haven't implemented those yet...
   // TODO also probably include file names somewhere, as well
   private final Stack<Integer> lineInfo = new Stack<>();
   
   private int numParams = 0;
   
   private boolean hasVargs = false;
   
   private int localsSize = 0;
   private int maxLocalsSize = 0;
   
   private int stackSize = 0;
   private int maxStackSize = 0;
   
   private int numUpVals = 0;
   
   public boolean generator = false;
   
   private Scope scope = null;
   private Block block = null;
   
   public final LayeFunctionBuilder parent;
   
   public LayeFunctionBuilder(final LayeFunctionBuilder parent)
   {
      this.parent = parent;
   }
   
   public void startScope()
   {
      scope = new Scope(scope);
   }
   
   public void endScope()
   {
      final int oldOuters = getNumUpVals();
      if (getLocalsSize() != scope.initialLocalsSize)
      {
         setLocalsSize(scope.initialLocalsSize);
         if (oldOuters != getNumUpVals())
         {
            visitOpClose(scope.initialLocalsSize);
         }
      }
      scope = scope.previous;
   }
   
   public void startBlock()
   {
      block = new Block(block);
   }
   
   public void endBlock()
   {
      final int endPosition = getCurrentPos();
      for (int i = block.startPosition + 1; i < endPosition; i++)
      {
         final int c = code.get(i);
         if (Laye.GET_OP(c) == Laye.TEMP_OP_RETURN)
         {
            final boolean isResultRequired = Laye.GET_B(c) == 1;
            code.set(i, Laye.SET_A(Laye.SET_B(Laye.OP_RETURN, isResultRequired ? 1 : 0), endPosition - i));
         }
      }
      block = block.previous;
   }
   
   public FunctionPrototype build()
   {
      // TODO what do we do with FileData?? Because we preprocess, everything is
      // one string...
      // final LayeFileData file = LayeFileData.UNKNOWN;
      final LayeValue[] k = this.k.toArray(new LayeValue[this.k.size()]);
      // @SuppressWarnings("unchecked")
      // final HashMap<LayeValue, Integer>[] jumpTables =
      // this.jumpTables.toArray(new HashMap<LayeValue, Integer>[0]);
      final int[] code = Util.toIntArray(this.code); // convertTempOps();
      final FunctionPrototype[] nested = this.nested.toArray(new FunctionPrototype[this.nested.size()]);
      final UpValueInfo[] upValues = upvals.toArray(new UpValueInfo[upvals.size()]);
      final int[] lineInfo = Util.toIntArray(this.lineInfo);
      return new FunctionPrototype(k, jumpTables, code, nested, upValues, lineInfo, LayeFunctionBuilder.LINE_INFOS,
            numParams, hasVargs, maxLocalsSize, maxStackSize, generator);
   }
   
   public void setHasVargs()
   {
      hasVargs = true;
   }
   
   public boolean hasVargs()
   {
      return hasVargs;
   }
   
   /*
    * private int[] convertTempOps() { final Stack<Integer> newCode = new Stack<>(); for (final int op : code) {
    * newCode.push(op); } return Util.toIntArray(newCode); }
    */
   
   public int addNestedFunction(final LayeFunctionBuilder builder)
   {
      nested.add(builder.build());
      return nested.size() - 1;
   }
   
   public int addParam(final String name, final boolean isConst)
   {
      numParams++;
      return allocLocalVar(name, isConst);
   }
   
   // Locals and UpValueInfos
   
   private int allocLocalVar(final String name, final boolean isConst)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("name cannot be null");
      }
      for (final LocalValueInfo var : locals)
      {
         if (var.name.equals(name))
         {
            return -1;
         }
      }
      final int pos = locals.size();
      locals.add(new LocalValueInfo(name, pos, isConst));
      if ((localsSize = pos + 1) > maxLocalsSize)
      {
         if ((maxLocalsSize = localsSize) > Laye.MAX_STACK_SIZE)
         {
            throw new IllegalStateException("compiler error: too many local variables");
         }
      }
      return pos;
   }
   
   public void changeStackSize(final int amt)
   {
      Logger.debug("BUILD", "stackSize " + (amt < 0 ? "" + amt : "+" + amt));
      stackSize += amt;
      if (LayeFunctionBuilder.DEBUG_STACK_SIZE)
      {
         System.out.println("@line " + new Exception().getStackTrace()[1].getLineNumber() + " alloc:" + stackSize + "/"
               + maxStackSize);
      }
      if (stackSize > maxStackSize)
      {
         if ((maxStackSize = stackSize) > Laye.MAX_STACK_SIZE)
         {
            throw new IllegalStateException("compiler error: too many stack slots");
         }
      }
      else if (stackSize < 0)
      {
         throw new IllegalStateException("stackSize cannot be negative");
      }
   }
   
   public void increaseStackSize()
   {
      changeStackSize(1);
   }
   
   public void decreaseStackSize()
   {
      changeStackSize(-1);
   }
   
   public int addLocal(final String name, final boolean isConst)
   {
      final int local = allocLocalVar(name, isConst);
      if (local == -1)
      {
         throw new IllegalArgumentException("local variable '" + name + "' already defined in function."); // TODO
         // different
         // exception
      }
      return local;
   }
   
   public int getLocal(final String name)
   {
      for (final LocalValueInfo var : locals)
      {
         if (var.name.equals(name))
         {
            return var.location;
         }
      }
      return -1;
   }
   
   public String getLocalName(final int local)
   {
      for (final LocalValueInfo var : locals)
      {
         if (var.location == local)
         {
            return var.name;
         }
      }
      return "<not found>";
   }
   
   public boolean isLocalConst(final int local)
   {
      for (final LocalValueInfo var : locals)
      {
         if (var.location == local)
         {
            return var.isConst;
         }
      }
      return false;
   }
   
   public int getUpValue(final String name)
   {
      final int outerSize = upvals.size();
      for (int i = 0; i < outerSize; i++)
      {
         if (upvals.get(i).name.equals(name))
         {
            return i;
         }
      }
      int pos = -1;
      if (parent != null)
      {
         pos = parent.getLocal(name);
         if (pos == -1)
         {
            pos = parent.getUpValue(name);
            if (pos != -1)
            {
               upvals.add(new UpValueInfo(name, pos, UpValueInfo.UP_VALUE, parent.isUpValueConst(pos)));
               return upvals.size() - 1;
            }
         }
         else
         {
            parent.markLocalAsUpValue(pos);
            upvals.add(new UpValueInfo(name, pos, UpValueInfo.LOCAL, parent.isLocalConst(pos)));
            return upvals.size() - 1;
         }
      }
      return -1;
   }
   
   public String getUpValueName(final int outer)
   {
      for (final UpValueInfo var : upvals)
      {
         if (var.pos == outer)
         {
            return var.name;
         }
      }
      return "<not found>";
   }
   
   public boolean isUpValueConst(final int outer)
   {
      for (final UpValueInfo var : upvals)
      {
         if (var.pos == outer)
         {
            return var.isConst;
         }
      }
      int pos = -1;
      if (parent != null)
      {
         final String name = getUpValueName(outer);
         pos = parent.getLocal(name);
         if (pos == -1)
         {
            pos = parent.getUpValue(name);
            if (pos != -1)
            {
               return parent.isUpValueConst(pos);
            }
         }
         else
         {
            return parent.isLocalConst(pos);
         }
      }
      return false;
   }
   
   public void markLocalAsUpValue(final int local)
   {
      locals.get(local).endOp = LocalValueInfo.IS_UP_VALUE;
      numUpVals++;
   }
   
   public int getLocalsSize()
   {
      return locals.size();
   }
   
   public void setLocalsSize(final int n)
   {
      int size = locals.size();
      while (size > n)
      {
         size--;
         final LocalValueInfo var = locals.remove(size);
         if (var.endOp == LocalValueInfo.IS_UP_VALUE)
         {
            numUpVals--;
         }
         var.endOp = getCurrentPos();
         // TODO ? locals.add(var);
      }
   }
   
   public int getNumUpVals()
   {
      return numUpVals;
   }
   
   public int getStackSize()
   {
      return stackSize;
   }
   
   // Instruction Helpers
   
   private void addOp(final int op)
   {
      code.add(op);
   }
   
   private void addOp_A(final int op, final int a)
   {
      code.add(Laye.SET_A(op, a));
   }
   
   private void addOp_SA(final int op, final int a)
   {
      code.add(Laye.SET_SA(op, a));
   }
   
   private void addOp_B(final int op, final int b)
   {
      code.add(Laye.SET_B(op, b));
   }
   
   /*
    * private void addOp_SB(final int op, final int b) { code.add(Laye.SET_SB(op, b)); }
    */
   
   private void addOp_AB(final int op, final int a, final int b)
   {
      code.add(Laye.SET_B(Laye.SET_A(op, a), b));
   }
   
   private void addOp_SAB(final int op, final int a, final int b)
   {
      code.add(Laye.SET_B(Laye.SET_SA(op, a), b));
   }
   
   /*
    * private void addOp_ASB(final int op, final int a, final int b) { code.add(Laye.SET_SB(Laye.SET_A(op, a), b)); }
    * 
    * private void addOp_SASB(final int op, final int a, final int b) { code.add(Laye.SET_SB(Laye.SET_SA(op, a), b)); }
    * 
    * private void addOp_C(final int op, final int c) { code.add(Laye.SET_C(op, c)); }
    * 
    * private void addOp_SC(final int op, final int c) { code.add(Laye.SET_SC(op, c)); }
    */
   
   public void setOp_SA(final int idx, final int sa)
   {
      code.set(idx, Laye.SET_SA(code.get(idx), sa));
   }
   
   public void setOp_AB(final int idx, final int a, final int b)
   {
      code.set(idx, Laye.SET_A(Laye.SET_B(code.get(idx), b), a));
   }
   
   public int addConst(final LayeValue c)
   {
      int idx = k.indexOf(c);
      if (idx == -1)
      {
         idx = k.size();
         k.add(c);
      }
      return idx;
   }
   
   public int addConsti(final long i)
   {
      return addConst(LayeInt.valueOf(i));
   }
   
   public int addConstf(final double f)
   {
      return addConst(LayeFloat.valueOf(f));
   }
   
   public int addConsts(final String s)
   {
      return addConst(LayeString.valueOf(s));
   }
   
   public int addMatchJumpTable(final Map<LayeValue, Integer> jumpTable)
   {
      jumpTables.add(jumpTable);
      return jumpTables.size() - 1;
   }
   
   // Instruction Visiting
   
   public int getCurrentPos()
   {
      return code.size() - 1;
   }
   
   public int previous()
   {
      return code.get(code.size() - 1);
   }
   
   public void popOp()
   {
      code.pop();
   }
   
   public void returnFromBlock(final boolean isResultRequired)
   {
      addOp_B(Laye.TEMP_OP_RETURN, isResultRequired ? 1 : 0);
   }
   
   public void visitLoadNull()
   {
      visitOpLoadNull(1);
   }
   
   public void visitLoadBool(final boolean b)
   {
      visitOpLoadBool(b);
   }
   
   public void visitLoadInt(final long i)
   {
      final int k = addConsti(i);
      visitOpLoadConst(k);
   }
   
   public void visitLoadFloat(final double f)
   {
      final int k = addConstf(f);
      visitOpLoadConst(k);
   }
   
   public void visitLoadString(final String s)
   {
      final int k = addConsts(s);
      visitOpLoadConst(k);
   }
   
   public void visitLoadFn(final FunctionPrototype fn)
   {
      final int idx = nested.size();
      nested.add(fn);
      visitOpFn(idx);
   }
   
   public void visitPrefixExpr(final Operator operator)
   {
      if (operator.image.endsWith("="))
      {
         throw new CompilerException("operators ending in '=' are reserved for assignment expressions.");
      }
      switch (operator.image)
      {
         case "+":
            visitOpPosit();
            break;
         case "-":
            visitOpNegate();
            break;
         case "~":
            visitOpCompl();
            break;
         default:
            final int operatork = addConsts(operator.image);
            visitOpPrefix(operatork);
      }
   }
   
   public void visitPostfixExpr(final Operator operator)
   {
      if (operator.image.endsWith("="))
      {
         throw new CompilerException("operators ending in '=' are reserved for assignment expressions.");
      }
      final int operatork = addConsts(operator.image);
      visitOpPostfix(operatork);
   }
   
   public void visitInfixExpr(final Operator operator)
   {
      if (operator.isAssignment())
      {
         throw new CompilerException("operators ending in '=' are reserved for assignment expressions.");
      }
      switch (operator.image)
      {
         case "+":
            visitOpAdd();
            break;
         case "-":
            visitOpSub();
            break;
         case "*":
            visitOpMul();
            break;
         case "/":
            visitOpDiv();
            break;
         case "//":
            visitOpIDiv();
            break;
         case "%":
            visitOpRem();
            break;
         case "^":
            visitOpPow();
            break;
         case "&":
            visitOpAnd();
            break;
         case "|":
            visitOpOr();
            break;
         case "~":
            visitOpXor();
            break;
         case "<<":
            visitOpLsh();
            break;
         case ">>":
            visitOpRsh();
            break;
         case ">>>":
            visitOpUrsh();
            break;
         case "<>":
            visitOpConcat();
            break;
         case "==":
            visitOpEq();
            break;
         case "!=":
            visitOpEq();
            visitOpNot();
            break;
         case "<":
            visitOpLt();
            break;
         case "<=":
            visitOpLe();
            break;
         case ">":
            visitOpLe();
            visitOpNot();
            break;
         case ">=":
            visitOpLt();
            visitOpNot();
            break;
         case "<=>":
            visitOp3Comp();
            break;
         default:
            final int operatork = addConsts(operator.image);
            visitOpInfix(operatork);
      }
   }
   
   public void visitRefLocalVar(final int local)
   {
      visitOpRef(local, 0);
   }
   
   public void visitRefUpValue(final int up)
   {
      visitOpRef(up, 1);
   }
   
   public void visitRefIndex()
   {
      visitOpRef(0, 2);
   }
   
   public void visitGetVariable(final String name)
   {
      // position
      int pos;
      // FIRST check if this is a local variable
      if ((pos = getLocal(name)) != -1)
      {
         visitOpLoad(pos);
      }
      else if ((pos = getUpValue(name)) != -1)
      {
         visitOpGetUp(pos);
      }
      else
      {
         visitOpLoadRoot();
         visitLoadString(name);
         visitOpGetIndex();
      }
   }
   
   // Raw Instruction Visiting (THESE HANDLE ALL STACK ALLOCATIONS I THINK)
   
   public void visitOpClose(final int targetSize)
   {
      Logger.debug("BUILD", "CLOSE");
      addOp_A(Laye.OP_CLOSE, targetSize);
   }
   
   public void visitOpPop(final int amount)
   {
      Logger.debug("BUILD", "POP");
      changeStackSize(-amount);
      addOp_A(Laye.OP_POP, amount);
   }
   
   public void visitOpDup(final int position)
   {
      Logger.debug("BUILD", "DUP");
      increaseStackSize();
      addOp_A(Laye.OP_DUP, position);
   }
   
   public void visitOpLoadRoot()
   {
      Logger.debug("BUILD", "LOAD_ROOT");
      increaseStackSize();
      addOp(Laye.OP_LOAD_ROOT);
   }
   
   public void visitOpLoad(final int local)
   {
      Logger.debug("BUILD", "LOAD");
      increaseStackSize();
      addOp_A(Laye.OP_LOAD, local);
   }
   
   public void visitOpStore(final int local)
   {
      Logger.debug("BUILD", "STORE");
      addOp_A(Laye.OP_STORE, local);
   }
   
   public void visitOpNewSlot(final boolean isConst)
   {
      Logger.debug("BUILD", "NEW_SLOT");
      changeStackSize(-2);
      addOp_A(Laye.OP_NEW_SLOT, isConst ? 1 : 0);
   }
   
   public void visitOpDelSlot()
   {
      Logger.debug("BUILD", "DEL_SLOT");
      changeStackSize(-1);
      addOp(Laye.OP_DEL_SLOT);
   }
   
   public void visitOpGetUp(final int upval)
   {
      Logger.debug("BUILD", "GET_UP");
      increaseStackSize();
      addOp_A(Laye.OP_GET_UP, upval);
   }
   
   public void visitOpSetUp(final int upval)
   {
      Logger.debug("BUILD", "SET_UP");
      addOp_A(Laye.OP_SET_UP, upval);
   }
   
   public void visitOpGetIndex()
   {
      Logger.debug("BUILD", "GET_INDEX");
      decreaseStackSize();
      addOp(Laye.OP_GET_INDEX);
   }
   
   public void visitOpSetIndex()
   {
      Logger.debug("BUILD", "SET_INDEX");
      changeStackSize(-2);
      addOp(Laye.OP_SET_INDEX);
   }
   
   public void visitOpLoadConst(final int k)
   {
      Logger.debug("BUILD", "LOAD_CONST");
      increaseStackSize();
      addOp_A(Laye.OP_LOAD_CONST, k);
   }
   
   public void visitOpLoadBool(final boolean value)
   {
      Logger.debug("BUILD", "LOAD_BOOL");
      increaseStackSize();
      addOp_A(Laye.OP_LOAD_BOOL, value ? 1 : 0);
   }
   
   public void visitOpLoadNull(final int amount)
   {
      Logger.debug("BUILD", "LOAD_NULL");
      increaseStackSize();
      addOp_A(Laye.OP_LOAD_NULL, amount);
   }
   
   public void visitOpFn(final int id)
   {
      Logger.debug("BUILD", "FN");
      increaseStackSize();
      addOp_A(Laye.OP_FUNCTION, id);
   }
   
   public void visitOpType()
   {
      Logger.debug("BUILD", "TYPE");
      // TODO visit OP_TYPE
      addOp(Laye.OP_TYPE);
      throw new LayeException("todo plz");
   }
   
   public void visitOpPosit()
   {
      Logger.debug("BUILD", "POSIT");
      addOp(Laye.OP_POSIT);
   }
   
   public void visitOpNegate()
   {
      Logger.debug("BUILD", "NEGATE");
      addOp(Laye.OP_NEGATE);
   }
   
   public void visitOpNot()
   {
      Logger.debug("BUILD", "NOT");
      addOp(Laye.OP_NOT);
   }
   
   public void visitOpCompl()
   {
      Logger.debug("BUILD", "COMPL");
      addOp(Laye.OP_COMPL);
   }
   
   public void visitOpTypeof()
   {
      Logger.debug("BUILD", "TYPEOF");
      addOp(Laye.OP_TYPEOF);
   }
   
   public void visitOpPrefix(final int operatork)
   {
      Logger.debug("BUILD", "PREFIX");
      addOp_A(Laye.OP_PREFIX, operatork);
   }
   
   public void visitOpPostfix(final int operatork)
   {
      Logger.debug("BUILD", "POSTFIX");
      addOp_A(Laye.OP_POSTFIX, operatork);
   }
   
   public void visitOpAdd()
   {
      Logger.debug("BUILD", "ADD");
      decreaseStackSize();
      addOp(Laye.OP_ADD);
   }
   
   public void visitOpSub()
   {
      Logger.debug("BUILD", "SUB");
      decreaseStackSize();
      addOp(Laye.OP_SUB);
   }
   
   public void visitOpMul()
   {
      Logger.debug("BUILD", "MUL");
      decreaseStackSize();
      addOp(Laye.OP_MUL);
   }
   
   public void visitOpDiv()
   {
      Logger.debug("BUILD", "DIV");
      decreaseStackSize();
      addOp(Laye.OP_DIV);
   }
   
   public void visitOpIDiv()
   {
      Logger.debug("BUILD", "IDIV");
      decreaseStackSize();
      addOp(Laye.OP_IDIV);
   }
   
   public void visitOpRem()
   {
      Logger.debug("BUILD", "REM");
      decreaseStackSize();
      addOp(Laye.OP_REM);
   }
   
   public void visitOpPow()
   {
      Logger.debug("BUILD", "POW");
      decreaseStackSize();
      addOp(Laye.OP_POW);
   }
   
   public void visitOpAnd()
   {
      Logger.debug("BUILD", "AND");
      decreaseStackSize();
      addOp(Laye.OP_AND);
   }
   
   public void visitOpOr()
   {
      Logger.debug("BUILD", "OR");
      decreaseStackSize();
      addOp(Laye.OP_OR);
   }
   
   public void visitOpXor()
   {
      Logger.debug("BUILD", "XOR");
      decreaseStackSize();
      addOp(Laye.OP_XOR);
   }
   
   public void visitOpLsh()
   {
      Logger.debug("BUILD", "LSH");
      decreaseStackSize();
      addOp(Laye.OP_LSH);
   }
   
   public void visitOpRsh()
   {
      Logger.debug("BUILD", "RSH");
      decreaseStackSize();
      addOp(Laye.OP_RSH);
   }
   
   public void visitOpUrsh()
   {
      Logger.debug("BUILD", "URSH");
      decreaseStackSize();
      addOp(Laye.OP_URSH);
   }
   
   public void visitOpConcat()
   {
      Logger.debug("BUILD", "CONCAT");
      decreaseStackSize();
      addOp(Laye.OP_CONCAT);
   }
   
   public void visitOpInfix(final int operatork)
   {
      Logger.debug("BUILD", "INFIX");
      decreaseStackSize();
      addOp_A(Laye.OP_INFIX, operatork);
   }
   
   public void visitOpBoolAnd(final int jump)
   {
      Logger.debug("BUILD", "BOOL_AND");
      addOp_A(Laye.OP_BOOL_AND, jump);
   }
   
   public void visitOpBoolOr(final int jump)
   {
      Logger.debug("BUILD", "BOOL_OR");
      addOp_A(Laye.OP_BOOL_OR, jump);
   }
   
   public void visitOpBoolXor()
   {
      Logger.debug("BUILD", "BOOL_XOR");
      decreaseStackSize();
      addOp(Laye.OP_BOOL_XOR);
   }
   
   public void visitOpBoolNand(final int jump)
   {
      Logger.debug("BUILD", "BOOL_NAND");
      visitOpBoolAnd(jump);
      visitOpNot();
   }
   
   public void visitOpBoolNor(final int jump)
   {
      Logger.debug("BUILD", "BOOL_NOR");
      visitOpBoolOr(jump);
      visitOpNot();
   }
   
   public void visitOpBoolXnor()
   {
      Logger.debug("BUILD", "BOOL_XNOR");
      visitOpBoolXor();
      visitOpNot();
   }
   
   public void visitOpIsTypeof()
   {
      Logger.debug("BUILD", "IS_TYPEOF");
      decreaseStackSize();
      addOp(Laye.OP_IS_TYPEOF);
   }
   
   public void visitOpEq()
   {
      Logger.debug("BUILD", "EQ");
      decreaseStackSize();
      addOp(Laye.OP_EQ);
   }
   
   public void visitOpLt()
   {
      Logger.debug("BUILD", "LT");
      decreaseStackSize();
      addOp(Laye.OP_LT);
   }
   
   public void visitOpLe()
   {
      Logger.debug("BUILD", "LE");
      decreaseStackSize();
      addOp(Laye.OP_LE);
   }
   
   public void visitOpNeq()
   {
      Logger.debug("BUILD", "NEQ");
      visitOpEq();
      visitOpNot();
   }
   
   public void visitOpGt()
   {
      Logger.debug("BUILD", "GT");
      visitOpLe();
      visitOpNot();
   }
   
   public void visitOpGe()
   {
      Logger.debug("BUILD", "GE");
      visitOpLt();
      visitOpNot();
   }
   
   public void visitOp3Comp()
   {
      Logger.debug("BUILD", "3COMP");
      decreaseStackSize();
      addOp(Laye.OP_3COMP);
   }
   
   public int visitOpTest(final int jump, final boolean expected)
   {
      Logger.debug("BUILD", "TEST");
      addOp_SAB(Laye.OP_TEST, jump, expected ? 1 : 0);
      return getCurrentPos();
   }
   
   public int visitOpJump(final int jump)
   {
      Logger.debug("BUILD", "JUMP");
      addOp_SA(Laye.OP_JUMP, jump);
      return getCurrentPos();
   }
   
   public void visitOpCall(final int nargs)
   {
      Logger.debug("BUILD", "CALL");
      changeStackSize(-nargs);
      addOp_A(Laye.OP_CALL, nargs);
   }
   
   public void visitOpMethod(final int nargs)
   {
      Logger.debug("BUILD", "METHOD");
      changeStackSize(-nargs - 1);
      addOp_A(Laye.OP_METHOD, nargs);
   }
   
   public void visitOpHalt(final boolean hasValue)
   {
      Logger.debug("BUILD", "HALT");
      addOp_A(Laye.OP_HALT, hasValue ? 1 : 0);
   }
   
   public void visitOpYield()
   {
      Logger.debug("BUILD", "YIELD");
      addOp(Laye.OP_YIELD);
   }
   
   public void visitOpResume()
   {
      Logger.debug("BUILD", "RESUME");
      decreaseStackSize();
      addOp(Laye.OP_RESUME);
   }
   
   public void visitOpThis()
   {
      Logger.debug("BUILD", "THIS");
      increaseStackSize();
      addOp(Laye.OP_THIS);
   }
   
   public void visitOpBase()
   {
      Logger.debug("BUILD", "BASE");
      increaseStackSize();
      addOp(Laye.OP_BASE);
   }
   
   public void visitOpRef(final int location, final int type)
   {
      Logger.debug("BUILD", "REF");
      addOp_AB(Laye.OP_REF, location, type);
   }
   
   public void visitOpDeref()
   {
      Logger.debug("BUILD", "DEREF");
      addOp(Laye.OP_DEREF);
   }
   
   public void visitOpIs()
   {
      Logger.debug("BUILD", "IS");
      decreaseStackSize();
      addOp(Laye.OP_IS);
   }
   
   public void visitOpList(final int amount)
   {
      Logger.debug("BUILD", "LIST");
      changeStackSize(1 - amount);
      addOp_A(Laye.OP_LIST, amount);
   }
   
   public void visitOpTuple(final int amount)
   {
      Logger.debug("BUILD", "TUPLE");
      changeStackSize(1 - amount);
      addOp_A(Laye.OP_TUPLE, amount);
   }
   
   public void visitOpNewTable()
   {
      Logger.debug("BUILD", "NEW_TABLE");
      increaseStackSize();
      addOp(Laye.OP_NEW_TABLE);
   }
   
   public void visitOpNewInstance(final int nargs, int nameid)
   {
      Logger.debug("BUILD", "NEW_INSTANCE");
      if (nameid >= Laye.MAX_B)
      {
         throw new CompilerException("const index out of bounds: " + nameid);
      }
      if (nameid == -1)
      {
         nameid = Laye.MAX_B;
      }
      addOp_AB(Laye.OP_NEW_INSTANCE, nargs, nameid);
   }
   
   public void visitOpForPrep(final boolean hasStep, final int localStart)
   {
      Logger.debug("BUILD", "FOR_PREP");
      addOp_AB(Laye.OP_FOR_PREP, hasStep ? 1 : 0, localStart);
   }
   
   public int visitOpForTest(final int jump, final int localStart)
   {
      Logger.debug("BUILD", "FOR_TEST");
      addOp_SAB(Laye.OP_FOR_TEST, jump, localStart);
      return getCurrentPos();
   }
   
   public int visitOpMatch(final int map, final int defaultJump)
   {
      Logger.debug("BUILD", "MATCH");
      addOp_AB(Laye.OP_MATCH, defaultJump, map);
      return getCurrentPos();
   }
}
