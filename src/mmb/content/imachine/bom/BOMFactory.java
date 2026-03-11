package mmb.content.imachine.bom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.annotations.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.imachine.chest.ChestGui;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.json.JsonTool;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

public class BOMFactory extends BlockEntityData implements BlockActivateListener{
	
	public final SimpleInventory inv = new SimpleInventory();
	
	public BOMFactory() {
		inv.setCapacity(2);
	}

	@Override
	public BlockType itemType() {
		return ContentsBlocks.BOMMAKER;
	}

	@Override
	public BlockEntry blockCopy() {
		BOMFactory copy = new BOMFactory();
		copy.inv.set(inv);
		return copy;
	}

	
	public static BOMFactory load(@Nil JsonNode data) {
		var result = new BOMFactory();
		if(data == null) return result;
		result.inv.load(JsonTool.requestArray("inventory", data));
		result.inv.setCapacity(2);
		return result;
	}

	@Override
	protected void save0(ObjectNode node) {
		node.set("inventory", inv.save());
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openAndShowWindow(new BOMChestGUI(this, window), "chest");
	}

}
