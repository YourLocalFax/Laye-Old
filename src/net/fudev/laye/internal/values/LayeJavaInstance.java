package net.fudev.laye.internal.values;

import java.util.Map;

import net.fudev.laye.err.LayeException;
import net.fudev.laye.internal.ValueType;
import net.fudev.laye.internal.java.JavaMethod;

public final class LayeJavaInstance extends LayeValue
{
   public static LayeJavaInstance valueOf(final Object object)
   {
      final LayeJavaType type = LayeJavaType.get(object.getClass());
      return new LayeJavaInstance(object, type);
   }
   
   private final Map<String, JavaMethod> instanceMethods;
   private final Map<String, JavaMethod> infixOperators;
   
   public final Object object;
   
   LayeJavaInstance(final Object object, final LayeJavaType type)
   {
      super(ValueType.JAVA_INSTANCE);
      this.object = object;
      this.instanceMethods = type.instanceMethods;
      this.infixOperators = type.infixOperators;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      if (other instanceof LayeJavaInstance)
      {
         return object.equals(((LayeJavaInstance) other).object);
      }
      return false;
   }
   
   @Override
   public int hashCode()
   {
      return object.hashCode();
   }
   
   @Override
   public String asstring()
   {
      return object.toString();
   }
   
   @Override
   public LayeValue callChildMethod(final LayeValue name, final LayeValue... args)
   {
      final String methodName = name.asstring();
      final JavaMethod method = instanceMethods.get(methodName);
      if (method == null)
      {
         throw new LayeException("No method named " + methodName + " found.");
      }
      final Object result = method.invoke(object, args);
      return LayeValue.valueOf(result);
   }
   
   @Override
   public LayeValue infixOp(final String op, final LayeValue right)
   {
      final JavaMethod method = infixOperators.get(op);
      if (method == null)
      {
         throw new LayeException("No operator " + op + " found.");
      }
      final Object result = method.invoke(object, right);
      return LayeValue.valueOf(result);
   }
}
