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

import java.util.Arrays;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;

public final class LayeTuple extends LayeValue
{
   // TODO unsafe variants?
   public static LayeTuple valueOf(final LayeValue[] values)
   {
      if (values.length < 2)
      {
         throw new LayeException("tuples require at least two values.");
      }
      return new LayeTuple(values, 0, values.length);
   }
   
   public static LayeTuple valueOf(final LayeValue[] values, final int index, final int count)
   {
      if (values.length < 2 || count < 2)
      {
         throw new LayeException("tuples require at least two values.");
      }
      return new LayeTuple(values, index, count);
   }
   
   private final LayeValue[] values;
   
   LayeTuple(LayeValue[] values, final int index, final int count)
   {
      super(ValueType.TUPLE);
      values = Arrays.copyOfRange(values, index, index + count);
      for (int i = 0; i < count; i++)
      {
         if (values[i] == null)
         {
            values[i] = LayeValue.NULL;
         }
      }
      this.values = values;
   }
   
   @Override
   public String asstring()
   {
      final StringBuilder sb = new StringBuilder().append('(');
      final LayeValue[] values = this.values;
      for (int i = 0, len = values.length; i < len; i++)
      {
         if (i > 0)
         {
            sb.append(", ");
         }
         sb.append(values[i]);
      }
      return sb.append(')').toString();
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (other.valueType != ValueType.TUPLE)
      {
         return false;
      }
      return Arrays.equals(values, ((LayeTuple) other).values);
   }
   
   @Override
   public int hashCode()
   {
      return Arrays.hashCode(values);
   }
   
   @Override
   public LayeValue get(final LayeValue key)
   {
      if (key.isint())
      {
         return values[key.asint()];
      }
      throw new LayeException("");
   }
}
