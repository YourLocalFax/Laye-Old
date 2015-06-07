package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprTableCtor implements Expression
{
   public final Vector<Expression> keys = new Vector<>();
   public final Vector<Expression> values = new Vector<>();
   public final Vector<Boolean> constant = new Vector<>();
   
   public ExprTableCtor ()
   {
   }
   
   public void addEntry (final Expression key, final Expression value, final boolean con)
   {
      keys.addElement(key);
      values.addElement(value);
      constant.addElement(con);
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         builder.visitOpNewTable();
         final int amount = keys.size();
         for (int i = 0; i < amount; i++)
         {
            final Expression key = keys.get(i);
            final Expression value = values.get(i);
            final boolean con = constant.get(i);
            // get the table again
            builder.visitOpDup(1);
            // key/value
            key.accept(builder, true);
            value.accept(builder, true);
            // create the slot
            builder.visitOpNewSlot(con);
            builder.visitOpPop(1);
         }
      }
   }
}
