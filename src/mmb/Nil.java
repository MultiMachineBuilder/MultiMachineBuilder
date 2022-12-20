/**
 * 
 */
package mmb;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, LOCAL_VARIABLE, TYPE_PARAMETER, TYPE_USE})
/**
 * Indicates that annotated element may take on {@code null}
 * @author oskar
 */
public @interface Nil {
	//empty
}
