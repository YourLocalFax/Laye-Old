package net.fudev.laye.internal.types;

import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.values.LayeValue;

public abstract class LayeValueType extends LayeValue
{
   protected LayeValueType ()
   {
      super(ValueType.TYPE);
   }
}
