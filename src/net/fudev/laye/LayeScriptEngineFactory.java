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

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import net.fudev.laye.internal.Laye;
import net.fudev.laye.util.ImmutableArrayList;

public final class LayeScriptEngineFactory implements ScriptEngineFactory
{
   
   private static final ImmutableArrayList<String> names = LayeScriptEngineFactory.asImmutableList("skarendal",
         "Skarendal", "laye", "Laye");
   private static final ImmutableArrayList<String> mimeTypes = LayeScriptEngineFactory.asImmutableList();
   private static final ImmutableArrayList<String> extensions = LayeScriptEngineFactory.asImmutableList("laye");
   
   private static final ThreadLocal<LayeScriptEngine> engines = new ThreadLocal<>();
   
   private static final ImmutableArrayList<String> asImmutableList(final String... values)
   {
      return new ImmutableArrayList<>(values);
   }
   
   private static ClassLoader getAppClassLoader()
   {
      final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
      return ccl == null ? LayeScriptEngineFactory.class.getClassLoader() : ccl;
   }
   
   public LayeScriptEngineFactory()
   {
   }
   
   public @Override String getEngineName()
   {
      return (String) getParameter(ScriptEngine.ENGINE);
   }
   
   public @Override String getEngineVersion()
   {
      return (String) getParameter(ScriptEngine.ENGINE_VERSION);
   }
   
   public @Override List<String> getExtensions()
   {
      return LayeScriptEngineFactory.extensions;
   }
   
   public @Override String getLanguageName()
   {
      return (String) getParameter(ScriptEngine.LANGUAGE);
   }
   
   public @Override String getLanguageVersion()
   {
      return (String) getParameter(ScriptEngine.LANGUAGE_VERSION);
   }
   
   public @Override String getMethodCallSyntax(final String target, final String method, final String... args)
   {
      final StringBuilder builder = new StringBuilder(target).append('.').append(method).append('(');
      for (int i = 0, len = args.length, lenm1 = len - 1; i < len; i++)
      {
         builder.append(args[i]);
         if (i < lenm1)
         {
            builder.append(',');
         }
      }
      return builder.append(')').toString();
   }
   
   public @Override List<String> getMimeTypes()
   {
      return LayeScriptEngineFactory.mimeTypes;
   }
   
   public @Override List<String> getNames()
   {
      return LayeScriptEngineFactory.names;
   }
   
   public @Override String getOutputStatement(final String output)
   {
      return "println(" + output + ");";
   }
   
   public @Override Object getParameter(final String key)
   {
      switch (key)
      {
         case ScriptEngine.NAME:
            return "laye";
         case ScriptEngine.ENGINE:
            return "Forever Untitled Skarendal";
         case ScriptEngine.ENGINE_VERSION:
            return Version.VERSION;
         case ScriptEngine.LANGUAGE:
            return Laye.NAME;
         case ScriptEngine.LANGUAGE_VERSION:
            return Laye.VERSION;
         default:
            throw new IllegalArgumentException("Invalid key: '" + key + "'");
      }
   }
   
   public @Override String getProgram(final String... statements)
   {
      final StringBuilder sb = new StringBuilder();
      final int len = statements.length;
      for (int i = 0; i < len; i++)
      {
         if (i > 0)
         {
            sb.append('\n');
         }
         sb.append(statements[i]);
      }
      return sb.toString();
   }
   
   public @Override LayeScriptEngine getScriptEngine()
   {
      LayeScriptEngine engine = LayeScriptEngineFactory.engines.get();
      if (engine == null)
      {
         engine = new LayeScriptEngine(this, LayeScriptEngineFactory.getAppClassLoader());
         LayeScriptEngineFactory.engines.set(engine);
      }
      return engine;
   }
   
}
