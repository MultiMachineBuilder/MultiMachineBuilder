/**
 * 
 */
package mmb.world.items.electronics;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;

import mmb.beans.Titled;
import mmb.data.contents.Textures;
import mmb.data.contents.Textures.Texture;
import mmb.graphics.awt.ColorMapper;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class ComponentGenerator implements Titled, Identifiable<String>{
	/** The color of the component's contacts */
	@Nonnull public final Texture tex;
	/** The prefix for component titles */
	@Nonnull public final String pre;
	/** The ID used for item ID generation*/
	@Nonnull public final String id;
	public ComponentGenerator(String id) {
		this.tex = Textures.get1("item/adv "+id+".png");
		this.id= id;
		this.pre = " "+$res("component-"+id);
		Electronics.comptypes0.add(this);
	}
	@Override
	public String id() {
		return id;
	}
	@Override
	public String title() {
		return pre;
	}
	/**
	 * @param componentTier component tier to use
	 * @return generated component texture
	 */
	@Nonnull public BufferedImage newTexture(ComponentTier componentTier) {
		ColorMapper mapper = ColorMapper.ofType(tex.img.getType(), Color.RED, componentTier.c);
		LookupOp op = new LookupOp(mapper, null);
		BufferedImage img = op.createCompatibleDestImage(tex.img, null);
		op.filter(tex.img, img);
		return img;
	}
}
