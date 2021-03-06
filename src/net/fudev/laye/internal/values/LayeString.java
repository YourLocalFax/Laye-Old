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

import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.internal.ValueType;

public final class LayeString extends LayeValue
{
   private static final Map<String, LayeString> strings = new HashMap<>();
   
   public static LayeString valueOf(final String value)
   {
      LayeString string = strings.get(value);
      if (string == null)
      {
         string = new LayeString(value);
         strings.put(value, string);
      }
      return string;
   }
   
   // TODO Laye will use its own string implementation, and we'll convert
   // between them.
   public final String value;
   
   private LayeString(final String value)
   {
      super(ValueType.STRING);
      this.value = value;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (!other.isstring())
      {
         return false;
      }
      return value.equals(other.asstring());
   }
   
   @Override
   public int hashCode()
   {
      return value.hashCode();
   }
   
   @Override
   public String asstring()
   {
      return value;
   }
   
   @Override
   public LayeString checkstring()
   {
      return this;
   }
   
}
