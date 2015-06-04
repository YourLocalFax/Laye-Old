package net.fudev.laye.internal.parse.ast.expr;

import java.util.Vector;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;
import net.fudev.laye.internal.parse.ast.SyntaxNode;

public class ExprBlock implements Expression
{
   public final Vector<SyntaxNode> nodes = new Vector<>();

   public ExprBlock()
   {
   }

   @Override
   public void accept(final LayeFunctionBuilder builder, final boolean isResultRequired)
   {
      builder.startBlock();
      final int size = nodes.size(), sizem1 = size - 1;
      for (int i = 0; i < size; i++)
      {
         final SyntaxNode node = nodes.get(i);
         final boolean required = isResultRequired && i == sizem1;
         node.accept(builder, required);
      }
      builder.endBlock();
   }
}
