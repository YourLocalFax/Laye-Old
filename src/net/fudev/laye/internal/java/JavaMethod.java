package net.fudev.laye.internal.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.values.LayeValue;

public final class JavaMethod
{
   private static final class LayeTypeSignature
   {
      private final ValueType[] types;
      
      public LayeTypeSignature (final ValueType[] types)
      {
         this.types = types;
      }
      
      @Override
      public int hashCode ()
      {
         final int prime = 31;
         int result = 1;
         result = prime * result + Arrays.hashCode(types);
         return result;
      }
      
      @Override
      public boolean equals (final Object obj)
      {
         if (this == obj)
         {
            return true;
         }
         if (obj == null)
         {
            return false;
         }
         if (getClass() != obj.getClass())
         {
            return false;
         }
         @SuppressWarnings("unchecked")
         final LayeTypeSignature other = (LayeTypeSignature) obj;
         if (!Arrays.equals(types, other.types))
         {
            return false;
         }
         return true;
      }
      
      @Override
      public String toString ()
      {
         return Arrays.toString(types);
      }
   }
   
   private static LayeTypeSignature createTypeSignature (
         final Class<?>... argTypes)
   {
      final Vector<ValueType> types = new Vector<>();
      for (final Class<?> type : argTypes)
      {
         types.addElement(ValueType.getFromClass(type));
      }
      return new LayeTypeSignature(types.toArray(new ValueType[types.size()]));
   }
   
   private static LayeTypeSignature createTypeSignature (
         final LayeValue... argValues)
   {
      final Vector<ValueType> types = new Vector<>();
      for (final LayeValue value : argValues)
      {
         types.addElement(value.valueType);
      }
      return new LayeTypeSignature(types.toArray(new ValueType[types.size()]));
   }
   
   private static Object toJavaObject (final ValueType type,
         final Class<?> javaType, final LayeValue arg)
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
         default:
            throw new LayeException("TODO");
      }
      throw new LayeException("TODO");
   }
   
   private final Map<LayeTypeSignature, Method> methods = new HashMap<>();
   
   public JavaMethod ()
   {
   }
   
   public void addMethod (final Method method)
   {
      if (method.isVarArgs())
      {
         throw new LayeException(
               "VarArgs methods are not supported currently.");
      }
      final LayeTypeSignature sig = createTypeSignature(
            method.getParameterTypes());
      if (methods.containsKey(sig))
      {
         throw new LayeException("Signature for method " + method.toString()
         + " conflicts with method " + methods.get(sig).toString());
      }
      methods.put(sig, method);
   }
   
   public Object invoke (final Object owner, final LayeValue... args)
   {
      // TODO optimize plz
      final LayeTypeSignature sig = createTypeSignature(args);
      final Method method = methods.get(sig);
      if (method == null)
      {
         throw new LayeException(
               "No suitable method found for argument types " + sig.toString());
      }
      final Class<?>[] methodArgs = method.getParameterTypes();
      final Object[] javaArgs = new Object[args.length];
      for (int i = 0; i < args.length; i++)
      {
         final Class<?> methodArg = methodArgs[i];
         final LayeValue arg = args[i];
         javaArgs[i] = toJavaObject(sig.types[i], methodArg, arg);
      }
      try
      {
         return method.invoke(owner, javaArgs);
      }
      catch (final IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e)
      {
         e.printStackTrace();
         return null;
      }
   }
}
