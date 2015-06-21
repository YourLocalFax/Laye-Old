package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprGetIndex implements Expression
{
   public Expression value;
   public Expression index;
   
   public ExprGetIndex(final Expression value, final Expression index)
   {
      this.value = value;
      this.index = index;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         value.accept(builder, true);
         index.accept(builder, true);
         builder.visitOpGetIndex();
      }
   }
}
