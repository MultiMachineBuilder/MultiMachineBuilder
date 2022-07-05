/**
 * 
 */
package mmb.WORLD.blocks.chest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.*;
import mmb.DATA.Save;
import mmb.DATA.contents.Textures;
import mmb.GRAPHICS.awt.ColorMapper;
import mmb.GRAPHICS.awt.MappedColorTexture;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class Chest extends AbstractChest implements BlockActivateListener, Colorable {
	private Color c = Color.WHITE;
	private static final BufferedImage origTexture = Textures.get("machine/chest1.png");
	
	@Override
	public Color getColor() {
		return c;
	}
	@Override
	public void setColor(Color c) {
		this.c = c;
		mctexture.setTo(c);
	}

	private final BlockType type;
	private final double capacity;
	private final BufferedImage texture;
	@SuppressWarnings("null")
	private final MappedColorTexture mctexture;
	/**
	 * Creates a chest
	 * @param capacity capacity in cubic meters
	 * @param type block type
	 */
	public Chest(double capacity, BlockType type, BufferedImage texture) {
		this.capacity = capacity;
		this.type = type;
		this.texture = texture;
		this.mctexture = new MappedColorTexture(Color.WHITE, Color.WHITE, texture);
		inv.setCapacity(capacity);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.CHEST;
	}

	@SuppressWarnings("null")
	@Override
	protected void load1(ObjectNode node) {
		inv.setCapacity(capacity);
		ObjectNode on = node;
		JsonNode cnode = on.get("color");
		if(!(cnode == null || cnode.isMissingNode() || cnode.isNull()))
			setColor(Save.loadColor(cnode));
		else
			setColor(Color.WHITE);
	}

	@SuppressWarnings("null")
	@Override
	protected void save1(ObjectNode node) {
		ObjectNode on = node;
		on.set("color", Save.saveColor(c));
	}

	
	@Override
	public void render(int x, int y, Graphics g, int side) {
		mctexture.draw(this, x, y, g, side);
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openAndShowWindow(new ChestGui(this, window), "chest");
	}
	@Override
	public BlockEntry blockCopy() {
		Chest copy = new Chest(capacity, type, texture);
		copy.c = c;
		copy.inv.set(inv);
		return copy;
	}
	
}