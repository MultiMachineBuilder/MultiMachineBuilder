/**
 * 
 */
package mmb.engine.item;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.AlreadyExistsException;
import mmb.PropertyExtension;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.beans.IDescription;
import mmb.beans.IStorageVolume;
import mmb.beans.ITexture;
import mmb.beans.ITitle;
import mmb.beans.IWindowTool;
import mmb.content.modular.part.PartEntry;
import mmb.content.modular.part.PartType.PartFactory;
import mmb.engine.Verify;
import mmb.engine.blockdrawer.BlockDrawer;
import mmb.engine.recipe3.BadElementException;
import mmb.engine.recipe3.Group;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.wtool.ToolStandard;
import mmb.menu.wtool.WindowTool;
import monniasza.collects.Identifiable;

/**
 * Describes the base type of an item or an item entity type, without any data.
 * Simple items and item entity types are item types
 * Simple items are also item entries
 * @author oskar
 */
public class ItemType implements ITitle, Identifiable<String>, IDescription, ITexture, IWindowTool, IStorageVolume{
	@NN private static final String NO_DESCRIPTION = GlobalSettings.$res("nodescr");
	/** Placeholder for the title when the title has not been set. Chosen to bring attention to any potential bugs. Placed before the item ID*/
	@NN public static final String INVALID_TITLE = "000 Unnamed Item ";
	
	
	@NN public final String id;
	@NN public final Group singletonGroup;
	
	@NN private String title;
	@NN private String description = NO_DESCRIPTION;
	@NN private BlockDrawer texture = BlockDrawer.ofColor(new Color(0, 0, 0, 0));
	
	private ItemFactory factory;
	/** 
	 * Sets a part factory. It must not be null. 
	 * @param factory new part factory. Must not be null
	 * @throws NullPointerException when factory == null
	 */
	public void setItemFactory(ItemFactory factory) {
		Objects.requireNonNull(factory, "factory is null");
		this.factory = factory;
	}
	/** 
	 * Creates or loads parts. The part factory must be set first. 
	 * @param data JSON data to load. If null, the part is freshly created.
	 * @return a newly created part
	 * @throws IllegalStateException when the part factory has not been set
	 * @throws NullPointerException when the part factory returns null
	 */
	public ItemEntry createItem(@Nil JsonNode data) {
		if(factory == null) throw new IllegalStateException("Item factory not initialized");
		return Objects.requireNonNull(factory.create(data, this), "The item factory returned null");
	}
	
	/** 
	 * The minimal constructor for item types 
	 * @param id unique item type ID
	 * @param properties additional properties
	 */
	@SafeVarargs
	public ItemType(String id, PropertyExtension... properties){
		if(Items.items.get(id) != null) throw new AlreadyExistsException(id + " is already registred");
		this.id = id;
		singletonGroup = Group.of("item:"+id);
		Items.register(this);
		
		Objects.requireNonNull(id, "id is null");
		
		this.title = INVALID_TITLE + id;

		addGroup(singletonGroup);
		addGroup(Group.ANY);
		
		for(var prop: properties) prop.addProperty(this);
	}

	@Override public String id() {return id;}
	@Override public String title() {return title;}
	
	@NN private HashSet<Group> internalGroups = new HashSet<>();
	@NN public final Set<Group> groups = Collections.unmodifiableSet(internalGroups);
	/**
	 * Adds an item group to this item. It must not be {@code null}, {@link Group#NONE} or a singleton group not belonging to this item.
	 * @param group the new item group
	 * @return has the item not had the specified group previously
	 * @throws NullPointerException when the group is null
	 * @throws IllegalArgumentException if the group is NONE
	 * @throws IllegalArgumentException if the group is other item's singleton group
	 */
	public boolean addGroup(Group group){
		Objects.requireNonNull(group, "group is null");
		if(group == Group.NONE)
			throw new IllegalArgumentException("Items must not have the NONE group");
		if(group != singletonGroup && Items.singletonGroups.contains(group))
			throw new IllegalArgumentException("Items must not have singleton groups belonging to other items");
		return internalGroups.add(group); 
	}
	/**
	 * Remove an item group from this item. It must not be {@code null}, {@link Group#ANY} or this item's singleton group
	 * @param group the item group to be removed
	 * @return has the item had the specified group previously
	 * @throws NullPointerException when the group is null
	 * @throws IllegalArgumentException if the group is ANY
	 * @throws IllegalArgumentException if the group is this item's singleton group
	 */
	public boolean removeGroup(Group group){
		Objects.requireNonNull(group, "group is null");
		if(group == singletonGroup)
			throw new IllegalArgumentException("Item must have its singleton group");
		if(group == Group.ANY)
			throw new IllegalArgumentException("Item must have the ANY group");
		return internalGroups.remove(group);
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String descr) {
		Objects.requireNonNull(descr, "descr is null");
		this.description = descr;
	}
	@Override
	public void setTitle(String title) {
		Objects.requireNonNull(title, "title is null");
		this.title = title;
	}
	
	
	//OVERRIDES
	/**
	 * Class-specific default storage volume. 
	 * @return the target class storage volume
	 */
	public double getTypeDefaultVolume() {
		return 1.0 / 1024;
	}
	@Override
	public BlockDrawer getTexture() {
		return texture;
	}
	@Override
	public void setTexture(BlockDrawer texture) {
		this.texture = Objects.requireNonNull(texture, "texture is null");
	}
	
	//TOOLS
	private WindowTool tool;
	@Override
	public WindowTool getWindowTool() {
		if(tool == null) {
			tool = new ToolStandard();
		}
		return tool;
	}
	@Override
	public void setWindowTool(WindowTool tool) {
		Objects.requireNonNull(tool, "tool is null");
	}
	

    private double volume = getTypeDefaultVolume();
	@Override
	public double getStorageVolume() {
		return volume;
	}
	@Override
	public void setStorageVolume(double volume) {
		Verify.requirePositive(volume);
		this.volume = volume;
	}
	
	
}
