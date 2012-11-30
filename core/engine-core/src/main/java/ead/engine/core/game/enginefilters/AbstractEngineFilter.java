package ead.engine.core.game.enginefilters;

public abstract class AbstractEngineFilter<T> implements EngineFilter<T> {
	
	private int priority;
	
	public AbstractEngineFilter( int priority ){
		this.priority = priority;
	}

	@Override
	public int compareTo(EngineFilter<?> f) {
		return priority - f.getPriority();
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(int priority) {		
		this.priority = priority;
	}

}
