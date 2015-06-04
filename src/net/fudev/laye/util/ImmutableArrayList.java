package net.fudev.laye.util;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

public class ImmutableArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable
{

   private static final long serialVersionUID = 325689144664986201L;

   private final E[] values;
   private final int length;

   public ImmutableArrayList(final E[] values)
   {
      this.length = values.length;
      this.values = Arrays.copyOf(values, this.length);
   }

   public @Override E get(final int index)
   {
      if (index < 0 || index >= this.length)
      {
         throw new IndexOutOfBoundsException(Integer.toString(index));
      }
      return this.values[index];
   }

   public @Override int size()
   {
      return this.length;
   }

}
