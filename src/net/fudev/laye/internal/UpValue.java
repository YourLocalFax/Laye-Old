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
package net.fudev.laye.internal;

import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeValue;

public final class UpValue
{
   
   private LayeValue[] values;
   private int index;
   
   public UpValue(final LayeValue[] stack, final int index)
   {
      values = stack;
      this.index = index;
   }
   
   public @Override String toString()
   {
      return "[" + index + "/" + values.length + "] " + values[index];
   }
   
   public LayeString tostring()
   {
      return values[index].tostring();
   }
   
   public String asstring()
   {
      return values[index].asstring();
   }
   
   public LayeValue getValue()
   {
      return values[index];
   }
   
   public void setValue(final LayeValue value)
   {
      values[index] = value;
   }
   
   public void close()
   {
      final LayeValue[] old = values;
      values = new LayeValue[] { old[index] };
      old[index] = null;
      index = 0;
   }
   
   public int getIndex()
   {
      return index;
   }
}
