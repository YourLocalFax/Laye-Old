package net.fudev.laye.internal.parse;

import java.io.Reader;
import java.util.Vector;

import net.fudev.laye.err.CompilerException;
import net.fudev.laye.internal.lexical.LayeLexer;
import net.fudev.laye.internal.lexical.Modifiers;
import net.fudev.laye.internal.lexical.Operator;
import net.fudev.laye.internal.lexical.Token;
import net.fudev.laye.internal.lexical.TokenData;
import net.fudev.laye.internal.parse.ast.ASTFunctionPrototype;
import net.fudev.laye.internal.parse.ast.SyntaxNode;
import net.fudev.laye.internal.parse.ast.expr.*;
import net.fudev.laye.internal.parse.ast.stat.StatFunction;
import net.fudev.laye.internal.parse.ast.stat.StatLocalFunction;
import net.fudev.laye.internal.parse.ast.stat.StatLocalVariable;
import net.fudev.laye.internal.parse.ast.stat.StatNewSlotMutable;
import net.fudev.laye.internal.parse.ast.stat.Statement;
import net.fudev.laye.internal.values.LayeFloat;
import net.fudev.laye.internal.values.LayeInt;
import net.fudev.laye.internal.values.LayeString;
import net.fudev.laye.internal.values.LayeValue;
import net.fudev.laye.util.Logger;

public class LayeParser
{
   private static final String LOG_GROUP = "PARSER";
   
   private LayeLexer lexer;
   
   public LayeParser ()
   {
   }
   
   public ASTFunctionPrototype parse (final Reader reader)
   {
      final Vector<SyntaxNode> nodes = new Vector<>();
      
      lexer = new LayeLexer(reader);
      
      lex(); // get the first token
      while (token != Token.NO_TOKEN)
      {
         final SyntaxNode node = topLevel();
         // should be with semicolons ending the file
         if (node != null)
         {
            nodes.add(node);
         }
      }
      
      debug("parsing finished!");
      
      final ASTFunctionPrototype proto = new ASTFunctionPrototype();
      proto.addNodes(nodes.toArray(new SyntaxNode[nodes.size()]));
      
      return proto;
   }
   
   private void error (final String message)
   {
      // Logger.error(LOG_GROUP, message);
      throw new CompilerException("line " + lexer.getLine() + ": " + message);
   }
   
   private static void debug (final String message)
   {
      Logger.debug(LayeParser.LOG_GROUP, message);
   }
   
   // Acquire Tokens
   
   private Token lastToken = Token.NO_TOKEN;
   private Token token = Token.NO_TOKEN;
   private Token peekedToken = Token.NO_TOKEN;
   
   private TokenData tokenData = TokenData.EMPTY;
   private TokenData peekedTokenData = TokenData.EMPTY;
   
   private Token getNextToken ()
   {
      Token result = lexer.lex();
      switch (result)
      {
         case NOT:
            final Token next = lexer.peek();
            switch (next)
            {
               case TYPEOF:
                  lexer.lex();
                  result = Token.NOT_TYPEOF;
                  break;
               default:
                  break;
            }
            break;
         default:
            break;
      }
      return result;
   }
   
   private void lex ()
   {
      if (peekedToken != Token.NO_TOKEN)
      {
         token = peekedToken;
         tokenData = peekedTokenData;
         peekedToken = Token.NO_TOKEN;
         peekedTokenData = TokenData.EMPTY;
      }
      else
      {
         lastToken = token;
         token = getNextToken();
         tokenData = lexer.getTokenData();
      }
   }
   
   private void peek ()
   {
      if (peekedToken == Token.NO_TOKEN)
      {
         peekedToken = getNextToken();
         peekedTokenData = lexer.getTokenData();
      }
   }
   
   private boolean expect (final Token kind)
   {
      return expect(kind, kind + " expected, got " + token);
   }
   
