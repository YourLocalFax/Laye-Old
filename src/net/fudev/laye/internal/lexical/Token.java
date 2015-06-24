/**
 * Copyright (C) 2015 Sekai Kyoretsuna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.fudev.laye.internal.lexical;

public enum Token
{
   /** No token, used to indicate the end of the token stream. */
   NO_TOKEN,
   
   /** An identifier. */
   IDENT,
   
   // Brackets
   
   /** ( */
   OPEN_BRACKET,
   /** ) */
   CLOSE_BRACKET,
   /** { */
   OPEN_CURLY_BRACKET,
   /** } */
   CLOSE_CURLY_BRACKET,
   /** [ */
   OPEN_SQUARE_BRACKET,
   /** ] */
   CLOSE_SQUARE_BRACKET,
   
   // Operators and Symbols
   
   /** Overloadable operators. */
   OPERATOR,
   
   // Other symbols or non-overloadable operators.
   
   /** _ */
   WILDCARD,
   /** ; */
   SEMI,
   /** : */
   COLON,
   /** , */
   COMMA,
   /** <- */
   NEW_SLOT,
   /** = */
   EQUAL,
   /** . */
   DOT,
   /** @ */
   AT,
   /** .. */
   VARGS,
   
   // Keyword operators
   
   AND, OR, XOR, NAND, NOR, XNOR, NOT, REF, DEREF, DELETE, TYPEOF, NOT_TYPEOF,
   
   // Literals
   
   INT_LITERAL, FLOAT_LITERAL, STRING_LITERAL, TRUE, FALSE, NULL,
   
   // Modifiers
   
   LOC, MUT, STAT,
   
   // Object keywords
   
   FN, GEN, TYPE,
   
   // Type-related keywords
   
   HAS, CTOR, PREFIX, POSTFIX, INFIX, NEW,
   
   // Control flow keywords
   
   IF, EL, FOR, EACH, TO, BY, IN, WHILE, TAKE, MATCH, CASE, RET, BREAK, CONT, RESUME, YIELD, DO, END,
}
