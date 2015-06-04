package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public final class LayeBool extends LayeValue
{
   static final LayeBool TRUE_INTERNAL = new LayeBool(true);
   static final LayeBool FALSE_INTERNAL = new LayeBool(false);

   public final boolean value;

   LayeBool(final boolean value)
   {
      super(ValueType.BOOL);
      this.value = value;
   }

   @Override
   public LayeBool tobool()
   {
      return this;
   }

   @Override
   public boolean asbool()
   {
      return value;
   }

   @Override
   public String asstring()
   {
      return value ? "true" : "false";
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }

   @Override
   public int hashCode()
   {
      return Boolean.hashCode(value);
   }
}
