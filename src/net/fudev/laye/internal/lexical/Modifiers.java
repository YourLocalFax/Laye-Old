package net.fudev.laye.internal.lexical;

public final class Modifiers
{
   public static final Modifiers NONE = new Modifiers(false, false, false);
   
   public static final Modifiers LOCAL = new Modifiers(true, false, false);
   public static final Modifiers CONST = new Modifiers(false, true, false);
   public static final Modifiers STATIC = new Modifiers(false, false, true);
   
   public static final Modifiers LOCAL_CONST = new Modifiers(true, true, false);
   public static final Modifiers LOCAL_STATIC = new Modifiers(true, false,
         true);
   public static final Modifiers STATIC_CONST = new Modifiers(false, true,
         true);
         
   public static final Modifiers LOCAL_STATIC_CONST = new Modifiers(true, true,
         true);
         
   public static Modifiers get(final boolean isLocal, final boolean isConst,
         final boolean isStatic)
   {
      if (isLocal)
      {
         if (isConst)
         {
            if (isStatic)
            {
               return Modifiers.LOCAL_STATIC_CONST;
            }
            return Modifiers.LOCAL_CONST;
         }
         if (isStatic)
         {
            return Modifiers.LOCAL_STATIC;
         }
         return Modifiers.LOCAL;
      }
      if (isConst)
      {
         if (isStatic)
         {
            return Modifiers.STATIC_CONST;
         }
         return Modifiers.CONST;
      }
      if (isStatic)
      {
         return Modifiers.STATIC;
      }
      return Modifiers.NONE;
   }
   
   public final boolean loc;
   public final boolean mut;
   public final boolean stat;
   
   public Modifiers(final boolean loc, final boolean mut, final boolean stat)
   {
      this.loc = loc;
      this.mut = mut;
      this.stat = stat;
   }
}
