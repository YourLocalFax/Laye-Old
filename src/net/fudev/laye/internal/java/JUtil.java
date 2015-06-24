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

import java.util.List;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.values.LayeJavaInstance;
import net.fudev.laye.internal.values.LayeJavaType;
import net.fudev.laye.internal.values.LayeValue;

public final class JUtil
{
   static Object[] toJavaObjectArrayFromArgs(final LayeTypeSignature sig, final LayeValue[] args,
         final Class<?>[] javaTypes)
   {
      final Object[] javaArgs = new Object[args.length];
      for (int i = 0; i < args.length; i++)
      {
         final Class<?> methodArg = javaTypes[i];
         final LayeValue arg = args[i];
         javaArgs[i] = JUtil.toJavaObject(sig.types[i], methodArg, arg);
      }
      return javaArgs;
   }
   
   static Object toJavaObject(final ValueType type, final Class<?> javaType, final LayeValue arg)
   {
      switch (type)
      {
         case NULL:
            return null;
         case BOOL:
            return arg.asbool();
         case INT:
         {
            if (javaType == byte.class || javaType == Byte.class)
            {
               return arg.asbyte();
            }
            else if (javaType == short.class || javaType == Short.class)
            {
               return arg.asshort();
            }
            else if (javaType == int.class || javaType == Integer.class)
            {
               return arg.asint();
            }
            else if (javaType == long.class || javaType == Long.class)
            {
               return arg.aslong();
            }
            else
            {
               throw new LayeException(javaType.toString());
            }
         }
         case FLOAT:
         {
            if (javaType == float.class || javaType == Float.class)
            {
               return arg.asfloat();
            }
            else if (javaType == double.class || javaType == Double.class)
            {
               return arg.asdouble();
            }
            break;
         }
         case STRING:
            return arg.asstring();
         case LIST:
         {
            if (List.class.isAssignableFrom(javaType))
            {
               final List<?> listJavaArg;
               try
               {
                  listJavaArg = (List<?>) javaType.newInstance();
               }
               catch (InstantiationException | IllegalAccessException e)
               {
                  e.printStackTrace();
                  return null;
               }
               // TODO fill the list.
               return javaType.cast(listJavaArg);
            }
            // TODO finish normal array checks
            throw new LayeException("TODO");
            // break;
         }
         case JAVA_TYPE:
            return ((LayeJavaType) arg).getJavaType();
         case JAVA_INSTANCE:
            return ((LayeJavaInstance) arg).object;
            
         default:
            throw new LayeException("TODO");
      }
      throw new LayeException("TODO");
   }
   
   private JUtil()
   {
   }
}
