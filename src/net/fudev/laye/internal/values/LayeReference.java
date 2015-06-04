package net.fudev.laye.internal.values;

import net.fudev.laye.internal.UpValue;
import net.fudev.laye.internal.ValueType;

public abstract class LayeReference extends LayeValue
{
   LayeReference()
   {
      super(ValueType.REFERENCE);
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }

   public abstract LayeValue getValue();

   public abstract void setValue(LayeValue value);
}

class LayeLocalRef extends LayeReference
{
   private final LayeValue[] locals;
   private final int localIndex;

   public LayeLocalRef(final LayeValue[] locals, final int localIndex)
   {
      this.locals = locals;
      this.localIndex = localIndex;
   }

   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (!(other instanceof LayeLocalRef))
      {
         return false;
      }
      final LayeLocalRef ref = (LayeLocalRef) other;
      return locals == ref.locals && localIndex == ref.localIndex;
   }

   public @Override LayeValue getValue()
   {
      return locals[localIndex];
   }

   public @Override void setValue(final LayeValue value)
   {
      locals[localIndex] = value;
   }

   public @Override LayeString tostring()
   {
      return locals[localIndex].tostring();
   }

   public @Override String asstring()
   {
      return locals[localIndex].asstring();
   }

   public @Override LayeValue get(final LayeValue index)
   {
      return locals[localIndex].get(index);
   }

   public @Override void set(final LayeValue index, final LayeValue value)
   {
      locals[localIndex].set(index, value);
   }
}

class LayeUpValueRef extends LayeReference
{
   private final UpValue[] ups;
   private final int upIndex;

   public LayeUpValueRef(final UpValue[] outers, final int upIndex)
   {
      ups = outers;
      this.upIndex = upIndex;
   }

   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (!(other instanceof LayeUpValueRef))
      {
         return false;
      }
      final LayeUpValueRef ref = (LayeUpValueRef) other;
      return ups == ref.ups && upIndex == ref.upIndex;
   }

   public @Override LayeValue getValue()
   {
      return ups[upIndex].getValue();
   }

   public @Override void setValue(final LayeValue value)
   {
      ups[upIndex].setValue(value);
   }

   public @Override LayeString tostring()
   {
      return ups[upIndex].tostring();
   }

   public @Override String asstring()
   {
      return ups[upIndex].asstring();
   }

   public @Override LayeValue get(final LayeValue index)
   {
      return ups[upIndex].getValue().get(index);
   }

   public @Override void set(final LayeValue index, final LayeValue value)
   {
      ups[upIndex].getValue().set(index, value);
   }
}

// TODO split into TableRef, ArrayRef, and IndexRef (classes)
class LayeIndexRef extends LayeReference
{
   private final LayeValue object;
   private final LayeValue index;

   public LayeIndexRef(final LayeValue object, final LayeValue index)
   {
      this.object = object;
      this.index = index;
   }

   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (!(other instanceof LayeIndexRef))
      {
         return false;
      }
      final LayeIndexRef ref = (LayeIndexRef) other;
      return object == ref.object && index.equals(ref.index);
   }

   public @Override LayeValue getValue()
   {
      return object.get(index);
   }

   public @Override void setValue(final LayeValue value)
   {
      object.set(index, value);
   }

   public @Override LayeString tostring()
   {
      return object.get(index).tostring();
   }

   public @Override String asstring()
   {
      return object.get(index).asstring();
   }

   public @Override LayeValue get(final LayeValue index)
   {
      return object.get(index).get(index);
   }

   public @Override void set(final LayeValue index, final LayeValue value)
   {
      object.get(this.index).set(index, value);
   }
}
