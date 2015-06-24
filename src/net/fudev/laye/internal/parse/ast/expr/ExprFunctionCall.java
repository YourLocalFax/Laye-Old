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

public class ExprFunctionCall implements Expression
{
   public Expression value;
   public Vector<Expression> args;
   
   public ExprFunctionCall(final Expression value, final Vector<Expression> args)
   {
      this.value = value;
      this.args = args;
   }
   
   public ExprFunctionCall(final Expression value, final Expression arg)
   {
      this.value = value;
      args = new Vector<>();
      args.add(arg);
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      value.accept(builder, true);
      for (final Expression arg : args)
      {
         arg.accept(builder, true);
      }
      builder.visitOpCall(args.size());
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
