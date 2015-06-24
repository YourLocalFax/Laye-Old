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

import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;

public final class ExprFunction implements Expression
{
   public boolean gen;
   public ASTFunctionPrototype prototype;
   
   public ExprFunction(final boolean gen, final ASTFunctionPrototype prototype)
   {
      this.gen = gen;
      this.prototype = prototype;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final FunctionPrototype proto = prototype.generate(builder, gen);
      builder.visitLoadFn(proto);
   }
}
