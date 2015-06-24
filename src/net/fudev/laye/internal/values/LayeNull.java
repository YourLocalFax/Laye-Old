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
