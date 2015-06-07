package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.lexical.Operator;

public class ExprInfix implements Expression
{
   public Expression left, right;
   public Operator op;
   
   public ExprInfix (final Expression left, final Expression right, final Operator op)
   {
      this.left = left;
      this.right = right;
      this.op = op;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      Expression.convertFromListToValueInOperatorExpression(builder, left);
      Expression.convertFromListToValueInOperatorExpression(builder, right);
      builder.visitInfixExpr(op);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
