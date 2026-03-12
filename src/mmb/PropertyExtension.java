package mmb;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import mmb.beans.IDescription;
import mmb.beans.IDrop;
import mmb.beans.ITexture;
import mmb.beans.ITitle;
import mmb.beans.IWindowTool;
import mmb.engine.block.BlockType;
import mmb.engine.block.BlockType.BlockFactory;
import mmb.engine.blockdrawer.BlockDrawer;
import mmb.engine.blockdrawer.ColorDrawer;
import mmb.engine.blockdrawer.TextureDrawer;
import mmb.engine.chance.Chance;
import mmb.engine.recipe.ItemList;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;
import mmb.menu.wtool.WindowTool;

public interface PropertyExtension {
	/**
	 * Adds a property to an item/block type
	 * @param element the target to add properties
	 * @throws ClassCastException if the property does not support the specified object
	 */
	public void addProperty(Object element);
	
	public static PropertyExtension translateTitle(String translation) {
		return setTitle(GlobalSettings.$str1(translation));
	}
	public static PropertyExtension setTitle(String title) {
		return (object) -> ((ITitle)object).setTitle(title);
	}
	public static PropertyExtension translateDescription(String translation) {
		return setDescription(GlobalSettings.$str1(translation));
	}
	public static PropertyExtension setDescription(String title) {
		return (object) -> ((IDescription)object).setDescription(title);
	}
	public static PropertyExtension setTexture(BlockDrawer texture) {
		return (object) -> ((ITexture)object).setTexture(texture);
	}
	public static PropertyExtension setTextureAsset(String texture) {
		return setTexture(new TextureDrawer(Textures.get(texture)));
	}
	public static PropertyExtension setTextureColor(Color texture) {
		return setTexture(new ColorDrawer(texture));
	}
	public static PropertyExtension setSurface(boolean isSurface) {
		return (object) -> ((BlockType)object).isSurface = isSurface;
	}
	public static PropertyExtension setLeaveBehind(BlockType block) {
		return (object) -> ((BlockType)object).setLeaveBehind(block);
	}
	public static PropertyExtension setBlockFactory(BlockFactory factory) {
		return (object) -> ((BlockType)object).setBlockFactory(factory);
	}
	public static PropertyExtension setMiningDrops(Chance drops) {
		return (object) -> ((IDrop)object).setDrop(drops);
	}
	public static PropertyExtension setWindowTool(WindowTool tool) {
		return (object) -> ((IWindowTool)object).setWindowTool(tool);
	}
	public static PropertyExtension setTextureImage(BufferedImage img) {
		return setTexture(new TextureDrawer(img));
	}
}
