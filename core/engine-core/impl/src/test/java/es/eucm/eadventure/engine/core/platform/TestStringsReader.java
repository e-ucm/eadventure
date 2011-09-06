package es.eucm.eadventure.engine.core.platform;

import java.util.Map;

import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

public class TestStringsReader implements StringHandler {

	@Override
	public String getString(EAdString string) {
		return "string";
	}

	@Override
	public void setString(EAdString eAdString, String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStrings(Map<EAdString, String> strings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addStrings(Map<EAdString, String> strings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EAdString addString(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<EAdString, String> getStrings() {
		// TODO Auto-generated method stub
		return null;
	}

}
