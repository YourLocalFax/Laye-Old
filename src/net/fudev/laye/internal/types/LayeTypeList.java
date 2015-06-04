package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeList extends LayeType
{
   public static final LayeType TYPE = new LayeTypeList();

   private LayeTypeList()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "List";
   }
}
