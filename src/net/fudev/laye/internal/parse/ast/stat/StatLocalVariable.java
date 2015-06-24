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
