/**
 * Copyright 2015 YourLocalFax
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
