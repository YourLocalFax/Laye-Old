package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprFunctionCall implements Expression
{
   public Expression value;
   public Vector<Expression> args;
   
   public ExprFunctionCall(final Expression value, final Vector<Expression> args)
   {
      this.value = value;
      this.args = args;
   }
   
   public ExprFunctionCall(final Expression value, final Expression arg)
   {
      this.value = value;
      args = new Vector<>();
      args.add(arg);
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      value.accept(builder, true);
      for (final Expression arg : args)
      {
         arg.accept(builder, true);
      }
      builder.visitOpCall(args.size());
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
