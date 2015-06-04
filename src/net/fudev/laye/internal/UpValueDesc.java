package net.fudev.laye.internal;

public final class UpValueDesc
{

   public final boolean inStack;
   public final int pos;

   public UpValueDesc(final boolean inStack, final int pos)
   {
      this.inStack = inStack;
      this.pos = pos;
   }

}
