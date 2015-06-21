package net.fudev.laye.internal;

import java.util.List;

import net.fudev.laye.internal.types.LayeTypeBool;
import net.fudev.laye.internal.types.LayeTypeFloat;
import net.fudev.laye.internal.types.LayeTypeFunction;
import net.fudev.laye.internal.types.LayeTypeInstance;
import net.fudev.laye.internal.types.LayeTypeInt;
import net.fudev.laye.internal.types.LayeTypeJavaInstance;
import net.fudev.laye.internal.types.LayeTypeJavaType;
import net.fudev.laye.internal.types.LayeTypeList;
import net.fudev.laye.internal.types.LayeTypeNull;
import net.fudev.laye.internal.types.LayeTypeNumber;
import net.fudev.laye.internal.types.LayeTypeReference;
import net.fudev.laye.internal.types.LayeTypeStream;
import net.fudev.laye.internal.types.LayeTypeString;
import net.fudev.laye.internal.types.LayeTypeTable;
import net.fudev.laye.internal.types.LayeTypeTuple;
import net.fudev.laye.internal.types.LayeTypeType;
import net.fudev.laye.internal.types.LayeValueType;

public enum ValueType
{
   NULL(LayeTypeNull.TYPE), BOOL(LayeTypeBool.TYPE), NUMBER(LayeTypeNumber.TYPE), INT(LayeTypeInt.TYPE), FLOAT(
         LayeTypeFloat.TYPE), STRING(LayeTypeString.TYPE), TUPLE(LayeTypeTuple.TYPE), TABLE(LayeTypeTable.TYPE), LIST(
         LayeTypeList.TYPE), STREAM(LayeTypeStream.TYPE), FUNCTION(LayeTypeFunction.TYPE),
   // TODO remove Reference type?
   REFERENCE(LayeTypeReference.TYPE), TYPE(LayeTypeType.TYPE), INSTANCE(LayeTypeInstance.TYPE), JAVA_TYPE(
         LayeTypeJavaType.TYPE), JAVA_INSTANCE(LayeTypeJavaInstance.TYPE);
   
   public static ValueType getFromClass(final Class<?> cls)
   {
      if (cls.isArray() || List.class.isAssignableFrom(cls))
      {
         return LIST;
      }
      // TODO other types
      if (cls == String.class)
      {
         return STRING;
      }
      else if (cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class
            || cls == byte.class || cls == short.class || cls == int.class || cls == long.class)
      {
         return INT;
      }
      else if (cls == Float.class || cls == Double.class || cls == float.class || cls == double.class)
      {
         return FLOAT;
      }
      else if (cls == Boolean.class)
      {
         return BOOL;
      }
      // TODO others
      return JAVA_INSTANCE;
   }
   
   public final LayeValueType type;
   
   ValueType(final LayeValueType type)
   {
      this.type = type;
   }
   
   @Override
   public String toString()
   {
      return type == null ? "<no-type>" : type.toString();
   }
}
