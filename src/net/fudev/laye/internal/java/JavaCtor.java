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
package net.fudev.laye.internal.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.values.LayeValue;

public final class JavaCtor
{
   private final Map<LayeTypeSignature, Constructor<?>> ctors = new HashMap<>();
   
   public JavaCtor()
   {
   }
   
   public void addConstructor(final Constructor<?> ctor)
   {
      if (ctor.isVarArgs())
      {
         throw new LayeException("VarArgs methods are not supported currently.");
      }
      final LayeTypeSignature sig = LayeTypeSignature.createTypeSignature(ctor.getParameterTypes());
      if (ctors.containsKey(sig))
      {
         throw new LayeException("Signature for method " + ctor.toString() + " conflicts with method "
               + ctors.get(sig).toString());
      }
      ctors.put(sig, ctor);
   }
   
   public Object newInstance(final LayeValue... args)
   {
      // TODO optimize plz
      final LayeTypeSignature sig = LayeTypeSignature.createTypeSignature(args);
      final Constructor<?> ctor = ctors.get(sig);
      if (ctor == null)
      {
         throw new LayeException("No suitable method found for argument types " + sig.toString());
      }
      final Class<?>[] javaTypes = ctor.getParameterTypes();
      final Object[] javaArgs = JUtil.toJavaObjectArrayFromArgs(sig, args, javaTypes);
      try
      {
         return ctor.newInstance(javaArgs);
      }
      catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | InstantiationException e)
      {
         e.printStackTrace();
         return null;
      }
   }
}
