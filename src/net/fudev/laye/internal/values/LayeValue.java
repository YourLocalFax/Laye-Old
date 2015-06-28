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

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;

public abstract class LayeValue
{
   public static LayeValue valueOf(final Object value)
   {
      if (value == null)
      {
         return NULL;
      }
      if (value instanceof Class)
      {
         return LayeJavaType.get((Class<?>) value);
      }
      final Class<?> cls = value.getClass();
      if (cls == String.class)
      {
         return LayeString.valueOf((String) value);
      }
      else if (cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class)
      {
         return LayeInt.valueOf(((Number) value).longValue());
      }
      else if (cls == Float.class || cls == Double.class)
      {
         return LayeValue.valueOf(((Number) value).doubleValue());
      }
      else if (cls == Boolean.class)
      {
         return value == Boolean.TRUE ? TRUE : FALSE;
      }
      // TODO lists and maps
      return LayeJavaInstance.valueOf(value);
   }
   
   // ---------- Constants ---------- //
   
   public static final LayeNull NULL = LayeNull.NULL_INTERNAL;
   
   public static final LayeBool TRUE = LayeBool.TRUE_INTERNAL;
   public static final LayeBool FALSE = LayeBool.FALSE_INTERNAL;
   
   public final ValueType valueType;
   
   protected LayeValue(final ValueType type)
   {
      valueType = type;
   }
   
   public final String typeString()
   {
      return valueType.type.toString();
   }
   
   public @Override String toString()
   {
      return asstring();
   }
   
   // ---------- HashCode && Equals ---------- //
   
   @Override
   public abstract int hashCode();
   
