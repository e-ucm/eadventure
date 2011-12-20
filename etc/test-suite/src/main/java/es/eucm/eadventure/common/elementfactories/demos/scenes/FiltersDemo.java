package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.filters.FilteredDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.filters.impl.FilteredDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.filters.impl.MatrixFilter;
import es.eucm.eadventure.common.util.impl.EAdMatrixImpl;

public class FiltersDemo extends EmptyScene {

	public FiltersDemo() {
		EAdMatrixImpl m = new EAdMatrixImpl();
		m.scale(-1.0f, 1.0f, true);
		Image i = new ImageImpl("@drawable/ng_key.png");
		FilteredDrawable d = new FilteredDrawableImpl(i, new MatrixFilter(m, 1.0f, 0.0f));
		EAdBasicSceneElement e = new EAdBasicSceneElement(d);
		e.setScale(0.8f);
		e.setPosition(Corner.CENTER, 400, 300);
		
		EAdBasicSceneElement e2 = new EAdBasicSceneElement(i);
		e2.setPosition(Corner.CENTER, 400, 400);
		e2.setScale(0.8f);
		getComponents().add(e2);
		getComponents().add(e);
	}

	@Override
	public String getSceneDescription() {
		return "An scene showing filters.";
	}

	public String getDemoName() {
		return "Filters Scene";
	}

}
