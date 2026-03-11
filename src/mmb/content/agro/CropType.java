package mmb.content.agro;

import mmb.PropertyExtension;
import mmb.engine.block.BlockType;
import mmb.engine.recipe3.RecipeOutput;

public class CropType extends BlockType{
	public CropType(String id, PropertyExtension... properties) {
		super(id, properties);
	}
	
	public int time;
	public RecipeOutput drops;
}
