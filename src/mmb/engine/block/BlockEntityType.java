/**
 * 
 */
package mmb.engine.block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import mmb.NN;
import mmb.engine.chance.Chance;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.worlds.world.World;

/**
 * @author oskar
 * This class represents a block entity
 * <br> All block types can be used as item types too.
 */
public class BlockEntityType extends BlockBase{
	//Behavior
	/**
	 * <br>Set this variable to handle startup.
	 * <br>Does not run when block is placed.
	 * <br>To handle placement, set onPlace variable
	 * <br>
	 * <br>Event: world loading
	 * <br>Input: newly created block entry
	 * <br>Purpose: initialize block
	 * <br>Exception handling: If exception occurs, a block is not properly initialized and error is logged into:
	 * <ul>
	 * 	<li>log file, which remains on a server</li>
	 * 	<li>error statement, sent to registered server users and newsletter subscribers</li>
	 * </ul>
	 */
	/**
	 * Set this variable to run an update on this block for every tick
	 * <br>NOTE: design this function carefully to prevent logspam
	 * <br>
	 * <br>Event: tick update (50 times per second)
	 * <br>Input: block entry to be updated
	 * <br>Purpose: run normal updates
	 * <br>Exception handling: If exception occurs, block is not properly updated and error is logged and chatted
	 * 	<ul>
	 * 		<li>to a player, if action was requested by a player or theirs block,</li>
	 * 		<li>or public, if action was requested by unclaimed or server owned block</li>
	 * 	</ul>
	 */
	/**
	 * Set this variable to prepare a block for server shutdown.
	 * <br>Does not run when block is broken.
	 * <br>To handle block removal,set onRemove variable
	 * <br>
	 * <br>Event: server shutdown
	 * <br>Input: block entry to be saved
	 * <br>Purpose: finalize block before disposal
	 * <br>Exception handling: If exception occurs, block is not properly finalized and error is logged into:
	 * <ul>
	 * 	<li>log file, which remains on a server</li>
	 * 	<li>error statement, sent to registered server users and newsletter subscribers</li>
	 * </ul>
	 */
	/**
	 * <br>Set this variable to handle the block being placed.
	 * <ul>
	 * 	<li>Event: block placement</li>
	 * 	<li>Purpose: correctly place blocks</li>
	 * 	<li>Input 1: newly created block entry, about to be placed</li>
	 * 	<li>Input 2: object which requested placement:
	 * 		<br>class name if GameObject item not provided
	 * 		<br>player instance if placed by player
	 * 		<br>WorldBehavior instance if placed by a WorldBehavior
	 * 		<br>MapBehavior instance if placed by a MapBehavior
	 * 	</li>
	 * 	<li>
	 * 		Exception handling: If exception occurs, block is not placed and error is logged and chatted
	 * 		<ul>
	 * 			<li>to a player, if action was requested by a player or theirs block,</li>
	 * 			<li>or public, if action was requested by unclaimed or server owned block</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 */
	/**
	 * <br>Set this variable to handle the block being mined.
	 * <ul>
	 * 	<li>Event: block mining</li>
	 * 	<li>Purpose: correctly mine blocks</li>
	 * 	<li>Input 1: block entry, about to be removed</li>
	 * 	<li>Input 2: object which requested placement:
	 * 		<br>class name if GameObject item not provided
	 * 		<br>player instance if mined by player
	 * 		<br>WorldBehavior instance if mined by a WorldBehavior
	 * 		<br>MapBehavior instance if mined by a MapBehavior
	 * 	</li>
	 * 	<li>
	 * 		Exception handling: If exception occurs, block is not removed and error is logged and chatted
	 * 		<ul>
	 * 			<li>to a player, if action was requested by a player or theirs block,</li>
	 * 			<li>or public, if action was requested by unclaimed or server owned block</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 */
	
	@Override
	public BlockEntry place(int x, int y, World map) {
		return map.place(this, x, y);
	}
	@Override
	public @NN BlockEntry createBlock() {
		return factory.get();
	}

	//Factory
	private Supplier<@NN BlockEntry> factory;
	/**
	 * @param factory the factory to set
	 */
	public void setFactory(Supplier<@NN BlockEntry> factory) {
		this.factory = factory;
	}
	
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets block factory.This is a convenience chainable method
	 * @param factory1 block entity factory lambda expression
	 * @return this
	 */
	@NN public BlockEntityType factory(Supplier<@NN BlockEntry> factory1) {
		setFactory(factory1);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Override
	@NN public BlockEntityType title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Override
	@NN public BlockEntityType describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Sets dropped item.This is a convenience chainable method
	 * @param drop drop
	 * @return this
	 */
	@Override
	@NN public BlockEntityType drop(Chance drop) {
		setDrop(drop);
		return this;
	}
	/**
	 * Registers this block. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Override
	@NN public BlockEntityType finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param block title
	 * @return this
	 */
	@Override
	@NN public BlockEntityType leaveBehind(BlockType block) {
		setLeaveBehind(block);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Override
	@NN public BlockEntityType volumed(double volume) {
		setVolume(volume);
		return this;
	}
}
