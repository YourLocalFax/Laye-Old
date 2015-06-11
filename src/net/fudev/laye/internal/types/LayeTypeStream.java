package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeStream extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeStream();
   
   private final String name = "Stream";
   private final int hashCode = name.hashCode() * 61;
   
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
