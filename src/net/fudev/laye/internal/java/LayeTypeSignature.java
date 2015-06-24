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
package net.fudev.laye.internal.java;

import java.util.Arrays;
import java.util.Vector;

import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.values.LayeValue;

final class LayeTypeSignature
{
   static LayeTypeSignature createTypeSignature(final Class<?>... argTypes)
   {
      final Vector<ValueType> types = new Vector<>();
      for (final Class<?> type : argTypes)
      {
         types.addElement(ValueType.getFromClass(type));
      }
      return new LayeTypeSignature(types.toArray(new ValueType[types.size()]));
   }
   
   static LayeTypeSignature createTypeSignature(final LayeValue... argValues)
   {
      final Vector<ValueType> types = new Vector<>();
      for (final LayeValue value : argValues)
      {
         types.addElement(value.valueType);
      }
      return new LayeTypeSignature(types.toArray(new ValueType[types.size()]));
   }
   
   final ValueType[] types;
   
   LayeTypeSignature(final ValueType[] types)
   {
      this.types = types;
   }
   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(types);
      return result;
   }
   
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final LayeTypeSignature other = (LayeTypeSignature) obj;
      if (!Arrays.equals(types, other.types))
      {
         return false;
      }
      return true;
   }
   
   @Override
   public String toString()
   {
      return Arrays.toString(types);
   }
}
