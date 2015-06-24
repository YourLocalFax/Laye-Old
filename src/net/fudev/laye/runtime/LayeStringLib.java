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
