package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprGetVar implements Expression
{
   public String name;
   
   public ExprGetVar(final String name)
   {
      this.name = name;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         builder.visitGetVariable(name);
      }
   }
}
