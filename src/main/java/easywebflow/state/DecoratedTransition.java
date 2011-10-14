package easywebflow.state;

public abstract class DecoratedTransition implements Transition{

	protected Transition delegate;

	public DecoratedTransition(Transition delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public String transition() {
		return delegate.transition();
	}
	
	
}
