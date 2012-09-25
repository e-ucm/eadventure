package ead.engine.core.platform;

import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.engine.core.game.ValueMap;

@Singleton
public class TweenControllerImpl implements TweenController,
		TweenAccessor<EAdField<?>> {

	public static final int DEFAULT = 0;

	private TweenManager t;

	private ValueMap valueMap;

	@Inject
	public TweenControllerImpl(ValueMap valueMap) {
		this.valueMap = valueMap;
		t = new TweenManager( );
		Tween.registerAccessor(EAdField.class, this);
		Tween.registerAccessor(BasicField.class, this);
	}

	@Override
	public TweenManager add(BaseTween<?> object) {
		return t.add(object);
	}

	@Override
	public boolean containsTarget(Object arg0, int arg1) {
		return t.containsTarget(arg0, arg1);
	}

	@Override
	public boolean containsTarget(Object arg0) {
		return t.containsTarget(arg0);
	}

	@Override
	public List<BaseTween<?>> getObjects() {
		return t.getObjects();
	}

	@Override
	public int getRunningTimelinesCount() {
		return t.getRunningTimelinesCount();
	}

	@Override
	public int getRunningTweensCount() {
		return t.getRunningTweensCount();
	}

	@Override
	public void killAll() {
		t.killAll();

	}

	@Override
	public void killTarget(Object arg0, int arg1) {
		t.killTarget(arg0, arg1);

	}

	@Override
	public void killTarget(Object arg0) {
		t.killTarget(arg0);

	}

	@Override
	public void pause() {
		t.pause();

	}

	@Override
	public void resume() {
		t.resume();

	}

	@Override
	public int size() {
		return t.size();
	}

	@Override
	public void update(float arg0) {
		t.update(arg0);

	}

	@Override
	public int getValues(EAdField<?> field, int type, float[] values) {
		switch (type) {
		default:
			Object o = valueMap.getValue(field);
			if (o instanceof Number) {
				values[0] = ((Number) o).floatValue();
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public void setValues(EAdField<?> field, int type, float[] values) {
		switch (type) {
		default:
			valueMap.setValue(field, values[0]);
		}

	}

}
