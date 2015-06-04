package net.fudev.laye.internal.values;

import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.ValueType;

public class LayeTable extends LayeValue
{
   public static final class Entry
   {
      final boolean isConst;
      LayeValue value;

      public Entry(final LayeValue value, final boolean isConst)
      {
         this.isConst = isConst;
         this.value = value;
      }

      public LayeValue getValue()
      {
         return value;
      }

      @Override
      public int hashCode()
      {
         final int prime = 31;
         int result = 1;
         result = prime * result + (isConst ? 1231 : 1237);
         result = prime * result + ((value == null) ? 0 : value.hashCode());
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
         final Entry other = (Entry) obj;
         if (isConst != other.isConst)
         {
            return false;
         }
         if (value == null)
         {
            if (other.value != null)
            {
               return false;
            }
         }
         else if (!value.equals(other.value))
         {
            return false;
         }
         return true;
      }

      @Override
      public String toString()
      {
         return value.toString();
      }
   }

   public static LayeTable valueOf(final Map<String, LayeValue> table)
   {
      final Map<String, Entry> result = new HashMap<>();
      for (final Map.Entry<String, LayeValue> entry : table.entrySet())
      {
         result.put(entry.getKey(), new Entry(entry.getValue(), false));
      }
      return new LayeTable(result);
   }

   protected final Map<String, Entry> table;

   public LayeTable()
   {
      super(ValueType.TABLE);
      table = new HashMap<>();
   }

   private LayeTable(final Map<String, Entry> table)
   {
      super(ValueType.TABLE);
      this.table = table;
   }

   private LayeTable(final LayeTable other)
   {
      super(ValueType.TABLE);
      table = new HashMap<>(other.table);
   }

   @Override
   public int hashCode()
   {
      return table.hashCode();
   }

   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (!other.istable())
      {
         return false;
      }
      final LayeTable table = (LayeTable) other;
      return this.table.equals(table.table);
   }

   @Override
   public String asstring()
   {
      final StringBuilder builder = new StringBuilder().append('{');
      boolean comma = false;
      for (final Map.Entry<String, Entry> entry : table.entrySet())
      {
         // commas woo
         if (comma)
         {
            builder.append(", ");
         }
         else
         {
            comma = true;
         }
         if (!entry.getValue().isConst)
         {
            builder.append("mut ");
         }
         builder.append(entry.getKey()).append('=');
         builder.append(entry.getValue().value.toString());
      }
      return builder.append('}').toString();
   }

   @Override
   public LayeValue callChildMethod(final LayeValue key, final LayeValue... args)
   {
      return callChildMethod(key.asstring(), args);
   }

   public LayeValue callChildMethod(final String key, final LayeValue... args)
   {
      final LayeValue value = get(key);
      return value.callAsMethod(this, args);
   }

   @Override
   public void newSlotMutable(final LayeValue key, final LayeValue value)
   {
      newSlotMutable(key.asstring(), value);
   }

   public void newSlotMutable(final LayeValue key, final Root root, final LayeJavaFunction.Function value)
   {
      newSlotMutable(key.asstring(), LayeJavaFunction.create(root, value));
   }

   public void newSlotMutable(final String key, final Root root, final LayeJavaFunction.Function value)
   {
      newSlotMutable(key, LayeJavaFunction.create(root, value));
   }

   public void newSlotMutable(final String key, final LayeValue value)
   {
      final Entry entry = table.get(key);
      if (entry != null && entry.isConst)
      {
         throw LayeException.constEntry(key);
      }
      table.put(key, new Entry(value, false));
   }

   @Override
   public void newSlot(final LayeValue key, final LayeValue value)
   {
      newSlot(key.asstring(), value);
   }

   public void newSlot(final LayeValue key, final Root root, final LayeJavaFunction.Function value)
   {
      newSlot(key.asstring(), LayeJavaFunction.create(root, value));
   }

   public void newSlot(final String key, final Root root, final LayeJavaFunction.Function value)
   {
      newSlot(key, LayeJavaFunction.create(root, value));
   }

   public void newSlot(final String key, final LayeValue value)
   {
      final Entry entry = table.get(key);
      if (entry != null && entry.isConst)
      {
         throw LayeException.constEntry(key);
      }
      table.put(key, new Entry(value, true));
   }

   @Override
   public LayeValue delSlot(final LayeValue key)
   {
      return delSlot(key.asstring());
   }

   public LayeValue delSlot(final String key)
   {
      final Entry entry = table.get(key);
      if (entry == null)
      {
         return LayeValue.NULL;
      }
      if (entry.isConst)
      {
         throw new LayeException("slot '" + key + "' is const, cannot modify.");
      }
      // remove afterwards just in case const above.
      table.remove(key);
      return entry.value;
   }

   @Override
   public LayeValue get(final LayeValue key)
   {
      return get(key.asstring());
   }

   public LayeValue get(final String key)
   {
      final Entry entry = table.get(key);
      if (entry == null)
      {
         throw new LayeException("key '" + key + "' is undefined.");
      }
      return entry.value;
   }

   @Override
   public void set(final LayeValue key, final LayeValue value)
   {
      set(key.asstring(), value);
   }

   public void set(final String key, final LayeValue value)
   {
      final Entry entry = table.get(key);
      if (entry == null)
      {
         throw new LayeException("key '" + key + "' is undefined.");
      }
      if (entry.isConst)
      {
         throw new LayeException("slot '" + key + "' is const, cannot modify.");
      }
      entry.value = value;
   }
}
