package net.fudev.laye;

import net.fudev.laye.api.LayeMethod;
import net.fudev.laye.api.LayeType;

@LayeType
public final class StaticTypeTest
{
   @LayeMethod
   public static int getNumber()
   {
      return 25;
   }
   
   @LayeMethod
   public static int doubleInt(final int value)
   {
      return value * 2;
   }
}
