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
