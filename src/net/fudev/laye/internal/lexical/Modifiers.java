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
package net.fudev.laye.internal.lexical;

public final class Modifiers
{
   public static final Modifiers NONE = new Modifiers(false, false, false);
   
   public static final Modifiers LOCAL = new Modifiers(true, false, false);
   public static final Modifiers CONST = new Modifiers(false, true, false);
   public static final Modifiers STATIC = new Modifiers(false, false, true);
   
   public static final Modifiers LOCAL_CONST = new Modifiers(true, true, false);
   public static final Modifiers LOCAL_STATIC = new Modifiers(true, false, true);
   public static final Modifiers STATIC_CONST = new Modifiers(false, true, true);
   
   public static final Modifiers LOCAL_STATIC_CONST = new Modifiers(true, true, true);
   
   public static Modifiers get(final boolean isLocal, final boolean isConst, final boolean isStatic)
   {
      if (isLocal)
      {
         if (isConst)
         {
            if (isStatic)
            {
               return Modifiers.LOCAL_STATIC_CONST;
            }
            return Modifiers.LOCAL_CONST;
         }
         if (isStatic)
         {
            return Modifiers.LOCAL_STATIC;
         }
         return Modifiers.LOCAL;
      }
      if (isConst)
      {
         if (isStatic)
         {
            return Modifiers.STATIC_CONST;
         }
         return Modifiers.CONST;
      }
      if (isStatic)
      {
         return Modifiers.STATIC;
      }
      return Modifiers.NONE;
   }
   
   public final boolean loc;
   public final boolean mut;
   public final boolean stat;
   
   public Modifiers(final boolean loc, final boolean mut, final boolean stat)
   {
      this.loc = loc;
      this.mut = mut;
      this.stat = stat;
   }
}
