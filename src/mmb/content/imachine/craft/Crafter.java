package mmb.content.imachine.craft;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ditems.Stencil;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.RecipeUtil;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * Crafts items using a stencil
 */
public class Crafter extends BlockEntityData implements BlockActivateListener{
	public final SingleItemInventory stencil = new SingleItemInventory();
	public final int delay;
	private final BlockEntityType type;
	private int timer = 0;
	public final SimpleInventory incoming = new SimpleInventory();
	protected final SimpleInventory outgoing = new SimpleInventory();
	public final Inventory output = outgoing.lockInsertions();
	
	/**
	 * @param delay time between crafts in ticks 
	 */
	public Crafter(int delay, BlockEntityType type) {
		this.delay = delay;
		this.type = type;
	}

	@Override
	public @NN BlockType type() {
		return type;
	}

	@Override
	public @NN BlockEntry blockCopy() {
		Crafter crafter = new Crafter(delay, type);
		crafter.timer = timer;
		crafter.incoming.set(incoming);
		crafter.outgoing.set(outgoing);
		crafter.stencil.set(stencil);
		return crafter;
	}

	@Override
	public void load(@Nil JsonNode node) {
		if(node == null) return;
		ArrayNode invin = JsonTool.requestArray("in", node);
		incoming.load(invin);
		ArrayNode invout = JsonTool.requestArray("out", node);
		outgoing.load(invout);
		JsonNode invstencil = node.get("stencil");
		stencil.load(invstencil);
		timer = JsonTool.requestInt(node, "timer", 0);
		incoming.setCapacity(2);
		outgoing.setCapacity(2);
	}
	
	@Override
	protected void save0(ObjectNode node) {
		node.set("in", incoming.save());
		node.set("out", outgoing.save());
		node.set("stencil", stencil.save());
		node.put("timer", timer);
	}
	
	@Override
	public void onTick(MapProxy map) {
		timer++;
		if(timer < delay) return;
		timer = 0;
		ItemEntry item = stencil.getContents();
		if(item == null) return;
		if (!(item instanceof Stencil)) return;
		Stencil s = (Stencil) item;
		RecipeOutput inputs = s.in();
		RecipeOutput output1 = s.out();
		RecipeUtil.transact(inputs, incoming, output1, outgoing, 1);
		if(gui != null) gui.refresh();
	}

	public SingleItemInventory getStencil() {
		return stencil;
	}

	@Nil CrafterGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(gui != null) return;
		gui = new CrafterGUI(this, window);
		window.openAndShowWindow(gui, type.title());
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	@Override
	public InventoryReader getOutput(Side s) {
		return output.createReader();
	}

	@Override
	public InventoryWriter getInput(Side s) {
		return incoming.createWriter();
	}
	
	
}
