package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprAssign implements Expression
{
   public Expression index, value;
   
   public ExprAssign (final Expression index, final Expression value)
   {
      this.index = index;
      this.value = value;
   }
   
   @Override
   public void accept (final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      // we make this leave its value because we have to get the insn for it:
      index.accept(builder, true);
      
      final int lastOp = builder.previous();
      switch (Laye.GET_OP(lastOp))
      {
         case Laye.OP_LOAD:
         {
            final int local = Laye.GET_A(lastOp);
            if (builder.isLocalConst(local))
            {
               throw new CompilerException(
                     "local " + builder.getLocalName(local) + " was defined constant, cannot modify.");
            }
            // undo load
            builder.popOp();
            builder.decreaseStackSize();
            value.accept(builder, true);
            builder.visitOpStore(local);
            break;
         }
         case Laye.OP_GET_UP:
         {
            final int up = Laye.GET_A(lastOp);
            if (builder.isUpValueConst(up))
            {
               throw new CompilerException(
                     "local " + builder.getUpValueName(up) + " was defined constant, cannot modify.");
            }
            // undo get_up
            builder.popOp();
            builder.decreaseStackSize();
            value.accept(builder, true);
            builder.visitOpSetUp(up);
            break;
         }
         case Laye.OP_GET_INDEX:
         {
            // undo get_index
            builder.popOp();
            builder.increaseStackSize();
            value.accept(builder, true);
            builder.visitOpSetIndex();
            break;
         }
         default:
            throw new CompilerException("can only assign to a variable.");
      }
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
