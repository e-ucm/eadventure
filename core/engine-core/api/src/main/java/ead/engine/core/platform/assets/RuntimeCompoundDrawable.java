package ead.engine.core.platform.assets;

import java.util.List;

import ead.common.resources.assets.drawable.EAdDrawable;

public interface RuntimeCompoundDrawable<T extends EAdDrawable> extends
		RuntimeAsset<T> {

	RuntimeDrawable<?, ?> getDrawable(int time, List<String> states, int level);

}
