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
package net.fudev.laye.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on class constructors. When that class is sent to Laye as a type, this annotation is used to
 * provide the correct implementation details.
 * 
 * Methods may be tagged with this annotation multiple times to generate multiple operators.
 * 
 * 
 * @author YourLocalFax
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(LayeInfixRepeatable.class)
public @interface LayeInfix
{
   /**
    */
   String operator() default "";
   
   /**
    */
   LayeOperator.Assoc assoc() default LayeOperator.Assoc.LEFT;
}
