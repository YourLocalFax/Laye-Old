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
