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
package net.fudev.laye.util;

import java.util.Arrays;
import java.util.List;

import net.fudev.laye.internal.values.LayeTuple;
import net.fudev.laye.internal.values.LayeValue;

public final class Util
{
   
   private Util()
   {
   }
   
   public static String concatValues(final LayeValue[] values, final String separator)
   {
      return Util.concatValuesRange(values, 0, values.length, separator);
   }
   
   public static String concatValuesRange(final LayeValue[] values, final int offset, final int len,
         final String separator)
   {
      final StringBuilder sb = new StringBuilder();
      for (int i = offset; i < len; i++)
      {
         if (i > offset)
         {
            sb.append(separator);
         }
         sb.append(values[i]);
      }
      return sb.toString();
   }
   
   public static LayeValue[] createValueArray(final int length)
   {
      final LayeValue[] result = new LayeValue[length];
      Arrays.fill(result, LayeValue.NULL);
      return result;
   }
   
   public static LayeValue[] createValueArray(final int length, final LayeValue... initial)
   {
      final int initialLength = initial.length;
      if (initialLength >= length)
      {
         return Arrays.copyOf(initial, length);
      }
      else
      {
         final LayeValue[] result = new LayeValue[length];
         System.arraycopy(initial, 0, result, 0, initialLength);
         Arrays.fill(result, initialLength, length, LayeValue.NULL);
         return result;
      }
   }
   
   public static LayeValue[] createValueArrayLocals(final int length, final boolean hasVargs,
         final LayeValue... initial)
   {
      final int initialLength = initial.length;
      // if no vargs OR not enough for vargs anyway:
      if (!hasVargs || initialLength < length)
      {
         return Util.createValueArray(length, initial);
      }
      else
      {
         final LayeValue[] result = new LayeValue[length];
         System.arraycopy(initial, 0, result, 0, length - 1);
         result[length - 1] = LayeTuple.valueOf(initial, length - 1, initialLength);
         return result;
      }
   }
   
   public static int[] toIntArray(final List<Integer> code)
   {
      final int len = code.size(), res[] = new int[len];
      for (int i = 0; i < len; i++)
      {
         res[i] = code.get(i);
      }
      return res;
   }
   
   public static boolean arrayEquals(final LayeValue[] a, final LayeValue[] b, final int length)
   {
      for (int i = 0; i < length; i++)
      {
         final LayeValue asub = a[i];
         if (!(asub == null ? b[i] == null : asub.equals(b[i])))
         {
            return false;
         }
      }
      return true;
   }
   
}
