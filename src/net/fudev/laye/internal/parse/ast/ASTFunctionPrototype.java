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
package net.fudev.laye.internal.parse.ast;

import java.util.Vector;

import net.fudev.laye.internal.FunctionPrototype;
import net.fudev.laye.internal.compile.laye.LayeFunctionBuilder;

public final class ASTFunctionPrototype
{
   private static final class Param
   {
      public final String name;
      public final boolean con;
      
      public Param(final String name, final boolean con)
      {
         this.name = name;
         this.con = con;
      }
   }
   
   public boolean vargs;
   public final Vector<Param> args = new Vector<>();
   public final Vector<SyntaxNode> nodes = new Vector<>();
   
   public ASTFunctionPrototype()
   {
   }
   
   public void addParam(final String name, final boolean con)
   {
      args.add(new Param(name, con));
   }
   
   public void addNodes(final SyntaxNode... nodes)
   {
      for (final SyntaxNode node : nodes)
      {
         this.nodes.addElement(node);
      }
   }
   
   public FunctionPrototype generate(final LayeFunctionBuilder parent, final boolean gen)
   {
      final LayeFunctionBuilder builder = new LayeFunctionBuilder(parent);
      builder.generator = gen;
      
      for (final Param arg : args)
      {
         builder.addParam(arg.name, arg.con);
      }
      
      if (vargs)
      {
         builder.setHasVargs();
      }
      
      final int numNodes = nodes.size(), numNodesMinus1 = numNodes - 1;
      for (int i = 0; i < numNodes; i++)
      {
         final SyntaxNode node = nodes.get(i);
         node.accept(builder, i == numNodesMinus1);
      }
      
      builder.visitOpHalt(numNodes > 0);
      
      return builder.build();
   }
}
