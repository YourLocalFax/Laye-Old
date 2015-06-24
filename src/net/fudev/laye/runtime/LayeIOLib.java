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
import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeValue;
import net.fudev.laye.util.Util;

public final class LayeIOLib extends LayeStdLib
{
   protected LayeIOLib(final Vector<LayeStdLib> natives, final Root thisRoot)
   {
      super(natives, thisRoot);
      
      addFn("input", (root, parent, args) ->
      {
         root.getOut().print(Util.concatValues(args, " "));
         final String result = root.getIn().nextLine();
         return LayeString.valueOf(result);
      });
      
      addFn("print", (root, parent, args) ->
      {
         if (args.length == 0)
         {
            throw new LayeException("at least one argument expected in fn 'print'.");
         }
         root.getOut().print(Util.concatValues(args, " "));
         return LayeValue.NULL;
      });
      addFn("println", (root, parent, args) ->
      {
         root.getOut().println(Util.concatValues(args, " "));
         return LayeValue.NULL;
      });
      
      addFn("error", (root, parent, args) ->
      {
         if (args.length == 0)
         {
            throw new LayeException("at least one argument expected in fn 'error'.");
         }
         root.getErr().print(Util.concatValues(args, " "));
         return LayeValue.NULL;
      });
      addFn("errorln", (root, parent, args) ->
      {
         root.getErr().println(Util.concatValues(args, " "));
         return LayeValue.NULL;
      });
   }
}
