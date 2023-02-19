package org.sdi.annotations;

import java.lang.annotation.*;

/**
 * @author Luis Miguel Barcos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Component {
    public Class<?>[] classes();
}
