package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprListCtor implements Expression
{
   public final Vector<Expression> values;
   
   public ExprListCtor(final Vector<Expression> values)
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
         builder.visitOpList(size);
      }
   }
}
