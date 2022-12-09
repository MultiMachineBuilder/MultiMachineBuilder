/**
 * 
 */
package mmbgame.modular.part;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

import mmbeng.chance.Chance;
import mmbeng.craft.RecipeOutput;
import mmbeng.texture.BlockDrawer;

/**
 *
 * @author oskar
 *
 */
public class Part extends PartBase implements PartEntry{
	@Override
	public Part type() {
		return this;
	}
	@Override
	public PartEntry createPart() {
		return this;
	}
	@Override
	public PartEntry partClone() {
		return this;
	}
	
	//Chainable methods
	@Override
	public Part texture(String texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public Part texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public Part texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	@Override
	public Part texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public Part title(String title) {
		setTitle(title);
		return this;
	}
	@Override
	public Part describe(String description) {
		setDescription(description);
		return this;
	}
	@Override
	public Part finish(String id) {
		register(id);
		return this;
	}
	@Override
	@Nonnull public Part volumed(double volume) {
		setVolume(volume);
		return this;
	}
	@Override public Part rtp(RecipeOutput rtp) {
		super.rtp(rtp);
		return this;
	}
	@Override public Part drop(Chance chance) {
		super.drop(chance);
		return this;
	}
	
}
