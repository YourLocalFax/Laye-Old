package net.fudev.laye.internal.java;

import java.util.Arrays;
import java.util.Vector;

import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.values.LayeValue;

final class LayeTypeSignature
{
   static LayeTypeSignature createTypeSignature(
         final Class<?>... argTypes)
   {
      final Vector<ValueType> types = new Vector<>();
      for (final Class<?> type : argTypes)
      {
         types.addElement(ValueType.getFromClass(type));
      }
      return new LayeTypeSignature(types.toArray(new ValueType[types.size()]));
   }
   
   static LayeTypeSignature createTypeSignature(
         final LayeValue... argValues)
   {
      final Vector<ValueType> types = new Vector<>();
      for (final LayeValue value : argValues)
      {
         types.addElement(value.valueType);
      }
      return new LayeTypeSignature(types.toArray(new ValueType[types.size()]));
   }
   
   final ValueType[] types;
   
   LayeTypeSignature(final ValueType[] types)
   {
      this.types = types;
   }
   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(types);
      return result;
   }
   
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final LayeTypeSignature other = (LayeTypeSignature) obj;
      if (!Arrays.equals(types, other.types))
      {
         return false;
      }
      return true;
   }
   
   @Override
   public String toString()
   {
      return Arrays.toString(types);
   }
}