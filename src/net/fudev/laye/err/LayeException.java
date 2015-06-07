package net.fudev.laye.err;

import net.fudev.laye.internal.ValueType;

public class LayeException extends RuntimeException
{
   
   // TODO refactor all of these static methods back out into their own classes
   // that extend this class.
   // I did a dumb
   
   private static final long serialVersionUID = 8822339207319769927L;
   
   public static LayeException constEntry (final String entryName)
   {
      return new LayeException(entryName + " is a const entry.");
   }
   
   public static LayeException invalidTypeComparison (final ValueType left, final ValueType right)
   {
      return new LayeException("attempt to compare type " + left.type + " with type " + right.type + ".");
   }
   
   public static LayeException invalidTypeConversion (final ValueType left, final ValueType right)
   {
      return new LayeException("attempt to convert from type " + left.type + " to type " + right.type + ".");
   }
   
   public static LayeException unexpectedType (final ValueType left, final ValueType right)
   {
      return new LayeException("expected type " + right.type + ", got type " + left.type + ".");
   }
   
   public static LayeException unsupportedOperation (final String operation)
   {
      return new LayeException(operation);
   }
   
   public LayeException ()
   {
   }
   
   public LayeException (final String message)
   {
      super(message);
   }
   
}
