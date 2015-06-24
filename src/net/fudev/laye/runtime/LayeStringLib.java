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

import java.util.Vector;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.values.LayeInt;
import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeTable;

public final class LayeStringLib extends LayeStdLib
{
   protected LayeStringLib(final Vector<LayeStdLib> natives, final Root thisRoot)
   {
      super(natives, thisRoot);
      
      final LayeTable string = new LayeTable();
      addConst("string", string);
      
      string.newSlot("chars", thisRoot, (root, parent, args) ->
      {
         final char[] chars = new char[args.length];
         for (int i = 0; i < args.length; i++)
         {
            final int value = args[i].asint();
            chars[i] = (char) value;
         }
         return LayeString.valueOf(new String(chars));
      });
      
      string.newSlot("code", thisRoot, (root, parent, args) ->
      {
         final String str;
         final int idx;
         if (args.length == 0)
         {
            throw new LayeException("string expected as first argument, no arguments given.");
         }
         else if (args.length == 1)
         {
            str = args[0].checkstring().value;
            idx = 0;
         }
         else
         {
            str = args[0].checkstring().value;
            idx = args[1].asint();
         }
         return LayeInt.valueOf(str.charAt(idx));
      });
   }
}
