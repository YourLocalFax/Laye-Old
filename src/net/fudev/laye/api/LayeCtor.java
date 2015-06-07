package net.fudev.laye.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on class constructors. When that class is sent to
 * Laye as a type, this annotation is used to provide the correct implementation
 * details.
 * 
 * @author YourLocalFax
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayeCtor
{
   /**
    * The name of this constructor in the Laye exposed type. If no name is
    * provided, the constructor is used as a default constructor. Names must
    * comply with Laye's identifiers.
    */
   String name () default "";
}
