/**
 * Copyright 2015 YourLocalFax
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
