package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprLiteralNull implements Expression
{
   public ExprLiteralNull()
   {
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder,
         final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         builder.visitLoadNull();
      }
   }
}
