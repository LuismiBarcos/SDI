package org.sdi.annotations;

import java.lang.annotation.*;

/**
 * @author Luis Miguel Barcos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@Documented
public @interface Inject {
    public String value() default "";
}
