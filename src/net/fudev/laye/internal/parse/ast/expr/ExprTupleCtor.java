package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprTupleCtor implements Expression
{
   public final Vector<Expression> values;
   
   public ExprTupleCtor(final Vector<Expression> values)
   {
      this.values = values;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         final int size = values.size();
         for (int i = 0; i < size; i++)
         {
            values.get(i).accept(builder, true);
         }
         builder.visitOpTuple(size);
      }
   }
}
