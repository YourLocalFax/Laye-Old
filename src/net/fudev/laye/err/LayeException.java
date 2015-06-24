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
package net.fudev.laye.err;

import net.fudev.laye.internal.ValueType;

public class LayeException extends RuntimeException
{
   
   // TODO refactor all of these static methods back out into their own classes
   // that extend this class.
   // I did a dumb
   
   private static final long serialVersionUID = 8822339207319769927L;
   
   public static LayeException constEntry(final String entryName)
   {
      return new LayeException(entryName + " is a const entry.");
   }
   
   public static LayeException invalidTypeComparison(final ValueType left, final ValueType right)
   {
      return new LayeException("attempt to compare type " + left.type + " with type " + right.type + ".");
   }
   
   public static LayeException invalidTypeConversion(final ValueType left, final ValueType right)
   {
      return new LayeException("attempt to convert from type " + left.type + " to type " + right.type + ".");
   }
   
   public static LayeException unexpectedType(final ValueType left, final ValueType right)
   {
      return new LayeException("expected type " + right.type + ", got type " + left.type + ".");
   }
   
   public static LayeException unsupportedOperation(final String operation)
   {
      return new LayeException(operation);
   }
   
   public LayeException()
   {
   }
   
   public LayeException(final String message)
   {
      super(message);
   }
   
}
