/**
 * 
 */
package mmb.DATA.contents.texture;

/**
 * @author oskar
 *
 */
public class TextureNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7047254397578982800L;

	public TextureNotFoundException() {
		super();
	}

	public TextureNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TextureNotFoundException(String arg0) {
		super(arg0);
	}

	public TextureNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
