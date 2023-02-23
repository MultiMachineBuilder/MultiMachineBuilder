/**
 * 
 */
package mmbtest.fuzz;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

import mmb.engine.item.Items;

/**
 *
 * @author oskar
 *
 */
class FuzzGetItem {

	@FuzzTest
	void test(FuzzedDataProvider fdp) {
		Items.get(fdp.consumeAsciiString(12));
	}

}
