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
package net.fudev.laye.util;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

public class ImmutableArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable
{
   
   private static final long serialVersionUID = 325689144664986201L;
   
   private final E[] values;
   private final int length;
   
   public ImmutableArrayList(final E[] values)
   {
      this.length = values.length;
      this.values = Arrays.copyOf(values, this.length);
   }
   
   public @Override E get(final int index)
   {
      if (index < 0 || index >= this.length)
      {
         throw new IndexOutOfBoundsException(Integer.toString(index));
      }
      return this.values[index];
   }
   
   public @Override int size()
   {
      return this.length;
   }
   
}
