package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.lexical.Operator;

public class ExprPrefix implements Expression
{
   public Expression value;
   public Operator op;

   public ExprPrefix(final Expression value, final Operator op)
   {
      this.value = value;
      this.op = op;
   }

   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      Expression.convertFromListToValueInOperatorExpression(builder, value);
      builder.visitPrefixExpr(op);

      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
