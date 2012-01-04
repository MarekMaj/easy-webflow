package easywebflow.configuration;

public class StateIdentifier {

	private String flowName;
	private String stateName;
	
	public StateIdentifier(String flowName, String stateName) {
		super();
		this.flowName = flowName;
		this.stateName = stateName;
	}
	
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		StateIdentifier sId = (StateIdentifier) obj;
		return (sId.getStateName().equals(this.getStateName()) && sId.getFlowName().equals(this.getFlowName()));
	}
	
	@Override
	public int hashCode() {
		int result = 17;
        
        result = 37 * result + this.getFlowName().hashCode();
        result = 37 * result + this.getStateName().hashCode();
        
        return result;
	}
}
