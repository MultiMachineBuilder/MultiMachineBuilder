/**
 * 
 */
package mmb.content.modular.part;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import mmb.annotations.NN;
import mmb.engine.chance.Chance;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.texture.BlockDrawer;

/**
 *
 * @author oskar
 *
 */
public class PartEntityType extends PartBase{
	@Override
	public PartEntityType type() {
		return this;
	}
	
	//Factory
	@MonotonicNonNull private Supplier<@NN PartEntry> factory;
	/**
	 * Sets the factory used to create
	 * @param newfactory
	 * @return this
	 */
	public PartEntityType factory(Supplier<@NN PartEntry> newfactory) {
		factory = newfactory;
		return this;
	}
	@Override
	public PartEntry createPart() {
		Supplier<@NN PartEntry> factory0 = factory;
		if(factory0 == null) throw new IllegalArgumentException("The factory is not set");
		return factory0.get();
	}
	
	//Chainable methods
	@Override
	public PartEntityType texture(String texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public PartEntityType texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public PartEntityType texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	@Override
	public PartEntityType texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public PartEntityType title(String title) {
		setTitle(title);
		return this;
	}
	@Override
	public PartEntityType describe(String description) {
		setDescription(description);
		return this;
	}
	@Override
	public PartEntityType finish(String id) {
		register(id);
		return this;
	}
	@Override
	@NN public PartEntityType volumed(double volume) {
		setVolume(volume);
		return this;
	}
	@Override public PartEntityType rtp(RecipeOutput rtp) {
		super.rtp(rtp);
		return this;
	}
	@Override public PartEntityType drop(Chance chance) {
		super.drop(chance);
		return this;
	}
}
