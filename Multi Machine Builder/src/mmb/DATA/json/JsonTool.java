/**
 * 
 */
package mmb.DATA.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	public static JsonArray requestArray(String name, JsonObject obj) {
		JsonElement e = obj.get(name);
		if(e == null) return new JsonArray();
		return e.getAsJsonArray();
	}
	public static JsonObject requestObject(String name, JsonObject obj) {
		JsonElement e = obj.get(name);
		if(e == null) return new JsonObject();
		return e.getAsJsonObject();
	}
	public static int requestInt(String name, JsonObject obj, int def) {
		JsonElement e = obj.get(name);
		if(e == null) return def;
		return e.getAsInt();
	}
}
