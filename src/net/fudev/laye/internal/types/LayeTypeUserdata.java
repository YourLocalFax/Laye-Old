package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeUserdata extends LayeType
{
   public static final LayeType TYPE = new LayeTypeUserdata();

   private LayeTypeUserdata()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Userdata";
   }
}