   private boolean expect (final Token kind, final String msg)
   {
      if (token != kind)
      {
         error(msg);
         return false;
      }
      lex();
      return true;
   }
   
   private void checkSemi (final String message)
   {
      peek();
      if ((lastToken != Token.SEMI && lastToken != Token.END) && !expect(Token.SEMI))
      {
         error(message);
      }
   }
   
   private Modifiers gatherModifiers ()
   {
      boolean isLocal = false;
      boolean isMutable = false;
      boolean isStatic = false;
      
      loop:
         while (true)
         {
            switch (token)
            {
               case LOC:
                  if (isLocal)
                  {
                     error("cannot use a modifier twice.");
                  }
                  isLocal = true;
                  lex(); // local
                  break;
               case MUT:
                  if (isMutable)
                  {
                     error("cannot use a modifier twice.");
                  }
                  isMutable = true;
                  lex(); // mut
                  break;
               case STAT:
                  if (isStatic)
                  {
                     error("cannot use a modifier twice.");
                  }
                  isStatic = true;
                  lex(); // static
                  break;
               default:
                  break loop;
            }
         }
      
      return Modifiers.get(isLocal, isMutable, isStatic);
   }
   
   private String ident ()
   {
      return ident("identifier expected as function name");
   }
   
   private String ident (final String message)
   {
      if (token != Token.IDENT)
      {
         error(message);
      }
      
      final String name = tokenData.string;
      lex(); // 'ident'
      
      return name;
   }
   
   // ---------- PARSING ---------- //
   
   private SyntaxNode topLevel ()
   {
      debug("starting top level with " + token);
      final SyntaxNode result;
      switch (token)
      {
         case SEMI:
            lex(); // ;
            result = topLevel();
            break;
         case LOC:
         case MUT:
         case STAT:
         {
            final Modifiers mods = gatherModifiers();
            if (mods.stat)
            {
               error("'static' modifier cannot be used outside of type definitions.");
            }
            if (token == Token.FN)
            {
               result = statFnDef(mods.loc, !mods.mut);
            }
            else
            {
               if (mods.loc)
               {
                  result = statLocVarDef(!mods.mut);
               }
               else
               {
                  // here, we should have a const modifier:
                  // TODO const name = thing; newSlot operator
                  result = statNewSlotMut();
               }
            }
            // TODO better error messages.
            checkSemi("';' expected.");
            break;
         }
         case FN:
            result = statFnDef(false, false);
            checkSemi("';' expected.");
            break;
         default:
            // Here, we check for a generic top level expression.
            // When an expression is parsed from the topLevel, a semicolon is
            // needed
            result = expression();
            checkSemi("top-level expressions must be followed by a semicolon.");
      }
      return result;
   }
   
   // ---------- STATEMENTS ---------- //
   
   private Statement statLocVarDef (final boolean con)
   {
      final Vector<String> names = new Vector<>();
      final Vector<Expression> values = new Vector<>();
      
      while (true)
      {
         final String localName = ident();
         
         final Expression value;
         if (token == Token.EQUAL)
         {
            lex(); // =
            value = expression();
         }
         else
         {
            value = new ExprLiteralNull();
         }
         
         names.add(localName);
         values.add(value);
         
         if (token == Token.COMMA)
         {
            lex(); // ,
         }
         else
         {
            break;
         }
      }
      
      return new StatLocalVariable(con, names, values);
   }
   
   private Statement statNewSlotMut ()
   {
      final Vector<String> names = new Vector<>();
      final Vector<Expression> values = new Vector<>();
      
      while (true)
      {
         final String newSlotName = ident();
         
         final Expression value;
         if (token == Token.EQUAL)
         {
            lex(); // =
            value = expression();
         }
         else
         {
            value = new ExprLiteralNull();
         }
         
         names.add(newSlotName);
         values.add(value);
         
         if (token == Token.COMMA)
         {
            lex(); // ,
         }
         else
         {
            break;
         }
      }
      
      return new StatNewSlotMutable(names, values);
   }
   
