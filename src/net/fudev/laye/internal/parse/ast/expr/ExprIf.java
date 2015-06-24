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

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public final class ExprIf implements Expression
{
   public Expression condition;
   public Expression pass, fail;
   
   public ExprIf(final Expression condition, final Expression pass, final Expression fail)
   {
      this.condition = condition;
      this.pass = pass;
      this.fail = fail;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      condition.accept(builder, true);
      
      final int ifStart = builder.visitOpTest(0, true);
      
      builder.startScope();
      pass.accept(builder, isResultRequired);
      builder.endScope();
      
      final int ifEnd = builder.getCurrentPos();
      final int elStart = builder.visitOpJump(0);
      
      builder.startScope();
      fail.accept(builder, isResultRequired);
      builder.endScope();
      
      builder.setOp_SA(elStart, builder.getCurrentPos() - elStart);
      builder.setOp_SA(ifStart, ifEnd - ifStart + 1);
   }
}
