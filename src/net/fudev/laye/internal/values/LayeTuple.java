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
