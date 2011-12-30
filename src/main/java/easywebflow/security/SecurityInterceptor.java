package easywebflow.security;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import easywebflow.configuration.StateIdentifier;
import easywebflow.state.IllegalTransitionException;
import easywebflow.state.StateContext;

@Interceptor
@Secured
public class SecurityInterceptor {

	@Inject 
	private SecurityData securityData;
	
	@AroundInvoke
	public Object checkSecurity(InvocationContext ic) throws IllegalTransitionException, SecurityException, Exception{
		System.out.println("jestem w interceptorze");
		System.out.println("target: " +ic.getTarget().toString());
		System.out.println("method: "+ic.getMethod().toString());
		System.out.println("params: "+ic.getParameters().toString());
		try {
			StateContext sc = (StateContext) ic.getTarget();
			String transitionName = (String) ic.getParameters()[0];
			String flowName = sc.getFlowName();

			// extract state name for this transition
			String targetStateName = sc.getStateNameForTransition(transitionName);
			// extract security constraint for this state
			ArrayList<String> roles = securityData.getSecurityConstraint(new StateIdentifier(flowName, targetStateName)).getRoleNamesAllowed();
			
			// if state is secured
			if (!roles.isEmpty()){
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				Boolean authorized = false;
				
				// check if user is in any of permitted roles
				for (String role: roles){
					if (request.isUserInRole(role)){
						authorized = true;
						break;
					}
				}
				
				if (!authorized){
					throw new SecurityException(flowName, targetStateName);
				}
			}
		
		} catch (ClassCastException e){
			// TODO 
		}
		
		System.out.println("OK interceptor kontynuuje");
		return ic.proceed();
	}
}
