package easywebflow.security;

import java.util.HashMap;

import easywebflow.configuration.Configuration;
import easywebflow.configuration.StateIdentifier;

public class SecurityData {

	private HashMap<StateIdentifier, SecurityConstraint> securityConstraintsMap = Configuration.getInstance().getSecuredStatesMap();
	
	public SecurityData(){}
	
	public SecurityConstraint getSecurityConstraint(StateIdentifier stateId){
		if (this.getsecurityConstraintsMap().get(stateId) == null){
			return new SecurityConstraint();
		}
		return this.getsecurityConstraintsMap().get(stateId);
	}

	public HashMap<StateIdentifier, SecurityConstraint> getsecurityConstraintsMap() {
		if (this.securityConstraintsMap == null){
			this.securityConstraintsMap = new HashMap<StateIdentifier, SecurityConstraint>(); 
		}
		return securityConstraintsMap;
	}
	
}
