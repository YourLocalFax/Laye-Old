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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.values.LayeValue;

public final class JavaMethod
{
   private final Map<LayeTypeSignature, Method> methods = new HashMap<>();
   
   public JavaMethod()
   {
   }
   
   public void addMethod(final Method method)
   {
      if (method.isVarArgs())
      {
         throw new LayeException("VarArgs methods are not supported currently.");
      }
      final LayeTypeSignature sig = LayeTypeSignature.createTypeSignature(method.getParameterTypes());
      if (methods.containsKey(sig))
      {
         throw new LayeException("Signature for method " + method.toString() + " conflicts with method "
               + methods.get(sig).toString());
      }
      methods.put(sig, method);
   }
   
   public Object invoke(final Object owner, final LayeValue... args)
   {
      // TODO optimize plz
      final LayeTypeSignature sig = LayeTypeSignature.createTypeSignature(args);
      final Method method = methods.get(sig);
      if (method == null)
      {
         throw new LayeException("No suitable method found for argument types " + sig.toString());
      }
      final Class<?>[] javaTypes = method.getParameterTypes();
      final Object[] javaArgs = JUtil.toJavaObjectArrayFromArgs(sig, args, javaTypes);
      try
      {
         return method.invoke(owner, javaArgs);
      }
      catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
      {
         e.printStackTrace();
         return null;
      }
   }
}
