package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeString extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeString();
   
   private final String name = "String";
   private final int hashCode = name.hashCode() * 7;
   
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
