package net.fudev.laye.internal.values;

import java.util.Arrays;
import java.util.Map;

import net.fudev.laye.err.LayeRuntimeException;
import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.InsnPrint;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.UpValue;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.compile.laye.UpValueInfo;
import net.fudev.laye.util.Logger;
import net.fudev.laye.util.Util;

public final class LayeFunction extends LayeValue
{
   private static final boolean DO_DEBUG = false;
   
   private final LayeString stringValue;
   
   private FunctionPrototype proto = null;
   private Root root = null;
   
   private UpValue[] ups = null;
   
   private final String insnFormat;
   private int pc = 0;
   
   public LayeFunction(final FunctionPrototype proto, final Root root)
   {
      super(ValueType.FUNCTION);
      this.proto = proto;
      this.root = root;
      ups = new UpValue[proto.maxStackSize];
      stringValue = LayeString.valueOf("func:" + hashCode());
      insnFormat = "%" + Integer.toString(proto.code.length - 1).length()
            + "d <%S>";
   }
   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((insnFormat == null) ? 0 : insnFormat.hashCode());
      result = prime * result + pc;
      result = prime * result + ((proto == null) ? 0 : proto.hashCode());
      result = prime * result + ((root == null) ? 0 : root.hashCode());
      result = prime * result
            + ((stringValue == null) ? 0 : stringValue.hashCode());
      result = prime * result + Arrays.hashCode(ups);
      return result;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue obj)
   {
      if (getClass() != obj.getClass())
      {
         return false;
      }
      
      final LayeFunction other = (LayeFunction) obj;
      
      if (proto == null)
      {
         if (other.proto != null)
         {
            return false;
         }
      }
      else if (!proto.equals(other.proto))
      {
         return false;
      }
      
      return true;
   }
   
   public @Override LayeString tostring()
   {
      return stringValue;
   }
   
   @Override
   public String asstring()
   {
      return stringValue.value;
   }
   
   @Override
   public LayeValue call(final LayeValue... args)
   {
      return callAsMethod(LayeValue.NULL, args);
   }
   
   @Override
   public LayeValue callAsMethod(final LayeValue parent,
         final LayeValue... args)
   {
      if (LayeFunction.DO_DEBUG)
      {
         Logger.debug("EXE", "calling function...");
      }
      pc = 0;
      final LayeValue[] locals = Util
            .createValueArrayLocals(proto.maxLocalsSize, proto.hasVargs, args);
      final LayeValue[] stack = Util.createValueArray(proto.maxStackSize);
      return execute(parent, locals, stack);
   }
   
   // TODO move resume to a generator class
   /*
    * public LayeValue resume() { if (!proto.generator) { throw new
    * IllegalStateException("function is not a generator, cannot resume."); }
    * return execute(lastParent, lastLocals, lastStack); } //
    */
   
   // TODO refactor some of this to move the loop into a separate method for
   // convenience?
   private LayeValue execute(final LayeValue parent, final LayeValue[] locals,
         final LayeValue[] stack)
   {
      final LayeValue[] k = proto.k;
      final FunctionPrototype[] p = proto.nested;
      
      final UpValue[] openUps = p.length > 0 ? new UpValue[stack.length] : null;
      
      final int[] code = proto.code;
      // final int codeLength = code.length;
      
      int i, a, b, temp;
      int top = 0;
      
      // TODO used to determine yield vs. return?
      boolean returned = false;
      
      try
      {
         while (true)
         {
            // TODO compilation should handle this but JUST IN CASE?
            /*
             * if (pc >= code.length) { pc = 0; return LayeValue.NULL; } //
             */
            
            i = code[pc++];
            
            if (LayeFunction.DO_DEBUG)
            {
               Logger.debug("EXE", String.format(insnFormat, (pc - 1),
                     InsnPrint.getOpCodeName(i)));
               Logger.debug("STK", top + " " + Arrays.toString(stack));
               Logger.debug("LOC", Arrays.toString(locals));
            }
            
            switch (i & Laye.MAX_OP)
            {
               case Laye.OP_CLOSE: // [openUps.len() - A] CLOSE(A) for(i =
                  // #openUps; --i >= A;) openUps[i].close();
                  for (temp = openUps.length, a = i >>> Laye.POS_A; --temp >= a;)
                  {
                     if (openUps[temp] != null)
                     {
                        openUps[temp].close();
                        openUps[temp] = null;
                     }
                  }
                  continue;
                  
               case Laye.OP_POP: // [A, -A] POP(A) TOP -= A;
                  top -= (i >>> Laye.POS_A);
                  continue;
                  
               case Laye.OP_DUP: // [0, 1] DUP(A) STK[TOP++] = STK[TOP - A - 1];
                  stack[top++] = stack[top - (i >>> Laye.POS_A) - 1];
                  continue;
                  
               case Laye.OP_LOAD_ROOT: // [0, 1] LOAD_ROOT() STK[TOP++] = ROOT;
                  stack[top++] = root;
                  continue;
                  
               case Laye.OP_LOAD: // [0, 1] LOAD(A) STK[TOP++] = LOC[A];
                  stack[top++] = locals[i >>> Laye.POS_A];
                  continue;
                  
               case Laye.OP_STORE: // [1, 0] STORE(A) LOC[A] = STK[TOP - 1];
                  // This leaves the value on the top of the stack, don't forget
                  // that!
                  if (locals[a = i >>> Laye.POS_A].isref())
                  {
                     ((LayeReference) locals[a]).setValue(stack[top - 1]);
                  }
                  else
                  {
                     locals[a] = stack[top - 1];
                  }
                  continue;
                  
               case Laye.OP_NEW_SLOT: // [3, -2] NEW_SLOT(A) STK[TOP -
                  // 3].newSlot(STK[TOP - 2], STK[TOP - 1]);
                  // STK[TOP - 3] = STK[TOP - 1];
               {
                  LayeValue target = stack[top - 3];
                  // TODO keep auto-deref?
                  if (target.isref())
                  {
                     target = ((LayeReference) target).getValue();
                  }
                  if ((i >>> Laye.POS_A) != 0)
                  {
                     target.newSlot(stack[top - 2],
                           stack[top - 3] = stack[top - 1]);
                  }
                  else
                  {
                     target.newSlotMutable(stack[top - 2],
                           stack[top - 3] = stack[top - 1]);
                  }
                  top -= 2;
                  continue;
               }
               
               case Laye.OP_DEL_SLOT: // [2, -1] DEL_SLOT() STK[TOP - 2] =
                  // STK[TOP - 2].delSlot(STK[--TOP]);
               {
                  LayeValue target = stack[top - 2];
                  // TODO keep auto-deref?
                  if (target.isref())
                  {
                     target = ((LayeReference) target).getValue();
                  }
                  stack[top - 2] = target.delSlot(stack[--top]);
                  continue;
               }
               
               case Laye.OP_GET_UP: // [1] GET_UP(A) STK[TOP++] = UPS[A];
                  stack[top++] = ups[i >>> Laye.POS_A].getValue();
                  continue;
                  
               case Laye.OP_SET_UP: // [0] SET_UP(A) UPS[A] = STK[TOP - 1];
                  ups[i >>> Laye.POS_A].setValue(stack[top - 1]);
                  continue;
                  
               case Laye.OP_GET_INDEX: // [-1] GET_INDEX() STK[TOP - 2] =
                  // STK[TOP - 2].get(STK[TOP - 1])
                  stack[top - 2] = stack[top - 2].get(stack[--top]);
                  continue;
                  
               case Laye.OP_SET_INDEX: // [-2] SET_INDEX() STK[TOP -
                  // 3].set(STK[TOP - 2], STK[TOP - 1]);
                  // STK[TOP - 3] = STK[TOP - 1]; TOP -= 2;
                  stack[top - 3].set(stack[top - 2],
                        stack[top - 3] = stack[top - 1]);
                  top -= 2;
                  continue;
                  
               case Laye.OP_LOAD_CONST: // [1] LOAD_CONST(A) STK[TOP++] = K[A];
                  stack[top++] = k[i >>> Laye.POS_A];
                  continue;
                  
               case Laye.OP_LOAD_BOOL: // [1] LOAD_BOOL(A) STK[TOP++] = if B
                  // then true; else false;
                  stack[top++] = (i >>> Laye.POS_A) != 0 ? LayeValue.TRUE
                        : LayeValue.FALSE;
                  continue;
                  
               case Laye.OP_LOAD_NULL: // [A] LOAD_NULL(A) (for i = 1 to A)
                  // STK[TOP++] = NULL;
                  for (temp = 0, a = i >>> Laye.POS_A; temp < a; temp++)
                  {
                     stack[top++] = LayeValue.NULL;
                  }
                  continue;
                  
               case Laye.OP_FUNCTION: // [1] FUNC(A) STK[TOP++] = P[A];
               {
                  final FunctionPrototype proto = p[i >>> Laye.POS_A];
                  final LayeFunction func = new LayeFunction(proto, root);
                  final UpValueInfo[] protoUps = proto.upValues;
                  final int upCount = protoUps.length;
                  for (temp = 0; temp < upCount; temp++)
                  {
                     if (protoUps[temp].type == UpValueInfo.LOCAL)
                     {
                        func.ups[temp] = findUpValue(locals, protoUps[temp].pos,
                              openUps);
                     }
                     else
                     {
                        func.ups[temp] = ups[protoUps[temp].pos];
                     }
                  }
                  func.root = root;
                  stack[top++] = func;
                  continue;
               }
               
               case Laye.OP_TYPE:
               {
                  final LayeValue value = stack[top - 1];
                  final ValueType valueType = value.valueType;
                  switch (valueType)
                  {
                     case TYPE:
                        stack[top - 1] = LayeValue.NULL;
                        break;
                     case INSTANCE:
                        stack[top - 1] = ((LayeInstance) value).type;
                        break;
                     default:
                        stack[top - 1] = valueType.type;
                        break;
                  }
                  continue;
               }
               
               // ---------- Unary Operations ---------- //
               
               case Laye.OP_POSIT: // [0] POSIT() STK[TOP - 1] = STK[TOP -
                  // 1].posit();
                  stack[top - 1] = stack[top - 1].posit();
                  continue;
                  
               case Laye.OP_NEGATE: // [0] NEGATE() STK[TOP - 1] = STK[TOP -
                  // 1].negate();
                  stack[top - 1] = stack[top - 1].negate();
                  continue;
                  
               case Laye.OP_NOT: // [0] NOT() STK[TOP - 1] = STK[TOP - 1].not();
                  stack[top - 1] = stack[top - 1].not();
                  continue;
                  
               case Laye.OP_COMPL: // [0] COMPL() STK[TOP - 1] = STK[TOP -
                  // 1].complement();
                  stack[top - 1] = stack[top - 1].complement();
                  continue;
                  
               case Laye.OP_TYPEOF: // [0] TYPEOF() STK[TOP - 1] = STK[TOP -
                  // 1].typeof();
                  stack[top - 1] = stack[top - 1].typeof();
                  continue;
                  
               case Laye.OP_PREFIX: // [0] PREFIX(A) STK[TOP - 1] = STK[TOP -
                  // 1].unaryPrefixOp(k[A]);
                  stack[top - 1] = stack[top - 1]
                        .unaryPrefixOp(k[i >>> Laye.POS_A].asstring());
                  continue;
                  
               case Laye.OP_POSTFIX: // [0] POSTFIX(A) STK[TOP - 1] = STK[TOP -
                  // 1].unaryPostfixOp(k[A]);
                  stack[top - 1] = stack[top - 1]
                        .unaryPostfixOp(k[i >>> Laye.POS_A].asstring());
                  continue;
                  
                  // ---------- Binary Operations ---------- //
                  
               case Laye.OP_ADD: // [-1] ADD() STK[TOP - 2] = STK[TOP -
                  // 2].add(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].add(stack[--top]);
                  continue;
                  
               case Laye.OP_SUB: // [-1] SUB() STK[TOP - 2] = STK[TOP -
                  // 2].subtract(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].subtract(stack[--top]);
                  continue;
                  
               case Laye.OP_MUL: // [-1] MUL() STK[TOP - 2] = STK[TOP -
                  // 2].multiply(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].multiply(stack[--top]);
                  continue;
                  
               case Laye.OP_DIV: // [-1] DIV() STK[TOP - 2] = STK[TOP -
                  // 2].divide(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].divide(stack[--top]);
                  continue;
                  
               case Laye.OP_IDIV: // [-1] IDIV() STK[TOP - 2] = STK[TOP -
                  // 2].intDivide(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].intDivide(stack[--top]);
                  continue;
                  
               case Laye.OP_REM: // [-1] REM() STK[TOP - 2] = STK[TOP -
                  // 2].remainder(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].remainder(stack[--top]);
                  continue;
                  
               case Laye.OP_POW: // [-1] POW() STK[TOP - 2] = STK[TOP -
                  // 2].power(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].power(stack[--top]);
                  continue;
                  
               case Laye.OP_AND: // [-1] AND() STK[TOP - 2] = STK[TOP -
                  // 2].and(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].and(stack[--top]);
                  continue;
                  
               case Laye.OP_OR: // [-1] OR() STK[TOP - 2] = STK[TOP -
                  // 2].or(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].or(stack[--top]);
                  continue;
                  
               case Laye.OP_XOR: // [-1] XOR() STK[TOP - 2] = STK[TOP -
                  // 2].xor(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].xor(stack[--top]);
                  continue;
                  
               case Laye.OP_LSH: // [-1] LSH() STK[TOP - 2] = STK[TOP -
                  // 2].shiftLeft(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].shiftLeft(stack[--top]);
                  continue;
                  
               case Laye.OP_RSH: // [-1] RSH() STK[TOP - 2] = STK[TOP -
                  // 2].shiftRight(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].shiftRight(stack[--top]);
                  continue;
                  
               case Laye.OP_URSH: // [-1] URSH() STK[TOP - 2] = STK[TOP -
                  // 2].shiftRightUnsigned(STK[--TOP]);
                  stack[top - 2] = stack[top - 2]
                        .shiftRightUnsigned(stack[--top]);
                  continue;
                  
               case Laye.OP_CONCAT: // [-1] CONCAT() STK[TOP - 2] = STK[TOP -
                  // 2].concat(STK[--TOP]);
                  stack[top - 2] = stack[top - 2].concat(stack[--top]);
                  continue;
                  
               case Laye.OP_INFIX: // [-1] INFIX(A) STK[TOP - 2] = STK[TOP -
                  // 2].infixOp(K[A], STK[--TOP]);
                  stack[top - 2] = stack[top - 2]
                        .infixOp(k[i >>> Laye.POS_A].asstring(), stack[--top]);
                  continue;
                  
                  // ---------- Misc Binary Operators ---------- //
                  
               case Laye.OP_BOOL_AND: // [0]
                  if (stack[top - 1].not() == LayeValue.FALSE)
                  {
                     top--;
                  }
                  else
                  {
                     pc += i >>> Laye.POS_A;
                  }
                  continue;
                  
               case Laye.OP_BOOL_OR: // [0]
                  if (stack[top - 1].not() == LayeValue.TRUE)
                  {
                     top--;
                  }
                  else
                  {
                     pc += i >>> Laye.POS_A;
                  }
                  continue;
                  
               case Laye.OP_BOOL_XOR: // [-1]
                  stack[top - 2] = stack[top - 2].not() != stack[top - 1].not()
                  ? LayeValue.TRUE : LayeValue.FALSE;
                  continue;
                  
               case Laye.OP_IS_TYPEOF: // [-1]
                  stack[top - 2] = stack[top - 2].istypeof(stack[top - 1]);
                  continue;
                  
                  // ---------- Comparison ---------- //
                  
               case Laye.OP_EQ: // [-1]
                  stack[top - 2] = stack[top - 2].equalTo(stack[--top]);
                  continue;
                  
               case Laye.OP_LT: // [-1]
                  stack[top - 2] = stack[top - 2].lessThan(stack[--top]);
                  continue;
                  
               case Laye.OP_LE: // [-1]
                  stack[top - 2] = stack[top - 2].lessEqual(stack[--top]);
                  continue;
                  
               case Laye.OP_3COMP: // [-1]
                  stack[top - 2] = LayeInt
                  .valueOf(stack[top - 2].compareTo(stack[--top]));
                  continue;
                  
                  // ---------- Program Counter ---------- //
                  
               case Laye.OP_TEST: // [-1] TEST(SA, B)
                  if (stack[--top]
                        .asbool() != (((i >>> Laye.POS_B) & Laye.MAX_B) != 0))
                  {
                     pc += ((i >>> Laye.POS_A) & Laye.MAX_A) + Laye.MIN_SA;
                  }
                  continue;
                  
               case Laye.OP_JUMP: // [0] JUMP(SA)
                  pc += ((i >>> Laye.POS_A) & Laye.MAX_A) + Laye.MIN_SA;
                  continue;
                  
                  // ---------- Functions ---------- //
                  
               case Laye.OP_CALL: // []
               {
                  final LayeValue[] argList = Arrays.copyOfRange(stack,
                        top - (a = i >>> Laye.POS_A), top);
                  final LayeValue target = stack[top = top - a - 1];
                  
                  stack[top++] = target.call(argList);
                  
                  /*
                   * switch (target.valueType) { case FUNCTION: { if (target
                   * instanceof LayeFunction) { final LayeFunction func =
                   * ((LayeFunction) target); if (func.proto.generator) { throw
                   * new LayeRuntimeException("<unknown>", proto.hasLineInfos ?
                   * proto.lineInfo[pc] : 0,
                   * "cannot call a generator function. use 'resume' instead.");
                   * } stack[top++] = func.call(argList); } else if (target
                   * instanceof LayeJavaFunction) { stack[top++] =
                   * ((LayeJavaFunction) target).call(argList); } continue; }
                   * default: throw new IllegalArgumentException(
                   * "could not call type " + target.typeof().asstring()); }
                   */
                  
                  continue;
               }
               
               case Laye.OP_METHOD:
               {
                  final LayeValue[] argList = Arrays.copyOfRange(stack,
                        top - (a = i >>> Laye.POS_A), top);
                  final LayeValue key = stack[top = top - a - 1];
                  final LayeValue target = stack[top - 1];
                  stack[top - 1] = target.callChildMethod(key, argList);
                  continue;
               }
               
               case Laye.OP_HALT: // Halt is used to return the last value or
                  // null
                  returned = true;
                  pc = 0;
                  if ((i >>> Laye.POS_A) == 1)
                  {
                     return stack[top - 1];
                  }
                  return LayeValue.NULL;
                  
                  // TODO do things with yield
               case Laye.OP_YIELD:
                  if ((i >>> Laye.POS_A) == 1)
                  {
                     return stack[top - 1];
                  }
                  return LayeValue.NULL;
                  
               case Laye.OP_RESUME: // [-1]
               {
                  // TODO create generator type and use checkgenerator();
                  /*
                   * final LayeFunction gen = stack[--top].checkfunction();
                   * gen.resume();
                   */
                  continue;
               }
               
               case Laye.OP_THIS: // [1]
                  stack[top++] = parent;
                  continue;
                  
                  // Should ONLY be allowed for function calls, error on
                  // anything else
               case Laye.OP_BASE: // [1]
                  // TODO figure out if this works how we want it
                  stack[top++] = parent.checkinstance().type.base;
                  continue;
                  
               case Laye.OP_REF: // [0]
                  switch ((i >>> Laye.POS_B) & Laye.MAX_B)
                  {
                     case 0: // local
                        stack[top++] = new LayeLocalRef(locals,
                              i >>> Laye.POS_A);
                        continue;
                     case 1: // up-value
                        stack[top++] = new LayeUpValueRef(ups,
                              i >>> Laye.POS_A);
                        continue;
                     case 2: // indices
                        stack[top - 2] = new LayeIndexRef(stack[top - 2],
                              stack[--top]);
                        continue;
                     default:
                        throw new RuntimeException(
                              "Unexpected reference type.");
                  }
                  
               case Laye.OP_DEREF: // [0]
                  if (!stack[top - 1].isref())
                  {
                     throw new LayeRuntimeException("<unknown>",
                           proto.hasLineInfos ? proto.lineInfo[pc] : 0,
                           "Can only dereference references to variables.");
                  }
                  stack[top - 1] = ((LayeReference) stack[top - 1]).getValue();
                  continue;
                  
               case Laye.OP_IS: // [-1]
                  stack[top - 2] = stack[top - 2] == stack[top - 1]
                        ? LayeValue.TRUE : LayeValue.FALSE;
                  continue;
                  
               case Laye.OP_LIST: // [1 - A] LIST(A)
                  if ((a = i >>> Laye.POS_A) == 0)
                  {
                     stack[top++] = LayeList.create(0);
                  }
                  else
                  {
                     final int to = top;
                     stack[(top -= a - 1) - 1] = LayeList
                           .valueOf(Arrays.copyOfRange(stack, top - 1, to));
                  }
                  continue;
                  
               case Laye.OP_TUPLE: // [A, 1 - A] TUPLE(A) (STK[TOP - A - 1],
                  // STK[TOP - A], ..., STK[TOP - 1])
                  stack[(top -= (a = i >>> Laye.POS_A) - 1) - 1] = LayeList
                  .valueOfUnsafe(stack, top - 1, a);
                  continue;
                  
               case Laye.OP_NEW_TABLE: // [1] NEW_TABLE()
               {
                  stack[top++] = new LayeTable();
                  continue;
               }
               
               case Laye.OP_NEW_INSTANCE: // [1 - A] NEW_INSTANCE(A, B)
               {
                  // TODO check these top calcs
                  final LayeValue[] argList = Arrays.copyOfRange(stack,
                        top - (a = i >>> Laye.POS_A), top);
                  final LayeValue type = stack[top = top - a - 1];
                  if ((b = (i >>> Laye.POS_B) & Laye.MAX_B) == Laye.MAX_B)
                  {
                     // new LayeInstance(type, argList);
                     stack[top++] = type.newinstance(argList);
                  }
                  else
                  {
                     // new LayeInstance(type, k[b].asstring(), argList);
                     stack[top++] = type.newinstance(k[b].asstring(), argList);
                  }
                  continue;
               }
               
               case Laye.OP_FOR_PREP: // [0] FOR_PREP(A, B)
               {
                  final LayeValue index, limit, step;
                  // index
                  (index = locals[b = (i >>> Laye.POS_B) & Laye.MAX_B])
                  .checknumber();
                  // limit
                  (limit = locals[b + 1]).checknumber();
                  // step
                  if ((i >>> Laye.POS_A) == 1)
                  {
                     (step = locals[b + 2]).checknumber();
                  }
                  else
                  {
                     step = locals[b + 2] = LayeInt
                           .valueOf(index.lessThan_b(limit) ? 1 : -1);
                  }
                  locals[b] = index.subtract(step);
                  // System.out.println("FOR PREP " + locals[b] + ", " + limit +
                  // ", " + step);
                  continue;
               }
               
               case Laye.OP_FOR_TEST: // [0] FOR_TEST(SA, B)
               {
                  final LayeValue limit = locals[(b = (i >>> Laye.POS_B)
                        & Laye.MAX_B) + 1];
                  final LayeValue step = locals[b + 2];
                  final LayeValue index = locals[b].add(step);
                  if (step.greaterThan_b(0) ? index.lessThan_b(limit)
                        : index.greaterThan_b(limit))
                  {
                     locals[b] = index;
                  }
                  else
                  {
                     pc += ((i >>> Laye.POS_A) & Laye.MAX_A) + Laye.MIN_SA;
                  }
                  continue;
               }
               
               case Laye.OP_FOR_EACH:
                  continue;
                  
               case Laye.OP_POST_FOR_EACH:
                  continue;
                  
               case Laye.OP_MATCH: // [-1] MATCH(A, B)
               {
                  final LayeValue value = stack[--top];
                  final Map<LayeValue, Integer> map = proto.jumpTables
                        .get((i >>> Laye.POS_B) & Laye.MAX_B);
                  final Integer jumpAmt = map.get(value);
                  if (jumpAmt == null)
                  {
                     pc += i >>> Laye.POS_A;
                  }
                  else
                  {
                     pc += jumpAmt.intValue();
                  }
                  continue;
               }
               
               case Laye.OP_RETURN:
               {
                  final boolean leaveValue = ((i >>> Laye.POS_B)
                        & Laye.MAX_B) == 1;
                  if (!leaveValue)
                  {
                     --top;
                  }
                  pc += i >>> Laye.POS_A;
                  continue;
               }
               
               default:
                  throw new IllegalArgumentException(
                        "Unrecognized operation code "
                              + String.format("0x%02x", i));
            }
         }
      }
      catch (final LayeRuntimeException e)
      {
         throw new LayeRuntimeException("<unknown>",
               proto.hasLineInfos ? proto.lineInfo[pc] : 0, e);
      }
      catch (final Exception e)
      {
         System.err.println("whoops, catch this!");
         throw e;
      }
      finally
      {
         if (returned && openUps != null)
         {
            for (int u = openUps.length; --u >= 0;)
            {
               if (openUps[u] != null)
               {
                  openUps[u].close();
               }
            }
         }
      }
   }
   
   private static UpValue findUpValue(final LayeValue[] locals, final int idx,
         final UpValue[] openUps)
   {
      final int n = openUps.length;
      for (int i = 0; i < n; i++)
      {
         if (openUps[i] != null && openUps[i].getIndex() == idx)
         {
            return openUps[i];
         }
      }
      for (int i = 0; i < n; i++)
      {
         if (openUps[i] == null)
         {
            return openUps[i] = new UpValue(locals, idx);
         }
      }
      throw new IllegalArgumentException("no space for new upvalue.");
   }
   
   public void setRoot(final Root root)
   {
      this.root = root;
   }
}
