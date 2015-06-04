package net.fudev.laye.internal;

public final class InsnPrint
{
   public static String getOpCodeName(final int op)
   {
      // & it so you can pass in the instruction raw, just in case.
      switch (op & Laye.MAX_OP)
      {
         case Laye.OP_CLOSE:
            return "close";
         case Laye.OP_POP:
            return "pop";
         case Laye.OP_DUP:
            return "dup";

         case Laye.OP_LOAD_ROOT:
            return "load_root";
         case Laye.OP_LOAD:
            return "load";
         case Laye.OP_STORE:
            return "store";
         case Laye.OP_NEW_SLOT:
            return "new_slot";
         case Laye.OP_DEL_SLOT:
            return "del_slot";
         case Laye.OP_GET_UP:
            return "get_up";
         case Laye.OP_SET_UP:
            return "set_up";
         case Laye.OP_GET_INDEX:
            return "get_index";
         case Laye.OP_SET_INDEX:
            return "set_index";
         case Laye.OP_LOAD_CONST:
            return "load_const";
         case Laye.OP_LOAD_BOOL:
            return "load_bool";
         case Laye.OP_LOAD_NULL:
            return "load_null";
         case Laye.OP_FUNCTION:
            return "function";
         case Laye.OP_TYPE:
            return "type";

         case Laye.OP_POSIT:
            return "posit";
         case Laye.OP_NEGATE:
            return "negate";
         case Laye.OP_NOT:
            return "not";
         case Laye.OP_COMPL:
            return "compl";
         case Laye.OP_TYPEOF:
            return "typeof";
         case Laye.OP_PREFIX:
            return "prefix";
         case Laye.OP_POSTFIX:
            return "postfix";

         case Laye.OP_ADD:
            return "add";
         case Laye.OP_SUB:
            return "sub";
         case Laye.OP_MUL:
            return "mul";
         case Laye.OP_DIV:
            return "div";
         case Laye.OP_IDIV:
            return "idiv";
         case Laye.OP_REM:
            return "rem";
         case Laye.OP_POW:
            return "pow";
         case Laye.OP_AND:
            return "and";
         case Laye.OP_OR:
            return "or";
         case Laye.OP_XOR:
            return "xor";
         case Laye.OP_LSH:
            return "lsh";
         case Laye.OP_RSH:
            return "rsh";
         case Laye.OP_URSH:
            return "ursh";
         case Laye.OP_CONCAT:
            return "concat";
         case Laye.OP_INFIX:
            return "infix";

         case Laye.OP_BOOL_AND:
            return "bool_and";
         case Laye.OP_BOOL_OR:
            return "bool_or";
         case Laye.OP_BOOL_XOR:
            return "bool_xor";
         case Laye.OP_IS_TYPEOF:
            return "is_typeof";

         case Laye.OP_EQ:
            return "eq";
         case Laye.OP_LT:
            return "lt";
         case Laye.OP_LE:
            return "le";
         case Laye.OP_3COMP:
            return "3comp";

         case Laye.OP_TEST:
            return "test";
         case Laye.OP_JUMP:
            return "jump";

         case Laye.OP_CALL:
            return "call";
         case Laye.OP_METHOD:
            return "method";
         case Laye.OP_TAIL_CALL:
            return "tail_call";
         case Laye.OP_HALT:
            return "halt";
         case Laye.OP_YIELD:
            return "yield";
         case Laye.OP_RESUME:
            return "resume";
         case Laye.OP_THIS:
            return "this";
         case Laye.OP_BASE:
            return "base";
         case Laye.OP_REF:
            return "ref";
         case Laye.OP_DEREF:
            return "deref";
         case Laye.OP_IS:
            return "is";
         case Laye.OP_MUT_LIST:
            return "mut_list";
         case Laye.OP_LIST:
            return "list";
         case Laye.OP_NEW_TABLE:
            return "new_table";
         case Laye.OP_NEW_INSTANCE:
            return "new_instance";

         case Laye.OP_FOR_PREP:
            return "for_prep";
         case Laye.OP_FOR_TEST:
            return "for_test";
         case Laye.OP_FOR_EACH:
            return "for_each";
         case Laye.OP_POST_FOR_EACH:
            return "post_for_each";

         case Laye.OP_MATCH:
            return "match";

         default:
            return "unknown";
      }
   }

   private InsnPrint()
   {
   }
}
