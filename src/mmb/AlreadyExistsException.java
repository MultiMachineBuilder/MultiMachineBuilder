package mmb;

import mmb.annotations.Nil;

/**
 * Throws when trying to add a duplicate resource where not permitted
 */
public class AlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 4332059951476865054L;

	public AlreadyExistsException() {
		super();
	}

	public AlreadyExistsException(@Nil String message, @Nil Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistsException(@Nil String message) {
		super(message);
	}

	public AlreadyExistsException(@Nil Throwable cause) {
		super(cause);
	}
}
