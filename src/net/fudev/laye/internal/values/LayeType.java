package net.fudev.laye.internal.values;

import net.fudev.laye.internal.TypePrototype;
import net.fudev.laye.internal.ValueType;

public class LayeType extends LayeValue
{

   final TypePrototype proto;

   final LayeType base;

   public LayeType(final TypePrototype proto)
   {
      super(ValueType.TYPE);
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
