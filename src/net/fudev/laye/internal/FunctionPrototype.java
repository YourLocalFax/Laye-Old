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
package net.fudev.laye.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.fudev.laye.internal.compile.laye.UpValueInfo;
import net.fudev.laye.internal.values.LayeValue;

public final class FunctionPrototype
{
   
   // public final LayeFileData file;
   
   /** Constants used by the function. */
   public final LayeValue[] k;
   /** all maps for match expressions */
   public final List<Map<LayeValue, Integer>> jumpTables;
   /** The op codes of this function. */
   public final int[] code;
   /** Functions defined inside this one. */
   public final FunctionPrototype[] nested;
   /** The up values this function references. */
   public final UpValueInfo[] upValues;
   /** Line number information. */
   public final int[] lineInfo;
   /** Did we store line info? */
   public final boolean hasLineInfos;
   
   /** How many parameters were given? */
   public final int numParams;
   /** Was a varg parameter given? */
   public final boolean hasVargs;
   /** Maximum locals size needed for this function. */
   public final int maxLocalsSize;
   /** Maximum stack size needed for this function. */
   public final int maxStackSize;
   
   /** Does this prototype define a generator? */
   public final boolean generator;
   
   public FunctionPrototype(/* final LayeFileData file, */final LayeValue[] k,
         final List<Map<LayeValue, Integer>> jumpTables, final int[] code, final FunctionPrototype[] nested,
         final UpValueInfo[] upValues, final int[] lineInfo, final boolean hasLineInfos, final int numParams,
         final boolean hasVargs, final int maxLocalsSize, final int maxStackSize, final boolean generator)
   {
      // this.file = file;
      this.k = k;
      this.jumpTables = jumpTables;
      this.code = code;
      this.nested = nested;
      this.upValues = upValues;
      this.lineInfo = lineInfo;
      this.hasLineInfos = hasLineInfos;
      this.numParams = numParams;
      this.hasVargs = hasVargs;
      this.maxLocalsSize = maxLocalsSize;
      this.maxStackSize = maxStackSize;
      this.generator = generator;
   }
   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(code);
      result = prime * result + (generator ? 1231 : 1237);
      result = prime * result + (hasLineInfos ? 1231 : 1237);
      result = prime * result + (hasVargs ? 1231 : 1237);
      result = prime * result + (jumpTables == null ? 0 : jumpTables.hashCode());
      result = prime * result + Arrays.hashCode(k);
      result = prime * result + Arrays.hashCode(lineInfo);
      result = prime * result + maxLocalsSize;
      result = prime * result + maxStackSize;
      result = prime * result + Arrays.hashCode(nested);
      result = prime * result + numParams;
      result = prime * result + Arrays.hashCode(upValues);
      return result;
   }
   
   @Override
   public boolean equals(final Object obj)
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
      final FunctionPrototype other = (FunctionPrototype) obj;
      if (!Arrays.equals(code, other.code))
      {
         return false;
      }
      if (generator != other.generator)
      {
         return false;
      }
      if (hasLineInfos != other.hasLineInfos)
      {
         return false;
      }
      if (hasVargs != other.hasVargs)
      {
         return false;
      }
      if (jumpTables == null)
      {
         if (other.jumpTables != null)
         {
            return false;
         }
      }
      else if (!jumpTables.equals(other.jumpTables))
      {
         return false;
      }
      if (!Arrays.equals(k, other.k))
      {
         return false;
      }
      if (!Arrays.equals(lineInfo, other.lineInfo))
      {
         return false;
      }
      if (maxLocalsSize != other.maxLocalsSize)
      {
         return false;
      }
      if (maxStackSize != other.maxStackSize)
      {
         return false;
      }
      if (!Arrays.equals(nested, other.nested))
      {
         return false;
      }
      if (numParams != other.numParams)
      {
         return false;
      }
      if (!Arrays.equals(upValues, other.upValues))
      {
         return false;
      }
      return true;
   }
   
}
