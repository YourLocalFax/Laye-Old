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
