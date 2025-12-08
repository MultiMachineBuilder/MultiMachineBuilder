/**
 * 
 */
package mmbbench;


import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.rainerhahnekamp.sneakythrow.Sneaky;

import mmb.annotations.NN;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.json.JsonTool;
import mmbtest.StandardTestReferences;

/**
 * A set of benchmarks for simple inventories
 * @author oskar
 */
class BenchSimpleInventory {
	@NN public final SimpleInventory inv = new SimpleInventory().setCapacity(9999_9999);
	
	//10M insertions
	@Test void benchInsertion() {
		for(int i = 0; i < 10_000_000; i++) inv.insert(StandardTestReferences.block, 1);
	}
	//10M extractions
	@Test void benchExtraction() {
		for(int i = 0; i < 10_000_000; i++) inv.extract(StandardTestReferences.block, 1);
	}
	
	//10M bulk insertions
	@Test void benchBulkInsertion() {
		for(int i = 0; i < 10_000_000; i++) inv.bulkInsert(StandardTestReferences.ilist, 1);
	}
	//10M bulk extractions
	@Test void benchBulkExtraction() {
		for(int i = 0; i < 10_000_000; i++) inv.bulkExtract(StandardTestReferences.ilist, 1);
	}
	
	//10M record gets
	@Test void benchGet() {
		for(int i = 0; i < 10_000_000; i++) inv.get(StandardTestReferences.bet);
	}
	//10M record nullable gets
	@Test void benchNGet() {
		for(int i = 0; i < 10_000_000; i++) inv.nget(StandardTestReferences.bet);
	}
	
	@NN public final JsonNode loadnode = Sneaky.sneak(() -> JsonTool.parse("[128.0, [\"THIS IS A TEST BLOCK DONT SAVE IT\", 64]]"));
	@NN public final SimpleInventory savetest() {
		SimpleInventory inv0 = new SimpleInventory();
		inv0.insert(StandardTestReferences.block, 64);
		return inv0;
	}
	
	//10k saves
	@Test void benchSave() {
		SimpleInventory stinv = savetest();
		for(int i = 0; i < 10_000; i++) stinv.save();
	}
	//10k saves
	@Test void benchLoad() {
		for(int i = 0; i < 10_000; i++) inv.load(loadnode);
	}
}
