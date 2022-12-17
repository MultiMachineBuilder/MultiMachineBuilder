package mmb;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({PACKAGE, MODULE})
/**
 * Specifies that package has annotations non-null be default
 * @author oskar
 */
public @interface NNByDefault {
	//nothing needed
}
