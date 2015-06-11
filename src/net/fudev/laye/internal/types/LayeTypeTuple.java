package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeTuple extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeTuple();
   
   private final String name = "Tuple";
   private final int hashCode = name.hashCode() * 71;
   
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