   private Statement statFnDef (final boolean isLocal, final boolean isConst)
   {
      // TODO make use of the isGen thing for generators
      final boolean isGen = token == Token.GEN;
      lex(); // fn | gen
      
      if (isLocal)
      {
         final String name = ident();
         final ASTFunctionPrototype proto = parseFnPrototype();
         return new StatLocalFunction(isGen, isConst, name, proto);
      }
      else
      {
         final Expression name = primaryExpression(false);
         final ASTFunctionPrototype proto = parseFnPrototype();
         return new StatFunction(isGen, isConst, name, proto);
      }
   }
   
   private ASTFunctionPrototype parseFnPrototype ()
   {
      final ASTFunctionPrototype proto = new ASTFunctionPrototype();
      
      // start with args
      
      if (token == Token.MUT)
      {
         lex(); // mut
         
         proto.addParam(ident(), false);
         
         if (token == Token.VARGS)
         {
            proto.vargs = true;
            lex(); // ...
         }
      }
      else if (token == Token.IDENT)
      {
         proto.addParam(tokenData.string, true);
         lex(); // 'ident'
         
         if (token == Token.VARGS)
         {
            proto.vargs = true;
            lex(); // ...
         }
      }
      else if (token == Token.OPEN_BRACKET)
      {
         lex(); // (
         while (true)
         {
            if (proto.vargs)
            {
               error("vargs parameter must be the last parameter.");
            }
            
            boolean isConst = true;
            if (token == Token.MUT)
            {
               isConst = false;
               lex(); // con
            }
            
            proto.addParam(ident(), isConst);
            
            if (token == Token.VARGS)
            {
               proto.vargs = true;
               lex(); // ...
            }
            
            if (token == Token.COMMA)
            {
               lex(); // ,
            }
            else
            {
               break;
            }
         }
         expect(Token.CLOSE_BRACKET);
      }
      
      expect(Token.EQUAL);
      proto.addNodes(expression());
      
      return proto;
   }
   
   // ---------- EXPRESSIONS ---------- //
   
   /**
    * Top level call for all expressions. This call will result in exactly one
    * new value on the stack, always, as Laye expressions are guaranteed to have
    * exactly one value.
    */
   private Expression expression ()
   {
      final Expression expr = factor();
      if (expr == null)
      {
         return null;
      }
      switch (token)
      {
         case NEW_SLOT:
         {
            lex(); // <-
            final Expression value = expression();
            return new ExprNewSlot(expr, value);
         }
         case EQUAL:
         {
            lex(); // =
            final Expression value = expression();
            return new ExprAssign(expr, value);
         }
         default:
            return expr;
      }
   }
   
   private Vector<Expression> commaSeparatedExpressions (final boolean allowTrailingComma)
   {
      final Vector<Expression> exprs = new Vector<>();
      while (true)
      {
         final Expression expr;
         if ((expr = expression()) == null)
         {
            if (exprs.size() > 0 && !allowTrailingComma)
            {
               error("expression expected.");
            }
            break;
         }
         exprs.add(expr);
         if (token == Token.COMMA)
         {
            lex(); // ,
         }
         else
         {
            break;
         }
      }
      return exprs;
   }
   
   private Expression primaryExpression ()
   {
      return primaryExpression(true);
   }
   
