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

public class LayeNull extends LayeValue
{
   static final LayeNull NULL_INTERNAL = new LayeNull();
   
   private LayeNull()
   {
      super(ValueType.NULL);
   }
   
   public @Override LayeBool equalTo(final LayeValue other)
   {
      return other == this ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "null";
   }
   
   @Override
   public int hashCode()
   {
      return 0;
   }
   
   @Override
   public boolean equals(final Object obj)
   {
      return obj == this;
   }
}
