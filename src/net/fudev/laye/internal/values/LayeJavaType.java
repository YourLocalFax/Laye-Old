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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.api.LayeCtor;
import net.fudev.laye.api.LayeInfix;
import net.fudev.laye.api.LayeMethod;
import net.fudev.laye.api.LayeType;
import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.java.JavaCtor;
import net.fudev.laye.internal.java.JavaMethod;

public class LayeJavaType extends LayeValue
{
   private static final Map<Class<?>, LayeJavaType> types = new HashMap<>();
   
   public static boolean isRegistered(final Class<?> type)
   {
      return types.containsKey(type);
   }
   
   public static LayeJavaType get(final Class<?> value)
   {
      LayeJavaType type = types.get(value);
      if (type == null)
      {
         type = new LayeJavaType(value);
         types.put(value, type);
      }
      return type;
   }
   
   private static void addMethod(final Method method, final LayeMethod methodAnnot,
         final Map<String, JavaMethod> methods)
   {
      final int modifiers = method.getModifiers();
      final boolean isPublic = Modifier.isPublic(modifiers);
      if (!isPublic)
      {
         throw new LayeException("Method " + method.getName() + " must be public to be exposed to Laye.");
      }
      
      // Method name
      final String name = methodAnnot.name().isEmpty() ? method.getName() : methodAnnot.name();
      
      JavaMethod javaMethod = methods.get(name);
      if (javaMethod == null)
      {
         javaMethod = new JavaMethod();
         methods.put(name, javaMethod);
      }
      javaMethod.addMethod(method);
   }
   
   private static void addOperator(final Method method, final String operator, final Map<String, JavaMethod> operators)
   {
      final int modifiers = method.getModifiers();
      final boolean isPublic = Modifier.isPublic(modifiers);
      if (!isPublic)
      {
         throw new LayeException("Method " + method.getName() + " must be public to be exposed to Laye.");
      }
      
      JavaMethod javaMethod = operators.get(operator);
      if (javaMethod == null)
      {
         javaMethod = new JavaMethod();
         operators.put(operator, javaMethod);
      }
      javaMethod.addMethod(method);
   }
   
   private final Class<?> value;
   
   final Map<String, JavaMethod> instanceMethods = new HashMap<>();
   
   final Map<String, JavaMethod> prefixOperators = new HashMap<>();
   final Map<String, JavaMethod> postfixOperators = new HashMap<>();
   final Map<String, JavaMethod> infixOperators = new HashMap<>();
   
   private final Map<String, JavaMethod> staticMethods = new HashMap<>();
   private final Map<String, JavaCtor> constructors = new HashMap<>();
   
   public LayeJavaType(final Class<?> value)
   {
      super(ValueType.TYPE);
      this.value = value;
      
      final LayeType typeAnnot = value.getDeclaredAnnotation(LayeType.class);
      if (typeAnnot == null)
      {
         throw new LayeException("Class must be tagged with the @LayeType annotation.");
      }
      
      // Gather methods (instance and static) TODO instance methods
      for (final Method method : value.getMethods())
      {
         final LayeMethod methodAnnot = method.getDeclaredAnnotation(LayeMethod.class);
         // final LayePrefix prefixAnnot = method
         // .getDeclaredAnnotation(LayePrefix.class);
         // final LayePostfix postfixAnnot = method
         // .getDeclaredAnnotation(LayePostfix.class);
         final LayeInfix infixAnnot = method.getDeclaredAnnotation(LayeInfix.class);
         
         final int modifiers = method.getModifiers();
         if (methodAnnot != null)
         {
            final boolean isStatic = Modifier.isStatic(modifiers);
            addMethod(method, methodAnnot, isStatic ? staticMethods : instanceMethods);
         }
         else if (infixAnnot != null)
         {
            addOperator(method, infixAnnot.operator(), infixOperators);
         }
      }
      
      // Gather methods (instance and static) TODO instance methods
      for (final Constructor<?> ctor : value.getConstructors())
      {
         final LayeCtor methodAnnot = ctor.getDeclaredAnnotation(LayeCtor.class);
         if (methodAnnot == null)
         {
            continue;
         }
         
         final int modifiers = ctor.getModifiers();
         final boolean isPublic = Modifier.isPublic(modifiers);
         if (!isPublic)
         {
            throw new LayeException("Constructor " + ctor.getName() + " must be public to be exposed to Laye.");
         }
         
         // Method name
         final String name = methodAnnot.name().isEmpty() ? "<default>" : methodAnnot.name();
         
         // TODO use a vector so we can check more methods of the same name
         JavaCtor javaMethod = constructors.get(name);
         if (javaMethod == null)
         {
            javaMethod = new JavaCtor();
            constructors.put(name, javaMethod);
         }
         javaMethod.addConstructor(ctor);
      }
   }
   
   @Override
   public int hashCode()
   {
      return value.hashCode();
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "type:" + value.getName();
   }
   
   @Override
   public LayeValue callChildMethod(final LayeValue name, final LayeValue... args)
   {
      final String methodName = name.asstring();
      final JavaMethod method = staticMethods.get(methodName);
      if (method == null)
      {
         throw new LayeException("No method named " + methodName + " found.");
      }
      final Object result = method.invoke(null, args);
      return LayeValue.valueOf(result);
   }
   
   @Override
   public LayeValue newinstance(final LayeValue... args)
   {
      final String ctorName = "<default>";
      final JavaCtor ctor = constructors.get(ctorName);
      if (ctor == null)
      {
         throw new LayeException("No default constructor found.");
      }
      final Object result = ctor.newInstance(args);
      return LayeValue.valueOf(result);
   }
   
   @Override
   public LayeValue newinstance(final String name, final LayeValue... args)
   {
      final JavaCtor ctor = constructors.get(name);
      if (ctor == null)
      {
         throw new LayeException("No constructor named " + name + " found.");
      }
      final Object result = ctor.newInstance(args);
      return LayeValue.valueOf(result);
   }
   
   public Class<?> getJavaType()
   {
      return value;
   }
}
