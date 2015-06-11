package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprReturn implements Expression
{
   public Expression value;
   
   public ExprReturn(final Expression value)
   {
      this.value = value;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder,
         final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         if (value == null)
         {
            builder.visitLoadNull();
         }
         else
         {
            value.accept(builder, isResultRequired);
         }
      }
      builder.returnFromBlock(isResultRequired);
   }
}
