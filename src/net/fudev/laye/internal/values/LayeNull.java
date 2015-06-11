package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public class LayeNull extends LayeValue
{
   static final LayeNull NULL_INTERNAL = new LayeNull();
   
   private LayeNull()
   {
      super(ValueType.NULL);
   }
   
   public @Override LayeBool equalTo(final LayeValue other)
   {
      return other == this ? LayeValue.TRUE : LayeValue.FALSE;
   }
   
   public @Override boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "null";
   }
   
   @Override
   public int hashCode()
   {
      return 0;
   }
   
   @Override
   public boolean equals(final Object obj)
   {
      return obj == this;
   }
}
