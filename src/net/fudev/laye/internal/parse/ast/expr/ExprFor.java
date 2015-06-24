/**
 * Copyright (C) 2015 Sekai Kyoretsuna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.values.LayeList;

public final class ExprFor implements Expression
{
   public String indexName;
   public Expression initial, limit, step;
   public Expression body;
   
   public ExprFor(final String indexName, final Expression initial, final Expression limit, final Expression step,
         final Expression body)
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
         // note that the isConst flag is set, so that the user cannot modify it
         // (but we can!)
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
            
            builder.visitOpList(0);
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
