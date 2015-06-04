package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprListCtor implements Expression
{
   public final Vector<Expression> values;
   public boolean immutable;

   public ExprListCtor(final Vector<Expression> values, final boolean immutable)
   {
      this.values = values;
      this.immutable = immutable;
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
         if (immutable)
         {
            builder.visitOpList(size);
         }
         else
         {
            builder.visitOpMutList(size);
         }
      }
   }
}
