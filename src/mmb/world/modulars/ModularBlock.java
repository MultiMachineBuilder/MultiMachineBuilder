/**
 * 
 */
package mmb.world.modulars;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.common.returnsreceiver.qual.This;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmb.texture.BlockDrawer;
import mmb.world.block.SensitiveBlock;
import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.electric.Electricity;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.NoSuchInventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.ItemEntry;
import mmb.world.modulars.chest.BlockModuleUniversal;
import mmb.world.modulars.gui.ModularChestGUI;
import mmb.world.part.PartEntry;
import mmb.world.part.PartType;
import mmb.world.rotate.ChiralRotation;
import mmb.world.rotate.Rotation;
import mmb.world.rotate.Side;
import mmb.world.worlds.world.World;

/**
 * A modular block.
 * @author oskar
 * @param <Tblock> type of the block
 * @param <Tmodule> type of modules (usually {@link BlockModuleUniversal})
 * @param <Tcore> type of core
 * @param <Tsettings> type of settings
 * @implSpec The type of the settings must be mutable to be loaded
 */
public interface ModularBlock<
	Tblock extends ModularBlock<Tblock, Tmodule, Tcore, Tsettings>,
	Tmodule extends BlockModule<Tmodule>,
	Tcore extends BlockCore<Tcore>,
	Tsettings> extends BlockActivateListener, SensitiveBlock{
	
	//Death handler
	/** Run when block is demolished */
	default void killModular() {
		//Kill the core
		Slot<Tcore> slotCore = slotC();
		if(slotCore != null) slotCore.set(null);
		
		//Kill modules
		killModule(slotInternal(Side.U));
		killModule(slotInternal(Side.D));
		killModule(slotInternal(Side.L));
		killModule(slotInternal(Side.R));
	}
	/**
	 * An iternal helper to kill a module
	 * @param slot
	 */
	default void killModule(@Nullable Slot<Tmodule> slot) {
		if(slot != null) slot.set(null);
	}
	
	//Click handler
	@Override
	default void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		boolean result = replaceHelper(window.selectedItem(), window.getPlayer().inv, partX, partY);
		if(!result) {
			ModularChestGUI tab = createGUI(window);
			boolean open =  openTab(tab);
			if(open) {
				window.openAndShowWindow(tab, type().title());
			}else {
				tab.close(window);
			}
		}
	}
	/**
	 * Internal method to handle clicks
	 * @param selected selected item record
	 * @param inv inventory
	 * @param partX X position of the click on this block
	 * @param partY Y position of the click on this block
	 * @return did the player click the slot?
	 */
	default boolean replaceHelper(@Nullable ItemRecord selected, Inventory inv, double partX, double partY) {
		final double left = 5.0/16;
		final double right = 11.0/16;
		final double depth = 3.0/16;
		final double antidepth = 13.0/16;
		boolean inX = partX < right && partX > left;
		boolean inY = partY < right && partY > left;
		if(inX) {
			if(inY){
				replace(slotC(), selected, inv); //Center
				return true;
			}else if(partY > antidepth){
				replace(slot(Side.D), selected, inv); //Down
				return true;
			}else if(partY < depth){
				replace(slot(Side.U), selected, inv); //Up
				return true;
			}
		}else if(inY) {
			if(partX > antidepth){
				replace(slot(Side.R), selected, inv); //Right
				return true;
			}else if(partX < depth){
				replace(slot(Side.L), selected, inv); //Left
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Replaces the module with one requested by the player
	 * @apiNote A helper method used in default implementation of the click handler.
	 * @param slot module slot
	 * @param selected item record
	 * @param inv player inventory
	 */
	default <Tmc extends BlockModuleOrCore<Tmc, ? super Tblock, ?>> void replace(@Nullable Slot<? extends Tmc> slot, @Nullable ItemRecord selected, Inventory inv) {
		//Null check
		if(slot == null) return;
		
		//Removal
		Tmc already = slot.get();
		if(already != null) {
			RecipeOutput toinsert = already.returnToPlayer();
			boolean insert = inv.bulkInsert(toinsert, 1) == 1;
			if(insert) slot.set(null);
			Chance drop = already.dropItems();
			drop.drop(null, owner(), posX(), posY());
		}
		
		//Addition
		if(selected != null) {
			if(!selected.canExtract()) return;
			if(selected.amount() == 0) return;
			ItemEntry selectedItem = selected.item();
			if(selectedItem instanceof PartType) {
				PartType pt = (PartType) selectedItem;
				PartEntry part = pt.createPart();
				boolean replace = slot.setto(part);
				if(replace) selected.extract(1);
			}
		}
	}
	
	//GUI
	/**
	 * Invoked when the tab is closed
	 * @param gui GUI to be closed
	 */
	public void closeTab(ModularChestGUI gui);
	/**
	 * Creates additional GUI for this block
	 * @return an additional GUI
	 */
	public default ModularChestGUI createGUI(WorldWindow window) {
		return new ModularChestGUI(window, this);
	}
	/**
	 * Invoked when the tab is opened
	 * @param tab tab to open
	 * @return is the tab already open?
	 */
	public boolean openTab(ModularChestGUI tab);
	
	//Provision of internal access points to modules
	/**
	 * Provides an inventory for purposes of block modules
	 * @param s side to access from
	 * @return an inventory
	 */	
	@Nonnull default Inventory i_inv(Side s) {
		return NoSuchInventory.INSTANCE;
	}
	/**
	 * Provides an inventory writer for purposes of block modules
	 * @param s side to access from
	 * @return an inventory writer
	 */
	default @Nonnull InventoryWriter i_in(Side s) {
		return i_inv(s).createWriter();
	}
	/**
	 * Provides an inventory reader for purposes of block modules
	 * @param s side to access from
	 * @return an inventory reader
	 */
	default @Nonnull InventoryReader i_out(Side s) {
		return i_inv(s).createReader();
	}
	/**
	 * Provides a boolean signal for purposes of block modules
	 * @param s side to access from
	 * @return a boolean signal
	 */
	default boolean i_signal(Side s) {
		return false;
	}
	/**
	 * Provides an electrical connection for purposes of block modules
	 * @param s side to access from
	 * @return an electrical connection
	 */
	@Nullable default Electricity i_elec(Side s) {
		return null;
	}
		
	//Provision of access points for outside blocks
	@Override
	default boolean provideSignal(Side s) {
		Tmodule module = module(s);
		if(module == null) return i_signal(s);
		return module.provideSignal(that(), s);
	}
	@Override
	default Inventory getInventory(Side s) {
		Tmodule module = module(s);
		if(module == null) return i_inv(s);
		return module.provideInventory(that(), s);
	}
	@Override
	default InventoryReader getOutput(Side s) {
		Tmodule module = module(s);
		if(module == null) return i_out(s);
		return module.provideOutput(that(), s);
	}
	@Override
	default InventoryWriter getInput(Side s) {
		Tmodule module = module(s);
		if(module == null) return i_in(s);
		return module.provideInput(that(), s);
	}
	@Override
	default Electricity getElectricalConnection(Side s) {
		Tmodule module = module(s);
		if(module == null) return i_elec(s);
		return module.provideElectricity(that(), s);
	}
	
	//Access to external blocks from outside
	/**
	 * Find the input signal for the block
	 * @param w world in which block is located
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @return a signal
	 */
	public default boolean findSignals(World w, int x, int y) {
		return findSignalsSub(w, x, y, Side.U)
			|| findSignalsSub(w, x, y, Side.D)
			|| findSignalsSub(w, x, y, Side.L)
			|| findSignalsSub(w, x, y, Side.R);
	}
	/**
	 * A helper for the {@link #findSignals(World, int, int)}
	 * @param w world in which block is located
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param s side, where the module is located
	 * @return a signal
	 */
	public default boolean findSignalsSub(World w, int x, int y, Side s) {
		Tmodule module = module(s);
		boolean sig = w.getAtSide(s, x, y).provideSignal(s.negate());
		if(module != null) sig = module.decorateExternalSignal(sig);
		return sig;
	}
	
	//Core
	/** @return core slot */
	@Nullable public Slot<@Nullable Tcore> slotC();
	/** @return the current core, or null if not found */
	@Nullable public default Tcore core() {
		Slot<@Nullable Tcore> slot = slotC();
		if(slot == null) return null;
		return slot.get();
	}
	/**
	 * Sets a core with types already checked (faster)
	 * @param core new core
	 * @return did the core chenge?
	 */
	public default boolean setCore(@Nullable Tcore core) {
		Slot<@Nullable Tcore> slot = slotC();
		if(slot == null) return false;
		slot.set(core);
		return true;
	}
	/**
	 * Sets a core withot types checked (slower)
	 * @param core new core
	 * @return did the core chenge?
	 */
	public default boolean settoCore(Object core) {
		Slot<@Nullable Tcore> slot = slotC();
		if(slot == null) return false;
		return slot.setto(core);
	}
	
	//Modules
	/** @return is the block modular?*/
	public boolean isModular();
	/**
	 * Gets a module slot for a logical side
	 * @param s side to access from
	 * @return a module slot
	 */
	@Nullable public Slot<@Nullable Tmodule> slotInternal(Side s);
	/**
	 * Gets a module slot for a side
	 * @implSpec This method should not be overridden
	 * @param s side to access from
	 * @return a module slot
	 */
	@Nullable public default Slot<@Nullable Tmodule> slot(Side s){
		return slotInternal(getChirotation().apply(s));
	}
	/**
	 * Gets a module
	 * @implSpec This method should not be overridden
	 * @param s side to access from
	 * @return a module at given side
	 */
	@Nullable public default Tmodule module(Side s) {
		Slot<Tmodule> slot = slot(s);
		if(slot == null) return null;
		return slot.get();
	}
	/**
	 * Gets a module using a logical side
	 * @implSpec This method should not be overridden
	 * @param s side to access from
	 * @return a module at given side
	 */
	@Nullable public default Tmodule moduleInternal(Side s) {
		Slot<Tmodule> slot = slotInternal(s);
		if(slot == null) return null;
		return slot.get();
	}
	/**
	 * Sets a module
	 * @param module replacement module
	 * @param s logical side of the module
	 * @return did the module change?
	 */
	public default boolean setModule(@Nullable Object module, Side s) {
		Slot<@Nullable Tmodule> slot = slotInternal(s);
		if(slot == null) return false;
		return slot.setto(module);
	}
	
	//Settings
	/**
	 * Loads settings from a JSON node
	 * @param node node to load from
	 * @param settings settings to load
	 */
	public void loadSettings(@Nullable JsonNode node, Tsettings settings);
	/**
	 * Saves settings
	 * @param node settings to save
	 * @return saved settings
	 */
	public JsonNode saveSettings(@Nullable Tsettings node);
	/**
	 * Gets the current settings
	 * @return current settings, or null if unsupported
	 */
	public @Nullable Tsettings getSettings();
	/**
	 * Replaces settings with the ones stored in the settings object
	 * @param settings settings to use
	 */
	public void setSettings(Tsettings settings);
	
	//Serialization
	/**
	 * Save core, modules and settings
	 * @param node object node to save to
	 */
	public default void saveModularHelper(ObjectNode node) {
		//Save modules
		if(isModular()) {
			node.set("moduleU", PartEntry.savePart(moduleInternal(Side.U)));
			node.set("moduleD", PartEntry.savePart(moduleInternal(Side.D)));
			node.set("moduleL", PartEntry.savePart(moduleInternal(Side.L)));
			node.set("moduleR", PartEntry.savePart(moduleInternal(Side.R)));
		}
		
		//Save core
		Slot<Tcore> coreSlot = slotC();
		if(coreSlot != null) {
			node.set("core", PartEntry.savePart(coreSlot.get()));
		}
		
		//Save settings
		node.set("settings", saveSettings(getSettings()));
	}
	/**
	 * Load core, module and settings
	 * @param node node to load from
	 */
	public default void loadModularHelper(@Nullable JsonNode node) {
		if(node == null) return;
		
		//Load core
		Slot<Tcore> slotC = slotC();
		if(slotC != null) slotC.setto(PartEntry.loadFromJsonExpectType(node.get("core"), slotC.type));
		
		//Load modules
		loadModuleHelper(node.get("moduleU"), Side.U);
		loadModuleHelper(node.get("moduleD"), Side.D);
		loadModuleHelper(node.get("moduleL"), Side.L);
		loadModuleHelper(node.get("moduleR"), Side.R);
		
		//Load settings
		Tsettings settings = getSettings();
		if(settings != null) loadSettings(node.get("settings"), settings);
	}
	/**
	 * Loads a single module
	 * @param sub node to load from
	 * @param side side of the slot
	 */
	public default void loadModuleHelper(@Nullable JsonNode sub, Side side) {
		Slot<Tmodule> slot = slotInternal(side);
		if(slot != null) slot.setto(PartEntry.loadFromJsonExpectType(sub, slot.type));
	}
	
	//Ticker
	/**
	 * A tick helper
	 * @param w world in which block is located
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 */
	public default void runAllModules(World w, int x, int y) {
		//Run the core
		Tcore core = core();
		if(core != null) core.runCore(that());
		
		ChiralRotation chirot = getChirotation();
		runModule(chirot, Side.U);
		runModule(chirot, Side.D);
		runModule(chirot, Side.L);
		runModule(chirot, Side.R);
	}
	/**
	 * @param chirot chirotation of the block
	 * @param s side, where the module is located
	 */
	public default void runModule(ChiralRotation chirot, Side s) {
		Tmodule module = moduleInternal(s);
		if(module != null) module.run(that(), s, chirot.apply(s));
	}
	
	/** @return this */
	@SuppressWarnings("unchecked")
	@This
	@Nonnull public default Tblock that() {
		return (Tblock) this;
	}
	
	//Rendering
	/**
	 * Renders the background
	 * @param x left X coordinate on the screen
	 * @param y upper Y coordinate on the screen
	 * @param g graphics context
	 * @param s size
	 */
	public default void renderBG(Graphics g, int x, int y, int s) {
		SensitiveBlock.super.render(x, y, g, s);
	}
	@Override
	default void render(int x, int y, Graphics g, int side) {
		//Render background
		renderBG(g, x, y, side);
		
		//Render modules
		renderModule(x, y, g, side, Rotation.N);
		renderModule(x, y, g, side, Rotation.E);
		renderModule(x, y, g, side, Rotation.S);
		renderModule(x, y, g, side, Rotation.W);
		
		//Render core
		Tcore core = core();
		if(core != null) core.render(x, y, g, side);
	}
	/**
	 * Renders a module
	 * @param x left X coordinate on the screen
	 * @param y upper Y coordinate on the screen
	 * @param g graphics context
	 * @param size size
	 * @param r rotation of the module
	 */
	default void renderModule(int x, int y, Graphics g, int size, Rotation r) {
		Side s = r.U();
		Tmodule module = module(s);
		if(module == null) return;
		int offsetX = (13*size*s.blockOffsetX)/32;
		int offsetY = (13*size*s.blockOffsetY)/32;
		int px = x+offsetX;
		int py = y+offsetY;
		BlockDrawer rotated = module.rig().get(r);
		rotated.draw(this, px, py, g, size);
	}
}
