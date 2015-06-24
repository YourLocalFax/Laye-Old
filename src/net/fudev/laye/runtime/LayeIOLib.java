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
