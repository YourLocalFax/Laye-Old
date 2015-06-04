package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeFunction extends LayeType
{
   public static final LayeType TYPE = new LayeTypeFunction();

   private LayeTypeFunction()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Function";
   }
}
