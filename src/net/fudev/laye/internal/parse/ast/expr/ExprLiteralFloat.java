package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprLiteralFloat implements Expression
{
   public double value;
   
   public ExprLiteralFloat (final double value)
   {
      this.value = value;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         builder.visitLoadFloat(value);
      }
   }
}
