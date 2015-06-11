package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprPrefixTypeof implements Expression
{
   public Expression value;
   
   public ExprPrefixTypeof(final Expression value)
   {
      this.value = value;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder,
         final boolean isResultRequired)
   {
      value.accept(builder, true);
      builder.visitOpTypeof();
   }
}
