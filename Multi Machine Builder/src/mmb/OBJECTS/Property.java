/**
 * 
 */
package mmb.OBJECTS;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * @author oskar
 * Represents a property tag. Intended for fast property lookup
 */
public final class Property {
	private static final Map<@Nonnull String, @Nonnull Property> props = new HashMap<>();
	/**
	 * Creates a new property tag
	 * @param name the name of the property
	 * @throws NullPointerException if name is null
	 * @return a {@code Property} object with given name
	 */
	@Nonnull public static Property of(String name) {
		return props.computeIfAbsent(Objects.requireNonNull(name, "name is null"), Property::new);
	}
	
	/**
	 * This property's name
	 */
	@Nonnull public final String name;
	private Property(String name) {
		this.name = name;
	}
	
	@Test static void test() {
		String name = "test";
		Property prop1 = of(name);
		Assertions.assertNotNull(prop1, "of(String) returns null");
		Property prop2 = of(name);
		Assertions.assertSame(prop1, prop2, "of(String) does not intern");
		Assertions.assertSame(name, prop1.name, "Name not properly passed");
	}

}
