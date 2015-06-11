package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeJavaInstance extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeJavaInstance();
   
   private final String name = "JavaInstance";
   private final int hashCode = name.hashCode() * 47;
   
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
