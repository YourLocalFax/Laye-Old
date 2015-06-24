/**
 * Copyright 2015 YourLocalFax
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public final class ExprIf implements Expression
{
   public Expression condition;
   public Expression pass, fail;
   
   public ExprIf(final Expression condition, final Expression pass, final Expression fail)
   {
      this.condition = condition;
      this.pass = pass;
      this.fail = fail;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
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
