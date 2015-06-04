package net.fudev.laye.internal.lexical;

import java.io.IOException;
import java.io.Reader;

import net.fudev.laye.err.LayeException;

public final class LayeLexer
{
   private final Reader reader;

   private int line = 1;

   private char c = '\0';
   private boolean eos = false;

   private StringBuilder stringBuilder = new StringBuilder();
   private TokenData tokenData;

   private Token peekedToken = Token.NO_TOKEN;
   private TokenData peekedTokenData;

   public LayeLexer(final Reader reader)
   {
      this.reader = reader;
      read();
   }

   public TokenData getTokenData()
   {
      return tokenData;
   }

   public TokenData getPeekedTokenData()
   {
      return peekedTokenData;
   }

   public int getLine()
   {
      return line;
   }

   // ---------- TEMP STRING ---------- //

   private void beginTempString()
   {
      stringBuilder.setLength(0);
      stringBuilder.trimToSize();
   }

   private String endTempString()
   {
      final String string = stringBuilder.toString();
      tokenData = new TokenData(string);
      return string;
   }

   private void putChar(final char c)
   {
      stringBuilder.append(c);
   }

   private void putChar()
   {
      stringBuilder.append(c);
      read();
   }

   // ---------- LEXING ---------- //

   private void read()
   {
      int value = -1;
      try
      {
         value = reader.read();
      }
      catch (final IOException e)
      {
         e.printStackTrace();
      }
      if (value == -1)
      {
         eos = true;
      }
      c = (char) value;
   }

   public Token peek()
   {
      if (peekedToken == Token.NO_TOKEN)
      {
         final TokenData prev = tokenData;
         peekedToken = lex();
         peekedTokenData = tokenData;
         tokenData = prev;
      }
      return peekedToken;
   }

   public Token lex()
   {
      if (peekedToken != Token.NO_TOKEN)
      {
         final Token result = peekedToken;
         tokenData = peekedTokenData;
         peekedTokenData = TokenData.EMPTY;
         peekedToken = Token.NO_TOKEN;
         return result;
      }
      while (!eos)
      {
         switch (c)
         {
            // get rid of whitespace, woo
            case ' ':
            case '\t':
            case '\r':
               read();
               continue;
               // special case for newline \n
            case '\n':
               read();
               line++;
               continue;
               // string literals (or chars if single char + ')
            case '\'':
            case '"':
               return stringLiteral();
               // simple
            case '(':
               read();
               return Token.OPEN_BRACKET;
            case ')':
               read();
               return Token.CLOSE_BRACKET;
            case '[':
               read();
               return Token.OPEN_SQUARE_BRACKET;
            case ']':
               read();
               return Token.CLOSE_SQUARE_BRACKET;
            case '{':
               read();
               return Token.OPEN_CURLY_BRACKET;
            case '}':
               read();
               return Token.CLOSE_CURLY_BRACKET;
            case ';':
               read();
               return Token.SEMI;
            case ',':
               read();
               return Token.COMMA;
            case '.':
               read();
               if (c == '.')
               {
                  read();
                  return Token.VARGS;
               }
               return Token.DOT;
            case '@':
               read();
               return Token.AT;
               // Anything else
            default:
               // check operators
               beginTempString();
               while (Operator.isValidOperatorSymbol(c))
               {
                  putChar();
               }
               final String string = endTempString();
               // if tokens, then operators
               if (string.length() > 0)
               {
                  switch (string)
                  {
                     case "<-":
                        return Token.NEW_SLOT;
                     case "=":
                        return Token.EQUAL;
                     default:
                        tokenData = new TokenData(Operator.get(string));
                        return Token.OPERATOR;
                  }
               }
               // else things
               if (Character.isDigit(c))
               {
                  return numberLiteral();
               }
               else if (Character.isAlphabetic(c) || c == '_')
               {
                  return identifier();
               }
               throw new LayeException("Unexpected character '" + c + "'");
         }
      }
      // Nothing!
      return Token.NO_TOKEN;
   }

