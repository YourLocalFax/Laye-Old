package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeString extends LayeType
{
   public static final LayeType TYPE = new LayeTypeString();

   private LayeTypeString()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "String";
   }
}
