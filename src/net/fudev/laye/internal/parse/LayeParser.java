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
import net.fudev.laye.internal.parse.ast.expr.ExprAssign;
import net.fudev.laye.internal.parse.ast.expr.ExprBlock;
import net.fudev.laye.internal.parse.ast.expr.ExprDelSlot;
import net.fudev.laye.internal.parse.ast.expr.ExprDeref;
import net.fudev.laye.internal.parse.ast.expr.ExprFor;
import net.fudev.laye.internal.parse.ast.expr.ExprFunction;
import net.fudev.laye.internal.parse.ast.expr.ExprFunctionCall;
import net.fudev.laye.internal.parse.ast.expr.ExprGetIndex;
import net.fudev.laye.internal.parse.ast.expr.ExprGetVar;
import net.fudev.laye.internal.parse.ast.expr.ExprIf;
import net.fudev.laye.internal.parse.ast.expr.ExprInfix;
import net.fudev.laye.internal.parse.ast.expr.ExprListCtor;
import net.fudev.laye.internal.parse.ast.expr.ExprLiteralBool;
import net.fudev.laye.internal.parse.ast.expr.ExprLiteralFloat;
import net.fudev.laye.internal.parse.ast.expr.ExprLiteralInt;
import net.fudev.laye.internal.parse.ast.expr.ExprLiteralNull;
import net.fudev.laye.internal.parse.ast.expr.ExprLiteralString;
import net.fudev.laye.internal.parse.ast.expr.ExprMatch;
import net.fudev.laye.internal.parse.ast.expr.ExprMethodCall;
import net.fudev.laye.internal.parse.ast.expr.ExprNewInstance;
import net.fudev.laye.internal.parse.ast.expr.ExprNewSlot;
import net.fudev.laye.internal.parse.ast.expr.ExprPostfix;
import net.fudev.laye.internal.parse.ast.expr.ExprPrefix;
import net.fudev.laye.internal.parse.ast.expr.ExprPrefixTypeof;
import net.fudev.laye.internal.parse.ast.expr.ExprRef;
import net.fudev.laye.internal.parse.ast.expr.ExprReturn;
import net.fudev.laye.internal.parse.ast.expr.ExprTableCtor;
import net.fudev.laye.internal.parse.ast.expr.ExprTupleCtor;
import net.fudev.laye.internal.parse.ast.expr.ExprWhile;
import net.fudev.laye.internal.parse.ast.expr.Expression;
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
   
   public LayeParser()
   {
   }
   
   public ASTFunctionPrototype parse(final Reader reader)
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
   
   private void error(final String message)
   {
      // Logger.error(LOG_GROUP, message);
      throw new CompilerException("line " + lexer.getLine() + ": " + message);
   }
   
   private static void debug(final String message)
   {
      Logger.debug(LayeParser.LOG_GROUP, message);
   }
   
   // Acquire Tokens
   
   private Token lastToken = Token.NO_TOKEN;
   private Token token = Token.NO_TOKEN;
   private Token peekedToken = Token.NO_TOKEN;
   
   private TokenData tokenData = TokenData.EMPTY;
   private TokenData peekedTokenData = TokenData.EMPTY;
   
   private Token getNextToken()
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
   
   private void lex()
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
   
   private void peek()
   {
      if (peekedToken == Token.NO_TOKEN)
      {
         peekedToken = getNextToken();
         peekedTokenData = lexer.getTokenData();
      }
   }
   
   private boolean expect(final Token kind)
   {
      return expect(kind, kind + " expected, got " + token);
   }
   
   private boolean expect(final Token kind, final String msg)
   {
      if (token != kind)
      {
         error(msg);
         return false;
      }
      lex();
      return true;
   }
   
   private void checkSemi(final String message)
   {
      peek();
      if (lastToken != Token.SEMI && lastToken != Token.END && !expect(Token.SEMI))
      {
         error(message);
      }
   }
   
   private Modifiers gatherModifiers()
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
   
   private String ident()
   {
      return ident("identifier expected, got " + token);
   }
   
   private String ident(final String message)
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
   
   private SyntaxNode topLevel()
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
   
   private Statement statLocVarDef(final boolean con)
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
   
   private Statement statNewSlotMut()
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
   
   private Statement statFnDef(final boolean isLocal, final boolean isConst)
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
   
   private ASTFunctionPrototype parseFnPrototype()
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
    * Top level call for all expressions. This call will result in exactly one new value on the stack, always, as Laye
    * expressions are guaranteed to have exactly one value.
    */
   private Expression expression()
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
   
   private Vector<Expression> commaSeparatedExpressions(final boolean allowTrailingComma)
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
   
   private Expression primaryExpression()
   {
      return primaryExpression(true);
   }
   
   private Expression primaryExpression(final boolean allowPostfixCall)
   {
      switch (token)
      {
         case OPERATOR:
         {
            final Operator prefix = tokenData.operator;
            lex(); // operator
            final Expression expr = primaryExpression();
            return new ExprPrefix(expr, prefix);
         }
         case OPEN_SQUARE_BRACKET: // list
         {
            lex(); // [
            final Vector<Expression> items = commaSeparatedExpressions(true);
            // TODO better error messages.
            expect(Token.CLOSE_SQUARE_BRACKET, "']' expected.");
            return postfix(new ExprListCtor(items), allowPostfixCall);
         }
         case OPEN_BRACKET: // immutable list
         {
            lex(); // (
            final Vector<Expression> items = commaSeparatedExpressions(true);
            // TODO better error messages.
            expect(Token.CLOSE_BRACKET, "')' expected.");
            final int size = items.size();
            if (size == 0)
            {
               // TODO better error messages
               error("values expected.");
               return null;
            }
            else if (size == 1)
            {
               return postfix(items.get(0), allowPostfixCall);
            }
            else
            {
               return postfix(new ExprTupleCtor(items), allowPostfixCall);
            }
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
            expect(Token.CLOSE_CURLY_BRACKET);
            return postfix(table, allowPostfixCall);
         }
         case NEW:
         {
            lex(); // new
            final Expression type = primaryExpression(false);
            final String name;
            if (token == Token.COLON)
            {
               lex(); // :
               name = ident();
            }
            else
            {
               name = null;
            }
            final Vector<Expression> args;
            switch (token)
            {
               case OPEN_BRACKET:
               {
                  lex(); // (
                  args = commaSeparatedExpressions(false);
                  expect(Token.CLOSE_BRACKET);
                  break;
               }
               case INT_LITERAL:
               {
                  final Expression literal = new ExprLiteralInt(tokenData.integer);
                  lex(); // 'int'
                  args = new Vector<>();
                  args.add(literal);
                  break;
               }
               case FLOAT_LITERAL:
               {
                  final Expression literal = new ExprLiteralFloat(tokenData.fractional);
                  lex(); // 'float'
                  args = new Vector<>();
                  args.add(literal);
                  break;
               }
               case STRING_LITERAL:
               {
                  final Expression literal = new ExprLiteralString(tokenData.string);
                  lex(); // 'string'
                  args = new Vector<>();
                  args.add(literal);
                  break;
               }
               default:
                  args = new Vector<>();
                  break;
            }
            return new ExprNewInstance(type, name, args);
         }
         case REF:
         {
            lex(); // ref
            final Expression expr = primaryExpression();
            return new ExprRef(expr);
         }
         case DEREF:
         {
            lex(); // deref
            final Expression expr = primaryExpression();
            return new ExprDeref(expr);
         }
         case DELETE:
         {
            lex(); // 'delete'
            final Expression expr = primaryExpression();
            return new ExprDelSlot(expr);
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
            return new ExprPrefixTypeof(expression());
         case IDENT:
            return postfix(new ExprGetVar(ident()), allowPostfixCall);
         case NULL:
            lex(); // null
            return new ExprLiteralNull();
         case TRUE:
         case FALSE:
         {
            final Expression result = new ExprLiteralBool(token == Token.TRUE);
            lex(); // (bool)
            return result;
         }
         case INT_LITERAL:
         {
            final Expression result = new ExprLiteralInt(tokenData.integer);
            lex(); // int_literal
            return result;
         }
         case FLOAT_LITERAL:
         {
            final Expression result = new ExprLiteralFloat(tokenData.fractional);
            lex(); // float_literal
            return result;
         }
         case STRING_LITERAL:
         {
            final Expression result = new ExprLiteralString(tokenData.string);
            lex(); // string_literal
            return result;
         }
         case DO:
            return exprBlock();
         case IF:
            return exprIf();
         case FOR:
            peek();
            if (peekedToken == Token.EACH)
            {
               return exprForEach();
            }
            else
            {
               return exprFor();
            }
         case WHILE:
            return exprWhile();
         case MATCH:
            return exprMatch();
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
            return new ExprReturn(value);
         default:
            return null;
      }
   }
   
   private Expression postfix(final Expression target, final boolean allowPostfixCall)
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
         // as an operator, mind you
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
   
   private Expression factor()
   {
      final Expression left;
      if ((left = primaryExpression()) == null)
      {
         return null;
      }
      return factorRHS(0, left);
   }
   
   private Expression factorRHS(final int minp, Expression left)
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
   
   private ExprBlock exprBlock()
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
   
   private ExprIf exprIf()
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
      
      return new ExprIf(condition, pass == null ? new ExprLiteralNull() : pass, fail == null ? new ExprLiteralNull()
            : fail);
   }
   
   private ExprFor exprFor()
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
   
   private Expression exprForEach()
   {
      // TODO for each
      lex(); // for
      lex(); // each
      return null;
   }
   
   private ExprWhile exprWhile()
   {
      lex(); // while
      
      expect(Token.OPEN_BRACKET);
      final Expression condition = expression();
      expect(Token.CLOSE_BRACKET);
      
      final Expression body = expression();
      
      return new ExprWhile(condition, body);
   }
   
   private ExprMatch exprMatch()
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
