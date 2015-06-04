package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.SyntaxNode;

/**
 * A Laye expression can be an r-value and always results in a value
 */
public interface Expression extends SyntaxNode
{
   public static void convertFromListToValueInOperatorExpression(final LayeFunctionBuilder builder, final Expression value)
   {
      // TODO I don't think Laye should ignore these, but think about it.
      if (value instanceof ExprListCtor)
      {
         final ExprListCtor list = (ExprListCtor) value;
         if (list.immutable && list.values.size() == 1)
         {
            list.values.get(0).accept(builder, true);
         }
         else
         {
            value.accept(builder, true);
         }
      }
      else
      {
         value.accept(builder, true);
      }
   }
}
