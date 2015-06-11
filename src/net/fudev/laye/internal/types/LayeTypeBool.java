package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeBool extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeBool();
   
   private final String name = "Bool";
   private final int hashCode = name.hashCode() * 31;
   
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
