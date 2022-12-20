/**
 * 
 */
package mmb.content.imachine.chest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.Nil;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.java2d.MappedColorTexture;
import mmb.engine.json.Save;
import mmb.engine.worlds.world.World;
import mmbbase.beans.*;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class Chest extends AbstractChest implements BlockActivateListener {
	private Color c = Color.WHITE;
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
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
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