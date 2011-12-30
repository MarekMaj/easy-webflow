package easywebflow.state;

public class SimpleTransition implements Transition {

	private String destinationStateName;
	private String name;
	
	public SimpleTransition(String name, String destinationStateName) {
		super();
		this.name = name;
		this.destinationStateName = destinationStateName;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getTargetStateName() {
		return this.destinationStateName;
	}
	@Override
	public Boolean isAllowed(String eventName) {
		return this.name.equals(eventName);
	}

	@Override
	public String transition() {
		return this.destinationStateName;
	}

}
