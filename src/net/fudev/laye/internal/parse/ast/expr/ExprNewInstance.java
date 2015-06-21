package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprNewInstance implements Expression
{
   public Expression type;
   public String name;
   public Vector<Expression> args;
   
   public ExprNewInstance(final Expression type, final String name, final Vector<Expression> args)
   {
      this.type = type;
      this.name = name;
      this.args = args;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         type.accept(builder, true);
         for (final Expression expr : args)
         {
            expr.accept(builder, true);
         }
         final int nameid;
         if (name != null)
         {
            nameid = builder.addConsts(name);
         }
         else
         {
            // Is converted for me from -1 in the NewInstance
            nameid = -1;
         }
         builder.visitOpNewInstance(args.size(), nameid);
      }
   }
}
