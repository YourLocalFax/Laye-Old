package net.fudev.laye.internal.parse.ast;

import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

/**
 * Any syntactic element in the Laye scripting language. This does not include
 * preprocessors.
 */
public interface SyntaxNode
{
   /**
    * @param builder
    *           The FunctionBuilder this node should act on.
    * @param isResultRequired
    *           <code>true</code> if this node should leave a value on the
    *           stack, <code>false</code> otherwise.
    */
   void accept(final LayeFunctionBuilder builder,
         final boolean isResultRequired);
}
