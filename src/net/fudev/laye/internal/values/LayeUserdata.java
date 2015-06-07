package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public final class LayeUserdata extends LayeValue
{
   
   public static LayeUserdata valueOf (final Object value)
   {
      return new LayeUserdata(value);
   }
   
   public final Object value;
   
   private LayeUserdata (final Object value)
   {
      super(ValueType.USERDATA);
      this.value = value;
   }
   
   @Override
   public int hashCode ()
   {
      return value.hashCode();
   }
   
   @Override
   public boolean equalTo_b (final LayeValue other)
   {
      if (!other.isuserdata())
      {
         return false;
      }
      final LayeUserdata userdata = (LayeUserdata) other;
      return value.equals(userdata.value);
   }
   
   @Override
   public String asstring ()
   {
      return value.toString();
   }
}
