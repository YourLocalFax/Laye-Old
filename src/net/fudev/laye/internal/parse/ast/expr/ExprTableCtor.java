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

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprTableCtor implements Expression
{
   public final Vector<Expression> keys = new Vector<>();
   public final Vector<Expression> values = new Vector<>();
   public final Vector<Boolean> constant = new Vector<>();
   
   public ExprTableCtor()
   {
   }
   
   public void addEntry(final Expression key, final Expression value, final boolean con)
   {
      keys.addElement(key);
      values.addElement(value);
      constant.addElement(con);
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         builder.visitOpNewTable();
         final int amount = keys.size();
         for (int i = 0; i < amount; i++)
         {
            final Expression key = keys.get(i);
            final Expression value = values.get(i);
            final boolean con = constant.get(i);
            // get the table again
            builder.visitOpDup(1);
            // key/value
            key.accept(builder, true);
            value.accept(builder, true);
            // create the slot
            builder.visitOpNewSlot(con);
            builder.visitOpPop(1);
         }
      }
   }
}
