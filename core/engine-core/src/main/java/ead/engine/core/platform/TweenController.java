package ead.engine.core.platform;

import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenManager;

public interface TweenController {

	TweenManager add(BaseTween<?> object);

	boolean containsTarget(Object arg0, int arg1);

	public boolean containsTarget(Object arg0);

	public List<BaseTween<?>> getObjects();

	public int getRunningTimelinesCount();

	public int getRunningTweensCount();

	public void killAll();

	public void killTarget(Object arg0, int arg1);

	public void killTarget(Object arg0);

	public void pause();

	public void resume();

	public int size();

	public void update(float arg0);

}
