package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public final class LayeInt extends LayeNumber
{

   public static LayeInt valueOf(final int value)
   {
      return new LayeInt(value);
   }

   public static LayeInt valueOf(final long value)
   {
      return new LayeInt(value);
   }

   public final long value;

   private LayeInt(final long value)
   {
      super(ValueType.INT);
      this.value = value;
   }

   @Override
   public LayeInt checkint()
   {
      return this;
   }

   @Override
   public LayeNumber checknumber()
   {
      return this;
   }

   public @Override LayeBool tobool()
   {
      return value != 0.0f ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override boolean asbool()
   {
      return value != 0.0f;
   }

   public @Override LayeInt toint()
   {
      return this;
   }

   public @Override LayeFloat tofloat()
   {
      return LayeFloat.valueOf((double) value);
   }

   @Override
   public byte asbyte()
   {
      return (byte) value;
   }

   @Override
   public short asshort()
   {
      return (short) value;
   }

   @Override
   public int asint()
   {
      return (int) value;
   }

   @Override
   public long aslong()
   {
      return value;
   }

   @Override
   public float asfloat()
   {
      return value;
   }

   @Override
   public double asdouble()
   {
      return value;
   }

   public @Override LayeString tostring()
   {
      return LayeString.valueOf(Long.toString(value));
   }

   public @Override String asstring()
   {
      return Long.toString(value);
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (other.isint())
      {
         return value == other.aslong();
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return Long.hashCode(value);
   }

   // ---------- UNARY OPERATORS ---------- //

   public @Override LayeValue posit()
   {
      return this;
   }

   public @Override LayeValue negate()
   {
      return LayeInt.valueOf(-value);
   }

   public @Override LayeValue not()
   {
      return value == 0 ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override LayeValue complement()
   {
      return LayeInt.valueOf(~value);
   }

   // ---------- BINARY ARITHMETIC ---------- //

   // Addition ( + )

   public @Override LayeValue add(final LayeValue right)
   {
      return right.addRev(value);
   }

   public @Override LayeValue add(final long right)
   {
      return LayeInt.valueOf(value + right);
   }

   public @Override LayeValue add(final double right)
   {
      return LayeFloat.valueOf(value + right);
   }

   public @Override LayeValue addRev(final long left)
   {
      return LayeInt.valueOf(left + value);
   }

   public @Override LayeValue addRev(final double left)
   {
      return LayeFloat.valueOf(left + value);
   }

   // Subtraction ( - )

   public @Override LayeValue subtract(final LayeValue right)
   {
      return right.subtractRev(value);
   }

   public @Override LayeValue subtract(final long right)
   {
      return LayeInt.valueOf(value - right);
   }

   public @Override LayeValue subtract(final double right)
   {
      return LayeFloat.valueOf(value - right);
   }

   public @Override LayeValue subtractRev(final long left)
   {
      return LayeInt.valueOf(left - value);
   }

   public @Override LayeValue subtractRev(final double left)
   {
      return LayeFloat.valueOf(left - value);
   }

   // Multiplication ( * )

   public @Override LayeValue multiply(final LayeValue right)
   {
      return right.multiplyRev(value);
   }

   public @Override LayeValue multiply(final long right)
   {
      return LayeInt.valueOf(value * right);
   }

   public @Override LayeValue multiply(final double right)
   {
      return LayeFloat.valueOf(value * right);
   }

   public @Override LayeValue multiplyRev(final long left)
   {
      return LayeInt.valueOf(left * value);
   }

   public @Override LayeValue multiplyRev(final double left)
   {
      return LayeFloat.valueOf(left * value);
   }

   // Division ( / )

   public @Override LayeValue divide(final LayeValue right)
   {
      return right.divideRev(value);
   }

   public @Override LayeValue divide(final long right)
   {
      return LayeInt.valueOf(value / right);
   }

   public @Override LayeValue divide(final double right)
   {
      return LayeFloat.valueOf(value / right);
   }

   public @Override LayeValue divideRev(final long left)
   {
      return LayeInt.valueOf(left / value);
   }

   public @Override LayeValue divideRev(final double left)
   {
      return LayeFloat.valueOf(left / value);
   }

   // Integer Division ( ~/ )

   public @Override LayeValue intDivide(final LayeValue right)
   {
      return right.intDivideRev(value);
   }

   public @Override LayeValue intDivide(final long right)
   {
      return LayeInt.valueOf(value / right);
   }

   public @Override LayeValue intDivide(final double right)
   {
      return LayeInt.valueOf(value / (int) right);
   }

   public @Override LayeValue intDivideRev(final long left)
   {
      return LayeInt.valueOf(left / value);
   }

   public @Override LayeValue intDivideRev(final double left)
   {
      return LayeInt.valueOf((int) left / value);
   }

   // Modulo ( % )

   public @Override LayeValue remainder(final LayeValue right)
   {
      return right.remainderRev(value);
   }

   public @Override LayeValue remainder(final long right)
   {
      return LayeInt.valueOf(value % right);
   }

   public @Override LayeValue remainder(final double right)
   {
      return LayeFloat.valueOf(value % right);
   }

   public @Override LayeValue remainderRev(final long left)
   {
      return LayeInt.valueOf(left % value);
   }

   public @Override LayeValue remainderRev(final double left)
   {
      return LayeFloat.valueOf(left % value);
   }

   // Power ( ^ )

   public @Override LayeValue power(final LayeValue right)
   {
      return right.powerRev(value);
   }

   public @Override LayeValue power(final long right)
   {
      return LayeFloat.valueOf((float) Math.pow(value, right));
   }

   public @Override LayeValue power(final double right)
   {
      return LayeFloat.valueOf((float) Math.pow(value, right));
   }

   public @Override LayeValue powerRev(final long left)
   {
      return LayeFloat.valueOf((float) Math.pow(left, value));
   }

   public @Override LayeValue powerRev(final double left)
   {
      return LayeFloat.valueOf((float) Math.pow(left, value));
   }

   // And ( & )

   public @Override LayeValue and(final LayeValue right)
   {
      return right.andRev(value);
   }

   public @Override LayeValue and(final long right)
   {
      return LayeInt.valueOf(value & right);
   }

   public @Override LayeValue andRev(final long left)
   {
      return LayeInt.valueOf(left & value);
   }

   // Or ( | )

   public @Override LayeValue or(final LayeValue right)
   {
      return right.orRev(value);
   }

   public @Override LayeValue or(final long right)
   {
      return LayeInt.valueOf(value | right);
   }

   public @Override LayeValue orRev(final long left)
   {
      return LayeInt.valueOf(left | value);
   }

   // Xor ( ~ )

   public @Override LayeValue xor(final LayeValue right)
   {
      return right.xorRev(value);
   }

   public @Override LayeValue xor(final long right)
   {
      return LayeInt.valueOf(value ^ right);
   }

   public @Override LayeValue xorRev(final long left)
   {
      return LayeInt.valueOf(left ^ value);
   }

   // Left shift ( << )

   public @Override LayeValue shiftLeft(final LayeValue right)
   {
      return right.shiftLeftRev(value);
   }

   public @Override LayeValue shiftLeft(final long right)
   {
      return LayeInt.valueOf(value << right);
   }

   public @Override LayeValue shiftLeftRev(final long left)
   {
      return LayeInt.valueOf(left << value);
   }

   // Right shift ( << )

   public @Override LayeValue shiftRight(final LayeValue right)
   {
      return right.shiftRightRev(value);
   }

   public @Override LayeValue shiftRight(final long right)
   {
      return LayeInt.valueOf(value >> right);
   }

   public @Override LayeValue shiftRightRev(final long left)
   {
      return LayeInt.valueOf(left >> value);
   }

   // Unsigned right shift ( >>> )

   public @Override LayeValue shiftRightUnsigned(final LayeValue right)
   {
      return right.shiftRightUnsignedRev(value);
   }

   public @Override LayeValue shiftRightUnsigned(final long right)
   {
      return LayeInt.valueOf(value >>> right);
   }

   public @Override LayeValue shiftRightUnsignedRev(final long left)
   {
      return LayeInt.valueOf(left >>> value);
   }

   // ---------- COMPARISON ---------- //

   @Override
   public LayeBool equalTo(final LayeValue right)
   {
      switch (right.valueType)
      {
         case INT:
            return value == ((LayeInt) right).value ? LayeValue.TRUE : LayeValue.FALSE;
         case FLOAT:
            return value == ((LayeFloat) right).value ? LayeValue.TRUE : LayeValue.FALSE;
         default:
            return LayeValue.FALSE;
      }
   }

   // Less than

   public @Override LayeBool lessThan(final LayeValue right)
   {
      return right.greaterThan(value);
   }

   public @Override LayeBool lessThan(final long right)
   {
      return value < right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override LayeBool lessThan(final double right)
   {
      return value < right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override boolean lessThan_b(final LayeValue right)
   {
      return right.greaterThan_b(value);
   }

   public @Override boolean lessThan_b(final long right)
   {
      return value < right;
   }

   public @Override boolean lessThan_b(final double right)
   {
      return value < right;
   }

   // Less than or Equal

   public @Override LayeBool lessEqual(final LayeValue right)
   {
      return right.greaterEqual(value);
   }

   public @Override LayeBool lessEqual(final long right)
   {
      return value <= right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override LayeBool lessEqual(final double right)
   {
      return value <= right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override boolean lessEqual_b(final LayeValue right)
   {
      return right.greaterEqual_b(value);
   }

   public @Override boolean lessEqual_b(final long right)
   {
      return value <= right;
   }

   public @Override boolean lessEqual_b(final double right)
   {
      return value <= right;
   }

   // Greater than

   public @Override LayeBool greaterThan(final LayeValue right)
   {
      return right.lessThan(value);
   }

   public @Override LayeBool greaterThan(final long right)
   {
      return value > right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override LayeBool greaterThan(final double right)
   {
      return value > right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override boolean greaterThan_b(final LayeValue right)
   {
      return right.lessThan_b(value);
   }

   public @Override boolean greaterThan_b(final long right)
   {
      return value > right;
   }

   public @Override boolean greaterThan_b(final double right)
   {
      return value > right;
   }

   // Greater than or Equal

   public @Override LayeBool greaterEqual(final LayeValue right)
   {
      return right.lessEqual(value);
   }

   public @Override LayeBool greaterEqual(final long right)
   {
      return value >= right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override LayeBool greaterEqual(final double right)
   {
      return value >= right ? LayeValue.TRUE : LayeValue.FALSE;
   }

   public @Override boolean greaterEqual_b(final LayeValue right)
   {
      return right.lessEqual_b(value);
   }

   public @Override boolean greaterEqual_b(final long right)
   {
      return value >= right;
   }

   public @Override boolean greaterEqual_b(final double right)
   {
      return value >= right;
   }

}