   private Expression primaryExpression (final boolean allowPostfixCall)
   {
      final Expression result;
      switch (token)
      {
         case OPERATOR:
         {
            final Operator prefix = tokenData.operator;
            lex(); // operator
            final Expression expr = primaryExpression();
            result = new ExprPrefix(expr, prefix);
            break;
         }
         case OPEN_SQUARE_BRACKET: // list
         {
            lex(); // [
            final Vector<Expression> items = commaSeparatedExpressions(true);
            // TODO better error messages.
            expect(Token.CLOSE_SQUARE_BRACKET, "']' expected.");
            result = new ExprListCtor(items, false);
            break;
         }
         case OPEN_BRACKET: // immutable list
         {
            lex(); // (
            final Vector<Expression> items = commaSeparatedExpressions(true);
            // TODO better error messages.
            expect(Token.CLOSE_BRACKET, "')' expected.");
            result = new ExprListCtor(items, true);
            break;
         }
         case OPEN_CURLY_BRACKET:
         {
            lex(); // {
            final ExprTableCtor table = new ExprTableCtor();
            while (token != Token.CLOSE_CURLY_BRACKET)
            {
               final boolean isConst;
               if (token == Token.MUT)
               {
                  lex(); // mut
                  isConst = false;
               }
               else
               {
                  isConst = true;
               }
               final Expression key = primaryExpression();
               expect(Token.EQUAL);
               final Expression value = primaryExpression();
               table.addEntry(key, value, isConst);
               if (token == Token.COMMA)
               {
                  lex(); // ,
               }
               else
               {
                  break;
               }
            }
            result = table;
            expect(Token.CLOSE_CURLY_BRACKET);
            break;
         }
         case REF:
         {
            lex(); // ref
            final Expression expr = primaryExpression();
            result = new ExprRef(expr);
            break;
         }
         case DEREF:
         {
            lex(); // deref
            final Expression expr = primaryExpression();
            result = new ExprDeref(expr);
            break;
         }
         case DELETE:
         {
            lex(); // 'delete'
            final Expression expr = primaryExpression();
            result = new ExprDelSlot(expr);
            break;
         }
         case FN:
         case GEN:
         {
            final boolean gen = token == Token.GEN;
            lex(); // fn | gen
            if (token != Token.OPEN_BRACKET)
            {
               // TODO make this nicer
               error("functions as values must begin their argument list with a '(' to ensure no confusion in the parser, please <3");
            }
            final ASTFunctionPrototype proto = parseFnPrototype();
            return new ExprFunction(gen, proto);
         }
         case TYPEOF:
            lex(); // typeof
            result = new ExprPrefixTypeof(expression());
            break;
         case IDENT:
            result = new ExprGetVar(tokenData.string);
            lex(); // ident
            break;
         case NULL:
            result = new ExprLiteralNull();
            lex(); // null
            break;
         case TRUE:
         case FALSE:
            result = new ExprLiteralBool(token == Token.TRUE);
            lex(); // (bool)
            break;
         case INT_LITERAL:
            result = new ExprLiteralInt(tokenData.integer);
            lex(); // int_literal
            break;
         case FLOAT_LITERAL:
            result = new ExprLiteralFloat(tokenData.fractional);
            lex(); // float_literal
            break;
         case STRING_LITERAL:
            result = new ExprLiteralString(tokenData.string);
            lex(); // string_literal
            break;
         case DO:
            result = exprBlock();
            break;
         case IF:
            result = exprIf();
            break;
         case FOR:
            peek();
            if (peekedToken == Token.EACH)
            {
               result = exprForEach();
            }
            else
            {
               result = exprFor();
            }
            break;
         case WHILE:
            result = exprWhile();
            break;
         case MATCH:
            result = exprMatch();
            break;
         case RET:
            lex(); // ret
            final Expression value;
            if (token != Token.SEMI)
            {
               value = expression();
            }
            else
            {
               value = null;
            }
            result = new ExprReturn(value);
            break;
         default:
            result = null;
      }
      if (lastToken == Token.END)
      {
         return result;
      }
      else
      {
         return postfix(result, allowPostfixCall);
      }
   }
   
