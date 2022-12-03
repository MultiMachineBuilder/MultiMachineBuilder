/**
 * 
 */
package mmb.world.modulars.universal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import mmb.GlobalSettings;
import mmb.MMBUtils;
import mmb.menu.world.inv.InventoryController;
import mmb.texture.BlockDrawer;
import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.ItemEntry;
import mmb.world.item.Items;
import mmb.world.items.SpeedUpgrade;
import mmb.world.modulars.ModularBlock;
import mmb.world.modulars.chest.BlockModuleUniversal;
import mmb.world.modulars.gui.ModuleConfigHandler;
import mmb.world.part.PartEntity;
import mmb.world.part.PartEntityType;
import mmb.world.part.PartEntry;
import mmb.world.part.PartType;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;

/**
 * A basic implementation of an item mover.
 * @author oskar
 */
public class MoverModule extends PartEntity implements BlockModuleUniversal {
	//Type definitions
	/**
	 * An importer+exporter module pair
	 * @author oskar
	 */
	public static class MoverPair{
		/** Importer of this pair */
		@Nonnull public final MoverDef importer;
		/** Exporter of this pair */
		@Nonnull public final MoverDef exporter;
		/**
		 * Creates a mover pair
		 * @param importer importer
		 * @param exporter exporter
		 */
		public MoverPair(MoverDef importer, MoverDef exporter) {
			this.importer = importer;
			this.exporter = exporter;
		}
	}
	/**
	 * An implementation of an item mover
	 * @author oskar
	 */
	public static interface ItemMoverHandle{
		/**
		 * Moves item entries
		 * @param ireader source
		 * @param iwriter destination
		 * @param settings settings item
		 * @param stacking stacking value of the mover
		 * @param maxVolume maximum volume
		 */
		public void moveItems(InventoryReader ireader, InventoryWriter iwriter, @Nullable ItemEntry settings, int stacking, double maxVolume);
	}
	/**
	 * A provider for a mover
	 * @author oskar
	 */
	public static class MoverDef extends PartEntityType{
		/** The tick handler for the mover */
		@Nonnull public final ItemMoverHandle handler;
		/** false - import, true-export */
		public final boolean direction;
		/** The rotated texture */
		@Nonnull public final RotatedImageGroup rig;
		
		/**
		 * Creates a mover type
		 * @param handler I/O handler
		 * @param direction direction of motion
		 * @param rig texture of this mover
		 */
		public MoverDef(
				ItemMoverHandle handler,
				boolean direction, RotatedImageGroup rig) {
			this.handler = handler;
			this.direction = direction;
			this.rig = rig;
			setTexture(rig.U);
		}
		/** Constant to declare an importing mover */
		public static final boolean IMPORT = false;
		/** Constant to declare an exporting mover */
		public static final boolean EXPORT = false;

		@Override
		public MoverDef texture(String texture) {
			setTexture(texture);
			return this;
		}
		@Override
		public MoverDef texture(BufferedImage texture) {
			setTexture(texture);
			return this;
		}
		@Override
		public MoverDef texture(Color texture) {
			setTexture(BlockDrawer.ofColor(texture));
			return this;
		}
		@Override
		public MoverDef texture(BlockDrawer texture) {
			setTexture(texture);
			return this;
		}
		@Override
		public MoverDef title(String title) {
			setTitle(title);
			return this;
		}
		@Override
		public MoverDef describe(String description) {
			setDescription(description);
			return this;
		}
		@Override
		public MoverDef finish(String id) {
			register(id);
			return this;
		}
		@Override
		@Nonnull public MoverDef volumed(double volume) {
			setVolume(volume);
			return this;
		}
		@Override public MoverDef rtp(RecipeOutput rtp) {
			super.rtp(rtp);
			return this;
		}
		@Override public MoverDef drop(Chance chance) {
			super.drop(chance);
			return this;
		}
	}
	
	//Utils
	@Nonnull private static final String IMPORTER = GlobalSettings.$res("modchest-import");
	@Nonnull private static final String EXPORTER = GlobalSettings.$res("modchest-export");
	/**
	 * Creates a pair of movers
	 * @param handle item move handler
	 * @param img texture
	 * @param title display title key (without suffixes)
	 * @param id part ID (without suffixes)
	 * @return a new pair (importing, exporting) of modular item movers
	 */
	@Nonnull public static MoverPair create(ItemMoverHandle handle, BufferedImage img, String title, String id){
		RotatedImageGroup rigExport = RotatedImageGroup.create(img);
		RotatedImageGroup rigImport = rigExport.flip();
		
		String trType = GlobalSettings.$str1(title);
		
		//Create the exporter
		MoverDef petExport = new MoverDef(handle, MoverDef.EXPORT, rigExport)
				.title(trType + " " + EXPORTER)
				.volumed(0.01)
				.finish("modchest.export."+id);
		petExport.factory(() -> new MoverModule(petExport, null, null, 1));
				
		
		//Create the importer
		MoverDef petImport = new MoverDef(handle, MoverDef.IMPORT, rigImport)
				.title(trType + " " + IMPORTER)
				.volumed(0.01)
				.finish("modchest.import."+id);
		petImport.factory(() -> new MoverModule(petImport, null, null, 1));
		
		String[] chesttags = {"modular", "module"};
		Items.tagsItems(chesttags, petImport, petExport);
		return new MoverPair(petImport, petExport);
	}
	
