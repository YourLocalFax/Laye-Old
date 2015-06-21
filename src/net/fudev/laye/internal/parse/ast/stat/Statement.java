package net.fudev.laye.internal.parse.ast.stat;

import net.fudev.laye.internal.parse.ast.SyntaxNode;

/**
 * A Laye statement cannot be used as an r-value, and only results in a value when it is the last node of a block.
 */
public interface Statement extends SyntaxNode
{
}
