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
package net.fudev.laye.internal;

public final class Laye
{
   private Laye()
   {
   }
   
   // ---------- Info ---------- //
   
   public static final String NAME = "Laye";
   public static final String VERSION_STRING = "0.0a";
   // Development or Release
   public static final String BUILD = "Development";
   
   public static final int VERSION_NUMBER = 00_0_01;
   
   public static final String VERSION = Laye.NAME + " " + Laye.VERSION_STRING + " " + Laye.BUILD;
   
   // ---------- Constants ---------- //
   
   // 0x AA AA BB OO
   // 0x CC CC CC OO
   
   /** Laye's max stack size. */
   public static final int MAX_STACK_SIZE = 0x10000;
   
   /** The bit width of an op code */
   public static final int SIZE_OP = 8;
   /** The bit width of argument A and its signed counterpart */
   public static final int SIZE_A = 16;
   /** The bit width of argument B and its signed counterpart */
   public static final int SIZE_B = 8;
   /**
    * The bit width of argument C (a combination of A and B) and its signed counterpart
    */
   public static final int SIZE_C = Laye.SIZE_A + Laye.SIZE_B;
   
   /**
    * How many bits left, from the right-most bit of the instruction, the op code starts
    */
   public static final int POS_OP = 0;
   /**
    * How many bits left, from the right-most bit of the instruction, argument B and its signed counterpart start
    */
   public static final int POS_B = Laye.SIZE_OP;
   /**
    * How many bits left, from the right-most bit of the instruction, argument A and its signed counterpart start
    */
   public static final int POS_A = Laye.POS_B + Laye.SIZE_B;
   /**
    * How many bits left, from the right-most bit of the instruction, argument C and its signed counterpart start
    */
   public static final int POS_C = Laye.POS_B;
   
   /** The maximum value of an op code */
   public static final int MAX_OP = (1 << Laye.SIZE_OP) - 1;
   /** The maximum value of argument A */
   public static final int MAX_A = (1 << Laye.SIZE_A) - 1;
   /** The maximum value of argument A, signed */
   public static final int MAX_SA = Laye.MAX_A >> 1;
   /** The minimum value of argument A, signed */
   public static final int MIN_SA = -Laye.MAX_SA - 1;
   /** The maximum value of argument B */
   public static final int MAX_B = (1 << Laye.SIZE_B) - 1;
   /** The maximum value of argument B, signed */
   public static final int MAX_SB = Laye.MAX_B >> 1;
   /** The minimum value of argument B, signed */
   public static final int MIN_SB = -Laye.MAX_SB - 1;
   /** The maximum value of argument C */
   public static final int MAX_C = (1 << Laye.SIZE_C) - 1;
   /** The maximum value of argument C, signed */
   public static final int MAX_SC = Laye.MAX_C >> 1;
   /** The minimum value of argument C, signed */
   public static final int MIN_SC = -Laye.MAX_SC - 1;
   
   /** The bit-mask used to get only the op code */
   public static final int MASK_OP = Laye.MAX_OP << Laye.POS_OP;
   /** The bit-mask used to get only argument A or its signed counterpart */
   public static final int MASK_A = Laye.MAX_A << Laye.POS_A;
   /** The bit-mask used to get only argument B or its signed counterpart */
   public static final int MASK_B = Laye.MAX_B << Laye.POS_B;
   /** The bit-mask used to get only argument C or its signed counterpart */
   public static final int MASK_C = Laye.MAX_C << Laye.POS_C;
   
   /** The bit-mask used to get everything but the op code */
   public static final int MASK_NOT_OP = ~Laye.MASK_OP;
   /**
    * The bit-mask used to get everything but argument A or its signed counterpart
    */
   public static final int MASK_NOT_A = ~Laye.MASK_A;
   /**
    * The bit-mask used to get everything but argument B or its signed counterpart
    */
   public static final int MASK_NOT_B = ~Laye.MASK_B;
   /**
    * The bit-mask used to get everything but argument C or its signed counterpart
    */
   public static final int MASK_NOT_C = ~Laye.MASK_C;
   
   // ---------- Macros ---------- //
   
   /**
    * @return the operation code of the given instruction
    */
   public static int GET_OP(final int i)
   {
      return i >>> Laye.POS_OP & Laye.MAX_OP;
   }
   
   /**
    * @return argument a of the given instruction
    */
   public static int GET_A(final int i)
   {
      return i >>> Laye.POS_A & Laye.MAX_A;
   }
   
   /**
    * @return argument a of the given instruction, signed
    */
   public static int GET_SA(final int i)
   {
      return (i >>> Laye.POS_A & Laye.MAX_A) + Laye.MIN_SA;
   }
   
   /**
    * @return argument b of the given instruction
    */
   public static int GET_B(final int i)
   {
      return i >>> Laye.POS_B & Laye.MAX_B;
   }
   
   /**
    * @return argument b of the given instruction, signed
    */
   public static int GET_SB(final int i)
   {
      return (i >>> Laye.POS_B & Laye.MAX_B) + Laye.MIN_SB;
   }
   
