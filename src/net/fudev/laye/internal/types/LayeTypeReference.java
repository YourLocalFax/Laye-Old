package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeReference extends LayeType
{
   public static final LayeType TYPE = new LayeTypeReference();

   private LayeTypeReference()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Reference";
   }
}
