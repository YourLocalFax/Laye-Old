package net.fudev.laye.internal.parse.ast.stat;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.expr.Expression;

public class StatNewSlotMutable implements Statement
{
   public Vector<String> names = new Vector<>();
   public Vector<Expression> values = new Vector<>();
   
   public StatNewSlotMutable(final Vector<String> names, final Vector<Expression> values)
   {
      this.names = names;
      this.values = values;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int size = names.size();
      for (int i = 0; i < size; i++)
      {
         final String name = names.get(i);
         final Expression value = values.get(i);
         
         builder.visitOpLoadRoot();
         builder.visitLoadString(name);
         
         value.accept(builder, true);
         
         builder.visitOpNewSlot(false);
         
         if (!isResultRequired)
         {
            builder.visitOpPop(1);
         }
      }
   }
}
