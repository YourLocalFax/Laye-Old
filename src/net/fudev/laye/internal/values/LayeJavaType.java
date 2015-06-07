package net.fudev.laye.internal.values;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.api.LayeMethod;
import net.fudev.laye.api.LayeType;
import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.java.JavaMethod;

public class LayeJavaType extends LayeValue
{
   private static final Map<Class<?>, LayeJavaType> types = new HashMap<>();
   
   public static LayeJavaType get (final Class<?> value)
   {
      LayeJavaType type = types.get(value);
      if (type == null)
      {
         type = new LayeJavaType(value);
         types.put(value, type);
      }
      return type;
   }
   
   private final Class<?> value;
   
   // TODO use a vector so we can check more methods of the same name
   private final Map<String, JavaMethod> staticMethods = new HashMap<>();
   
   public LayeJavaType (final Class<?> value)
   {
      super(ValueType.TYPE);
      this.value = value;
      
      //
      
      final LayeType typeAnnot = value.getDeclaredAnnotation(LayeType.class);
      if (typeAnnot == null)
      {
         throw new LayeException(
               "Class must be tagged with the @LayeType annotation.");
      }
      
      // Gather methods (instance and static) TODO instance methods
      
      final Method[] methods = value.getMethods();
      
      for (final Method method : methods)
      {
         final LayeMethod methodAnnot = method
               .getDeclaredAnnotation(LayeMethod.class);
         if (methodAnnot == null)
         {
            continue;
         }
         
         final int modifiers = method.getModifiers();
         final boolean isPublic = Modifier.isPublic(modifiers);
         if (!isPublic)
         {
            throw new LayeException("Method " + method.getName()
            + " must be public to be exposed to Laye.");
         }
         final boolean isStatic = Modifier.isStatic(modifiers);
         
         // Method name
         final String name = methodAnnot.name().isEmpty() ? method.getName()
               : methodAnnot.name();
         
         // TODO use a vector so we can check more methods of the same name
         if (isStatic)
         {
            JavaMethod javaMethod = staticMethods.get(name);
            if (javaMethod == null)
            {
               javaMethod = new JavaMethod();
               staticMethods.put(name, javaMethod);
            }
            javaMethod.addMethod(method);
         }
         else
         {
            throw new LayeException("NO INSTANCE METHODS YET");
         }
      }
   }
   
   @Override
   public int hashCode ()
   {
      return value.hashCode();
   }
   
   @Override
   public boolean equalTo_b (final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring ()
   {
      return "type:" + value.getName();
   }
   
   @Override
   public LayeValue callChildMethod (final LayeValue name,
         final LayeValue... args)
   {
      final String methodName = name.asstring();
      // TODO use a vector so we can check more methods of the same name
      final JavaMethod method = staticMethods.get(methodName);
      if (method == null)
      {
         throw new LayeException("No method named " + methodName + " found.");
      }
      final Object result = method.invoke(null, args);
      return LayeValue.valueOf(result);
   }
}
