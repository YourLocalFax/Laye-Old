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
