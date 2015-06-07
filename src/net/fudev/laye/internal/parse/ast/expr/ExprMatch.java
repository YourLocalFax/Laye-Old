package net.fudev.laye.internal.parse.ast.expr;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.values.LayeValue;

public final class ExprMatch implements Expression
{
   public Expression value;
   public final Vector<LayeValue[]> cases = new Vector<>();
   public final Vector<Expression> results = new Vector<>();
   
   public ExprMatch (final Expression value)
   {
      this.value = value;
   }
   
   public void addCase (final Vector<LayeValue> caseValues, final Expression result)
   {
      cases.addElement(caseValues.size() == 0 ? null : caseValues.toArray(new LayeValue[caseValues.size()]));
      results.addElement(result);
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int size = cases.size();
      if (size != results.size())
      {
         throw new CompilerException("number of cases does not match number of results in match expression.");
      }
      
      value.accept(builder, true);
      
      final int start = builder.visitOpMatch(0, 0);
      final Map<LayeValue, Integer> jumpTable = new HashMap<>();
      
      int defaultJump = -1;
      
      final int[] jumps = new int[size - 1];
      for (int i = 0; i < size; i++)
      {
         final LayeValue[] cases = this.cases.elementAt(i);
         final Expression result = results.elementAt(i);
         
         final int caseStart = builder.getCurrentPos();
         if (cases != null)
         {
            // set the appropriate jump offset in the table
            for (final LayeValue caseValue : cases)
            {
               jumpTable.put(caseValue, caseStart - start);
            }
         }
         else
         {
            defaultJump = caseStart;
         }
         
         final int jump;
         result.accept(builder, isResultRequired);
         jump = builder.visitOpJump(0);
         
         // because the last one doesn't need a break insn
         if (i < size - 1)
         {
            jumps[i] = jump;
         }
      }
      
      if (defaultJump == -1)
      {
         defaultJump = builder.getCurrentPos();
         builder.visitLoadNull();
      }
      
      // fix break jump amounts
      final int end = builder.getCurrentPos();
      for (int i = 0; i < size - 1; i++)
      {
         final int pos = jumps[i];
         builder.setOp_SA(pos, end - pos);
      }
      
      final int map = builder.addMatchJumpTable(jumpTable);
      builder.setOp_AB(start, defaultJump - start, map);
   }
}
