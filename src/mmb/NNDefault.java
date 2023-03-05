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
@Target({TYPE, FIELD, METHOD, PARAMETER, LOCAL_VARIABLE, TYPE_PARAMETER, TYPE_USE, PACKAGE, MODULE})
/**
 * Indicates that parameters in a given context won't accept null unless annotated with {@link Nil}
 * @author oskar
 */
public @interface NNDefault {
	//empty
}
