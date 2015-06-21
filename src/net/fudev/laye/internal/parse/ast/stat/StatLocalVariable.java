package net.fudev.laye.internal.parse.ast.stat;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.expr.Expression;

public final class StatLocalVariable implements Statement
{
   public boolean con;
   public final Vector<String> names;
   public final Vector<Expression> values;
   
   public StatLocalVariable(final boolean con, final Vector<String> names, final Vector<Expression> values)
   {
      this.con = con;
      this.names = names;
      this.values = values;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int size = names.size(), sizem1 = size - 1;
      for (int i = 0; i < size; i++)
      {
         final String localName = names.get(i);
         final Expression value = values.get(i);
         
         value.accept(builder, true);
         
         final int local = builder.addLocal(localName, con);
         builder.visitOpStore(local);
         
         if (i != sizem1 || !isResultRequired)
         {
            builder.visitOpPop(1);
         }
      }
   }
}
