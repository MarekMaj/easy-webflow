package easywebflow.state;

public abstract class DecoratedTransition implements Transition{

	protected Transition delegate;

	public DecoratedTransition(Transition delegate) {
		super();
		this.delegate = delegate;
	}
	
	@Override
	public String getName() {
		return this.delegate.getName();
	}
	
	@Override
	public String getTargetStateName() {
		return delegate.getTargetStateName();
	}
	
	@Override
	public String transition() {
		return delegate.transition();
	}
	
	@Override
	public Boolean isAllowed(String eventName) {
		return delegate.isAllowed(eventName);
	}
}
