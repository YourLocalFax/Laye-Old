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

public final class LayeInstance extends LayeValue
{
   public final LayeType type;
   
   public LayeInstance(final LayeType type, final LayeValue... ctorArgs)
   {
      this(type, null, ctorArgs);
   }
   
   public LayeInstance(final LayeType type, final String ctorName, final LayeValue... ctorArgs)
   {
      super(ValueType.INSTANCE);
      this.type = type;
      
      final LayeValue[] args = new LayeValue[ctorArgs.length + 1];
      System.arraycopy(ctorArgs, 0, args, 1, ctorArgs.length);
      args[0] = this;
      
      final LayeFunction ctor = type.getCtorByName(ctorName);
      ctor.call(args);
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
      // TODO overloaded == operator
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "instance-TODO";
   }
   
   @Override
   public LayeValue infixOp(final String op, final LayeValue right)
   {
      final LayeFunction leftOp = type.getLeftInfixOperator(op);
      if (leftOp != null)
      {
         return leftOp.call(this, right);
      }
      else
      {
         final LayeFunction rightOp = type.getRightInfixOperator(op);
         if (rightOp != null)
         {
            return rightOp.call(right, this);
         }
         else
         {
            // TODO instance type as string
            arithBinaryError(op, "instance", right.valueType.type.toString());
            return LayeValue.NULL;
         }
      }
   }
   
   @Override
   public LayeValue add(final LayeValue right)
   {
      return infixOp("+", right);
   }
}
