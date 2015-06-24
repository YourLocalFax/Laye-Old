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
package net.fudev.laye.internal.parse.ast;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

/**
 * Any syntactic element in the Laye scripting language. This does not include preprocessors.
 */
public interface SyntaxNode
{
   /**
    * @param builder
    *           The FunctionBuilder this node should act on.
    * @param isResultRequired
    *           <code>true</code> if this node should leave a value on the stack, <code>false</code> otherwise.
    */
   void accept(final LayeFunctionBuilder builder, final boolean isResultRequired);
}
