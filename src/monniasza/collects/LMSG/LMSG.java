/**
 * 
 */
package monniasza.collects.LMSG;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author oskar
 * Represents a List-Matched Selector Group. High performance alternative to from list maps
 * @param <K> key type
 * @param <V> value type. Should not be nullable
 */
public class LMSG<K, V> {
	/**
	 * The value in the node
	 */
	public @Nullable V value;
	/**
	 * The list of subnodes
	 */
	public final Map<K, LMSG<K, V>> subs = new HashMap<>();
	/**
	 * Adds a List-Matched Selector Group to this LMSG. Overwrites value
	 * @param lmsg
	 */
	public void add(LMSG<K, V> lmsg) {
		value = lmsg.value;
		subs.putAll(lmsg.subs);
	}
	/**
	 * Adds a List-Matched Selector Group to this LMSG. Overwrites value
	 * @param key key, under which LMSG should be placed
	 * @param lmsg
	 */
	public void add(K key, LMSG<K,V> lmsg) {
		LMSG<K, V> lmsg0 = subs.get(key);
		if(lmsg0 != null) lmsg.add(lmsg);
	}
	/**
	 * Gets a sub-LMSG, addressed with given list
	 * @param l LMSG address
	 * @return a sub-LMSG, or null if not found
	 */
	public LMSG<K, V> getSub(List<? extends K> l){
		LMSG<K, V> lmsg = this;
		for(K key: l) {
			lmsg = lmsg.subs.get(key);
			if(lmsg == null) return null;
		}
		return lmsg;
	}
	
}
