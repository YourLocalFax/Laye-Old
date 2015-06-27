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
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptException;

import net.fudev.laye.LayeScriptEngine.LayeScript;
import net.fudev.laye.util.Logger;
import net.fudev.laye.util.Logger.LoggerLevel;

public final class LayeTest
{
   public static void main(final String[] args)
   {
      Logger.setLoggerLevel(LoggerLevel.NONE);
      
      final Timer timer = new Timer();
      
      final LayeScriptEngineFactory factory = new LayeScriptEngineFactory();
      final LayeScriptEngine engine = factory.getScriptEngine();
      
      engine.put("Static", StaticTypeTest.class);
      engine.put("Vec2", Vec2.class);
      
      try (final InputStream scriptInputStream = LayeTest.class.getResourceAsStream("/main.laye"))
      {
         final InputStreamReader scriptReader = new InputStreamReader(scriptInputStream);
         
         timer.start();
         final LayeScript script = engine.compile(scriptReader);
         timer.end("Compiler took {time_sec} seconds ({time_milli} milliseconds {time_nano} nanoseconds)");

         timer.start();
         script.eval();
         timer.end("Execution took {time_sec} seconds ({time_milli} milliseconds {time_nano} nanoseconds)");
      }
      catch (final IOException | ScriptException e)
      {
         e.printStackTrace();
      }
   }
   
   private LayeTest()
   {
   }
}
