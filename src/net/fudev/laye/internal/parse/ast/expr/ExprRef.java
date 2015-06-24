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

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprRef implements Expression
{
   public Expression value;
   
   public ExprRef(final Expression value)
   {
      this.value = value;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         value.accept(builder, true);
         final int op = builder.previous();
         builder.popOp();
         
         switch (op & Laye.MAX_OP)
         {
            case Laye.OP_LOAD:
               // undo the LOAD stack mod
               builder.decreaseStackSize();
               builder.visitRefLocalVar(Laye.GET_A(op));
               break;
            case Laye.OP_GET_UP:
               // undo the GET_UP stack mod
               builder.decreaseStackSize();
               builder.visitRefUpValue(Laye.GET_A(op));
               break;
            case Laye.OP_GET_INDEX:
               // undo the GET_INDEX stack mod
               builder.increaseStackSize();
               builder.visitRefIndex();
               break;
            default:
               throw new CompilerException("can only reference variable locations!");
         }
      }
   }
}
