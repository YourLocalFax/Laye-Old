package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprDelSlot implements Expression
{
   public Expression value;
   
   public ExprDelSlot (final Expression value)
   {
      this.value = value;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         value.accept(builder, true);
         final int op = builder.previous();
         builder.popOp();
         
         switch (op & Laye.MAX_OP)
         {
            case Laye.OP_GET_INDEX:
               // undoo the GET_INDEX insn size mod
               builder.increaseStackSize();
               builder.visitOpDelSlot();
               break;
            default:
               throw new CompilerException("can only delete non-local variable locations!");
         }
      }
   }
}
