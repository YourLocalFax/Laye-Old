package net.fudev.laye.internal;

import net.fudev.laye.internal.types.LayeTypeBool;
import net.fudev.laye.internal.types.LayeTypeFloat;
import net.fudev.laye.internal.types.LayeTypeFunction;
import net.fudev.laye.internal.types.LayeTypeInt;
import net.fudev.laye.internal.types.LayeTypeList;
import net.fudev.laye.internal.types.LayeTypeNull;
import net.fudev.laye.internal.types.LayeTypeNumber;
import net.fudev.laye.internal.types.LayeTypeReference;
import net.fudev.laye.internal.types.LayeTypeStream;
import net.fudev.laye.internal.types.LayeTypeString;
import net.fudev.laye.internal.types.LayeTypeTable;
import net.fudev.laye.internal.types.LayeTypeUserdata;
import net.fudev.laye.internal.values.LayeType;

public enum ValueType
{
   NULL(LayeTypeNull.TYPE),
   BOOL(LayeTypeBool.TYPE),
   NUMBER(LayeTypeNumber.TYPE),
   INT(LayeTypeInt.TYPE),
   FLOAT(LayeTypeFloat.TYPE),
   STRING(LayeTypeString.TYPE),
   TABLE(LayeTypeTable.TYPE),
   LIST(LayeTypeList.TYPE),
   STREAM(LayeTypeStream.TYPE),
   FUNCTION(LayeTypeFunction.TYPE),
   // TODO remove Reference type?
   REFERENCE(LayeTypeReference.TYPE),
   USERDATA(LayeTypeUserdata.TYPE),
   TYPE(null),
   INSTANCE(null);

   public final LayeType type;

   ValueType(final LayeType type)
   {
      this.type = type;
   }

   @Override
   public String toString()
   {
      return type == null ? "<no-type>" : type.asstring();
   }

}
