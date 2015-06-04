package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeNull extends LayeType
{
   public static final LayeType TYPE = new LayeTypeNull();

   private LayeTypeNull()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Null";
   }
}
