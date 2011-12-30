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
	public String getStateNameForTransition(String name)
			throws IllegalTransitionException {
		return delegate.getStateNameForTransition(name);
	}
	
	@Override
	public String onTransition(String name) throws IllegalTransitionException{
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
