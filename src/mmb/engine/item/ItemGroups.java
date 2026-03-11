package mmb.engine.item;

import java.util.*;

import mmb.annotations.NN;
import mmb.engine.recipe3.BadElementException;
import mmb.engine.recipe3.Group;

/**
 * Contains the item's groups and enforces the <b>group policy</b>:
 * <ul>
 *  <li>the item must have its singleton group</li>
 *  <li>the item must have {@link Group#ANY}</li>
 *  <li>the item must not have {@link Group#NONE}</li>
 *  <li>the item must not have any other item's singleton group</li>
 *  <li>the item must have all of item type's groups</li>
 *  <li>the group collection's item group must not be {@code null}</li>
 *  <li>the item must not have {@code null}</li>
 * </ul>
 * The group policy has an additional point: <b>the group's item type must equal the target's item type</b>,
 * which should be enforced by the users of item group collections using the {@link #enforce(ItemType)} method
 */
public class ItemGroups {
	/** The item type the group belongs to. Enforced by users, especially as there is no way to have final default methods in interfaces. */
	@NN public final ItemType type;
	/** The full set of item groups. Checked against the group policy*/
	@NN public final Set<@NN Group> groups;
	/**
	 * Creates a full collection of item groups for an item of the specified item type.
	 * Automatically includes item groups of the item type, as they're required
	 * @param type the item type this collection will belong to
	 * @param groups desired additional item groups for the specific item entry
	 * @throws NullPointerException when type == null
	 * @throws NullPointerException when groups == null
	 * @throws BadElementException when groups contains null
	 * @throws BadElementException when groups contains {@link Group#NONE}
	 * @throws BadElementException when groups contains singleton groups not belonging to the target item/type
	 */
	public ItemGroups(ItemType type, Collection<Group> groups) {
		//Validate data
		Objects.requireNonNull(groups, "groups is null");
		Objects.requireNonNull(type, "type is null");
		for(Group group: groups) {
			if(group == null) throw new BadElementException("Items must not contain null groups");
			if(group == Group.NONE) throw new BadElementException("Items must not contain NONE groups");
			if(group != type.singletonGroup && Items.singletonGroups.contains(group)) throw new BadElementException("Items must not contain singleton groups belonging to other items");
		}
		
		//Add the type's groups
		List<@NN Group> builder = new ArrayList<>();
		builder.addAll(type.groups);
		builder.addAll(groups);
		
		this.groups = Set.copyOf(builder);
		this.type = type;
	}
	/**
	 * Enforces that the item type of this group is the specified item type
	 * @param requiredType the required item type
	 * @throws IllegalStateException if 
	 */
	public void enforce(ItemType requiredType) {
		if(type != requiredType) throw new IllegalStateException("The item group does not belong to "+requiredType.id);
	}
}
