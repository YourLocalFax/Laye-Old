/**
 * Copyright (C) 2015 Sekai Kyoretsuna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
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
