package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.values.LayeList;

public final class ExprWhile implements Expression
{
   public Expression condition;
   public Expression body;
   
   public ExprWhile (final Expression condition, final Expression body)
   {
      this.condition = condition;
      this.body = body;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int listLocal, pushMethodName;
      final int startLoc, test;
      
      builder.startScope();
      {
         if (isResultRequired)
         {
            listLocal = builder.addLocal("$while-" + System.nanoTime(), true);
            pushMethodName = builder.addConsts(LayeList.METHOD_PUSH_BACK);
            
            builder.visitOpMutList(0);
            builder.visitOpStore(listLocal);
            builder.visitOpPop(1);
         }
         else
         // shut up compiler
         {
            listLocal = -1;
            pushMethodName = -1;
         }
         
         startLoc = builder.getCurrentPos();
         
         condition.accept(builder, true);
         
         test = builder.visitOpTest(0, true);
         
         builder.startScope();
         {
            if (isResultRequired)
            {
               builder.visitOpLoad(listLocal);
               builder.visitOpLoadConst(pushMethodName);
            }
            body.accept(builder, isResultRequired);
            if (isResultRequired)
            {
               builder.visitOpMethod(1);
               builder.visitOpPop(1);
            }
         }
         builder.endScope();
      }
      builder.endScope();
      
      final int endLoc = builder.getCurrentPos() + 1;
      builder.visitOpJump(startLoc - endLoc);
      builder.setOp_SA(test, endLoc - test);
      
      if (isResultRequired)
      {
         builder.visitOpLoad(listLocal);
      }
   }
}
