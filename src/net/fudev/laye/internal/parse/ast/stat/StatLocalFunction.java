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

import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;

public final class StatLocalFunction implements Statement
{
   public boolean gen, con;
   public String name;
   public ASTFunctionPrototype prototype;
   
   public StatLocalFunction(final boolean gen, final boolean con, final String name,
         final ASTFunctionPrototype prototype)
   {
      this.gen = gen;
      this.con = con;
      this.name = name;
      this.prototype = prototype;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int local = builder.addLocal(name, con);
      final FunctionPrototype proto = prototype.generate(builder, gen);
      
      builder.visitLoadFn(proto);
      builder.visitOpStore(local);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
