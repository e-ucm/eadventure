package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.StringsReader;
import es.eucm.eadventure.common.resources.EAdString;

public class TestStringsReader implements StringsReader {

	@Override
	public String getString(EAdString string) {
		return "string";
	}

}
