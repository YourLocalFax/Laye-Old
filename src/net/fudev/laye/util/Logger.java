package net.fudev.laye.util;

public final class Logger
{
   public static enum LoggerLevel
   {
      NONE(0),
      ERROR(1),
      DEBUG(2);
      
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
   
   private static void log(final LoggerLevel at, final String group,
         final String message)
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
