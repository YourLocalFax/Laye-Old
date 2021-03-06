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