   private Token stringLiteral()
   {
      final char closing = c; // " | '
      beginTempString();
      read(); // " | '
      while (c != closing)
      {
         if (eos)
         {
            throw new LayeException("Unfinished string");
         }
         if (c == '\n')
         {
            line++;
            putChar();
         }
         else if (c == '\\')
         {
            read();
            putChar((char) readEscape());
         }
         else
         {
            putChar();
         }
      }
      endTempString();
      read(); // " | '
      return Token.STRING_LITERAL;
   }

   private int readEscape()
   {
      int res = -1;
      switch (c)
      {
         case 'u':
            read();
            final StringBuilder sb = new StringBuilder(stringBuilder.toString());
            beginTempString();
            int idx = 0;
            for (; !eos && Character.isDigit(c); idx++)
            {
               putChar();
            }
            if (idx != 4)
            {
               throw new LayeException("4 digits are expected when defining a unicode char, " + idx + " given.");
            }
            // Put the value into the intValue, since the compiler takes
            // from there for char literals
            res = Integer.parseInt(endTempString());
            stringBuilder = sb;
            break;
         case 'n':
            res = '\n';
            read();
            break;
         case 't':
            res = '\t';
            read();
            break;
         case '0':
            res = '\0';
            read();
            break;
         case '"':
            res = '"';
            read();
            break;
         case '\'':
            res = '\'';
            read();
            break;
         default:
            throw new LayeException("Escape character '" + c + "' not recognized.");
      }
      return res;
   }

   private static boolean doesCharDefineIntBase(final char c)
   {
      return c == 'x' || c == 'X' || c == 'b' || c == 'B';
   }

   private static boolean isHexChar(final char c)
   {
      return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
   }

   private static boolean isOctalChar(final char c)
   {
      return (c >= '0' && c <= '7');
   }

   private static boolean isBinaryChar(final char c)
   {
      return c == '0' || c == '1';
   }

