package net.fudev.laye.internal.lexical;

public enum Token
{
   /** No token, used to indicate the end of the token stream. */
   NO_TOKEN,
   
   /** An identifier. */
   IDENT,
   
   // Brackets
   
   /** ( */
   OPEN_BRACKET, /** ) */
   CLOSE_BRACKET, /** { */
   OPEN_CURLY_BRACKET, /** } */
   CLOSE_CURLY_BRACKET, /** [ */
   OPEN_SQUARE_BRACKET, /** ] */
   CLOSE_SQUARE_BRACKET,
   
   // Operators and Symbols
   
   /** Overloadable operators. */
   OPERATOR,
   
   // Other symbols or non-overloadable operators.
   
   /** _ */
   WILDCARD, /** ; */
   SEMI, /** , */
   COMMA, /** <- */
   NEW_SLOT, /** = */
   EQUAL, /** . */
   DOT, /** @ */
   AT, /** .. */
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
