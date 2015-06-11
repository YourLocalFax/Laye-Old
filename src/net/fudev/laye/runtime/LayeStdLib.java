package net.fudev.laye.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.values.LayeJavaFunction;
import net.fudev.laye.internal.values.LayeValue;

public abstract class LayeStdLib
{
   private static Vector<LayeStdLib> getNatives(final Root root)
   {
      final Vector<LayeStdLib> natives = new Vector<>();
      
      new LayeIOLib(natives, root);
      new LayeStringLib(natives, root);
      
      return natives;
   }
   
   public static void register(final Root root)
   {
      for (final LayeStdLib natives : getNatives(root))
      {
         // register k
         for (final Map.Entry<String, LayeValue> k : natives.k.entrySet())
         {
            root.newSlot(k.getKey(), k.getValue());
         }
      }
   }
   
   private final Map<String, LayeValue> k = new HashMap<>();
   protected final Root root;
   
   protected LayeStdLib(final Vector<LayeStdLib> natives, final Root root)
   {
      natives.addElement(this);
      this.root = root;
   }
   
   protected void addFn(final String key, final LayeJavaFunction.Function fn)
   {
      k.put(key, LayeJavaFunction.create(root, fn));
   }
   
   protected void addConst(final String key, final LayeValue k)
   {
      this.k.put(key, k);
   }
}
