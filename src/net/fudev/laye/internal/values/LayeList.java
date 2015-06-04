package net.fudev.laye.internal.values;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.err.ListIndexOutOfBounds;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.util.Util;

public final class LayeList extends LayeValue
{
   public static final String METHOD_PUSH_BACK = "pushBack";

   static final LayeList EMPTY_LIST_INTERNAL = new LayeList(true, new LayeValue[0], 0);

   public static LayeList valueOf(final LayeValue... values)
   {
      return valueOf(false, values);
   }

   public static LayeList valueOf(final boolean isMutable, LayeValue... values)
   {
      final int length = values.length;
      values = Arrays.copyOf(values, length);
      for (int i = 0; i < length; i++)
      {
         if (values[i] == null)
         {
            values[i] = LayeValue.NULL;
         }
      }
      return new LayeList(isMutable, values, length);
   }

   public static LayeList valueOfUnsafe(final LayeValue... values)
   {
      return valueOfUnsafe(false, values);
   }

   public static LayeList valueOfUnsafe(final boolean isMutable, LayeValue... values)
   {
      final int length = values.length;
      values = Arrays.copyOf(values, length);
      return new LayeList(isMutable, values, length);
   }

   public static LayeList valueOf(final LayeValue[] values, final int offset, final int length)
   {
      return valueOf(false, values, offset, length);
   }

   public static LayeList valueOf(final boolean isMutable, LayeValue[] values, final int offset, final int length)
   {
      values = Arrays.copyOfRange(values, offset, offset + length);
      for (int i = 0; i < length; i++)
      {
         if (values[i] == null)
         {
            values[i] = LayeValue.NULL;
         }
      }
      return new LayeList(isMutable, values, length);
   }

   public static LayeList valueOfUnsafe(final LayeValue[] values, final int offset, final int length)
   {
      return valueOfUnsafe(false, values, offset, length);
   }

   public static LayeList valueOfUnsafe(final boolean isMutable, LayeValue[] values, final int offset, final int length)
   {
      values = Arrays.copyOfRange(values, offset, offset + length);
      return new LayeList(isMutable, values, length);
   }

   public static LayeList create(final boolean isMutable, final int initialSize)
   {
      return new LayeList(isMutable, initialSize, initialSize < 10 ? 10 : initialSize);
   }

   public final boolean isMutable;

   private LayeValue[] values;

   private int length;
   private LayeInt len = null;

   private int capacity;

   private LayeList(final boolean isMutable, final LayeValue[] values, final int length)
   {
      super(ValueType.LIST);
      this.isMutable = isMutable;
      this.values = Arrays.copyOf(values, length);
      this.length = capacity = length;
   }

   private LayeList(final boolean isMutable, final int initialSize, final int initialCapacity)
   {
      super(ValueType.LIST);
      this.isMutable = isMutable;
      values = Util.createValueArray(capacity = initialCapacity);
      length = initialSize;
   }

   private LayeList(final LayeList other)
   {
      this(other.isMutable, other.values, other.length);
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
      if (!other.islist())
      {
         return false;
      }
      final LayeList list = ((LayeList) other);
      final int length = this.length;
      return other == this || (length == list.length && Util.arrayEquals(values, list.values, length));
   }

   @Override
   public String asstring()
   {
      final StringBuilder builder = new StringBuilder().append(isMutable ? '[' : '(');
      final LayeValue[] values = this.values;
      for (int i = 0, length = this.length; i < length; i++)
      {
         if (i > 0)
         {
            builder.append(", ");
         }
         builder.append(values[i].asstring());
      }
      return builder.append(isMutable ? ']' : ')').toString();
   }

   @Override
   public int length()
   {
      return length;
   }

   // ========== Methods Laye will use ========== //

   @Override
   public LayeInt len()
   {
      if (len == null)
      {
         len = LayeInt.valueOf(length);
      }
      return len;
   }

   @Override
   public LayeValue get(final LayeValue index)
   {
      if (!index.isint())
      {
         throw new LayeException("list indices must be integers.");
      }
      return get(index.asint());
   }

   public synchronized LayeValue get(final int index)
   {
      if (index < 0 || index >= length)
      {
         throw new ListIndexOutOfBounds(index);
      }
      return values[index];
   }

   @Override
   public void set(final LayeValue index, final LayeValue value)
   {
      if (!index.isint())
      {
         throw new LayeException("list indices must be integers.");
      }
      set(index.asint(), value);
   }

   public synchronized void set(final int index, final LayeValue value)
   {
      if (!isMutable)
      {
         throw new LayeException("cannot edit an immutable list.");
      }
      if (index < 0 || index >= length)
      {
         throw new ListIndexOutOfBounds(index);
      }
      values[index] = value;
   }

   public synchronized void ensureCapacity(final int newCapacity)
   {
      if (capacity >= newCapacity)
      {
         // we good!
         return;
      }
      final LayeValue[] newValues = new LayeValue[capacity = newCapacity];
      System.arraycopy(values, 0, newValues, 0, length);
      for (int i = length; i < newCapacity; i++)
      {
         newValues[i] = LayeValue.NULL;
      }
      values = newValues;
   }

   public synchronized void insert(final int index, final LayeValue value)
   {
      ensureCapacity(length + 1);
      System.arraycopy(values, index, values, index + 1, length - index);
      values[index] = value;
      len = null;
   }

   public void pushFront(final LayeValue value)
   {
      insert(0, value);
   }

   public synchronized void pushBack(final LayeValue value)
   {
      ensureCapacity(length + 1);
      values[length++] = value;
      len = null;
   }

   public synchronized LayeValue remove(final int index)
   {
      final LayeValue result = values[index];
      final int num = length - index - 1;
      if (num > 0)
      {
         System.arraycopy(values, index + 1, values, index, num);
      }
      length--;
      len = null;
      return result;
   }

   public LayeValue popFront()
   {
      return remove(0);
   }

   public LayeValue popBack()
   {
      len = null;
      return values[--length];
   }

   @Override
   public LayeValue callChildMethod(final LayeValue key, final LayeValue... args)
   {
      if (key.isstring())
      {
         final String name = key.asstring();
         switch (name)
         {
            // TODO more list methods!
            case "pushBack": // list.pushBack(all..)
               for (final LayeValue arg : args)
               {
                  pushBack(arg);
               }
               return LayeValue.NULL;
         }
      }
      else
      {
         // TODO check if is int, then normal function or something
         throw new LayeException("TODO");
      }
      throw new LayeException("cannot index a list with type " + key.valueType);
   }

   public void forEach(final Consumer<LayeValue> func)
   {
      final LayeValue[] values = this.values;
      for (int i = 0, length = this.length; i < length; i++)
      {
         func.accept(values[i]);
      }
   }

   public void parallelForEach(final Consumer<LayeValue> func)
   {
      Stream.of(Arrays.copyOf(values, length))
            .parallel().forEach(func);
   }

   public Stream<LayeValue> stream()
   {
      return Stream.of(Arrays.copyOf(values, length));
   }

   public Stream<LayeValue> paralellStream()
   {
      return Stream.of(Arrays.copyOf(values, length)).parallel();
   }

   public LayeValue[] toArray()
   {
      return Arrays.copyOf(values, length);
   }
}
