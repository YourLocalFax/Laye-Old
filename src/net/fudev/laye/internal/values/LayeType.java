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

import net.fudev.laye.internal.TypePrototype;
import net.fudev.laye.internal.types.LayeValueType;

public class LayeType extends LayeValueType
{
   final TypePrototype proto;
   
   final LayeType base;
   
   public LayeType(final TypePrototype proto)
   {
      this.proto = proto;
      base = null; // TODO base
   }
   
   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "type";
   }
   
   LayeFunction getCtorByName(final String name)
   {
      return null;
   }
   
   LayeFunction getPrefixOperator(final String operator)
   {
      return null;
   }
   
   LayeFunction getPostfixOperator(final String operator)
   {
      return null;
   }
   
   LayeFunction getLeftInfixOperator(final String operator)
   {
      return null;
   }
   
   LayeFunction getRightInfixOperator(final String operator)
   {
      return null;
   }
}
