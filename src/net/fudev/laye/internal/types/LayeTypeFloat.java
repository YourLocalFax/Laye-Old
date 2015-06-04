package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeFloat extends LayeTypeNumber
{
   public static final LayeType TYPE = new LayeTypeFloat();

   private LayeTypeFloat()
   {
      super();
   }

   @Override
   public String asstring()
   {
      return "Float";
   }
}
