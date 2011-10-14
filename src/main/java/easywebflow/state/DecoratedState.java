package easywebflow.state;

public abstract class DecoratedState implements State {
	protected State delegate;
	
	public DecoratedState(State state){
		delegate = state;
	}
	
	@Override
	public void onStart() {
		delegate.onStart();
	}

	@Override
	public String onTransition(String name) {
		return delegate.onTransition(name);
	}

	@Override
	public void onExit() {
		delegate.onExit();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

}
