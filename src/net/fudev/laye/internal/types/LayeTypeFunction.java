package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeFunction extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeFunction();
   
   private final String name = "Function";
   private final int hashCode = name.hashCode() * 73;
   
   @Override
   public String asstring ()
   {
      return name;
   }
   
   @Override
   public int hashCode ()
   {
      return hashCode;
   }
   
   @Override
   public boolean equalTo_b (final LayeValue other)
   {
      return other == this;
   }
}
