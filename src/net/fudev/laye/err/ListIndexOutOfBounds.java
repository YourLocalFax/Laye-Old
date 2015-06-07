package net.fudev.laye.err;

public final class ListIndexOutOfBounds extends LayeException
{
   
   private static final long serialVersionUID = -2399597073609803898L;
   
   public final int index;
   
   public ListIndexOutOfBounds (final int index)
   {
      super(Integer.toString(index));
      this.index = index;
   }
   
}
