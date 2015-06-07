package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public class LayeTypeNumber extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeNumber();
   
   private final String name = "Number";
   private final int hashCode = name.hashCode() * 31;
   
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
