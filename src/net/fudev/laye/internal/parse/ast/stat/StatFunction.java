package net.fudev.laye.internal.parse.ast.stat;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.Laye;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;
import net.fudev.laye.internal.parse.ast.expr.Expression;

public final class StatFunction implements Statement
{
   public boolean gen;
   public boolean isConst;
   public Expression name;
   public ASTFunctionPrototype prototype;

   public StatFunction(final boolean gen, final boolean isConst, final Expression name, final ASTFunctionPrototype prototype)
   {
      this.gen = gen;
      this.isConst = isConst;
      this.name = name;
      this.prototype = prototype;
   }

   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      name.accept(builder, true);

      final int pre = builder.previous();
      final int preop = Laye.GET_OP(pre);

      switch (preop)
      {
         case Laye.OP_GET_INDEX:
            break;
         // TODO handle up value names as global function names in scopes?
         case Laye.OP_GET_UP:
         case Laye.OP_LOAD:
            throw new CompilerException("local variable already defined");
         default:
            throw new CompilerException("invalid function destination");
      }

      final FunctionPrototype proto = prototype.generate(builder, gen);

      // undo get-idx
      builder.popOp();
      builder.increaseStackSize();

      builder.visitLoadFn(proto);

      builder.visitOpNewSlot(isConst);
   }
}
