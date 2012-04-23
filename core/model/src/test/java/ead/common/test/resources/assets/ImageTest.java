package ead.common.test.resources.assets;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ead.common.resources.assets.drawable.basics.Image;

public class ImageTest {
	
	private Image img1, img2, img3, img4;
	
	@Before
	public void setUp( ) {
		img1 = new Image("@drawable/image.png");
		img2 = new Image("@drawable/image.png");
		img3 = new Image("@drawable/otherimage.png");
		img4 = new Image("@drawable/anotherimage.png");
	}

	@Test
	public void testHashCode() {
		assertTrue(img1.hashCode() == img1.hashCode());
		assertTrue(img1.hashCode() == img2.hashCode());
		assertTrue(img2.hashCode() != img3.hashCode());
		assertTrue(img3.hashCode() != img4.hashCode());
		assertTrue(img1.hashCode() != img4.hashCode());
		assertTrue(img2.hashCode() != img4.hashCode());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(img1.equals(img1));
		assertTrue(img2.equals(img1));
		assertTrue(img1.equals(img2));
		assertTrue(!img3.equals(img1));
		assertTrue(!img4.equals(img1));
		assertTrue(!img3.equals(img4));
	}

}
