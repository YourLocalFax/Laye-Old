package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeNull extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeNull();
   
   private final String name = "Null";
   private final int hashCode = name.hashCode() * 19;
   
   @Override
   public String asstring()
   {
      return name;
   }
   
   @Override
   public int hashCode()
   {
      return hashCode;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
}
