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
