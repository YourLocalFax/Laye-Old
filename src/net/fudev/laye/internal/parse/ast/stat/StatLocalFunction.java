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
package net.fudev.laye.internal.parse.ast.stat;

import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;

public final class StatLocalFunction implements Statement
{
   public boolean gen, con;
   public String name;
   public ASTFunctionPrototype prototype;
   
   public StatLocalFunction(final boolean gen, final boolean con, final String name,
         final ASTFunctionPrototype prototype)
   {
      this.gen = gen;
      this.con = con;
      this.name = name;
      this.prototype = prototype;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int local = builder.addLocal(name, con);
      final FunctionPrototype proto = prototype.generate(builder, gen);
      
      builder.visitLoadFn(proto);
      builder.visitOpStore(local);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
