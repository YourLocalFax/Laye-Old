package net.fudev.laye.internal.types;

import net.fudev.laye.internal.values.LayeType;

public final class LayeTypeTable extends LayeType
{
   public static final LayeType TYPE = new LayeTypeTable();

   private LayeTypeTable()
   {
      super(null);
   }

   @Override
   public String asstring()
   {
      return "Table";
   }
}
