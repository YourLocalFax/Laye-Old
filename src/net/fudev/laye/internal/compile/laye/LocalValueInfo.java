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
package net.fudev.laye.internal.compile.laye;

public class LocalValueInfo
{
   
   public static final int IS_UP_VALUE = -1;
   
   public String name;
   public int location;
   public int startOp = 0, endOp = 0;
   public boolean isConst;
   
   public LocalValueInfo(final String name, final int location, final boolean isConst)
   {
      this.location = location;
      this.name = name;
      this.isConst = isConst;
   }
   
   private LocalValueInfo(final String name, final int location, final int startOp, final int endOp,
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
