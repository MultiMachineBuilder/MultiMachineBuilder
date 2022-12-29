/**
 * 
 */
package mmb.fuzz;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import com.fasterxml.jackson.databind.JsonNode;

import mmb.Main;
import mmb.engine.MMBUtils;
import mmb.engine.debug.Debugger;
import mmb.engine.json.JsonTool;
import mmb.engine.worlds.universe.Universe;

/**
 *
 * @author oskar
 *
 */
public class FuzzWorldLoader {
	private static final Debugger debug = new Debugger("FUZZ WORLD LOADER");
	private static Throwable crash;
	
	static void setup() throws Exception {
		//hook so the test can fail without crashing rest of the game
		Main.errorhook(e -> crash = e);
		//start the game as normal
		Main.main();
	}
	
	@FuzzTest
	public void fuzzWorldLoader(FuzzedDataProvider fdp) {
		try {
			Debugger.init();
			if(!Main.isRunning()) {
				//debug.printl("Setup...");
				//setup();
			}
			
			JsonNode node;
			boolean method = true;
			if(method) {
				node = Fuzzhelper.generateJsonNode(fdp);
				debug.printl("JSON data: ");
				debug.printl(JsonTool.save(node));
			}else {
				String s = fdp.consumeRemainingAsString();
				node = JsonTool.parse(s);
				debug.printl("JSON data: ");
				debug.printl(s);
			}
			Universe univ = new Universe();
			univ.load(node);
		}catch(Throwable e) {
			if(e instanceof InterruptedException) {
				debug.pstm(e, "INTERRUPTED!");
				Thread.currentThread().interrupt();
			}
			if(crash != null) {
				debug.pstm(e, "CRASH!");
				MMBUtils.shoot(e);
			}else {
				//debug.pstm(e, "Test exception, noncrashing");
			}
		}
	}
}
