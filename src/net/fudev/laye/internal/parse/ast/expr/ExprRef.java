package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public class ExprRef implements Expression
{
   public Expression value;
   
   public ExprRef(final Expression value)
   {
      this.value = value;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      if (isResultRequired)
      {
         value.accept(builder, true);
         final int op = builder.previous();
         builder.popOp();
         
         switch (op & Laye.MAX_OP)
         {
            case Laye.OP_LOAD:
               // undo the LOAD stack mod
               builder.decreaseStackSize();
               builder.visitRefLocalVar(Laye.GET_A(op));
               break;
            case Laye.OP_GET_UP:
               // undo the GET_UP stack mod
               builder.decreaseStackSize();
               builder.visitRefUpValue(Laye.GET_A(op));
               break;
            case Laye.OP_GET_INDEX:
               // undo the GET_INDEX stack mod
               builder.increaseStackSize();
               builder.visitRefIndex();
               break;
            default:
               throw new CompilerException("can only reference variable locations!");
         }
      }
   }
}
