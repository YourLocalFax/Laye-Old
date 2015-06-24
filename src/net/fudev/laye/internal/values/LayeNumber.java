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

public abstract class LayeNumber extends LayeValue
{
   
   LayeNumber(final ValueType type)
   {
      super(type);
   }
   
   @Override
   public int hashCode()
   {
      return isint() ? Long.hashCode(aslong()) : Double.hashCode(asdouble());
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (other.isfloat() || other.isint())
      {
         return Double.compare(other.asdouble(), asdouble()) == 0;
      }
      return false;
   }
   
   @Override
   public abstract byte asbyte();
   
   @Override
   public abstract short asshort();
   
   @Override
   public abstract int asint();
   
   @Override
   public abstract long aslong();
   
   @Override
   public abstract float asfloat();
   
   @Override
   public abstract double asdouble();
   
}
