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
package net.fudev.laye.util;

public final class Logger
{
   public static enum LoggerLevel
   {
      NONE(0), ERROR(1), DEBUG(2);
      
      final int level;
      
      LoggerLevel(final int level)
      {
         this.level = level;
      }
   }
   
   private static LoggerLevel level = LoggerLevel.NONE;
   
   public static void setLoggerLevel(LoggerLevel level)
   {
      if (level == null)
      {
         level = LoggerLevel.NONE;
      }
      Logger.level = level;
   }
   
   public static LoggerLevel getLoggerLevel()
   {
      return Logger.level;
   }
   
   private static void log(final LoggerLevel at, final String group, final String message)
   {
      if (at.level <= Logger.level.level)
      {
         final String output = group + ": " + message;
         if (at == LoggerLevel.ERROR)
         {
            System.err.println(output);
         }
         else
         {
            System.out.println(output);
         }
      }
   }
   
   public static void error(final String message)
   {
      Logger.error("ERROR", message);
   }
   
   public static void error(final String group, final String message)
   {
      Logger.log(LoggerLevel.ERROR, group, message);
   }
   
   public static void debug(final String message)
   {
      Logger.debug("DEBUG", message);
   }
   
   public static void debug(final String group, final String message)
   {
      Logger.log(LoggerLevel.DEBUG, group, message);
   }
   
   private Logger()
   {
   }
}
