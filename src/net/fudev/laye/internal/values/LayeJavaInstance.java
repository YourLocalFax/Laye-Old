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

import java.util.Map;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.java.JavaMethod;

// TODO java type inheritance

public final class LayeJavaInstance extends LayeValue
{
   public static LayeJavaInstance valueOf(final Object object)
   {
      final LayeJavaType type = LayeJavaType.get(object.getClass());
      return new LayeJavaInstance(object, type);
   }
   
   private final Map<String, JavaMethod> instanceMethods;
   private final Map<String, JavaMethod> prefixOperators;
   private final Map<String, JavaMethod> postfixOperators;
   private final Map<String, JavaMethod> infixOperators;
   private final Map<String, JavaMethod> infixRightAssocOperators;
   
   public final Object object;
   
   LayeJavaInstance(final Object object, final LayeJavaType type)
   {
      super(ValueType.JAVA_INSTANCE);
      this.object = object;
      this.instanceMethods = type.instanceMethods;
      this.prefixOperators = type.prefixOperators;
      this.postfixOperators = type.postfixOperators;
      this.infixOperators = type.infixOperators;
      this.infixRightAssocOperators = type.infixRightAssocOperators;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (other instanceof LayeJavaInstance)
      {
         return object.equals(((LayeJavaInstance) other).object);
      }
      return false;
   }
   
   @Override
   public int hashCode()
   {
      return object.hashCode();
   }
   
   @Override
   public String asstring()
   {
      return object.toString();
   }
   
   @Override
   public LayeValue callChildMethod(final LayeValue name, final LayeValue... args)
   {
      final String methodName = name.asstring();
      final JavaMethod method = instanceMethods.get(methodName);
      if (method == null)
      {
         throw new LayeException("No method named " + methodName + " found.");
      }
      final Object result = method.invoke(object, args);
      return LayeValue.valueOf(result);
   }
   
   // TODO long/double
   
   @Override
   public LayeValue prefixOp(final String op)
   {
      final JavaMethod method = prefixOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No prefix operator " + op + " found.");
      }
      final Object result = method.invoke(object);
      return LayeValue.valueOf(result);
   }
   
   @Override
   public LayeValue postfixOp(final String op)
   {
      final JavaMethod method = postfixOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No postfix operator " + op + " found.");
      }
      final Object result = method.invoke(object);
      return LayeValue.valueOf(result);
   }
   
   // TODO consolidate
   
   @Override
   public LayeValue infixOp(final String op, final LayeValue right)
   {
      final JavaMethod method = infixOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No infix operator " + op + " found.");
      }
      final Object result = method.invoke(object, right);
      return LayeValue.valueOf(result);
   }
   
   @Override
   public LayeValue infixOp(final String op, final long right)
   {
      final JavaMethod method = infixOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No infix operator " + op + " found.");
      }
      final Object result = method.invoke(object, right);
      return LayeValue.valueOf(result);
   }
   
   @Override
   public LayeValue infixOp(final String op, final double right)
   {
      final JavaMethod method = infixOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No infix operator " + op + " found.");
      }
      final Object result = method.invoke(object, right);
      return LayeValue.valueOf(result);
   }

   @Override
   public LayeValue infixOpRev(final String op, final LayeValue left)
   {
      final JavaMethod method = infixRightAssocOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No infix operator " + op + " found.");
      }
      final Object result = method.invoke(object, left);
      return LayeValue.valueOf(result);
   }

   @Override
   public LayeValue infixOpRev(final String op, final long left)
   {
      final JavaMethod method = infixRightAssocOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No infix operator " + op + " found.");
      }
      final Object result = method.invoke(object, left);
      return LayeValue.valueOf(result);
   }

   @Override
   public LayeValue infixOpRev(final String op, final double left)
   {
      final JavaMethod method = infixRightAssocOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No infix operator " + op + " found.");
      }
      final Object result = method.invoke(object, left);
      return LayeValue.valueOf(result);
   }
}
