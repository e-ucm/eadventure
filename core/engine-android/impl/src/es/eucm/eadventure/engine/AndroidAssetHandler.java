package es.eucm.eadventure.engine;

import java.util.Map;
import java.util.logging.Logger;

import android.content.res.Resources;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.impl.AbstractAssetHandler;


@Singleton
public class AndroidAssetHandler extends AbstractAssetHandler {

		private Resources resources;
		
		private static final Logger logger = Logger.getLogger("AndroidAssetHandler");

		@Inject
		public AndroidAssetHandler(
				Injector injector,
				Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap) {
			super(injector, classMap);
		}
		
		@Override
		public void initilize() {
			// TODO Auto-generated method stub

		}

		@Override
		public void terminate() {
			// TODO Auto-generated method stub

		}
		
		public void setResources(Resources resources) {
			this.resources = resources;
			setLoaded(true);
		}

		@Override
		public String getAbsolutePath(String uri) {
			return uri.replace("@", "/sdcard/eAd2/");
		}

	}