   /**
    * @return argument c of the given instruction
    */
   public static int GET_C(final int i)
   {
      return i >>> Laye.POS_C & Laye.MAX_C;
   }
   
   /**
    * @return argument c of the given instruction, signed
    */
   public static int GET_SC(final int i)
   {
      return (i >>> Laye.POS_C & Laye.MAX_C) + Laye.MIN_SC;
   }
   
   /**
    * @return the given instruction with the operation code set to the given argument
    */
   public static int SET_OP(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_OP | (arg & Laye.MAX_OP) << Laye.POS_OP;
   }
   
   /**
    * @return the given instruction with argument A set to the given argument
    */
   public static int SET_A(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_A | (arg & Laye.MAX_A) << Laye.POS_A;
   }
   
   /**
    * @return the given instruction with argument A, signed, set to the given argument
    */
   public static int SET_SA(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_A | (arg - Laye.MIN_SA & Laye.MAX_A) << Laye.POS_A;
   }
   
   /**
    * @return the given instruction with argument B set to the given argument
    */
   public static int SET_B(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_B | (arg & Laye.MAX_B) << Laye.POS_B;
   }
   
   /**
    * @return the given instruction with argument B, signed, set to the given argument
    */
   public static int SET_SB(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_B | (arg - Laye.MIN_SB & Laye.MAX_B) << Laye.POS_B;
   }
   
   /**
    * @return the given instruction with argument C set to the given argument
    */
   public static int SET_C(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_C | (arg & Laye.MAX_C) << Laye.POS_C;
   }
   
   /**
    * @return the given instruction with argument C, signed, set to the given argument
    */
   public static int SET_SC(final int i, final int arg)
   {
      return i & Laye.MASK_NOT_C | (arg - Laye.MIN_SC & Laye.MAX_C) << Laye.POS_C;
   }
   
   // ---------- Operation Codes ---------- //
   
   /**
    * Closes all open outers from A forward.
    * 
    * <pre>
    * [0] CLOSE(A)<br/>for(i = openUps.len(); --i >= A;) openUps[i].close();
    * </pre>
    */
   public static final int OP_CLOSE = 0x00;
   /**
    * Pops A values from the stack.
    * 
    * <pre>
    * [-A] POP(A)<br/>
    * TOP -= A;
    */
   public static final int OP_POP = 0x01;
   /**
    * Duplicates the value at (TOP - A - 1) and puts it on the top of the stack.
    * 
    * <pre>
    * [1] DUP(A)<br/>STK[TOP++] = STK[TOP - A - 1];
    * </pre>
    */
   public static final int OP_DUP = 0x02;
   
   /**
    * Push the root table on to the stack.
    * 
    * <pre>
    * [1] LOAD_ROOT()<br/>STK[TOP++] = ROOT;
    * </pre>
    */
   public static final int OP_LOAD_ROOT = 0x03;
   /**
    * Loads local variable A and pushes it onto the stack.
    * 
    * <pre>
    * [1] LOAD(A)<br/>STK[TOP++] = LOC[A];
    * </pre>
    */
   public static final int OP_LOAD = 0x04;
   /**
    * Stores the top value in the stack in local variable A.
    * 
    * <pre>
    * [0] STORE(A)<br/>LOC[A] = STK[TOP - 1];
    * </pre>
    */
   public static final int OP_STORE = 0x05;
   /**
    * Creates a new slot in STK[TOP - 3] with the key STK[TOP - 2] and value STK[TOP - 1]. If A is not zero, the slot is
    * made constant.
    * 
    * <pre>
    * [-2] NEW_SLOT(A)<br/>STK[TOP - 3].newSlot(STK[TOP - 2], STK[TOP - 1]);<br/>STK[TOP - 3] = STK[TOP - 1];
    * </pre>
    */
   public static final int OP_NEW_SLOT = 0x06;
   /**
    * Deletes a slot in STK[top - 2] with the key STK[TOP - 1]. The value that was previously stored in that slot is
    * pushed to the stack. If no slot existed, null is pushed instead.
    * 
    * <pre>
    * [-1] DEL_SLOT()<br/>STK[TOP - 2] = STK[TOP - 2].delSlot(STK[--TOP]);
    * </pre>
    */
   public static final int OP_DEL_SLOT = 0x07;
   /**
    * Loads up value A and pushes it onto the stack.
    * 
    * <pre>
    * [1] GET_UP(A)<br/>STK[TOP++] = UPS[A];
    * </pre>
    */
   public static final int OP_GET_UP = 0x08;
   /**
    * Stores the top value in the stack in up value A.
    * 
    * <pre>
    * [0] SET_UP(A)<br/>UPS[A] = STK[TOP - 1];
    * </pre>
    */
   public static final int OP_SET_UP = 0x09;
   /**
    * Gets a value at the key STK[TOP - 1] from value STK[TOP - 2] and pushes it to the stack.
    * 
    * <pre>
    * [-1] GET_INDEX()<br/>STK[TOP - 2] = STK[TOP - 2].get(STK[TOP - 1])
    * </pre>
    */
   public static final int OP_GET_INDEX = 0x0A;
   /**
    * Sets a value at they key STK[TOP - 2] from the value STK[TOP - 3] to STK[TOP - 1] and leaves that value on the
    * stack.
    * 
    * <pre>
    * [-2] SET_INDEX()<br/>STK[TOP - 3].set(STK[TOP - 2], STK[TOP - 1]);<br/>STK[TOP - 3] = STK[TOP - 1];<br/>TOP -= 2;
    * </pre>
    */
   public static final int OP_SET_INDEX = 0x0B;
   /**
    * Loads a constant from the constant pool and pushes it onto the stack.
    * 
    * <pre>
    * [1] LOAD_CONST(A)<br/>STK[TOP++] = K[A];
    * </pre>
    */
   public static final int OP_LOAD_CONST = 0x0C;
   /**
    * Loads a bool constant to the stack: <code>true</code> if A != 0, <code>false</code> otherwise.
    * 
    * <pre>
    * [1] LOAD_BOOL(A)<br/>STK[TOP++] = A != 0;
    * </pre>
    */
   public static final int OP_LOAD_BOOL = 0x0D;
   /** */
   public static final int OP_LOAD_NULL = 0x0E;
   /** */
   public static final int OP_FUNCTION = 0x0F;
   /** */
   public static final int OP_TYPE = 0x10;
   
