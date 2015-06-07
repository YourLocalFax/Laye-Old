package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public abstract class LayeNumber extends LayeValue
{
   
   LayeNumber (final ValueType type)
   {
      super(type);
   }
   
   @Override
   public int hashCode ()
   {
      return isint() ? Long.hashCode(aslong()) : Double.hashCode(asdouble());
   }
   
   @Override
   public boolean equalTo_b (final LayeValue other)
   {
      if (other.isfloat() || other.isint())
      {
         return Double.compare(other.asdouble(), asdouble()) == 0;
      }
      return false;
   }
   
   @Override
   public abstract byte asbyte ();
   
   @Override
   public abstract short asshort ();
   
   @Override
   public abstract int asint ();
   
   @Override
   public abstract long aslong ();
   
   @Override
   public abstract float asfloat ();
   
   @Override
   public abstract double asdouble ();
   
}
