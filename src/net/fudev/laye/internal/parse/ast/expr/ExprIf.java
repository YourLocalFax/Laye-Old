package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public final class ExprIf implements Expression
{
   public Expression condition;
   public Expression pass, fail;
   
   public ExprIf (final Expression condition, final Expression pass, final Expression fail)
   {
      this.condition = condition;
      this.pass = pass;
      this.fail = fail;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      condition.accept(builder, true);
      
      final int ifStart = builder.visitOpTest(0, true);
      
      builder.startScope();
      pass.accept(builder, isResultRequired);
      builder.endScope();
      
      final int ifEnd = builder.getCurrentPos();
      final int elStart = builder.visitOpJump(0);
      
      builder.startScope();
      fail.accept(builder, isResultRequired);
      builder.endScope();
      
      builder.setOp_SA(elStart, builder.getCurrentPos() - elStart);
      builder.setOp_SA(ifStart, ifEnd - ifStart + 1);
   }
}
