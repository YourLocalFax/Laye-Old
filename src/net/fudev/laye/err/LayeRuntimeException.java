package net.fudev.laye.err;

public final class LayeRuntimeException extends RuntimeException
{
   
   private static String constructMessage (final String message, final String fileName, final int line)
   {
      return message + "\n\tin " + fileName + (line > 0 ? " (" + line + ")" : "");
   }
   
   private static final long serialVersionUID = 2746804479342721270L;
   
   public final String fileName;
   public final int line;
   
   public LayeRuntimeException (final String fileName, final int line, final String message)
   {
      super(LayeRuntimeException.constructMessage(message, fileName, line));
      this.fileName = fileName;
      this.line = line;
   }
   
   public LayeRuntimeException (final String fileName, final int line, final LayeRuntimeException previous)
   {
      super(LayeRuntimeException.constructMessage(previous.getMessage(), fileName, line));
      this.fileName = fileName;
      this.line = line;
   }
   
}
