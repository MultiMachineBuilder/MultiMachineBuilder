/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.DataProcessor;
import mmb.GRAPHICS.awt.GraphicsUtil;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class Chest extends AbstractChest implements BlockActivateListener {
	private Color c = Color.WHITE;
	
	/**
	 * @return the c
	 */
	public Color getColor() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setColor(Color c) {
		this.c = c;
	}

	public Chest(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.CHEST;
	}

	@Override
	protected void load1(JsonNode node) {
		ObjectNode on = (ObjectNode) node;
		JsonNode cnode = on.get("color");
		if(!(cnode.isMissingNode() || cnode.isNull()))
			c = DataProcessor.loadColor(cnode);
		else
			c = Color.WHITE;
	}

	@Override
	protected void save1(JsonNode node) {
		ObjectNode on = (ObjectNode) node;
		on.set("color", DataProcessor.saveColor(c));
	}

	@Override
	public void render(int x, int y, Graphics g) {
		super.render(x, y, g);
		GraphicsUtil.filledCrossedBox(x+2, y+2, 27, 27, c, Color.BLACK, g);
	}

	@Override
	public void click(int blockX, int blockY, World map, WorldWindow window) {
		if(window == null) return;
		window.openAndShowWindow(new ChestGui(this, window), "chest");
	}
	
}