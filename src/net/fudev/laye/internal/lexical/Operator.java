package net.fudev.laye.internal.lexical;

import java.util.HashMap;
import java.util.Map;

public final class Operator
{
   public static final int PREC_DEFAULT = 20;
   
   public static final int PREC_BIT = 10;
   public static final int PREC_MULT = 30;
   public static final int PREC_POW = 40;
   
   private static final Map<String, Operator> operators = new HashMap<>();
   
   public static Operator get(final String operator)
   {
      Operator result = Operator.operators.get(operator);
      if (result == null)
      {
         result = new Operator(operator);
         Operator.operators.put(operator, result);
      }
      return result;
   }
   
   /**
    * @param symbol
    *           The symbol in question
    * @return <code>true</code> for all valid operator symbols,
    *         <code>false</code> otherwise.
    */
   public static boolean isValidOperatorSymbol(final char symbol)
   {
      switch (symbol)
      {
         case '`':
         case '~':
         case '!':
         case '%':
         case '^':
         case '&':
         case '*':
         case '-':
         case '=':
         case '+':
         case '\\':
         case '|':
         case '<':
         case '>':
         case '/':
         case '?':
            return true;
         default:
            return false;
      }
   }
   
   static
   {
      final String[] defaultOperators = {
            "+", "-", "*", "/", "//", "%", "^", "+=", "-=", "*=", "/=", "//=",
            "%=", "^=", "&", "|", "~", "<<", ">>",
            ">>>", "<>", "&=", "|=", "~=", "<<=", ">>=", ">>>=", "<>=", "==",
            "!=", "<", "<=", ">", ">=", "<=>", "<-",
            "=",
      };
      for (final String operator : defaultOperators)
      {
         for (final char c : operator.toCharArray())
         {
            if (!Operator.isValidOperatorSymbol(c))
            {
               throw new IllegalArgumentException(
                     "operator contains an invalid operator token, '" + c
                           + "'");
            }
         }
         Operator.get(operator);
      }
   }
   
   public final String image;
   
   public int precedence = Operator.PREC_DEFAULT;
   
   private Operator(final String image)
   {
      this.image = image;
   }
   
   /**
    * Used to determine if a given operator can be overloaded. This returns
    * false for any operator ending with <code>=</code>, as Laye reserves those
    * for translation to assignment operations, and for the new-slot operator
    * <code>&lt;-</code>
    *
    * @param operator
    * @return
    */
   public boolean isOverloadable()
   {
      // TODO will <= and >= stick around in Laye, or will those be changed? (to
      // match the standard '=' postfix standard)
      if (image.endsWith("="))
      {
         // There are special cases where '=' post isn't quite straightforward
         switch (image)
         {
            case "==":
            case "<=":
               return true;
            default:
               return false;
         }
      }
      switch (image)
      {
         case "<-":
         case "!=": // is translated to {not (a == b)}
         case ">": // is translated to {not (a <= b)}
         case "=": // covered by endsWith('='), but left here for clarity
         case ">=": // see >, covered by endsWith('='), but left here for
                    // clarity
            return false;
         default:
            return true;
      }
   }
   
   public boolean isAssignment()
   {
      if (image.endsWith("="))
      {
         switch (image)
         {
            case "==":
            case "!=":
            case "<=":
            case ">=":
               return false;
            default:
               return true;
         }
      }
      return false;
   }
   
   public Operator infixFromAssignment()
   {
      return Operator.get(image.substring(0, image.length() - 2));
   }
}
