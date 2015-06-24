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

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprMethodCall implements Expression
{
   public Expression value;
   public Expression name;
   public Vector<Expression> args;
   
   public ExprMethodCall(final Expression value, final Expression name, final Vector<Expression> args)
   {
      this.value = value;
      this.name = name;
      this.args = args;
   }
   
   public ExprMethodCall(final Expression value, final Expression name, final Expression arg)
   {
      this.value = value;
      this.name = name;
      args = new Vector<>();
      args.add(arg);
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
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
