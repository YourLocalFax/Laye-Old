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

public final class LayeContext extends SimpleScriptContext implements ScriptContext
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
   
   public LayeContext()
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
