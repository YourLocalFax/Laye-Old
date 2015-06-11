package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeType extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeType();
   
   private final String name = "Type";
   private final int hashCode = name.hashCode() * 43;
   
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
