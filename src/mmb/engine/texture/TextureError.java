/**
 * 
 */
package mmb.engine.texture;

/**
 * A fatal error thrown when there is not enough room for textures
 * @author oskar
 */
public class TextureError extends Error {
	private static final long serialVersionUID = -63170535805769130L;

	public TextureError() {
		super();
	}

	public TextureError(String message, Throwable cause) {
		super(message, cause);
	}

	public TextureError(String message) {
		super(message);
	}

	public TextureError(Throwable cause) {
		super(cause);
	}
}
