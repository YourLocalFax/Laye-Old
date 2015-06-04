package net.fudev.laye;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import net.fudev.laye.internal.Laye;
import net.fudev.laye.util.ImmutableArrayList;

public final class SkarendalScriptEngineFactory implements ScriptEngineFactory
{

   private static final ImmutableArrayList<String> names = SkarendalScriptEngineFactory.asImmutableList("skarendal", "Skarendal", "laye", "Laye");
   private static final ImmutableArrayList<String> mimeTypes = SkarendalScriptEngineFactory.asImmutableList();
   private static final ImmutableArrayList<String> extensions = SkarendalScriptEngineFactory.asImmutableList("laye");

   private static final ThreadLocal<SkarendalScriptEngine> engines = new ThreadLocal<>();

   private static final ImmutableArrayList<String> asImmutableList(final String... values)
   {
      return new ImmutableArrayList<>(values);
   }

   private static ClassLoader getAppClassLoader()
   {
      final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
      return (ccl == null) ? SkarendalScriptEngineFactory.class.getClassLoader() : ccl;
   }

   public SkarendalScriptEngineFactory()
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
      return SkarendalScriptEngineFactory.extensions;
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
      return SkarendalScriptEngineFactory.mimeTypes;
   }

   public @Override List<String> getNames()
   {
      return SkarendalScriptEngineFactory.names;
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

   public @Override SkarendalScriptEngine getScriptEngine()
   {
      SkarendalScriptEngine engine = SkarendalScriptEngineFactory.engines.get();
      if (engine == null)
      {
         engine = new SkarendalScriptEngine(this, SkarendalScriptEngineFactory.getAppClassLoader());
         SkarendalScriptEngineFactory.engines.set(engine);
      }
      return engine;
   }

}
