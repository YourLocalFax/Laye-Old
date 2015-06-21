package net.fudev.laye.internal.parse.ast.expr;

import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;

public final class ExprFunction implements Expression
{
   public boolean gen;
   public ASTFunctionPrototype prototype;
   
   public ExprFunction(final boolean gen, final ASTFunctionPrototype prototype)
   {
      this.gen = gen;
      this.prototype = prototype;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final FunctionPrototype proto = prototype.generate(builder, gen);
      builder.visitLoadFn(proto);
   }
}