	//Constructor
	/**
	 * Creates an item mover
	 * @param type part type
	 * @param settings settings
	 * @param upgrade 
	 * @param stacking 
	 */
	public MoverModule(MoverDef type, @Nullable ItemEntry settings, @Nullable SpeedUpgrade upgrade, int stacking) {
		this.type = type;
		this.settings = settings;
		this.upgrade = upgrade;
		this.stacking = stacking;
	}

	//Part definition
	@Nonnull private final MoverDef type;
	@Nullable private ItemEntry settings;
	/** The current upgrade */
	@Nullable public final SpeedUpgrade upgrade;
	/** The current stack value */
	public final int stacking;
	
	//Part atributes
	/** @return current settings item */
	public ItemEntry settings() {
		return settings;
	}
	@Override
	public PartEntry partClone() {
		return this;
	}
	@Override
	public PartType type() {
		return type;
	}
	@Override
	public RotatedImageGroup rig() {
		return type.rig;
	}
	/** @return speed mutiplier */
	public double speedup() {
		return SpeedUpgrade.speedup(upgrade);
	}

	//Module Config Handler
	@Override
	public ModuleConfigHandler<BlockModuleUniversal, ?> mch() {
		return mch;
	}
	@Nonnull private static final ModuleConfigHandler<BlockModuleUniversal, ?> mch = MMBUtils.thisIsAReallyLongNameUnsafeCastNN(new MMMCH());
	private static class MMMCH implements ModuleConfigHandler<MoverModule, MoverModuleSetup>{

		@Override
		public MoverModuleSetup newComponent(InventoryController invctrl) {
			return new MoverModuleSetup(invctrl);
		}

		@Override
		public MoverModule elementFromGUI(MoverModuleSetup gui, MoverModule oldElement) throws Exception {
			MoverDef type = oldElement.type;
			ItemEntry settings = gui.settings.getSelection();
			Integer stacking = (Integer) gui.stacker.getValue();
			SpeedUpgrade upgrade = oldElement.upgrade;
			return new MoverModule(type, settings, upgrade, stacking);
		}

		@Override
		public void loadGUI(MoverModule element, MoverModuleSetup gui) {
			gui.settings.setSelection(element.settings);
			gui.stacker.setValue(element.stacking);
		}

		@Override
		public MoverModule replaceUpgradesWithinItem(MoverModule element, @Nullable ItemEntry upgrade) {
			if(!(upgrade instanceof SpeedUpgrade)) return null;
			return new MoverModule(element.type, element.settings, (SpeedUpgrade) upgrade, element.stacking);
		}

		@Override
		public ItemEntry upgrades(MoverModule element) {
			return element.upgrade;
		}
	}
	
	//Equality
	@Override
	protected int hash0() {
		return Objects.hashCode(settings);
	}
	@Override
	protected boolean equal0(PartEntity other) {
		if(other instanceof MoverModule) 
			return Objects.equals(((MoverModule)other).settings, settings);
		return false;
	}
	
	//Serialization
	@Override
	public JsonNode save() {
		return ItemEntry.saveItem(settings);
	}
	@Override
	public void load(@Nullable JsonNode data) {
		settings = ItemEntry.loadFromJson(data);
	}
	
	//Run
	@Override
	public void run(ModularBlock<?, BlockModuleUniversal, ?, ?> block, Side storedSide, Side realSide) {
		if(type.direction) {
			//export 
			InventoryReader ir = block.i_out(realSide);
			InventoryWriter iw = block.owner().getAtSide(realSide, block.posX(), block.posY()).getInput(realSide.negate());
			type.handler.moveItems(ir, iw, settings, stacking, 0.2 * speedup());
		}else {
			//import
			InventoryReader ir = block.owner().getAtSide(realSide, block.posX(), block.posY()).getOutput(realSide.negate());
			InventoryWriter iw = block.i_in(realSide);
			type.handler.moveItems(ir, iw, settings, stacking, 0.2 * speedup());
		}
	}
}
