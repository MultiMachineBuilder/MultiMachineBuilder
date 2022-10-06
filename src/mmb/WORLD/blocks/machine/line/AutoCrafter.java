/**
 * 
 */
package mmb.WORLD.blocks.machine.line;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.BEANS.BlockActivateListener;
import mmb.MENU.world.window.WorldWindow;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.SkeletalBlockLinear;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.data.Stencil;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class AutoCrafter extends SkeletalBlockLinear implements BlockActivateListener {

	private static final RotatedImageGroup rig = RotatedImageGroup.create("machine/AutoCrafter 1.png");
	private Stencil stencil;
	ItemEntry toCraft;
	int remaining;
	
	@Override
	public BlockType type() {
		return ContentsBlocks.AUTOCRAFTER;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		openWindow = new AutoCraftGUI(this, window);
		window.openAndShowWindow(openWindow, "AutoCrafting");
	}

	@Override
	protected void cycle() {
		if(remaining == 0) {
			//Time to take a new item
			for(ItemRecord ir: incoming) {
				if(ir.amount() == 0) continue;
				//Item exists
				if(stencil != null && stencil.recipe().containsRequiredIngredients(incoming)) {
					RecipeOutput ins = stencil.recipe().inputs();
					for(Entry<ItemEntry> irecord: ins.getContents().object2IntEntrySet()) {
						incoming.extract(irecord.getKey(), irecord.getIntValue());
					}
					//Extracted
					remaining = 50;
					toCraft = ir.item();
					if(openWindow != null) openWindow.getInvIn().refresh();
					return;
				}//else item is not smeltable, do not take it
			}
		}else if(remaining < 0) {
			remaining = 0;
		}else{
			//Continue smelting
			remaining--;
			if(remaining == 0) {
				//Time to eject an item
				RecipeOutput result = stencil.recipe().output();
				if(result == RecipeOutput.NONE) {
					//Unsmeltable, return original
					if(toCraft != null) 
						outgoing.insert(toCraft, 1);
					
				}else{
					//Smeltable, eject expected item
					result.produceResults(outgoing.createWriter());
				}
				toCraft = null;
				if(openWindow != null) openWindow.getInvOut().refresh();
			}// else continue smelting
		}
		if(openWindow != null) openWindow.refresh();
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	protected void save2(ObjectNode node) {
		node.set("stencil", ItemEntry.saveItem(stencil));
		JsonNode smeltData = ItemEntry.saveItem(toCraft);
		node.set("smelt", smeltData);
		node.set("remain", new IntNode(remaining));
	}

	@Override
	protected void load2(ObjectNode node) {
		//Load the progress
		JsonNode itemUnderWay = node.get("smelt");
		toCraft = ItemEntry.loadFromJson(itemUnderWay);
		JsonNode remainNode = node.get("remain");
		if(remainNode != null) remaining = remainNode.asInt();
		
		//Load the stencil
		ItemEntry stencil0 = ItemEntry.loadFromJson(node.get("stencil"));
		
		//Validate the stencil
		if(stencil0 == null) {
			stencil = null;
		}else if(stencil0 instanceof Stencil) {
			stencil = (Stencil) stencil0;
		}else throw new IllegalStateException("The stencil item is invalid: "+stencil0);
	}
	
	AutoCraftGUI openWindow = null;
	void closeWindow() {
		openWindow = null;
	}

	/**
	 * @return currenct stencil
	 */
	@Nullable public Stencil getStencil() {
		return stencil;
	}

	/**
	 * Sets the stencil. 
	 * Only stencils at most 3*3 in size are acceepted
	 * @param stencil new stencil
	 * @return did the stencil change?
	 */
	public boolean setStencil(@Nullable Stencil stencil) {
		if(stencil == null) {
			this.stencil = null;
			return true;
		}else if(stencil.width() > 3 || stencil.height() > 3) {
			return false;
		}else {
			this.stencil = stencil;
			return true;
		}
	}

	@Override
	public BlockEntry blockCopy() {
		AutoCrafter copy = new AutoCrafter();
		copy.incoming.set(incoming);
		copy.outgoing.set(outgoing);
		copy.remaining = remaining;
		copy.toCraft = toCraft;
		copy.stencil = stencil;
		return copy;
	}

}