   private Expression postfix (final Expression target, final boolean allowPostfixCall)
   {
      if (target == null)
      {
         return null;
      }
      switch (token)
      {
         case DOT:
         {
            lex(); // .
            final Expression name = new ExprLiteralString(ident());
            if (token == Token.OPEN_BRACKET && allowPostfixCall)
            {
               // Method call:
               lex(); // (
               final Vector<Expression> args = commaSeparatedExpressions(false);
               expect(Token.CLOSE_BRACKET);
               return postfix(new ExprMethodCall(target, name, args), allowPostfixCall);
            }
            else
            {
               return postfix(new ExprGetIndex(target, name), allowPostfixCall);
            }
         }
         case OPEN_SQUARE_BRACKET:
         {
            lex(); // [
            final Expression index = expression();
            expect(Token.CLOSE_SQUARE_BRACKET);
            if (token == Token.OPEN_BRACKET && allowPostfixCall)
            {
               // Method call:
               lex(); // (
               final Vector<Expression> args = commaSeparatedExpressions(false);
               expect(Token.CLOSE_BRACKET);
               return postfix(new ExprMethodCall(target, index, args), allowPostfixCall);
            }
            else
            {
               return postfix(new ExprGetIndex(target, index), allowPostfixCall);
            }
         }
         case OPEN_BRACKET:
         {
            if (!allowPostfixCall)
            {
               break;
            }
            lex(); // (
            final Vector<Expression> args = commaSeparatedExpressions(false);
            expect(Token.CLOSE_BRACKET);
            return postfix(new ExprFunctionCall(target, args), allowPostfixCall);
         }
         case INT_LITERAL:
         {
            final Expression literal = new ExprLiteralInt(tokenData.integer);
            lex(); // 'int'
            return postfix(new ExprFunctionCall(target, literal), allowPostfixCall);
         }
         case FLOAT_LITERAL:
         {
            final Expression literal = new ExprLiteralFloat(tokenData.fractional);
            lex(); // 'float'
            return postfix(new ExprFunctionCall(target, literal), allowPostfixCall);
         }
         case STRING_LITERAL:
         {
            final Expression literal = new ExprLiteralString(tokenData.string);
            lex(); // 'string'
            return postfix(new ExprFunctionCall(target, literal), allowPostfixCall);
         }
         case IDENT:
         {
            if (!allowPostfixCall)
            {
               break;
            }
            final Expression name = new ExprLiteralString(ident());
            switch (token)
            {
               case INT_LITERAL:
               {
                  final Expression literal = new ExprLiteralInt(tokenData.integer);
                  lex(); // 'int'
                  return postfix(new ExprMethodCall(target, name, literal), allowPostfixCall);
               }
               case FLOAT_LITERAL:
               {
                  final Expression literal = new ExprLiteralFloat(tokenData.fractional);
                  lex(); // 'float'
                  return postfix(new ExprMethodCall(target, name, literal), allowPostfixCall);
               }
               case STRING_LITERAL:
               {
                  final Expression literal = new ExprLiteralString(tokenData.string);
                  lex(); // 'string'
                  return postfix(new ExprMethodCall(target, name, literal), allowPostfixCall);
               }
               default:
                  // TODO how do we handle this..?
                  error("cannot do sry FIX TODO >>> LayeParser.postfix case IDENT");
                  break;
            }
         }
         default:
            break;
      }
      return target;
   }
   
   private Expression factor ()
   {
      final Expression left;
      if ((left = primaryExpression()) == null)
      {
         return null;
      }
      return factorRHS(0, left);
   }
   
   private Expression factorRHS (final int minp, Expression left)
   {
      while (token == Token.OPERATOR && tokenData.operator.precedence >= minp)
      {
         final Operator op = tokenData.operator;
         lex(); // operator
         // Load up the right hand side, if one exists
         Expression right;
         if ((right = primaryExpression()) != null)
         {
            while (token == Token.OPERATOR && tokenData.operator.precedence > op.precedence)
            {
               right = factorRHS(tokenData.operator.precedence, right);
            }
            if (op.isAssignment())
            {
               left = new ExprAssign(left, new ExprInfix(left, right, op.infixFromAssignment()));
            }
            else
            {
               left = new ExprInfix(left, right, op);
            }
         }
         else
         {
            return new ExprPostfix(left, op);
         }
      }
      return left;
   }
   
