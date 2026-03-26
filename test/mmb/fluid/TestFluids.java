package mmb.fluid;

import java.awt.Color;

import mmb.PropertyExtension;
import mmb.engine.block.Block;
import mmb.engine.block.Blocks;
import mmb.engine.blockdrawer.ColorDrawer;

public class TestFluids {
	private static final Block WATER_BLOCK = new Block(
		"test.water",
		PropertyExtension.setTexture(new ColorDrawer(Color.BLUE)),
		PropertyExtension.setTitle("Test Water")
	);

	private static final Block LAVA_BLOCK = new Block(
		"test.lava",
		PropertyExtension.setTexture(new ColorDrawer(Color.ORANGE)),
		PropertyExtension.setTitle("Test Lava")
	);

	public static final Fluid WATER = new Fluid(WATER_BLOCK);
	public static final Fluid LAVA = new Fluid(LAVA_BLOCK);
}
