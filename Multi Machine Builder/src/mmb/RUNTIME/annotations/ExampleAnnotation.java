/**
 * 
 */
package mmb.RUNTIME.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author oskar
 *
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, TYPE_PARAMETER})
public @interface ExampleAnnotation {

}
