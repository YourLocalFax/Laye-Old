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
