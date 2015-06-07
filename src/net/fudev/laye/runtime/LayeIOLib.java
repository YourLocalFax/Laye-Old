package net.fudev.laye.runtime;

import java.util.Vector;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeValue;
import net.fudev.laye.util.Util;

public final class LayeIOLib extends LayeStdLib
{
   protected LayeIOLib (final Vector<LayeStdLib> natives, final Root thisRoot)
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
