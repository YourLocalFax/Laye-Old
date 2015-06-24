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
package net.fudev.laye;

public class Version
{
   private Version()
   {
   }
   
   // ---------- Info ---------- //
   
   public static final String NAME = "Laye";
   public static final String VERSION_STRING = "0.0a";
   // Development or Release
   public static final String BUILD = "Development";
   
   public static final int VERSION_NUMBER = 00_0_01;
   
   public static final String VERSION = Version.NAME + " " + Version.VERSION_STRING + " " + Version.BUILD;
}
