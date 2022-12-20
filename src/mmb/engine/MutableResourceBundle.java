/**
 * 
 */
package mmb.engine;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterators;

import monniasza.collects.Collects;

/**
 * A resource bundle whose contents may change
 * @author oskar
 */
public class MutableResourceBundle extends ResourceBundle {
	
	/** Creates an empty mutable resource bundle*/
	public MutableResourceBundle() {}
	
	/** 
	 * Creates a mutable resource bundle with contents of the input bundle
	 * @param rb source bundle
	 */
	public MutableResourceBundle(ResourceBundle rb) {add(rb);}

	/** The data array used by this bundle */
	public final Map<String, Object> map = new HashMap<>();
	
	/**
	 * Adds contents of the resource bundle to this bundle
	 * @param rb source bundle
	 */
	public void add(ResourceBundle rb) {
		for(String key: Collects.iter(rb.getKeys())) {
			map.put(key, rb.getObject(key));
		}
	}
	
	@Override
	protected Object handleGetObject(@Nullable String key) {
		return map.get(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		return Iterators.asEnumeration(map.keySet().iterator());
	}

	@Override
	public boolean containsKey(@Nullable String key) {
		return map.containsKey(key);
	}

	@Override
	protected Set<String> handleKeySet() {
		return map.keySet();
	}
	@Override
	public @Nonnull Set<String> keySet() {
		return map.keySet();
	}	
}
