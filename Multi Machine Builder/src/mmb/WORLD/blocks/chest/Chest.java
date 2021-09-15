/**
 * 
 */
package mmb.WORLD.blocks.chest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.*;
import mmb.DATA.Save;
import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.awt.MappedColorTexture;
import mmb.WORLD.block.BlockEntity;
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
		texture.setTo(c);
	}

	public Chest() {
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.CHEST;
	}

	@Override
	protected void load1(JsonNode node) {
		inv.capacity = 6;
		ObjectNode on = (ObjectNode) node;
		JsonNode cnode = on.get("color");
		if(!(cnode == null || cnode.isMissingNode() || cnode.isNull()))
			setColor(Save.loadColor(cnode));
		else
			setColor(Color.WHITE);
	}

	@Override
	protected void save1(JsonNode node) {
		ObjectNode on = (ObjectNode) node;
		on.set("color", Save.saveColor(c));
	}

	private final MappedColorTexture texture = new MappedColorTexture(Color.WHITE, Color.WHITE, origTexture);
	@Override
	public void render(int x, int y, Graphics g, int side) {
		texture.draw(this, x, y, g, side);
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window) {
		if(window == null) return;
		window.openAndShowWindow(new ChestGui(this, window), "chest");
	}
	@Override
	public Chest clone() {
		Chest copy = (Chest) super.clone();
		copy.setColor(c);
		return copy;
	}
	
}