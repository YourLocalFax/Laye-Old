package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.values.LayeList;

public final class ExprFor implements Expression
{
   public String indexName;
   public Expression initial, limit, step;
   public Expression body;

   public ExprFor(final String indexName, final Expression initial, final Expression limit, final Expression step, final Expression body)
   {
      this.indexName = indexName;
      this.initial = initial;
      this.limit = limit;
      this.step = step;
      this.body = body;
   }

   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int localName, limitName, stepName;
      final int listLocal, pushMethodName;
      final int startLoc, test;

      builder.startScope();
      {
         // first, get the local name:
         // note that the isConst flag is set, so that the user cannot modify it (but we can!)
         localName = builder.addLocal(indexName, true);
         limitName = builder.addLocal("$for-limit-" + System.nanoTime(), true);
         stepName = builder.addLocal("$for-step-" + System.nanoTime(), true);

         // init and limit
         initial.accept(builder, true);
         builder.visitOpStore(localName);
         builder.visitOpPop(1);

         limit.accept(builder, true);
         builder.visitOpStore(limitName);
         builder.visitOpPop(1);

         // step
         if (step != null)
         {
            step.accept(builder, true);
            builder.visitOpStore(stepName);
            builder.visitOpPop(1);
         }

         builder.visitOpForPrep(step != null, localName);

         if (isResultRequired)
         {
            listLocal = builder.addLocal("$for-" + System.nanoTime(), true);
            pushMethodName = builder.addConsts(LayeList.METHOD_PUSH_BACK);

            builder.visitOpMutList(0);
            builder.visitOpStore(listLocal);
            builder.visitOpPop(1);
         }
         else
         {
            listLocal = -1;
            pushMethodName = -1;
         }

         startLoc = builder.getCurrentPos();
         test = builder.visitOpForTest(0, localName);

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
