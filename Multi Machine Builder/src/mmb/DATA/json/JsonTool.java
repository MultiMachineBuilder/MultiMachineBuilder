/**
 * 
 */
package mmb.DATA.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author oskar
 *
 */
public class JsonTool {
	public static final GsonBuilder builder;
	public static final Gson gson;
	static {
		builder = new GsonBuilder(); 
		builder.setPrettyPrinting(); 
		gson = builder.create();
	}

}
