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
