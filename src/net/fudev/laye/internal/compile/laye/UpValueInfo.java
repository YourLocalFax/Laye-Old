package net.fudev.laye.internal.compile.laye;

public final class UpValueInfo
{
   public static final int LOCAL = 0;
   public static final int UP_VALUE = 0;
   
   public final String name;
   public final int pos;
   public int type;
   public boolean isConst;
   
   public UpValueInfo(final String name, final int pos, final int type, final boolean isConst)
   {
      this.name = name;
      this.pos = pos;
      this.type = type;
      this.isConst = isConst;
   }
   
   public UpValueInfo(final UpValueInfo other)
   {
      this(other.name, other.pos, other.type, other.isConst);
   }
   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (isConst ? 1231 : 1237);
      result = prime * result + (name == null ? 0 : name.hashCode());
      result = prime * result + pos;
      result = prime * result + type;
      return result;
   }
   
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final UpValueInfo other = (UpValueInfo) obj;
      if (isConst != other.isConst)
      {
         return false;
      }
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      if (pos != other.pos)
      {
         return false;
      }
      if (type != other.type)
      {
         return false;
      }
      return true;
   }
}
