package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeJavaType extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeJavaType();
   
   private final String name = "JavaClass";
   private final int hashCode = name.hashCode() * 41;
   
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
