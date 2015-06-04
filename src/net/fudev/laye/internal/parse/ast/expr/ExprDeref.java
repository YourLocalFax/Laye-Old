package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprDeref implements Expression
{
   public Expression value;

   public ExprDeref(final Expression value)
   {
      this.value = value;
   }

   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         value.accept(builder, true);
         builder.visitOpDeref();
      }
   }
}
