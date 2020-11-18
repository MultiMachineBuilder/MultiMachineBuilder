package mmb.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mmb.DATA.file.AdvancedFile;
import mmb.WORLD.inventory.ItemInventory;
import mmb.WORLD.inventory.SimpleItem;
import mmb.debug.Debugger;

class Testing {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Debugger debug = new Debugger("TEST");
		String name = "brokenSoundData/sound/song.mp3";
		String[] tmp = AdvancedFile.baseExtension(name);
		String ext = tmp[1];
		String base = tmp[0];
		debug.printl(base);
		debug.printl(ext);
	}

}
