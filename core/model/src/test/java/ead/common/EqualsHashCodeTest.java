package ead.common;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test equals and hashcode methods
 * 
 */
public abstract class EqualsHashCodeTest<T> {

	protected T[] objects;

	@Before
	public void setUp() {
		objects = getObjects();
	}

	/**
	 * Data must be an array with length divisible by 2, and grouped by equals
	 * pairs, i.e. object[i].equals(object[i+1]) must be true,
	 * object[i+2].equals(object[i+3]) must be true, and so on. No pair can be
	 * repeated
	 * 
	 * @return
	 */
	public abstract T[] getObjects();

	@Test
	public void testHashCode() {
		for (int i = 0; i < objects.length; i += 2) {
			assertTrue(objects[i].hashCode() == objects[i + 1].hashCode());
			for (int j = 0; j < objects.length; j++) {
				if (j != i && j != i + 1) {
					assertTrue(objects[i].hashCode() != objects[j]
							.hashCode());
				}
			}
		}
	}

	@Test
	public void testEqualsObject() {
		for (int i = 0; i < objects.length; i += 2) {
			assertTrue(objects[i].equals(objects[i + 1]));
			for (int j = 0; j < objects.length; j++) {
				if (j != i && j != i + 1) {
					assertTrue(!objects[i].equals(objects[j]));
					assertTrue(!objects[j].equals(objects[i]));
				}
			}
		}
	}

}
