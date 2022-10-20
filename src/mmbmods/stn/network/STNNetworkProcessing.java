/**
 * 
 */
package mmbmods.stn.network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.world.blocks.ContentsBlocks;
import mmb.world.items.ItemEntry;
import mmbmods.stn.block.STNBaseMachine;

/**
 * The implementation of the Auto-Crafting component of the STN
 * @author oskar
 */
public class STNNetworkProcessing {
	/**
	 * A recipe tag used by STN machines
	 * @author oskar
	 */
	public class STNRGroupTag{
		/** The icon shown in lists */
		@Nonnull public ItemEntry icon = ContentsBlocks.blockVoid;
		/** The name of the crafting group */
		@Nonnull public final String name;
		/**
		 * Creates a recipe tag
		 * @param icon the recipe tag's icon
		 * @param s name of the recipe tag
		 */
		public STNRGroupTag(ItemEntry icon, String s) {
			this.icon = icon;
			this.name = s;
		}
		
		//Equality checks for data structures
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + name.hashCode();
			return result;
		}
		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			STNRGroupTag other = (STNRGroupTag) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return name.equals(other.name);
		}
		private STNNetworkProcessing getEnclosingInstance() {
			return STNNetworkProcessing.this;
		}
		
		//The recipes themselves
		
		//Recipe indexing
	}
	
	/**
	 * @param dataLayerSTN
	 */
	public STNNetworkProcessing(DataLayerSTN dataLayerSTN) {
		// TODO Auto-generated constructor stub
	}

	//Crafting system
	void theProcessingMachineIsGone(STNBaseMachine m) {
		/*if(m instanceof STNProcessor) {
			
		}else if(m instanceof STNCrafter){
		
		}*/
	}
	
	
	//CRAFTING PLANNER
}
