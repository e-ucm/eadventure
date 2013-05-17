package ead.engine.core.gdx.desktop.debugger.hooks;

import java.util.ArrayList;

/**
 * @author anserran
 *         Date: 17/05/13
 *         Time: 12:08
 */
public class DebuggerHook<T> {

	private ArrayList<HookListener<T>> listeners;

	public DebuggerHook() {
		listeners = new ArrayList<HookListener<T>>();
	}

	public void addListener(HookListener<T> hookListener) {
		listeners.add(hookListener);
	}

	public void fireListeners(T o) {
		for (HookListener<T> l : listeners) {
			l.handle(o);
		}
	}

	public interface HookListener<S> {

		void handle(S element);

	}

}
