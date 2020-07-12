/**
 * 
 */
package mmb.data.saves;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * @author oskar
 *
 */
public class DataSaveLoaderFromFields {
	public Hashtable<String, DataValue> fields;
	public String id;
	public ModDataLoader ldFromFields() {
		List<Runnable> loaders = new ArrayList<Runnable>();
		Enumeration<String> keys = fields.keys();
		for(String i = keys.nextElement() ; keys.hasMoreElements(); i = keys.nextElement()) {
			String j = i;
			loaders.add(() -> {fields.get(j);});
		}
		
		return new ModDataLoader() {

			@Override
			public String mainDBMapKey() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void apply(Hashtable<String, String> data) {
				// TODO Auto-generated method stub
				data.forEach((String k, String v) -> {fields.});
			}
			
		};
	}
	
	public ModDataSaver svrFromFields() {
		fields.
	}
}
