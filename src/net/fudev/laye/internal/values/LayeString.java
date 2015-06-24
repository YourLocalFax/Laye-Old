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
