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
package net.fudev.laye;

import net.fudev.laye.api.LayeCtor;
import net.fudev.laye.api.LayeField;
import net.fudev.laye.api.LayeInfix;
import net.fudev.laye.api.LayeOperator;
import net.fudev.laye.api.LayeType;

@LayeType
public class Vec2
{
   @LayeField
   public float x;
   @LayeField
   public float y;
   
   // new Vec2:zero();
   @LayeCtor(name = "zero")
   public Vec2()
   {
      this(0.0f, 0.0f);
   }
   
   // new Vec2:xy(10.0f);
   @LayeCtor(name = "xy")
   public Vec2(final float c)
   {
      this(c, c);
   }
   
   // new Vec2:copy(vec);
   @LayeCtor(name = "copy")
   public Vec2(final Vec2 other)
   {
      this(other.x, other.y);
   }
   
   // new Vec2(1.0f, 2.0f);
   @LayeCtor
   public Vec2(final float x, final float y)
   {
      this.x = x;
      this.y = y;
   }
   
   @Override
   public String toString()
   {
      return "<" + x + ", " + y + ">";
   }
   
   // ========== Addition ========== //
   
   // loc vecC = vecA + vecB;
   @LayeInfix(operator = "+")
   public Vec2 add(final Vec2 right)
   {
      return new Vec2(this.x + right.x, this.y + right.y);
   }
   
   // vecA + 2.0f
   @LayeInfix(operator = "+")
   public Vec2 add(final float scalar)
   {
      return new Vec2(this.x + scalar, this.y + scalar);
   }
   
   // vecA !+ vecB #/ vecA
   @LayeInfix(operator = "!+")
   public Vec2 addLocal(final Vec2 right)
   {
      this.x += right.x;
      this.y += right.y;
      return this;
   }
   
   // vecA !+ 2.0f
   @LayeInfix(operator = "!+")
   public Vec2 addLocal(final float scalar)
   {
      this.x += scalar;
      this.y += scalar;
      return this;
   }
   
   // ========== Subtraction ========== //
   
   // loc vecC = vecA - vecB;
   @LayeInfix(operator = "-")
   public Vec2 sub(final Vec2 right)
   {
      return new Vec2(this.x - right.x, this.y - right.y);
   }
   
   // vecA - 2.0f
   @LayeInfix(operator = "-")
   public Vec2 sub(final float scalar)
   {
      return new Vec2(this.x - scalar, this.y - scalar);
   }
   
   // vecA !- vecB #/ vecA
   @LayeInfix(operator = "!-")
   public Vec2 subLocal(final Vec2 right)
   {
      this.x -= right.x;
      this.y -= right.y;
      return this;
   }
   
   // vecA !- 2.0f
   @LayeInfix(operator = "!-")
   public Vec2 subLocal(final float scalar)
   {
      this.x -= scalar;
      this.y -= scalar;
      return this;
   }
   
   // ========== Multiplication ========== //
   
   // loc vecC = vecA * vecB;
   @LayeInfix(operator = "*")
   public Vec2 mul(final Vec2 right)
   {
      return new Vec2(this.x * right.x, this.y * right.y);
   }
   
   // vecA * 2.0f OR 2.0f * vecA
   @LayeInfix(operator = "*")
   @LayeInfix(operator = "*", assoc = LayeOperator.Assoc.RIGHT)
   public Vec2 mul(final float scalar)
   {
      return new Vec2(this.x * scalar, this.y * scalar);
   }
   
   // vecA !* vecB #/ vecA
   @LayeInfix(operator = "!*")
   public Vec2 mulLocal(final Vec2 right)
   {
      this.x *= right.x;
      this.y *= right.y;
      return this;
   }
   
   // vecA !* 2.0f
   @LayeInfix(operator = "!*")
   public Vec2 mulLocal(final float scalar)
   {
      this.x *= scalar;
      this.y *= scalar;
      return this;
   }
   
   // ========== Division ========== //
   
   // loc vecC = vecA / vecB;
   @LayeInfix(operator = "/")
   public Vec2 div(final Vec2 right)
   {
      return new Vec2(this.x / right.x, this.y / right.y);
   }
   
   // vecA / 2.0f
   @LayeInfix(operator = "/")
   public Vec2 div(final float scalar)
   {
      return new Vec2(this.x / scalar, this.y / scalar);
   }
   
   // vecA !/ vecB #/ vecA
   @LayeInfix(operator = "!/")
   public Vec2 divLocal(final Vec2 right)
   {
      this.x /= right.x;
      this.y /= right.y;
      return this;
   }
   
   // vecA !/ 2.0f
   @LayeInfix(operator = "!/")
   public Vec2 divLocal(final float scalar)
   {
      this.x /= scalar;
      this.y /= scalar;
      return this;
   }
   
   // ========== Integer Division ========== //
   
   // loc vecC = vecA // vecB;
   @LayeInfix(operator = "//")
   public Vec2 idiv(final Vec2 right)
   {
      return new Vec2((int) this.x / (int) right.x, (int) this.y / (int) right.y);
   }
   
   // vecA // 2.0f
   @LayeInfix(operator = "//")
   public Vec2 idiv(final float scalar)
   {
      return new Vec2((int) this.x / (int) scalar, (int) this.y / (int) scalar);
   }
   
   // vecA !// vecB #/ vecA
   @LayeInfix(operator = "!//")
   public Vec2 idivLocal(final Vec2 right)
   {
      this.x = (int) this.x / (int) right.x;
      this.y = (int) this.x / (int) right.y;
      return this;
   }
   
   // vecA !// 2.0f
   @LayeInfix(operator = "!//")
   public Vec2 idivLocal(final float scalar)
   {
      this.x = (int) this.x / (int) scalar;
      this.y = (int) this.y / (int) scalar;
      return this;
   }
}
