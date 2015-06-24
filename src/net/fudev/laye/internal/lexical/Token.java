/**
 * Copyright 2015 YourLocalFax
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
