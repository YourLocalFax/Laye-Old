package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprMethodCall implements Expression
{
   public Expression value;
   public Expression name;
   public Vector<Expression> args;
   
   public ExprMethodCall (final Expression value, final Expression name, final Vector<Expression> args)
   {
      this.value = value;
      this.name = name;
      this.args = args;
   }
   
   public ExprMethodCall (final Expression value, final Expression name, final Expression arg)
   {
      this.value = value;
      this.name = name;
      args = new Vector<>();
      args.add(arg);
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      value.accept(builder, true);
      name.accept(builder, true);
      for (final Expression arg : args)
      {
         arg.accept(builder, true);
      }
      builder.visitOpMethod(args.size());
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
