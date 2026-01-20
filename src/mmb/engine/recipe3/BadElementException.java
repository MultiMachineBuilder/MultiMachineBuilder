package mmb.engine.recipe3;

import mmb.annotations.Nil;

/**
 * Thrown when a collection contains a forbidden item
 * <br> Example: {@link mmb.engine.recipe3.RecipeSpec#check()}
 */
public class BadElementException extends IllegalArgumentException {
	private static final long serialVersionUID = -2532875915107763413L;

	public BadElementException() {
		super();
	}

	public BadElementException(@Nil String message, @Nil Throwable cause) {
		super(message, cause);
	}

	public BadElementException(@Nil String s) {
		super(s);
	}

	public BadElementException(@Nil Throwable cause) {
		super(cause);
	}
	
}
