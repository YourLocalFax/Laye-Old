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
