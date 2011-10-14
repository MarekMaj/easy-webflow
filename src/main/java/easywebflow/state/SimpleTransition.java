package easywebflow.state;

public class SimpleTransition implements Transition {

	private String destinationStateName;
	
	public SimpleTransition(String destinationStateName) {
		super();
		this.destinationStateName = destinationStateName;
	}

	@Override
	public String transition() {
		return this.destinationStateName;
	}

}
