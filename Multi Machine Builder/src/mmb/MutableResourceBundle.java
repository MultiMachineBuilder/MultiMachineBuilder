/**
 * 
 */
package mmb;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.common.collect.Iterators;

import monniasza.collects.Collects;

/**
 * @author oskar
 *
 */
public class MutableResourceBundle extends ResourceBundle {
	
	public MutableResourceBundle() {}
	
	public MutableResourceBundle(ResourceBundle rb) {add(rb);}

	public final Map<String, Object> map = new HashMap<>();
	
	public void add(ResourceBundle rb) {
		for(String key: Collects.iter(rb.getKeys())) {
			map.put(key, rb.getObject(key));
		}
	}
	
	@Override
	protected Object handleGetObject(String key) {
		return map.get(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		return Iterators.asEnumeration(map.keySet().iterator());
	}

}
