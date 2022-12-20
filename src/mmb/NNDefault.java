/**
 * 
 */
package mmb;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, LOCAL_VARIABLE, TYPE_PARAMETER, TYPE_USE})
/**
 * Indicates that parameters won't accept null unless annotated with {@link Nil}
 * @author oskar
 */
public @interface NNDefault {
	//empty
}
