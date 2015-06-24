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
