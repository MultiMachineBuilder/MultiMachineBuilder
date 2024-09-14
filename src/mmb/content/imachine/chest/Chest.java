/**
 * 
 */
package mmb.content.imachine.chest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.java2d.MappedColorTexture;
import mmb.engine.json.JsonTool;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A basic chest
 * @author oskar
 */
public class Chest extends AbstractChest implements BlockActivateListener {
	//Constructors
	/**
	 * Creates a chest
	 * @param capacity capacity in cubic meters
	 * @param type block type
	 * @param texture chest inner texture
	 * @wbp.parser.entryPoint
	 */
	public Chest(double capacity, BlockType type, BufferedImage texture) {
		this.capacity = capacity;
		this.type = type;
		this.texture = texture;
		this.mctexture = new MappedColorTexture(Color.WHITE, Color.WHITE, texture);
		inv.setCapacity(capacity);
	}
	
	//Contents
	@NN private Color c = Color.WHITE;
	@Override
	public Color getColor() {
		return c;
	}
	@Override
	public void setColor(Color c) {
		this.c = c;
		mctexture.setTo(c);
	}
	
	private final double capacity;
	
	//Block methods
	@NN private final BlockType type;
	@NN private final BufferedImage texture;
	@NN private final MappedColorTexture mctexture;
	@Override
	public BlockType type() {
		return type;
	}
	@Override
	public BlockEntry blockCopy() {
		Chest copy = new Chest(capacity, type, texture);
		copy.c = c;
		copy.inv.set(inv);
		return copy;
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
	
	//Serialization
	@SuppressWarnings("null")
	@Override
	protected void load1(ObjectNode node) {
		inv.setCapacity(capacity);
		ObjectNode on = node;
		JsonNode cnode = on.get("color");
		if(!(cnode == null || cnode.isMissingNode() || cnode.isNull()))
			setColor(JsonTool.loadColor(cnode));
		else
			setColor(Color.WHITE);
	}
	@Override
	protected void save1(ObjectNode node) {
		ObjectNode on = node;
		on.set("color", JsonTool.saveColor(c));
	}
}