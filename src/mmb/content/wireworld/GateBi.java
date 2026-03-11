/**
 * 
 */
package mmb.content.wireworld;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.PropertyExtension;
import mmb.annotations.Nil;
import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.texture.TextureDrawer;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;

/**
 * This block reads two signals, from both rear corners and combines them in a specific fashion, defined by this block's type,
 * and outputs the result of computation forward
 * @author oskar
 */
public final class GateBi extends BlockEntityRotary{
	protected boolean result;
	public final BiGateType type;
	
	public GateBi(BiGateType type) {
		this.type = type;
	}
	
	public static interface BiBool{
		/**
		 * @param a the left input
		 * @param b the right input
		 * @return the result
		 */
		public boolean run(boolean a, boolean b);
	}
	
	/**
	 * @author oskar
	 * The block type for block entity
	 */
	public static class BiGateType extends BlockType{
		public final BiBool gate;
		public final RotatedImageGroup rig;
		public BiGateType(String id, String texture, BiBool gate, PropertyExtension... props) {
			super(id, props);
			this.rig = RotatedImageGroup.create(texture);
			setTexture(new TextureDrawer(Textures.get(texture)));
			setBlockFactory(json -> GateBi.load(this, json));
			this.gate = gate;
		}
	}
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	public static GateBi load(BiGateType biGateType, @Nil JsonNode json) {
		GateBi result = new GateBi(biGateType);
		result.loadRotation(json);
		return result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().DL(), posX(), posY()).provideSignal(getRotation().DL());
		boolean b = owner().getAtSide(getRotation().DR(), posX(), posY()).provideSignal(getRotation().DR());
		result = type.gate.run(a, b);
	}
	@Override
	public BiGateType itemType() {
		return type;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockEntity result = new GateBi(type);
		result.setRotation(getRotation());
		return result;
	}
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}
}