   public @Override boolean equals(final Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null)
      {
         return false;
      }
      if (!(other instanceof LayeValue))
      {
         return false;
      }
      return equalTo_b((LayeValue) other);
   }
   
   // ---------- Errors ---------- //
   
   /** Throws an UnsupportedOperation for a unary prefix operation error. */
   final static void arithUnaryPreError(final String op, final String value)
   {
      throw LayeException.unsupportedOperation("prefix unary '" + op + "' is undefined for type '" + value + "'");
   }
   
   /** Throws an UnsupportedOperation for a unary postfix operation error. */
   final static void arithUnaryPostError(final String op, final String value)
   {
      throw LayeException.unsupportedOperation("postfix unary '" + op + "' is undefined for type '" + value + "'");
   }
   
   /** Throws an UnsupportedOperation for a binary operation error. */
   final static void arithBinaryError(final String op, final String left, final String right)
   {
      throw LayeException.unsupportedOperation("binary '" + op + "' is undefined for types '" + left + "' and '"
            + right + "'");
   }
   
   /** Throws an UnsupportedOperation for a comparison error. */
   final static void compError(final String op, final String left, final String right)
   {
      throw LayeException.unsupportedOperation("comparison '" + op + "' is undefined for types '" + left + "' and '"
            + right + "'");
   }
   
   // ---------- Type Checking ---------- //
   
   public boolean isnull()
   {
      return valueType == ValueType.NULL;
   }
   
   public boolean isbool()
   {
      return valueType == ValueType.BOOL;
   }
   
   public boolean isnumber()
   {
      return valueType == ValueType.NUMBER || isint() || isfloat();
   }
   
   public boolean isint()
   {
      return valueType == ValueType.INT;
   }
   
   public boolean isfloat()
   {
      return valueType == ValueType.FLOAT;
   }
   
   public boolean isstring()
   {
      return valueType == ValueType.STRING;
   }
   
   public boolean istable()
   {
      return valueType == ValueType.TABLE;
   }
   
   public boolean ismutablelist()
   {
      return valueType == ValueType.LIST;
   }
   
   public boolean islist()
   {
      return valueType == ValueType.LIST;
   }
   
   public boolean isfunction()
   {
      return valueType == ValueType.FUNCTION;
   }
   
   public boolean isref()
   {
      return valueType == ValueType.REFERENCE;
   }
   
   public boolean istype()
   {
      return valueType == ValueType.TYPE;
   }
   
   // ---------- Type Conversion ---------- //
   
   public LayeBool tobool()
   {
      if (this == LayeValue.NULL || this == LayeValue.FALSE)
      {
         return LayeValue.FALSE;
      }
      return LayeValue.TRUE;
   }
   
   public LayeInt toint()
   {
      throw LayeException.unexpectedType(valueType, ValueType.INT);
   }
   
   public LayeFloat tofloat()
   {
      throw LayeException.unexpectedType(valueType, ValueType.FLOAT);
   }
   
   public LayeString tostring()
   {
      return LayeString.valueOf(asstring());
   }
   
   // ---------- Checked Type Conversion ---------- //
   
   public LayeBool checkbool()
   {
      if (valueType != ValueType.BOOL)
      {
         throw LayeException.unexpectedType(valueType, ValueType.BOOL);
      }
      return (LayeBool) this;
   }
   
   public LayeInt checkint()
   {
      if (valueType != ValueType.INT)
      {
         throw LayeException.unexpectedType(valueType, ValueType.INT);
      }
      return (LayeInt) this;
   }
   
   public LayeFloat checkfloat()
   {
      if (valueType != ValueType.FLOAT)
      {
         throw LayeException.unexpectedType(valueType, ValueType.FLOAT);
      }
      return (LayeFloat) this;
   }
   
   public LayeNumber checknumber()
   {
      if (valueType != ValueType.FLOAT && valueType != ValueType.INT)
      {
         throw LayeException.unexpectedType(valueType, ValueType.NUMBER);
      }
      return (LayeNumber) this;
   }
   
   public LayeString checkstring()
   {
      if (valueType != ValueType.STRING)
      {
         throw LayeException.unexpectedType(valueType, ValueType.STRING);
      }
      return (LayeString) this;
   }
   
   public LayeTable checktable()
   {
      if (valueType != ValueType.TABLE)
      {
         throw LayeException.unexpectedType(valueType, ValueType.TABLE);
      }
      return (LayeTable) this;
   }
   
   public LayeList checklist()
   {
      if (valueType != ValueType.LIST)
      {
         throw LayeException.unexpectedType(valueType, ValueType.LIST);
      }
      return (LayeList) this;
   }
   
   public LayeFunction checkfunction()
   {
      if (valueType != ValueType.FUNCTION)
      {
         throw LayeException.unexpectedType(valueType, ValueType.FUNCTION);
      }
      return (LayeFunction) this;
   }
   
   public LayeReference checkreference()
   {
      if (valueType != ValueType.REFERENCE)
      {
         throw LayeException.unexpectedType(valueType, ValueType.REFERENCE);
      }
      return (LayeReference) this;
   }
   
   public LayeType checktype()
   {
      if (valueType != ValueType.TYPE)
      {
         throw LayeException.unexpectedType(valueType, ValueType.TYPE);
      }
      return (LayeType) this;
   }
   
   public LayeInstance checkinstance()
   {
      if (valueType != ValueType.INSTANCE)
      {
         throw LayeException.unexpectedType(valueType, ValueType.INSTANCE);
      }
      return (LayeInstance) this;
   }
   
   // ---------- Checked Java Type Conversion ---------- //
   
   public boolean asbool()
   {
      return checkbool().value;
   }
   
   public byte asbyte()
   {
      return (byte) checkint().value;
   }
   
   public short asshort()
   {
      return (short) checkint().value;
   }
   
   public int asint()
   {
      return (int) checkint().value;
   }
   
   public long aslong()
   {
      return checkint().value;
   }
   
   public float asfloat()
   {
      return (float) checkfloat().value;
   }
   
   public double asdouble()
   {
      return checkfloat().value;
   }
   
   public abstract String asstring();
   
   public LayeValue[] asarray()
   {
      // TODO handle this somewhere?
      return null;
   }
   
   // ---------- Calls ---------- //
   
   public LayeValue call(final LayeValue... args)
   {
      throw new LayeException("cannot call " + valueType.type.toString());
   }
   
   public LayeValue callAsMethod(final LayeValue parent, final LayeValue... args)
   {
      throw new LayeException("cannot call " + valueType.type.toString());
   }
   
   public LayeValue callChildMethod(final LayeValue name, final LayeValue... args)
   {
      throw new LayeException(valueType.type.toString() + " has no method '" + name + "'");
   }
   
   // ---------- Unary Operations ---------- //
   
   /** Performs the given prefix unary operation on this value. */
   public LayeValue prefixOp(final String op)
   {
      arithUnaryPreError(op, valueType.type.toString());
      return null;
   }
   
   /** Performs the given postfix unary operation on this value. */
   public LayeValue postfixOp(final String op)
   {
      arithUnaryPostError(op, valueType.type.toString());
      return null;
   }
   
   /** Takes the positive of this LayeValue. */
   public LayeValue posit()
   {
      arithUnaryPreError("+", valueType.type.toString());
      return null;
   }
   
   /** Takes the negative of this LayeValue. */
   public LayeValue negate()
   {
      arithUnaryPreError("-", valueType.type.toString());
      return null;
   }
   
   /** Takes the bool complement of this LayeValue. */
   public LayeValue not()
   {
      return tobool() == LayeValue.TRUE ? LayeValue.FALSE : LayeValue.TRUE;
   }
   
   /** Takes the integer complement of this LayeValue. */
   public LayeValue complement()
   {
      arithUnaryPreError("~", valueType.type.toString());
      return null;
   }
   
   /** Returns the type of this value. */
   public LayeValue typeof()
   {
      return LayeString.valueOf(valueType.toString());
   }
   
   /** Takes the length of this LayeValue. */
   public LayeValue len()
   {
      arithUnaryPreError("#", valueType.type.toString());
      return null;
   }
   
   /**
    * Takes the length of this LayeValue and returns it as an integer for Java to use.
    */
   public int length()
   {
      return len().asint();
   }
   
   // ---------- Binary Arithmetic ---------- //
   
   // TODO infixOpRev?
   
   /** Performs the given binary operation on this value and the right value. */
   public LayeValue infixOp(final String op, final LayeValue right)
   {
      arithBinaryError(op, valueType.type.toString(), right.valueType.type.toString());
      return null;
   }
   
   /** Performs the given binary operation on this value and the right value. */
   public LayeValue infixOp(final String op, final long right)
   {
      arithBinaryError(op, valueType.type.toString(), ValueType.INT.toString());
      return null;
   }
   
   /** Performs the given binary operation on this value and the right value. */
   public LayeValue infixOp(final String op, final double right)
   {
      arithBinaryError(op, valueType.type.toString(), ValueType.FLOAT.toString());
      return null;
   }
   
   // Concatenation ( <> )
   
   /** Concatenates two LayeValues as strings. */
   public LayeValue concat(final LayeValue right)
   {
      return LayeString.valueOf(asstring() + right.asstring());
   }
   
   // Addition ( + )
   
   /** Add two LayeValues. */
   public LayeValue add(final LayeValue right)
   {
      return infixOp("+", right);
   }
   
   /** Sum of this LayeValue and a long. */
   public LayeValue add(final long right)
   {
      return infixOp("+", right);
   }
   
   /** Sum of this LayeValue and a double. */
   public LayeValue add(final double right)
   {
      return infixOp("+", right);
   }
   
   /** Sum of a long and this LayeValue. */
   public LayeValue addRev(final long left)
   {
      arithBinaryError("+", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Sum of a double and this LayeValue. */
   public LayeValue addRev(final double left)
   {
      arithBinaryError("+", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Subtraction ( - )
   
   /** Subtract two LayeValues. */
   public LayeValue subtract(final LayeValue right)
   {
      return infixOp("-", right);
   }
   
   /** Difference of this LayeValue and a long. */
   public LayeValue subtract(final long right)
   {
      return infixOp("-", right);
   }
   
   /** Difference of this LayeValue and a double. */
   public LayeValue subtract(final double right)
   {
      return infixOp("-", right);
   }
   
   /** Difference of a long and this LayeValue. */
   public LayeValue subtractRev(final long left)
   {
      arithBinaryError("-", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Difference of a double and this LayeValue. */
   public LayeValue subtractRev(final double left)
   {
      arithBinaryError("-", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Multiplication ( * )
   
   /** Multiply two LayeValues. */
   public LayeValue multiply(final LayeValue right)
   {
      return infixOp("*", right);
   }
   
   /** Product of this LayeValue and a long. */
   public LayeValue multiply(final long right)
   {
      return infixOp("*", right);
   }
   
   /** Product of this LayeValue and a double. */
   public LayeValue multiply(final double right)
   {
      return infixOp("*", right);
   }
   
   /** Product of a long and this LayeValue. */
   public LayeValue multiplyRev(final long left)
   {
      arithBinaryError("*", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Product of a double and this LayeValue. */
   public LayeValue multiplyRev(final double left)
   {
      arithBinaryError("*", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Division ( / )
   
   /** Divide two LayeValues. */
   public LayeValue divide(final LayeValue right)
   {
      return infixOp("/", right);
   }
   
   /** Quotient of this LayeValue and a long. */
   public LayeValue divide(final long right)
   {
      return infixOp("/", right);
   }
   
   /** Quotient of this LayeValue and a double. */
   public LayeValue divide(final double right)
   {
      return infixOp("/", right);
   }
   
   /** Quotient of a long and this LayeValue. */
   public LayeValue divideRev(final long left)
   {
      arithBinaryError("/", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Quotient of a double and this LayeValue. */
   public LayeValue divideRev(final double left)
   {
      arithBinaryError("/", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Integer Division ( // )
   
   /** Integer divide two LayeValues. */
   public LayeValue intDivide(final LayeValue right)
   {
      return infixOp("//", right);
   }
   
   /** Integer quotient of this LayeValue and a long. */
   public LayeValue intDivide(final long right)
   {
      return infixOp("//", right);
   }
   
   /** Integer quotient of this LayeValue and a double. */
   public LayeValue intDivide(final double right)
   {
      return infixOp("//", right);
   }
   
   /** Integer quotient of a long and this LayeValue. */
   public LayeValue intDivideRev(final long left)
   {
      arithBinaryError("//", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Integer quotient of a double and this LayeValue. */
   public LayeValue intDivideRev(final double left)
   {
      arithBinaryError("//", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Modulo ( % )
   
   /** Modulo two LayeValues. */
   public LayeValue remainder(final LayeValue right)
   {
      return infixOp("%", right);
   }
   
   /** Remainder of this LayeValue and a long. */
   public LayeValue remainder(final long right)
   {
      return infixOp("%", right);
   }
   
   /** Remainder of this LayeValue and a double. */
   public LayeValue remainder(final double right)
   {
      return infixOp("%", right);
   }
   
   /** Remainder of a long and this LayeValue. */
   public LayeValue remainderRev(final long left)
   {
      arithBinaryError("%", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Remainder of a double and this LayeValue. */
   public LayeValue remainderRev(final double left)
   {
      arithBinaryError("%", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Power ( ^ )
   
   /** Power two LayeValues. */
   public LayeValue power(final LayeValue right)
   {
      return infixOp("^", right);
   }
   
   /** Power of this LayeValue and a long. */
   public LayeValue power(final long right)
   {
      return infixOp("^", right);
   }
   
   /** Power of this LayeValue and a double. */
   public LayeValue power(final double right)
   {
      return infixOp("^", right);
   }
   
   /** Power of a long and this LayeValue. */
   public LayeValue powerRev(final long left)
   {
      arithBinaryError("^", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   /** Power of a double and this LayeValue. */
   public LayeValue powerRev(final double left)
   {
      arithBinaryError("^", ValueType.FLOAT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // And ( & )
   
   /** And two LayeValues. */
   public LayeValue and(final LayeValue right)
   {
      return infixOp("&", right);
   }
   
   /** And of this LayeValue and a long. */
   public LayeValue and(final long right)
   {
      return infixOp("&", right);
   }
   
   /** And of a long and this LayeValue. */
   public LayeValue andRev(final long left)
   {
      arithBinaryError("&", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Or ( & )
   
   /** Or two LayeValues. */
   public LayeValue or(final LayeValue right)
   {
      return infixOp("|", right);
   }
   
   /** Or of this LayeValue and a long. */
   public LayeValue or(final long right)
   {
      return infixOp("|", right);
   }
   
   /** Or of a long and this LayeValue. */
   public LayeValue orRev(final long left)
   {
      arithBinaryError("|", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Xor ( ~ )
   
   /** Xor two LayeValues. */
   public LayeValue xor(final LayeValue right)
   {
      return infixOp("~", right);
   }
   
   /** Xor of this LayeValue and a long. */
   public LayeValue xor(final long right)
   {
      return infixOp("~", right);
   }
   
   /** Xor of a long and this LayeValue. */
   public LayeValue xorRev(final long left)
   {
      arithBinaryError("~", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Left shift ( << )
   
   /** Left shift two LayeValues. */
   public LayeValue shiftLeft(final LayeValue right)
   {
      return infixOp("<<", right);
   }
   
   /** Left shift of this LayeValue and a long. */
   public LayeValue shiftLeft(final long right)
   {
      return infixOp("<<", right);
   }
   
   /** Left shift of a long and this LayeValue. */
   public LayeValue shiftLeftRev(final long left)
   {
      arithBinaryError("<<", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Right shift ( << )
   
   /** Right shift two LayeValues. */
   public LayeValue shiftRight(final LayeValue right)
   {
      return infixOp(">>", right);
   }
   
   /** Right shift of this LayeValue and a long. */
   public LayeValue shiftRight(final long right)
   {
      return infixOp(">>", right);
   }
   
   /** Right shift of a long and this LayeValue. */
   public LayeValue shiftRightRev(final long left)
   {
      arithBinaryError(">>", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // Unsigned right shift ( >>> )
   
   /** Unsigned right shift two LayeValues. */
   public LayeValue shiftRightUnsigned(final LayeValue right)
   {
      return infixOp("<<<", right);
   }
   
   /** Unsigned right shift of this LayeValue and a long. */
   public LayeValue shiftRightUnsigned(final long right)
   {
      return infixOp("<<<", right);
   }
   
   /** Unsigned right shift of a long and this LayeValue. */
   public LayeValue shiftRightUnsignedRev(final long left)
   {
      arithBinaryError(">>>", ValueType.INT.type.toString(), valueType.type.toString());
      return null;
   }
   
   // ---------- Comparison ---------- //
   
   // TODO infixOps?
   
   public LayeValue equalTo(final LayeValue other)
   {
      return equalTo_b(other) ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public boolean equalTo_b(final LayeValue other)
   {
      return this == other;
   }
   
   public int compareTo(final LayeValue right)
   {
      return this.lessThan(right) == LayeValue.TRUE ? -1 : right.lessEqual(this) == LayeValue.TRUE ? 1 : 0;
   }
   
   // Less than
   
   /**
    * @return this < right
    */
   public LayeValue lessThan(final LayeValue right)
   {
      return infixOp("<", right);
   }
   
   /**
    * @return this < right
    */
   public boolean lessThan_b(final LayeValue right)
   {
      return lessThan(right).asbool();
   }
   
   /**
    * @return this < right
    */
   public LayeValue lessThan(final long right)
   {
      return infixOp("<", right);
   }
   
   /**
    * @return this < right
    */
   public boolean lessThan_b(final long right)
   {
      return lessThan(right).asbool();
   }
   
   /**
    * @return this < right
    */
   public LayeValue lessThan(final double right)
   {
      return infixOp("<", right);
   }
   
   /**
    * @return this < right
    */
   public boolean lessThan_b(final double right)
   {
      return lessThan(right).asbool();
   }
   
   // Less than or Equal
   
   /**
    * @return this <= right
    */
   public LayeValue lessEqual(final LayeValue right)
   {
      compError("<=", valueType.type.toString(), right.valueType.type.toString());
      return null;
   }
   
   /**
    * @return this <= right
    */
   public boolean lessEqual_b(final LayeValue right)
   {
      compError("<=", valueType.type.toString(), right.valueType.type.toString());
      return false;
   }
   
   /**
    * @return this <= right
    */
   public LayeValue lessEqual(final long right)
   {
      compError("<=", valueType.type.toString(), ValueType.INT.type.toString());
      return null;
   }
   
   /**
    * @return this <= right
    */
   public boolean lessEqual_b(final long right)
   {
      compError("<=", valueType.type.toString(), ValueType.INT.type.toString());
      return false;
   }
   
   /**
    * @return this <= right
    */
   public LayeValue lessEqual(final double right)
   {
      compError("<=", valueType.type.toString(), ValueType.FLOAT.type.toString());
      return null;
   }
   
   /**
    * @return this <= right
    */
   public boolean lessEqual_b(final double right)
   {
      compError("<=", valueType.type.toString(), ValueType.FLOAT.type.toString());
      return false;
   }
   
   // Greater than
   
   /**
    * @return this > right
    */
   public LayeValue greaterThan(final LayeValue right)
   {
      compError(">", valueType.type.toString(), right.valueType.type.toString());
      return null;
   }
   
   /**
    * @return this > right
    */
   public boolean greaterThan_b(final LayeValue right)
   {
      compError(">", valueType.type.toString(), right.valueType.type.toString());
      return false;
   }
   
   /**
    * @return this > right
    */
   public LayeValue greaterThan(final long right)
   {
      compError(">", valueType.type.toString(), ValueType.INT.type.toString());
      return null;
   }
   
   /**
    * @return this > right
    */
   public boolean greaterThan_b(final long right)
   {
      compError(">", valueType.type.toString(), ValueType.INT.type.toString());
      return false;
   }
   
   /**
    * @return this > right
    */
   public LayeValue greaterThan(final double right)
   {
      compError(">", valueType.type.toString(), ValueType.FLOAT.type.toString());
      return null;
   }
   
   /**
    * @return this > right
    */
   public boolean greaterThan_b(final double right)
   {
      compError(">", valueType.type.toString(), ValueType.FLOAT.type.toString());
      return false;
   }
   
   // Greater than or Equal
   
   /**
    * @return this >= right
    */
   public LayeValue greaterEqual(final LayeValue right)
   {
      compError(">=", valueType.type.toString(), right.valueType.type.toString());
      return null;
   }
   
   /**
    * @return this >= right
    */
   public boolean greaterEqual_b(final LayeValue right)
   {
      compError(">=", valueType.type.toString(), right.valueType.type.toString());
      return false;
   }
   
   /**
    * @return this >= right
    */
   public LayeValue greaterEqual(final long right)
   {
      compError(">=", valueType.type.toString(), ValueType.INT.type.toString());
      return null;
   }
   
   /**
    * @return this >= right
    */
   public boolean greaterEqual_b(final long right)
   {
      compError(">=", valueType.type.toString(), ValueType.INT.type.toString());
      return false;
   }
   
   /**
    * @return this >= right
    */
   public LayeValue greaterEqual(final double right)
   {
      compError(">=", valueType.type.toString(), ValueType.FLOAT.type.toString());
      return null;
   }
   
   /**
    * @return this >= right
    */
   public boolean greaterEqual_b(final double right)
   {
      compError(">=", valueType.type.toString(), ValueType.FLOAT.type.toString());
      return false;
   }
   
   // ---------- Indexing ---------- //
   
   public void newSlotMutable(final LayeValue key, final LayeValue value)
   {
      throw LayeException.unsupportedOperation("cannot 'newSlot' on type " + valueType.type.toString());
   }
   
   public void newSlot(final LayeValue key, final LayeValue value)
   {
      throw LayeException.unsupportedOperation("cannot 'newSlotConst' on type " + valueType.type.toString());
   }
   
   public LayeValue delSlot(final LayeValue key)
   {
      throw LayeException.unsupportedOperation("cannot 'delSlot' on type " + valueType.type.toString());
   }
   
   public LayeValue get(final LayeValue key)
   {
      throw LayeException.unsupportedOperation("cannot 'get' on type " + valueType.type.toString());
   }
   
   public void set(final LayeValue key, final LayeValue value)
   {
      throw LayeException.unsupportedOperation("cannot 'set' on type " + valueType.type.toString());
   }
   
   // ---------- Type ---------- //
   
   public LayeBool istypeof(final LayeValue type)
   {
      if (type.istype())
      {
         return valueType.type == type ? LayeValue.TRUE : LayeValue.FALSE;
      }
      else if (valueType == ValueType.TYPE && type == LayeValue.NULL)
      {
         return LayeValue.TRUE;
      }
      return LayeValue.FALSE;
   }
   
   public LayeValue newinstance(final LayeValue... argList)
   {
      throw LayeException.unsupportedOperation("cannot create a new instance of type " + valueType.type.toString());
   }
   
   public LayeValue newinstance(final String name, final LayeValue... argList)
   {
      throw LayeException.unsupportedOperation("cannot create a new instance of type " + valueType.type.toString());
   }
   
}
