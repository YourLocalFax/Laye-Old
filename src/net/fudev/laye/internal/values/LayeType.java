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
package net.fudev.laye.internal.values;

import net.fudev.laye.internal.TypePrototype;
import net.fudev.laye.internal.types.LayeValueType;

public class LayeType extends LayeValueType
{
   final TypePrototype proto;
   
   final LayeType base;
   
   public LayeType(final TypePrototype proto)
   {
      this.proto = proto;
      base = null; // TODO base
   }
   
   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "type";
   }
   
   LayeFunction getCtorByName(final String name)
   {
      return null;
   }
   
   LayeFunction getPrefixOperator(final String operator)
   {
      return null;
   }
   
   LayeFunction getPostfixOperator(final String operator)
   {
      return null;
   }
   
   LayeFunction getLeftInfixOperator(final String operator)
   {
      return null;
   }
   
   LayeFunction getRightInfixOperator(final String operator)
   {
      return null;
   }
}
