package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.lexical.Operator;

public class ExprPostfix implements Expression
{
   public Expression value;
   public Operator op;
   
   public ExprPostfix(final Expression value, final Operator op)
   {
      this.value = value;
      this.op = op;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder,
         final boolean isResultRequired)
   {
      value.accept(builder, true);
      builder.visitPostfixExpr(op);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
