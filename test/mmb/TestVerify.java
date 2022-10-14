/**
 * 
 */
package mmb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author oskar
 *
 */
class TestVerify {

	@Test
	void testPositive() {
		assertDoesNotThrow(() -> Verify.requirePositive(1));
		assertThrows(IllegalArgumentException.class, () -> Verify.requirePositive(0));
		assertThrows(IllegalArgumentException.class, () -> Verify.requirePositive(-1));
		assertThrows(IllegalArgumentException.class, () -> Verify.requirePositive(Double.POSITIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requirePositive(Double.NEGATIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requirePositive(Double.NaN));
	}
	@Test
	void testNonnegative() {
		assertDoesNotThrow(() -> Verify.requireNonNegative(1));
		assertDoesNotThrow(() -> Verify.requireNonNegative(0));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireNonNegative(-1));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireNonNegative(Double.POSITIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireNonNegative(Double.NEGATIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireNonNegative(Double.NaN));
	}
	@Test
	void testFinite() {
		assertDoesNotThrow(() -> Verify.requireFinite(1));
		assertDoesNotThrow(() -> Verify.requireFinite(0));
		assertDoesNotThrow(() -> Verify.requireFinite(-1));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireFinite(Double.POSITIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireFinite(Double.NEGATIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireFinite(Double.NaN));
	}
	@Test
	void testValid() {
		assertDoesNotThrow(() -> Verify.requireValid(1));
		assertDoesNotThrow(() -> Verify.requireValid(0));
		assertDoesNotThrow(() -> Verify.requireValid(-1));
		assertDoesNotThrow(() -> Verify.requireValid(Double.POSITIVE_INFINITY));
		assertDoesNotThrow(() -> Verify.requireValid(Double.NEGATIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> Verify.requireValid(Double.NaN));
	}

}
