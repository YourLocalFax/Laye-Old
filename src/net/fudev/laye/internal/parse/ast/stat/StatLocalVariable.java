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

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.expr.Expression;

public final class StatLocalVariable implements Statement
{
   public boolean con;
   public final Vector<String> names;
   public final Vector<Expression> values;
   
   public StatLocalVariable(final boolean con, final Vector<String> names, final Vector<Expression> values)
   {
      this.con = con;
      this.names = names;
      this.values = values;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int size = names.size(), sizem1 = size - 1;
      for (int i = 0; i < size; i++)
      {
         final String localName = names.get(i);
         final Expression value = values.get(i);
         
         value.accept(builder, true);
         
         final int local = builder.addLocal(localName, con);
         builder.visitOpStore(local);
         
         if (i != sizem1 || !isResultRequired)
         {
            builder.visitOpPop(1);
         }
      }
   }
}