   private ExprBlock exprBlock ()
   {
      final ExprBlock result = new ExprBlock();
      
      lex(); // do
      while (token != Token.END)
      {
         final SyntaxNode node = topLevel();
         if (node != null)
         {
            result.nodes.add(node);
         }
      }
      lex(); // end
      
      return result;
   }
   
   private ExprIf exprIf ()
   {
      lex(); // if
      
      expect(Token.OPEN_BRACKET);
      final Expression condition = expression();
      expect(Token.CLOSE_BRACKET);
      
      final Expression pass = expression();
      
      final Expression fail;
      if (token == Token.EL)
      {
         lex(); // el
         fail = expression();
      }
      else
      {
         fail = null;
      }
      
      return new ExprIf(condition, pass == null ? new ExprLiteralNull() : pass,
            fail == null ? new ExprLiteralNull() : fail);
   }
   
   private ExprFor exprFor ()
   {
      lex(); // for
      
      expect(Token.OPEN_BRACKET);
      
      final String indexName = ident();
      
      // init and limit
      expect(Token.EQUAL, "'=' expected for initial value declaration.");
      final Expression initial = expression();
      
      expect(Token.TO, "'to' expected for limit value.");
      final Expression limit = expression();
      
      // step
      final Expression step;
      if (token == Token.BY)
      {
         lex(); // by
         step = expression();
      }
      else
      {
         step = null;
      }
      
      expect(Token.CLOSE_BRACKET);
      
      final Expression body = expression();
      
      return new ExprFor(indexName, initial, limit, step, body);
   }
   
   private Expression exprForEach ()
   {
      // TODO for each
      lex(); // for
      lex(); // each
      return null;
   }
   
   private ExprWhile exprWhile ()
   {
      lex(); // while
      
      expect(Token.OPEN_BRACKET);
      final Expression condition = expression();
      expect(Token.CLOSE_BRACKET);
      
      final Expression body = expression();
      
      return new ExprWhile(condition, body);
   }
   
   private ExprMatch exprMatch ()
   {
      lex(); // match
      
      expect(Token.OPEN_BRACKET);
      final Expression value = expression();
      expect(Token.CLOSE_BRACKET);
      
      final ExprMatch match = new ExprMatch(value);
      
      while (token != Token.END)
      {
         final Vector<LayeValue> cases = new Vector<>();
         if (token == Token.WILDCARD)
         {
            // leaves cases.size() == 0, so that'll be null for default when
            // needed
            lex(); // _
         }
         else
         {
            while (true)
            {
               final LayeValue caseValue;
               // since we only accept literals, check this by hand
               switch (token)
               {
                  case NULL:
                     caseValue = LayeValue.NULL;
                     lex();
                     break;
                  case TRUE:
                     caseValue = LayeValue.TRUE;
                     lex();
                     break;
                  case FALSE:
                     caseValue = LayeValue.FALSE;
                     lex();
                     break;
                  case INT_LITERAL:
                     caseValue = LayeInt.valueOf(tokenData.integer);
                     lex();
                     break;
                  case FLOAT_LITERAL:
                     caseValue = LayeFloat.valueOf(tokenData.fractional);
                     lex();
                     break;
                  case STRING_LITERAL:
                     caseValue = LayeString.valueOf(tokenData.string);
                     lex();
                     break;
                  case IDENT:
                     // TODO allow local variable constants in match expression
                  default:
                     caseValue = null;
                     error("cases in a match expression must be literals or constant local literals.");
               }
               cases.addElement(caseValue);
               if (token == Token.COMMA)
               {
                  lex(); // ,
               }
               else
               {
                  break;
               }
            }
         }
         expect(Token.EQUAL);
         final Expression result = expression();
         checkSemi("';' expected to end case.");
         match.addCase(cases, result);
      }
      
      expect(Token.END);
      
      return match;
   }
}
