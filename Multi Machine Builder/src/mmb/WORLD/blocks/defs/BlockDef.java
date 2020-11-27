/**
 * 
 */
package mmb.WORLD.blocks.defs;

import java.util.List;

/**
 * @author oskar
 *
 */
public class BlockDef {
	public String title;
	public String ID;
	
	
	//Required for blocks that support JSON list attributes
	
	
	//Required for blocks that support modules
	@SuppressWarnings("rawtypes")
	public List<BlockModule> bmodules;
	
	//Required for blocks that support variants
	@SuppressWarnings("rawtypes")
	public VariantGroup vgroup;
}
