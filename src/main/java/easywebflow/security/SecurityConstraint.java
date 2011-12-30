package easywebflow.security;

import java.util.ArrayList;

public class SecurityConstraint {

	private String stateName;
	private ArrayList<String> roleNamesAllowed;
	
	public SecurityConstraint(){
		super();
	}
	
	public SecurityConstraint(String stateName, ArrayList<String> roleNamesAllowed) {
		super();
		this.stateName = stateName;
		this.roleNamesAllowed = roleNamesAllowed;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/** Returns list of roles which are allowed to access the state.
	 *  If null is returned it means that state is not secured at all and any user can enter it.
	 *  If empty list is returned, it means that state is basically unavailable for anyone.
	 *  Otherwise list of role names is returned.
	 *  
	 * @return java.util.ArrayList<String>
	 */
	public ArrayList<String> getRoleNamesAllowed() {
		if (this.roleNamesAllowed == null){
			roleNamesAllowed = new ArrayList<String>();
		}
		return roleNamesAllowed;
	}

	public void setRoleNamesAllowed(ArrayList<String> roleNamesAllowed) {
		this.roleNamesAllowed = roleNamesAllowed;
	}
	
	
}
