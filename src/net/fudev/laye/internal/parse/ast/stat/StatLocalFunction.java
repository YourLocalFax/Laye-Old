package net.fudev.laye.internal.parse.ast.stat;

import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;

public final class StatLocalFunction implements Statement
{
   public boolean gen, con;
   public String name;
   public ASTFunctionPrototype prototype;
   
   public StatLocalFunction(final boolean gen, final boolean con, final String name,
         final ASTFunctionPrototype prototype)
   {
      this.gen = gen;
      this.con = con;
      this.name = name;
      this.prototype = prototype;
   }
   
   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      final int local = builder.addLocal(name, con);
      final FunctionPrototype proto = prototype.generate(builder, gen);
      
      builder.visitLoadFn(proto);
      builder.visitOpStore(local);
      
      if (!isResultRequired)
      {
         builder.visitOpPop(1);
      }
   }
}
