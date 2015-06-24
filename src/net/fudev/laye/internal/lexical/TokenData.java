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

import java.util.HashMap;
import java.util.Map;

public final class TokenData
{
   public static final TokenData EMPTY = new TokenData(Token.NO_TOKEN);
   
   private static final Map<Token, TokenData> constantTokens = new HashMap<>();
   
   public static TokenData get(final Token token)
   {
      TokenData result = TokenData.constantTokens.get(token);
      if (result == null)
      {
         result = new TokenData(token);
         TokenData.constantTokens.put(token, result);
      }
      return result;
   }
   
   public final Token token;
   
   public final long integer;
   public final double fractional;
   public final String string;
   public final Operator operator;
   
   private TokenData(final Token token)
   {
      this.token = token;
      integer = 0L;
      fractional = 0.0;
      string = "";
      operator = null;
   }
   
   public TokenData(final long integer)
   {
      token = Token.INT_LITERAL;
      this.integer = integer;
      fractional = 0.0;
      string = "";
      operator = null;
   }
   
   public TokenData(final double fractional)
   {
      token = Token.FLOAT_LITERAL;
      integer = 0L;
      this.fractional = fractional;
      string = "";
      operator = null;
   }
   
   public TokenData(final String string)
   {
      token = Token.STRING_LITERAL;
      integer = 0L;
      fractional = 0.0;
      this.string = string;
      operator = null;
   }
   
   public TokenData(final Operator operator)
   {
      token = Token.OPERATOR;
      integer = 0L;
      fractional = 0.0;
      string = "";
      this.operator = operator;
   }
}