   // TODO check that this works plz
   private Token numberLiteral()
   {
      beginTempString();
      int radix = 10;
      char lastChar = c;
      read();
      Token numberType = Token.INT_LITERAL; // Default it to a standard one
      if (lastChar == '0' && c != '.')
      {
         if (doesCharDefineIntBase(c))
         {
            // These will all define some integer value (byte, short, int,
            // long)
            // The returned type will represent the smallest integer type
            // for the literal.
            final char base = c;
            read();
            switch (base)
            {
               case 'x':
               case 'X':
                  radix = 16;
                  while (!eos && isHexChar(c) || c == '_')
                  {
                     if (c == '_')
                     {
                        lastChar = c;
                        read();
                        continue;
                     }
                     lastChar = c;
                     putChar();
                  }
                  break;
               case 'b':
               case 'B':
                  radix = 2;
                  while (!eos && isBinaryChar(c) || c == '_')
                  {
                     if (c == '_')
                     {
                        lastChar = c;
                        read();
                        continue;
                     }
                     lastChar = c;
                     putChar();
                  }
                  break;
            }
            if (lastChar == '_')
            {
               throw new LayeException("Cannot place an underscore at the end of a number.");
            }
         }
         else
         {
            // Here we define octals
            radix = 8;
            putChar(lastChar);
            while (!eos && isOctalChar(c) || c == '_')
            {
               if (c == '_')
               {
                  lastChar = c;
                  read();
                  continue;
               }
               lastChar = c;
               putChar();
            }
            if (lastChar == '_')
            {
               throw new LayeException("Cannot place an underscore at the end of a number.");
            }
         }
      }
      else
      {
         // Here we're only expecting DECIMAL numbers, so special
         // characters for bases are not allowed.
         // The only "letter" characters we can accept are 'e' and 'f'
         // (exponent and float definition).
         putChar(lastChar);
         while (!eos && (Character.isDigit(c) || c == '_' || c == '.' || c == 'e'))
         {
            if (c == '_')
            {
               if (lastChar == '.')
               {
                  throw new LayeException("Cannot place an underscore next to a decimal point in numbers.");
               }
               lastChar = c;
               read(); // get rid of it
            }
            else if (c == '.')
            {
               if (lastChar == '_')
               {
                  throw new LayeException("Cannot place a decimal point next to an underscore in numbers.");
               }
               if (numberType == Token.FLOAT_LITERAL)
               {
                  throw new LayeException("Misplaced decimal point.");
               }
               lastChar = c;
               putChar();
               numberType = Token.FLOAT_LITERAL;
            }
            else if (c == 'e')
            {
               lastChar = c;
               putChar();
               if (c == '-')
               {
                  lastChar = c;
                  putChar();
               }
               numberType = Token.FLOAT_LITERAL;
            }
            else
            {
               lastChar = c;
               putChar();
            }
         }
         if (lastChar == '_')
         {
            throw new LayeException("Cannot place an underscore at the end of a number.");
         }
      }
      // if the final char is 'f', We know this should be a float literal.
      if (c == 'f' || c == 'F')
      {
         read(); // 'f' | 'F'
         numberType = Token.FLOAT_LITERAL;
      }
      if (!eos && Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')
      {
         throw new LayeException("unexpected characters at end of numeric literal.");
      }
      if (numberType == Token.INT_LITERAL)
      {
         tokenData = new TokenData(Long.parseLong(endTempString(), radix));
         return Token.INT_LITERAL;
      }
      else
      {
         tokenData = new TokenData(Double.parseDouble(endTempString()));
         return Token.FLOAT_LITERAL;
      }
   }

   private Token identifier()
   {
      beginTempString();
      while (!eos && Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')
      {
         putChar();
      }
      final String string = endTempString();
      return getTokenFromIdent(string);
   }

   private static Token getTokenFromIdent(final String ident)
   {
      switch (ident)
      {
         case "_":
            return Token.WILDCARD;

         case "and":
            return Token.AND;
         case "or":
            return Token.OR;
         case "xor":
            return Token.XOR;
         case "nand":
            return Token.NAND;
         case "nor":
            return Token.NOR;
         case "xnor":
            return Token.XNOR;
         case "not":
            return Token.NOT;
         case "ref":
            return Token.REF;
         case "deref":
            return Token.DEREF;
         case "typeof":
            return Token.TYPEOF;

         case "true":
            return Token.TRUE;
         case "false":
            return Token.FALSE;
         case "null":
            return Token.NULL;

         case "loc":
            return Token.LOC;
         case "stat":
            return Token.STAT;
         case "mut":
            return Token.MUT;

         case "fn":
            return Token.FN;
         case "gen":
            return Token.GEN;
         case "type":
            return Token.TYPE;

         case "has":
            return Token.HAS;
         case "ctor":
            return Token.CTOR;
         case "prefix":
            return Token.PREFIX;
         case "postfix":
            return Token.POSTFIX;
         case "unary":
            return Token.RET;
         case "new":
            return Token.NEW;

         case "if":
            return Token.IF;
         case "el":
            return Token.EL;
         case "for":
            return Token.FOR;
         case "each":
            return Token.EACH;
         case "to":
            return Token.TO;
         case "by":
            return Token.BY;
         case "in":
            return Token.IN;
         case "while":
            return Token.WHILE;
         case "take":
            return Token.TAKE;
         case "match":
            return Token.MATCH;
         case "case":
            return Token.CASE;
         case "ret":
            return Token.RET;
         case "break":
            return Token.BREAK;
         case "cont":
            return Token.CONT;
         case "resume":
            return Token.RESUME;
         case "yield":
            return Token.YIELD;
         case "do":
            return Token.DO;
         case "end":
            return Token.END;

         default:
            return Token.IDENT;
      }
   }
}
