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
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptException;

import net.fudev.laye.LayeScriptEngine.LayeScript;
import net.fudev.laye.util.Logger;
import net.fudev.laye.util.Logger.LoggerLevel;

public final class LayelTest
{
   public static void main(final String[] args)
   {
      Logger.setLoggerLevel(LoggerLevel.NONE);
      
      final LayeScriptEngineFactory factory = new LayeScriptEngineFactory();
      final LayeScriptEngine engine = factory.getScriptEngine();
      
      engine.put("Static", StaticTypeTest.class);
      engine.put("Vec2", Vec2.class);
      
      try (final InputStream scriptInputStream = LayelTest.class.getResourceAsStream("/main.laye"))
      {
         final InputStreamReader scriptReader = new InputStreamReader(scriptInputStream);
         final LayeScript script = engine.compile(scriptReader);
         
         script.eval();
      }
      catch (final IOException | ScriptException e)
      {
         e.printStackTrace();
      }
   }
   
   private LayelTest()
   {
   }
}
