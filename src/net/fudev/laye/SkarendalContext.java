package net.fudev.laye;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

import net.fudev.laye.internal.Root;

public final class SkarendalContext extends SimpleScriptContext implements ScriptContext
{
   private static final class WriterOutputStream extends OutputStream
   {
      private final Writer writer;

      WriterOutputStream(final Writer writer)
      {
         this.writer = writer;
      }

      @Override
      public void write(final int b) throws IOException
      {
         writer.write(new String(new byte[] { (byte) b }));
      }
   }

   public final Root root;

   public SkarendalContext()
   {
      root = new Root();
      setBindings(root, ScriptContext.ENGINE_SCOPE);
   }

   @Override
   public void setBindings(final Bindings bindings, final int scope)
   {
      if (scope == ScriptContext.ENGINE_SCOPE)
      {
         root.setToBindings(bindings);
         super.setBindings(root, scope);
      }
      else
      {
         super.setBindings(bindings, scope);
      }
   }

   @Override
   public void setErrorWriter(final Writer writer)
   {
      root.setErr(new PrintStream(new WriterOutputStream(writer)));
   }

   @Override
   public void setReader(final Reader reader)
   {
      root.setIn(new Scanner(reader));
   }

   @Override
   public void setWriter(final Writer writer)
   {
      root.setOut(new PrintStream(new WriterOutputStream(writer)));
   }
}
