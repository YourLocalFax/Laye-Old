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
