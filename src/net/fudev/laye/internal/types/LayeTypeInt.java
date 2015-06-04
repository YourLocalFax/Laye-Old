package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeInt extends LayeTypeNumber
{
   public static final LayeType TYPE = new LayeTypeInt();

   private LayeTypeInt()
   {
      super();
   }

   @Override
   public String asstring()
   {
      return "Int";
   }
}
