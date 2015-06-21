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
