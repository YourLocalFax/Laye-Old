package net.fudev.laye.internal;

import java.io.PrintStream;
import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import javax.script.Bindings;

import net.fudev.laye.SkarendalContext;
import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.parse.LayeParser;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;
import net.fudev.laye.internal.values.LayeFunction;
import net.fudev.laye.internal.values.LayeTable;
import net.fudev.laye.internal.values.LayeValue;
import net.fudev.laye.runtime.LayeStdLib;

public final class Root extends LayeTable implements Bindings
{
   private static final class LayeTableEntry implements Map.Entry<String, Object>
   {
      private final String key;
      private Object value;
      
      LayeTableEntry (final String key, final LayeValue value)
      {
         this.key = key;
         this.value = value;
      }
      
      @Override
      public String getKey ()
      {
         return key;
      }
      
      @Override
      public Object getValue ()
      {
         return value;
      }
      
      @Override
      public Object setValue (final Object value)
      {
         final Object old = this.value;
         this.value = value;
         return old;
      }
   }
   
   private PrintStream out = System.out;
   private PrintStream err = System.err;
   private Scanner in = new Scanner(System.in);
   
   public Root ()
   {
      LayeStdLib.register(this);
   }
   
   public Root (final Bindings bindings)
   {
      this();
      setToBindings(bindings);
   }
   
   public PrintStream getOut ()
   {
      return out;
   }
   
   public void setOut (final PrintStream out)
   {
      if (out == null)
      {
         throw new NullPointerException("out");
      }
      this.out = out;
   }
   
   public PrintStream getErr ()
   {
      return err;
   }
   
   public void setErr (final PrintStream err)
   {
      if (err == null)
      {
         throw new NullPointerException("err");
      }
      this.err = err;
   }
   
   public Scanner getIn ()
   {
      return in;
   }
   
   public void setIn (final Scanner in)
   {
      if (in == null)
      {
         throw new NullPointerException("in");
      }
      this.in = in;
   }
   
   public LayeFunction load (final Reader reader, final SkarendalContext context)
   {
      // TODO preprocessing, which puts all of the files together into a big
      // string basically
      final LayeParser parser = new LayeParser();
      final ASTFunctionPrototype nodes = parser.parse(reader);
      final LayeFunction func = new LayeFunction(nodes.generate(null, false), context.root);
      return func;
   }
   
   public void setToBindings (final Bindings bindings)
   {
      if (bindings == this)
      {
         return;
      }
      table.clear();
      LayeStdLib.register(this);
      for (final Map.Entry<String, Object> entry : bindings.entrySet())
      {
         final String key = entry.getKey();
         final Object value = entry.getValue();
         if (value instanceof LayeValue)
         {
            newSlot(key, (LayeValue) value);
         }
         else
         {
            // TODO own exception type
            throw new LayeException("values expected to be instances of LayeValue.");
         }
      }
   }
   
   // BINDINGS
   
   @Override
   public int size ()
   {
      return table.size();
   }
   
   @Override
   public boolean isEmpty ()
   {
      return table.isEmpty();
   }
   
   @Override
   public boolean containsValue (final Object value)
   {
      return table.containsValue(value);
   }
   
   @Override
   public void clear ()
   {
      table.clear();
   }
   
   @Override
   public Set<String> keySet ()
   {
      return table.keySet();
   }
   
   @Override
   public Collection<Object> values ()
   {
      final Vector<Object> result = new Vector<>();
      table.values().forEach(value -> result.add(value));
      return result;
   }
   
   @Override
   public Set<Map.Entry<String, Object>> entrySet ()
   {
      final Set<Map.Entry<String, Object>> result = new HashSet<>();
      table.entrySet().forEach(entry -> result.add(new LayeTableEntry(entry.getKey(), entry.getValue().getValue())));
      return result;
   }
   
   @Override
   public Object put (final String name, final Object value)
   {
      final LayeValue layeValue;
      if (value instanceof LayeValue)
      {
         layeValue = (LayeValue) value;
      }
      else
      {
         throw new IllegalArgumentException("value");
      }
      return put(name, layeValue);
   }
   
   public LayeValue put (final String name, final LayeValue value)
   {
      final LayeTable.Entry oldEntry = table.get(name);
      final LayeValue old;
      if (oldEntry == null)
      {
         old = LayeValue.NULL;
      }
      else
      {
         old = oldEntry.getValue();
      }
      newSlot(name, value);
      return old;
   }
   
   @Override
   public LayeValue delSlot (final LayeValue key)
   {
      return super.delSlot(key);
   }
   
   @Override
   public void putAll (final Map<? extends String, ? extends Object> toMerge)
   {
      toMerge.forEach(this::put);
   }
   
   @Override
   public boolean containsKey (final Object key)
   {
      if (key instanceof String)
      {
         return table.containsKey(key);
      }
      return false;
   }
   
   @Override
   public LayeValue get (final Object key)
   {
      if (key instanceof String)
      {
         return super.get((String) key);
      }
      throw new IllegalArgumentException("key");
   }
   
   @Override
   public Object remove (final Object key)
   {
      if (key instanceof String)
      {
         return table.remove(key);
      }
      throw new IllegalArgumentException("key");
   }
   
}
