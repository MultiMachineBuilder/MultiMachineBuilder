/**
 * 
 */
package mmbtest.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import mmb.GameLoader;

/**
 * @author oskar
 *
 */
class TestWalkDirecotry {

	@Test
	void testFind() {
		List<File> files = new ArrayList<>();
		GameLoader.walkDirectory(new File("mods/"), files);
		assertFalse(files.isEmpty(), "Directory not walked");
	}
	
	boolean isRun = false;
	@Test
	void testRun() {
		GameLoader.walkDirectory(new File("mods/"), (s, f) -> 
			isRun = true
		);
		assertTrue(isRun, "Directory not walked");
	}
}
