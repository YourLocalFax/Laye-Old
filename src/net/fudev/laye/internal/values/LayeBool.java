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

import net.fudev.laye.internal.ValueType;

public final class LayeBool extends LayeValue
{
   static final LayeBool TRUE_INTERNAL = new LayeBool(true);
   static final LayeBool FALSE_INTERNAL = new LayeBool(false);
   
   public final boolean value;
   
   LayeBool(final boolean value)
   {
      super(ValueType.BOOL);
      this.value = value;
   }
   
   @Override
   public LayeBool tobool()
   {
      return this;
   }
   
   @Override
   public boolean asbool()
   {
      return value;
   }
   
   @Override
   public String asstring()
   {
      return value ? "true" : "false";
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public int hashCode()
   {
      return Boolean.hashCode(value);
   }
}
