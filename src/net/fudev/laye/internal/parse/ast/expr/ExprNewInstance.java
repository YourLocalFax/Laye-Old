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

public class ExprNewInstance implements Expression
{
   public Expression type;
   public String name;
   public Vector<Expression> args;
   
   public ExprNewInstance(final Expression type, final String name, final Vector<Expression> args)
   {
      this.type = type;
      this.name = name;
      this.args = args;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         type.accept(builder, true);
         for (final Expression expr : args)
         {
            expr.accept(builder, true);
         }
         final int nameid;
         if (name != null)
         {
            nameid = builder.addConsts(name);
         }
         else
         {
            // Is converted for me from -1 in the NewInstance
            nameid = -1;
         }
         builder.visitOpNewInstance(args.size(), nameid);
      }
   }
}
