/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.*;
import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.awt.MappedColorTexture;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.BlockMap;
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

	/**
	 * @wbp.parser.entryPoint
	 */
	public Chest(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
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
			setColor(DataProcessor.loadColor(cnode));
		else
			setColor(Color.WHITE);
	}

	@Override
	protected void save1(JsonNode node) {
		ObjectNode on = (ObjectNode) node;
		on.set("color", DataProcessor.saveColor(c));
	}

	private final MappedColorTexture texture = new MappedColorTexture(Color.WHITE, Color.WHITE, origTexture);
	@Override
	public void render(int x, int y, Graphics g) {
		/*
		super.render(x, y, g);
		GraphicsUtil.filledCrossedBox(x+2, y+2, 27, 27, c, Color.BLACK, g);
		*/
		texture.draw(this, x, y, g);
	}

	@Override
	public void click(int blockX, int blockY, World map, WorldWindow window) {
		if(window == null) return;
		window.openAndShowWindow(new ChestGui(this, window), "chest");
	}
	
}