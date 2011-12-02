package es.eucm.eadventure.engine.html;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class HtmlEntryPoint implements EntryPoint {

	private static Logger logger = Logger.getLogger("HtmlEntryPoint");

	@Override
	public void onModuleLoad() {
		if (RootPanel.get("demo") != null) {
			logger.info("demo");
			EAdEngineHtml engineDemo = new EAdEngineHtml();
			engineDemo.onModuleLoad();
		} else {
			logger.info("eles");
			EAdEngineHTMLProtocol protocol = new EAdEngineHTMLProtocol();
			protocol.onModuleLoad();
		}
 		
	}

}
