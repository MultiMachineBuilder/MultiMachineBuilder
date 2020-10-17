/**
 * 
 */
package mmb.DDDEngine2;

/**
 * @author oskar
 *
 */
public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		super();
		this.rawModel = rawModel;
		this.texture = texture;
	}
	/**
	 * @return the rawModel
	 */
	public RawModel getRawModel() {
		return rawModel;
	}
	/**
	 * @return the texture
	 */
	public ModelTexture getTexture() {
		return texture;
	}
	
	
}
