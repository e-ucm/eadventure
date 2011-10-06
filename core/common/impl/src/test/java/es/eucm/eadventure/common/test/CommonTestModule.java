package es.eucm.eadventure.common.test;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

public class CommonTestModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(StringHandler.class).to(TestStringHandler.class);
	}
	
	
	public static class TestStringHandler implements StringHandler {

		@Override
		public String getString(EAdString string) {
			return string.toString();
		}

		@Override
		public void setString(EAdString eAdString, String string) {
			
		}

		@Override
		public void setStrings(Map<EAdString, String> strings) {
			
		}

		@Override
		public void addStrings(Map<EAdString, String> strings) {
			
		}

		@Override
		public Map<EAdString, String> getStrings() {
			return new HashMap<EAdString, String>();
		}
		
	}

}
