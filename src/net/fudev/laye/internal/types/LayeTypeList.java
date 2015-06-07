package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeValue;

public final class LayeTypeList extends LayeValueType
{
   public static final LayeValueType TYPE = new LayeTypeList();
   
   private final String name = "List";
   private final int hashCode = name.hashCode() * 43;
   
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
