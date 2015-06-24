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

public class ExprNewSlot implements Expression
{
   public Expression index, value;
   
   public ExprNewSlot(final Expression index, final Expression value)
   {
      this.index = index;
      this.value = value;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      // leave a value so we can check the instruction.
      index.accept(builder, true);
      final int lastOp = builder.previous();
      if (Laye.GET_OP(lastOp) != Laye.OP_GET_INDEX)
      {
         // TODO better error messages
         throw new CompilerException("Can only new-slot a variable or table field.");
      }
      
      // undo the GET_INDEX
      builder.popOp();
      builder.increaseStackSize();
      value.accept(builder, true);
      builder.visitOpNewSlot(true);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
