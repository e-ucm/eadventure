package ead.common.params;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ead.common.EqualsHashCodeTest;

public abstract class ParamsTest<T extends EAdParam> extends
		EqualsHashCodeTest<T> {

	public abstract T buildParam(String data);

	public abstract T defaultValue();

	@Test
	public void testToStringDataParse() {
		for (int i = 0; i < objects.length; i++) {
			String data = objects[i].toStringData();
			T c = buildParam(data);
			assertTrue(c.equals(objects[i]));
			assertTrue(objects[i].equals(c));
		}
	}

	@Test
	public void testParseCorruptedData(){
		String[] strings = new String[]{ null, "0;2;4;a", ";;;", "0:2;4.03f;0f", "dakjfaosidfyipu43676r21", "jo09ua87/77;;", ":0:2", "21;78;20:20", "ljasfasdfa", "123456789!"};
		T defaultValue = defaultValue();
		for ( String s: strings ){
			T c = buildParam(s);
			assertTrue(defaultValue.equals(c));
		}
	}
}
