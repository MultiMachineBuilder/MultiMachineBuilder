/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public abstract class SkeletalBlockMachine extends SkeletalBlockEntityData{
	protected final Battery output  = new Battery();
	protected final Battery in = new Battery();
	protected SkeletalBlockMachine(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public Inventory getInventory(Side s) {
		// TODO Auto-generated method stub
		return super.getInventory(s);
	}

	@Override
	public InventoryReader getOutput(Side s) {
		// TODO Auto-generated method stub
		return super.getOutput(s);
	}

	@Override
	public InventoryWriter getInput(Side s) {
		// TODO Auto-generated method stub
		return super.getInput(s);
	}

	@Override
	public Electricity getElectricalConnection(Side s) {
		// TODO Auto-generated method stub
		return super.getElectricalConnection(s);
	}

	@Override
	public void load(JsonNode data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void save0(ObjectNode node) {
		// TODO Auto-generated method stub
		
	}

}
