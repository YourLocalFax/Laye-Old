package net.fudev.laye.internal.compile.laye;

public class LocalValueInfo
{
   
   public static final int IS_UP_VALUE = -1;
   
   public String name;
   public int location;
   public int startOp = 0, endOp = 0;
   public boolean isConst;
   
   public LocalValueInfo(final String name, final int location,
         final boolean isConst)
   {
      this.location = location;
      this.name = name;
      this.isConst = isConst;
   }
   
   private LocalValueInfo(final String name, final int location,
         final int startOp, final int endOp,
         final boolean isConst)
   {
      this.location = location;
      this.name = name;
      this.startOp = startOp;
      this.endOp = endOp;
      this.isConst = isConst;
   }
   
   public LocalValueInfo copy()
   {
      return new LocalValueInfo(name, location, startOp, endOp, isConst);
   }
   
}
