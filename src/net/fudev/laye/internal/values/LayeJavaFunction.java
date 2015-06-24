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
package net.fudev.laye.internal.values;

import net.fudev.laye.internal.Root;
import net.fudev.laye.internal.ValueType;

public final class LayeJavaFunction extends LayeValue
{
   /**
    * A Java function that can be called from a Laye script. The first argument, 'root', is the root table of the
    * calling function, and the following 'args' array contains the arguments passed from Laye.
    */
   @FunctionalInterface
   public static interface Function
   {
      LayeValue call(final Root root, final LayeValue parent, final LayeValue... args);
   }
   
   public static LayeJavaFunction create(final Root root, final Function function)
   {
      if (function == null)
      {
         throw new IllegalArgumentException("null");
      }
      return new LayeJavaFunction(root, function);
   }
   
   private final Root root;
   private final Function function;
   
   private LayeJavaFunction(final Root root, final Function function)
   {
      super(ValueType.FUNCTION);
      this.function = function;
      this.root = root;
   }
   
   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "native-func-TODO";
   }
   
   @Override
   public LayeValue call(final LayeValue... args)
   {
      return function.call(root, LayeValue.NULL, args);
   }
   
   @Override
   public LayeValue callAsMethod(final LayeValue parent, final LayeValue... args)
   {
      return function.call(root, parent, args);
   }
}
