/**
 * 
 */
package mmb.engine.item;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;

import mmb.annotations.NN;
import mmb.engine.recipe3.Group;
import mmb.engine.settings.GlobalSettings;

/**
 * A simple item wrapper for ItemEntities, allowing them to be used in recipes
 * @author oskar
 */
public class ItemRaw extends Item {
	/** The item group for raw items */
	@NN public static final Group GROUP = Group.of("raw");
	@NN private static final String RAW = " "+GlobalSettings.$res("rawitem");
	@NN private static final LoadingCache<@NN ItemType, @NN ItemRaw> memoizer
	= CacheBuilder.newBuilder().build(CacheLoader.from(ItemRaw::new));
	
	/**
	 * Obtains a raw item for the item entity type. If it does not exist, the item will be registered and set up
	 * @param iet item entity type
	 * @return a raw item
	 * @throws InternalError when constructor evaluation fails (this WILL be a bug)
	 * @throws IllegalArgumentException when the supplied type is a simple item type {@link Item}
	 */
	@NN public static ItemRaw make(ItemType iet) {
		if(iet instanceof Item)
			throw new IllegalArgumentException("Supplied items must not be simple items");
		try {
			return memoizer.get(iet);
		} catch (ExecutionError|UncheckedExecutionException|ExecutionException e) {
			throw new InternalError("Evaluation failed for "+iet.id(), e);
		}
	}
	
	/** The wrapped item type */
	@NN public final ItemType iet;
	private ItemRaw(ItemType iet) {
		super(iet.id);
		this.iet = iet;
		setTitle(iet.title()+RAW);
		setTexture(iet.getTexture());
		setDescription(iet.getDescription());
		for(Group group: iet.groups) {
			if(group == Group.NONE) continue;
			addGroup(group);
		}
		addGroup(GROUP);
		volume = iet.volume;
		
		Items.tagsItem(this, Items.btags.get(iet));
		Items.tagItem("raw", this);
	}
	@Override
	public String toString() {
		return "ItemRaw " + iet;
	}
}
