package ead.engine.core.game.enginefilters;

public interface EngineFilter<T> extends Comparable<EngineFilter<?>> {

	/**
	 * Returns a filtered value for the given object
	 * 
	 * @param o
	 * @param params TODO
	 * @return
	 */
	T filter(T o, Object[] params);

	int getPriority();

	void setPriority(int priority);

}
