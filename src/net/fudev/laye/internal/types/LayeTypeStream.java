package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeStream extends LayeType
{
   public static final LayeType TYPE = new LayeTypeStream();

   private LayeTypeStream()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Stream";
   }
}