   /** */
   public static final int OP_POSIT = 0x11;
   /** */
   public static final int OP_NEGATE = 0x12;
   /** */
   public static final int OP_NOT = 0x13;
   /** */
   public static final int OP_COMPL = 0x14;
   /** */
   public static final int OP_TYPEOF = 0x15;
   /** */
   public static final int OP_PREFIX = 0x16;
   /** */
   public static final int OP_POSTFIX = 0x17;
   
   /** */
   public static final int OP_ADD = 0x18;
   /** */
   public static final int OP_SUB = 0x19;
   /** */
   public static final int OP_MUL = 0x1A;
   /** */
   public static final int OP_DIV = 0x1B;
   /** */
   public static final int OP_IDIV = 0x1C;
   /** */
   public static final int OP_REM = 0x1D;
   /** */
   public static final int OP_POW = 0x1E;
   /** */
   public static final int OP_AND = 0x1F;
   /** */
   public static final int OP_OR = 0x20;
   /** */
   public static final int OP_XOR = 0x21;
   /** */
   public static final int OP_LSH = 0x22;
   /** */
   public static final int OP_RSH = 0x23;
   /** */
   public static final int OP_URSH = 0x24;
   /** */
   public static final int OP_CONCAT = 0x25;
   /** */
   public static final int OP_INFIX = 0x26;
   
   /** */
   public static final int OP_BOOL_AND = 0x27;
   /** */
   public static final int OP_BOOL_OR = 0x28;
   /** */
   public static final int OP_BOOL_XOR = 0x29;
   /** */
   public static final int OP_IS_TYPEOF = 0x2A;
   
   /** */
   public static final int OP_EQ = 0x2B;
   /** */
   public static final int OP_LT = 0x2C;
   /** */
   public static final int OP_LE = 0x2D;
   /** */
   public static final int OP_3COMP = 0x2E;
   /** */
   public static final int OP_TEST = 0x2F;
   /** */
   public static final int OP_JUMP = 0x30;
   
   /** */
   public static final int OP_CALL = 0x31;
   /** */
   public static final int OP_METHOD = 0x32;
   /** */
   public static final int OP_TAIL_CALL = 0x33;
   /** */
   public static final int OP_HALT = 0x34;
   /** */
   public static final int OP_YIELD = 0x35;
   /** */
   public static final int OP_RESUME = 0x36;
   
   /** */
   public static final int OP_THIS = 0x37;
   /** */
   public static final int OP_BASE = 0x38;
   /** */
   public static final int OP_REF = 0x39;
   /** */
   public static final int OP_DEREF = 0x3A;
   /** */
   public static final int OP_IS = 0x3B;
   
   /** */
   public static final int OP_LIST = 0x3C;
   /** */
   public static final int OP_TUPLE = 0x3D;
   /** */
   public static final int OP_NEW_TABLE = 0x3E;
   /** */
   public static final int OP_NEW_INSTANCE = 0x3F;
   
   /** */
   public static final int OP_FOR_PREP = 0x40;
   /** */
   public static final int OP_FOR_TEST = 0x41;
   /** */
   public static final int OP_FOR_EACH = 0x42;
   /** */
   public static final int OP_POST_FOR_EACH = 0x43;
   
   /** */
   public static final int OP_MATCH = 0x44;
   /** */
   public static final int OP_RETURN = 0x45;
   
   /** Number of operation codes. */
   public static final int NUM_OPS = 0x46;
   
   public static final int TEMP_OP_RETURN = 0xFF;
}
