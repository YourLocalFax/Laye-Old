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

import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.values.LayeFloat;
import net.fudev.laye.internal.values.LayeFunction;
import net.fudev.laye.internal.values.LayeInt;
import net.fudev.laye.internal.values.LayeJavaType;
import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeValue;

public final class LayeScriptEngine extends AbstractScriptEngine implements ScriptEngine, Compilable
{
   public final class LayeScript extends CompiledScript
   {
      
      private final LayeFunction func;
      
      LayeScript(final LayeFunction func)
      {
         this.func = func;
      }
      
      @Override
      public LayeValue eval() throws ScriptException
      {
         return eval(getContext());
      }
      
      @Override
      public LayeValue eval(final Bindings bindings) throws ScriptException
      {
         final ScriptContext context = getContext();
         return eval(((LayeContext) context).root, bindings);
      }
      
      @Override
      public LayeValue eval(final ScriptContext context) throws ScriptException
      {
         return eval(((LayeContext) context).root, context.getBindings(ScriptContext.ENGINE_SCOPE));
      }
      
      private LayeValue eval(final Root root, final Bindings b)
      {
         func.setRoot(root);
         return func.call();
      }
      
      @Override
      public ScriptEngine getEngine()
      {
         return LayeScriptEngine.this;
      }
      
   }
   
   private final ScriptEngineFactory factory;
   
   private final LayeContext context;
   
   LayeScriptEngine(final LayeScriptEngineFactory factory, final ClassLoader classLoader)
   {
      this.factory = factory;
      
      context = new LayeContext();
      setContext(context);
   }
   
   @Override
   protected ScriptContext getScriptContext(final Bindings nn)
   {
      throw new IllegalStateException("SkaredalScriptEngine should not be allocating contexts.");
   }
   
   public @Override Bindings createBindings()
   {
      return new Root();
   }
   
   public @Override Object eval(final String script, final ScriptContext context) throws ScriptException
   {
      return compile(new StringReader(script)).eval(context);
   }
   
   public @Override Object eval(final Reader reader, final ScriptContext context) throws ScriptException
   {
      return compile(reader).eval(context);
   }
   
   public @Override Object eval(final String script, final Bindings bindings) throws ScriptException
   {
      return eval(new StringReader(script), bindings);
   }
   
   public @Override Object eval(final Reader reader, final Bindings bindings) throws ScriptException
   {
      return compile(reader).eval(context.root, bindings);
   }
   
   public @Override ScriptEngineFactory getFactory()
   {
      return factory;
   }
   
   public @Override LayeScript compile(final String script) throws ScriptException
   {
      return compile(new StringReader(script));
   }
   
   public @Override LayeScript compile(final Reader reader) throws ScriptException
   {
      try
      {
         final LayeFunction func = context.root.load(reader, context);
         return new LayeScript(func);
      }
      catch (final LayeException err)
      {
         // TODO catch exceptionsss
         err.printStackTrace();
      }
      return null;
   }
   
   public void put(final String key, final long value)
   {
      put(key, LayeInt.valueOf(value));
   }
   
   public void put(final String key, final double value)
   {
      put(key, LayeFloat.valueOf(value));
   }
   
   public void put(final String key, final String value)
   {
      put(key, LayeString.valueOf(value));
   }
   
   public void put(final String key, final Class<?> value)
   {
      put(key, LayeJavaType.get(value));
   }
   
   @Override
   public void put(final String key, final Object value)
   {
      if (!(value instanceof LayeValue))
      {
         throw new IllegalArgumentException("value must be an instance of LayeValue");
      }
      context.root.put(key, (LayeValue) value);
   }
   
   @Override
   public LayeValue get(final String key)
   {
      return context.root.get(key);
   }
   
}
