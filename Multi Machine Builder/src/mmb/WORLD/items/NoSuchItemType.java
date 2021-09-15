/**
 * 
 */
package mmb.WORLD.items;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class NoSuchItemType implements ItemType {
	public static final NoSuchItemType INSTANCE = new NoSuchItemType();
	@SuppressWarnings("null")
	private static final BlockDrawer texture = BlockDrawer.ofImage(Textures.get("special/invalid.png"));
	
	private NoSuchItemType() {}
	
	@Override
	public String title() {
		return "IF THIS ITEM APPEARS ON THE SCREEN, THE INVENTORY HANDLES ITEMS INCORRECTLY";
	}

	@Override
	public String id() {
		return null;
	}

	@Override
	public double volume() {
		return 0;
	}

	@Override
	public void setVolume(double volume) {
		throw new IllegalStateException("NoSuchItemType is not meant to be registered");
	}

	@Override
	public void setDescription(String description) {
		throw new IllegalStateException("NoSuchItemType is not meant to be registered");
	}

	@Override
	public String description() {
		return "Any appearance of item with big red cross and frame, signifies serious problems with how items are handled."
				+ " File any cases of this item appearing at "
				+ " https://www.github.com/MultiMachineBuilder/MultiMachineBuilder/issues, if this occurs in base game,"
				+ " or at the mod's bug tracker, if it occurs in the modded inventory";
	}

	@Override
	public void setID(String id) {
		throw new IllegalStateException("NoSuchItemType is not meant to be registered");
	}

	@Override
	public void setTitle(String title) {
		throw new IllegalStateException("NoSuchItemType is not meant to be registered");
	}

	@Override
	public void setTexture(BlockDrawer texture) {
		throw new IllegalStateException("NoSuchItemType is not meant to be registered");
	}

	@Override
	public BlockDrawer getTexture() {
		return texture;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		return other != null && other instanceof NoSuchItemType;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public ItemEntry create() {
		throw new IllegalStateException("Loading a NoSuchItemType");
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ItemEntry loadItem(JsonNode node) {
		throw new IllegalStateException("Loading a NoSuchItemType");
	}
}
