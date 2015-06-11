package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeInstance extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeInstance();
   
   private final String name = "Instance";
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
