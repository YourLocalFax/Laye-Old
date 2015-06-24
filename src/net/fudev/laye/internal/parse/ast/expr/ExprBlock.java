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
import net.fudev.laye.internal.parse.ast.SyntaxNode;

public class ExprBlock implements Expression
{
   public final Vector<SyntaxNode> nodes = new Vector<>();
   
   public ExprBlock()
   {
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      builder.startBlock();
      final int size = nodes.size(), sizem1 = size - 1;
      for (int i = 0; i < size; i++)
      {
         final SyntaxNode node = nodes.get(i);
         final boolean required = isResultRequired && i == sizem1;
         node.accept(builder, required);
      }
      builder.endBlock();
   }
}
