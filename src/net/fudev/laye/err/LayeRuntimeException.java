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
package net.fudev.laye.err;

public final class LayeRuntimeException extends RuntimeException
{
   
   private static String constructMessage(final String message, final String fileName, final int line)
   {
      return message + "\n\tin " + fileName + (line > 0 ? " (" + line + ")" : "");
   }
   
   private static final long serialVersionUID = 2746804479342721270L;
   
   public final String fileName;
   public final int line;
   
   public LayeRuntimeException(final String fileName, final int line, final String message)
   {
      super(LayeRuntimeException.constructMessage(message, fileName, line));
      this.fileName = fileName;
      this.line = line;
   }
   
   public LayeRuntimeException(final String fileName, final int line, final LayeRuntimeException previous)
   {
      super(LayeRuntimeException.constructMessage(previous.getMessage(), fileName, line));
      this.fileName = fileName;
      this.line = line;
   }
   
}
