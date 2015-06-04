package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public class LayeTypeNumber extends LayeType
{
   public static final LayeType TYPE = new LayeTypeNumber();

   LayeTypeNumber()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Number";
   }
}
