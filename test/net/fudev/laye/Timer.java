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
package net.fudev.laye;

/**
 * @author Sekai Kyoretsuna
 */
public final class Timer
{
   private static final long NANOS_PER_SECOND = 1_000_000_000;
   private static final long NANOS_PER_MILLI = 1_000_000;
   
   private long startTimeNanos;
   
   private long elapsedTimeNanos, elapsedTimeMillis;
   private double elapsedTime;
   
   private boolean running;
   
   /**
    * Starts this timer, invalidating the elapsed time fields.
    */
   public void start()
   {
      assert(!running) : "Timer has already been started.";
      elapsedTime = elapsedTimeMillis = elapsedTimeNanos = 0;
      startTimeNanos = System.nanoTime();
      running = true;
   }
   
   /**
    * End this timer. The elapsed time fields are then set and can be retrieved using the getters.
    */
   public void end()
   {
      assert(running) : "Timer has not been started yet.";
      elapsedTimeNanos = System.nanoTime() - startTimeNanos;
      elapsedTimeMillis = elapsedTimeNanos / NANOS_PER_MILLI;
      elapsedTime = (double) elapsedTimeNanos / NANOS_PER_SECOND;
      running = false;
   }
   
   /**
    * End this timer. The elapsed time fields are then set and can be retrieved using the getters. The given string
    * output is formatted by replacing each occurrence of "{time_sec}", "{time_milli}" and "{time_nano}" to the value of
    * the elapsed time in seconds, milliseconds, and nanoseconds respectively.
    */
   public void end(final String output)
   {
      end();
      System.out.println("[Timer] " + output.replace("{time_sec}", Double.toString(elapsedTime))
            .replace("{time_milli}", Long.toString(elapsedTimeMillis))
            .replace("{time_nano}", Long.toString(elapsedTimeNanos)));
   }
   
   /**
    * @return the elapsed time in nanoseconds if this timer has ended, 0 otherwise.
    */
   public long getElapsedTimeNanos()
   {
      return elapsedTimeNanos;
   }
   
   /**
    * @return the elapsed time in seconds if this timer has ended, 0 otherwise.
    */
   public double getElapsedTime()
   {
      return elapsedTime;
   }
   
   /**
    * @return true if this timer has started, false otherwise.
    */
   public boolean isRunning()
   {
      return running;
   }
}
