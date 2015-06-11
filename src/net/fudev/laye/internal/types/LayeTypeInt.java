package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeInt extends LayeTypeNumber
{
   public static final LayeValueType TYPE = new LayeTypeInt();
   
   private final String name = "Int";
   private final int hashCode = name.hashCode() * 29;
   
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
