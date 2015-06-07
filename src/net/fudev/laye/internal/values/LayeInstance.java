package net.fudev.laye.internal.values;

import net.fudev.laye.internal.ValueType;

public final class LayeInstance extends LayeValue
{
   public final LayeType type;
   
   public LayeInstance (final LayeType type, final LayeValue... ctorArgs)
   {
      this(type, null, ctorArgs);
   }
   
   public LayeInstance (final LayeType type, final String ctorName, final LayeValue... ctorArgs)
   {
      super(ValueType.INSTANCE);
      this.type = type;
      
      final LayeValue[] args = new LayeValue[ctorArgs.length + 1];
      System.arraycopy(ctorArgs, 0, args, 1, ctorArgs.length);
      args[0] = this;
      
      final LayeFunction ctor = type.getCtorByName(ctorName);
      ctor.call(args);
   }
   
   @Override
   public int hashCode ()
   {
      // TODO hashCode
      return 0;
   }
   
   @Override
   public boolean equalTo_b (final LayeValue other)
   {
      // TODO overloaded == operator
      return other == this;
   }
   
   @Override
   public String asstring ()
   {
      return "instance-TODO";
   }
   
   @Override
   public LayeValue infixOp (final String op, final LayeValue right)
   {
      final LayeFunction leftOp = type.getLeftInfixOperator(op);
      if (leftOp != null)
      {
         return leftOp.call(this, right);
      }
      else
      {
         final LayeFunction rightOp = type.getRightInfixOperator(op);
         if (rightOp != null)
         {
            return rightOp.call(right, this);
         }
         else
         {
            // TODO instance type as string
            arithBinaryError(op, "instance", right.valueType.type.toString());
            return LayeValue.NULL;
         }
      }
   }
   
   @Override
   public LayeValue add (final LayeValue right)
   {
      return infixOp("+", right);
   }
}
