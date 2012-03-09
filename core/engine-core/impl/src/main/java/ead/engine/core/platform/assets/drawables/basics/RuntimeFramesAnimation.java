package ead.engine.core.platform.assets.drawables.basics;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class RuntimeFramesAnimation extends
		AbstractRuntimeAsset<FramesAnimation> implements
		RuntimeCompoundDrawable<FramesAnimation> {

	private List<RuntimeDrawable<?, ?>> frames;

	private AssetHandler assetHandler;

	private boolean loaded;

	private boolean loading;

	@Inject
	public RuntimeFramesAnimation(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		frames = new ArrayList<RuntimeDrawable<?, ?>>();
		loaded = false;
		loading = false;
	}

	@Override
	public boolean loadAsset() {
		for (Frame f : descriptor.getFrames()) {
			RuntimeDrawable<?, ?> d = (RuntimeDrawable<?, ?>) assetHandler
					.getRuntimeAsset(f.getDrawable(), true);
			frames.add(d);
		}
		loading = true;
		return true;
	}

	@Override
	public void freeMemory() {
		for (RuntimeDrawable<?, ?> d : frames) {
			d.freeMemory();
		}
		frames.clear();
	}

	@Override
	public boolean isLoaded() {
		if (loading) {
			loaded = true;
			for (RuntimeDrawable<?, ?> d : frames) {
				if (!d.isLoaded()) {
					loaded = false;
					break;
				}
			}
			loading = false;
		}
		return loaded;
	}

	@Override
	public RuntimeDrawable<?, ?> getDrawable(int time, List<String> states,
			int level) {
		int index = descriptor.getFrameIndexFromTime(time);
		return frames.get(index);
	}

}
