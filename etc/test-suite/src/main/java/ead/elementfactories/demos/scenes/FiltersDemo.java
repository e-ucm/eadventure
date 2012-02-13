package ead.elementfactories.demos.scenes;

import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawableImpl;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.common.util.EAdMatrixImpl;
import ead.common.util.EAdPosition.Corner;

public class FiltersDemo extends EmptyScene {

	public FiltersDemo() {
		EAdMatrixImpl m = new EAdMatrixImpl();
		m.scale(-1.0f, 1.0f, true);
		ImageImpl i = new ImageImpl("@drawable/ng_key.png");
		FilteredDrawable d = new FilteredDrawableImpl(i, new MatrixFilter(m, 1.0f, 0.0f));
		SceneElementImpl e = new SceneElementImpl(d);
		e.setInitialScale(0.8f);
		e.setPosition(Corner.CENTER, 400, 300);
		
		SceneElementImpl e2 = new SceneElementImpl(i);
		e2.setPosition(Corner.CENTER, 400, 400);
		e2.setInitialScale(0.8f);
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
