package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprNewSlot implements Expression
{
   public Expression index, value;
   
   public ExprNewSlot (final Expression index, final Expression value)
   {
      this.index = index;
      this.value = value;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      // leave a value so we can check the instruction.
      index.accept(builder, true);
      final int lastOp = builder.previous();
      if (Laye.GET_OP(lastOp) != Laye.OP_GET_INDEX)
      {
         // TODO better error messages
         throw new CompilerException("Can only new-slot a variable or table field.");
      }
      
      // undo the GET_INDEX
      builder.popOp();
      builder.increaseStackSize();
      value.accept(builder, true);
      builder.visitOpNewSlot(true);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
