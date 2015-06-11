package net.fudev.laye;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptException;

import net.fudev.laye.SkarendalScriptEngine.LayeScript;
import net.fudev.laye.internal.values.LayeValue;
import net.fudev.laye.util.Logger;
import net.fudev.laye.util.Logger.LoggerLevel;

public final class SkarendalTest
{
   public static void main(final String[] args)
   {
      Logger.setLoggerLevel(LoggerLevel.NONE);
      
      final SkarendalScriptEngineFactory factory = new SkarendalScriptEngineFactory();
      final SkarendalScriptEngine engine = factory.getScriptEngine();
      
      engine.put("Static", StaticTypeTest.class);
      engine.put("Vec2", Vec2.class);
      
      try (final InputStream scriptInputStream = SkarendalTest.class
            .getResourceAsStream("/main.laye"))
      {
         final InputStreamReader scriptReader = new InputStreamReader(
               scriptInputStream);
         final LayeScript script = engine.compile(scriptReader);
         
         final LayeValue result = script.eval();
         System.out.println(result);
      }
      catch (final IOException | ScriptException e)
      {
         e.printStackTrace();
      }
   }
   
   private SkarendalTest()
   {
   }
}
