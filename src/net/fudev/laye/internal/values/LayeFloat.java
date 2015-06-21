package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public final class LayeFloat extends LayeNumber
{
   
   public static LayeFloat valueOf(final float value)
   {
      return new LayeFloat(value);
   }
   
   public static LayeFloat valueOf(final double value)
   {
      return new LayeFloat(value);
   }
   
   public final double value;
   
   private LayeFloat(final double value)
   {
      super(ValueType.FLOAT);
      this.value = value;
   }
   
   @Override
   public LayeFloat checkfloat()
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
      return LayeInt.valueOf((long) value);
   }
   
   public @Override LayeFloat tofloat()
   {
      return this;
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
      return (long) value;
   }
   
   @Override
   public float asfloat()
   {
      return (float) value;
   }
   
   @Override
   public double asdouble()
   {
      return value;
   }
   
   public @Override LayeString tostring()
   {
      return LayeString.valueOf(Double.toString(value));
   }
   
   public @Override String asstring()
   {
      return Double.toString(value);
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (other.isfloat())
      {
         return value == other.asdouble();
      }
      return false;
   }
   
   @Override
   public int hashCode()
   {
      return Double.hashCode(value);
   }
   
   // ---------- UNARY OPERATORS ---------- //
   
   public @Override LayeValue posit()
   {
      return this;
   }
   
   public @Override LayeValue negate()
   {
      return LayeFloat.valueOf(-value);
   }
   
   public @Override LayeValue not()
   {
      return value == 0.0f ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   // ---------- BINARY ARITHMETIC ---------- //
   
   // Addition ( + )
   
   public @Override LayeValue add(final LayeValue right)
   {
      return right.addRev(value);
   }
   
   public @Override LayeValue add(final long right)
   {
      return LayeFloat.valueOf(value + right);
   }
   
   public @Override LayeValue add(final double right)
   {
      return LayeFloat.valueOf(value + right);
   }
   
   public @Override LayeValue addRev(final long left)
   {
      return LayeFloat.valueOf(left + value);
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
      return LayeFloat.valueOf(value - right);
   }
   
   public @Override LayeValue subtract(final double right)
   {
      return LayeFloat.valueOf(value - right);
   }
   
   public @Override LayeValue subtractRev(final long left)
   {
      return LayeFloat.valueOf(left - value);
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
      return LayeFloat.valueOf(value * right);
   }
   
   public @Override LayeValue multiply(final double right)
   {
      return LayeFloat.valueOf(value * right);
   }
   
   public @Override LayeValue multiplyRev(final long left)
   {
      return LayeFloat.valueOf(left * value);
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
      return LayeFloat.valueOf(value / right);
   }
   
   public @Override LayeValue divide(final double right)
   {
      return LayeFloat.valueOf(value / right);
   }
   
   public @Override LayeValue divideRev(final long left)
   {
      return LayeFloat.valueOf(left / value);
   }
   
   public @Override LayeValue divideRev(final double left)
   {
      return LayeFloat.valueOf(left / value);
   }
   
   // Integer Division ( // )
   
   public @Override LayeValue intDivide(final LayeValue right)
   {
      return right.intDivideRev((long) value);
   }
   
   public @Override LayeValue intDivide(final long right)
   {
      return LayeFloat.valueOf((long) value / right);
   }
   
   public @Override LayeValue intDivide(final double right)
   {
      return LayeFloat.valueOf((long) value / (long) right);
   }
   
   public @Override LayeValue intDivideRev(final long left)
   {
      return LayeFloat.valueOf(left / (long) value);
   }
   
   public @Override LayeValue intDivideRev(final double left)
   {
      return LayeFloat.valueOf((long) left / (long) value);
   }
   
   // Modulo ( % )
   
   public @Override LayeValue remainder(final LayeValue right)
   {
      return right.remainderRev(value);
   }
   
   public @Override LayeValue remainder(final long right)
   {
      return LayeFloat.valueOf(value % right);
   }
   
   public @Override LayeValue remainder(final double right)
   {
      return LayeFloat.valueOf(value % right);
   }
   
   public @Override LayeValue remainderRev(final long left)
   {
      return LayeFloat.valueOf(left % value);
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
      return LayeFloat.valueOf(Math.pow(value, right));
   }
   
   public @Override LayeValue power(final double right)
   {
      return LayeFloat.valueOf(Math.pow(value, right));
   }
   
   public @Override LayeValue powerRev(final long left)
   {
      return LayeFloat.valueOf(Math.pow(left, value));
   }
   
   public @Override LayeValue powerRev(final double left)
   {
      return LayeFloat.valueOf(Math.pow(left, value));
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
   
   public @Override LayeValue lessThan(final LayeValue right)
   {
      return right.greaterThan(value);
   }
   
   public @Override LayeValue lessThan(final long right)
   {
      return value < right ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override LayeValue lessThan(final double right)
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
   
   public @Override LayeValue lessEqual(final LayeValue right)
   {
      return right.greaterEqual(value);
   }
   
   public @Override LayeValue lessEqual(final long right)
   {
      return value < right ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override LayeValue lessEqual(final double right)
   {
      return value < right ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override boolean lessEqual_b(final LayeValue right)
   {
      return right.greaterEqual_b(value);
   }
   
   public @Override boolean lessEqual_b(final long right)
   {
      return value < right;
   }
   
   public @Override boolean lessEqual_b(final double right)
   {
      return value < right;
   }
   
   // Greater than
   
   public @Override LayeValue greaterThan(final LayeValue right)
   {
      return right.lessThan(value);
   }
   
   public @Override LayeValue greaterThan(final long right)
   {
      return value > right ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override LayeValue greaterThan(final double right)
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
   
   public @Override LayeValue greaterEqual(final LayeValue right)
   {
      return right.lessEqual(value);
   }
   
   public @Override LayeValue greaterEqual(final long right)
   {
      return value >= right ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override LayeValue greaterEqual(final double right)
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
