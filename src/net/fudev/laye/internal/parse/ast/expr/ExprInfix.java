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
import net.fudev.laye.internal.lexical.Operator;

public class ExprInfix implements Expression
{
   public Expression left, right;
   public Operator op;
   
   public ExprInfix(final Expression left, final Expression right, final Operator op)
   {
      this.left = left;
      this.right = right;
      this.op = op;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      left.accept(builder, true);
      right.accept(builder, true);
      builder.visitInfixExpr(op);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
