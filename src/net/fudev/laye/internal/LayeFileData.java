package net.fudev.laye.internal;

public final class LayeFileData
{
   public static final LayeFileData UNKNOWN = new LayeFileData("<unknown>");
   
   public final String location;
   
   public LayeFileData (final String location)
   {
      this.location = location;
   }
   
}
