package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeBool extends LayeType
{
   public static final LayeType TYPE = new LayeTypeBool();

   private LayeTypeBool()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Bool";
   }
}
