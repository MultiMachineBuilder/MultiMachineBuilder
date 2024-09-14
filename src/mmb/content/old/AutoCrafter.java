/**
 * 
 */
package mmb.content.old;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.craft.CraftingRecipeGroup.CraftingRecipe;
import mmb.content.ditems.Stencil;
import mmb.content.machinemics.line.SkeletalBlockLinear;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;
import monniasza.collects.grid.Grid;

/**
 * Manual, unpowered autocrafter
 * @author oskar
 */
public class AutoCrafter extends SkeletalBlockLinear implements BlockActivateListener {
	private static final RotatedImageGroup rig = RotatedImageGroup.create("machine/AutoCrafter 1.png");
	private Stencil stencil;
	ItemEntry toCraft;
	int remaining;
	
	@Override
	public BlockType type() {
		return ContentsBlocks.old_AUTOCRAFTER;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		openWindow = new AutoCraftGUI(this, window);
		window.openAndShowWindow(openWindow, "AutoCrafting");
	}

	@Override
	protected void cycle() {
		Stencil stencil0 = stencil;
		CraftingRecipe recipe = stencil0==null?null:stencil0.recipe();
		if(remaining == 0) {
			//Time to take a new item
			for(ItemRecord ir: incoming) {
				if(ir.amount() == 0) continue;
				//Item exists
				if(recipe != null && recipe.containsRequiredIngredients(incoming)) {
					RecipeOutput ins = recipe.inputs();
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
				RecipeOutput result = Recipe.out(recipe);
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
	@Nil public Stencil getStencil() {
		return stencil;
	}

	/**
	 * Sets the stencil. 
	 * Only stencils at most 3*3 in size are acceepted
	 * @param stencil new stencil
	 * @return did the stencil change?
	 */
	public boolean setStencil(@Nil Stencil stencil) {
		if(stencil == null) {
			this.stencil = null;
			return true;
		}
		Grid<ItemEntry> grid = stencil.grid();
		if(grid.width() > 3 || grid.height() > 3) 
			return false;
		this.stencil = stencil;
		return true;
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
