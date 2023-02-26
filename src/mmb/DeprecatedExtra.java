/**
 * 
 */
package mmb;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Documented
@Retention(RUNTIME)
/**
 * Extra information about deprecated elements
 * @author oskar
 */
public @interface DeprecatedExtra {
	/** In which version a replacement is planned to be added, optional */
	public String replacementVer() default "";
	/** In which version the element is going to be removed, optional */
	public String removal() default "";
}
