package easywebflow.faces;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import easywebflow.configuration.Configuration;
import easywebflow.security.SecurityConstraint;
import easywebflow.security.SecurityData;

public class SecurityListener implements PhaseListener {

	private static final String PRE_LOGIN_PAGE_VIEW = "easywebflow-preLoginPage-view";
	private static final String PRE_LOGIN_PAGE_PARAMS = "easywebflow-preLoginPage-params";
	private SecurityData securityData = new SecurityData(); 
	private NamingTranslator translator = new NamingTranslator();

	@Override
	public void afterPhase(PhaseEvent event) {}

	@Override
	public void beforePhase(PhaseEvent event) {
		System.out.println("SECURITY LISTENER: START...");
		FacesContext facesContext = event.getFacesContext();
		String view = facesContext.getViewRoot().getViewId();;
		// TODO what if post afer something .. rethink

		// user is authenticated and there is a PRE_LOGIN_PAGE_VIEW in session parameters
		Principal principal = ((HttpServletRequest)facesContext.getExternalContext().getRequest()).getUserPrincipal();
		if ( principal != null 
				&& facesContext.getExternalContext().getSessionMap().containsKey(PRE_LOGIN_PAGE_VIEW)){
			// restore previous view and request parameters
			view = loadPreviousDestination(facesContext);
		}

		Boolean isAllowed = checkSecurity(facesContext, view);
		// if user is not allowed to display page 
		if (!isAllowed){
			if (principal != null){
				// user logged but page forbidden
				facesContext.getExternalContext().setResponseStatus(403);
			}
			else {
				// user not logged in - save current view and redirect to login page
				this.saveCurrentDestination(facesContext);
				this.redirectToLoginPage(facesContext);
			}
			facesContext.responseComplete();
		}
		// user allowed to display page - if not login page ? 
		else if (!view.substring(1, view.indexOf(".xhtml")).equals(Configuration.getConfigAttributeByName("login-page"))){
			// user resigned from login
			clearSessionMap(facesContext);
		}
		// else display anything
		
		System.out.println("SECURITY LISTENER: FINISH");
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	/**
	 * Checks if given view name is secured and if user is permitted to display it.
	 * Returns true if user is allowed to display that page and false in the opposite situation.
	 * 
	 * @param facesContext
	 * @param view
	 * @return
	 */
	private Boolean checkSecurity(FacesContext facesContext, String view){
		String viewId = view.substring(1, view.indexOf(".xhtml"));
		System.out.println("ViewId: "+viewId); 
		
		// if state exists
		if (translator.getStateForView(viewId) != null){
			// extract security constraints assigned to this view/state
			SecurityConstraint constraint = securityData.getSecurityConstraint(translator.getStateForView(viewId));

			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

			// state is not secured
			if (constraint.getRoleNamesAllowed().isEmpty()){
				return true;
			}
			
			// check if user is in any of permitted roles
			for (String s:constraint.getRoleNamesAllowed()){
				if (request.isUserInRole(s)){
					return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Sets redirection to login page. After invokation of this method response should be completed.
	 * @param context
	 */
	private void redirectToLoginPage(FacesContext context){
		System.out.println("Security - login Page");
		String loginPage = Configuration.getConfigAttributeByName("login-page");
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(
					context.getApplication().getViewHandler().getActionURL(context, loginPage));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Restores previous destination that user wanted to follow but login was required.
	 * Should be invoked after successful login.
	 * @throws IOException 
	 */
	private String loadPreviousDestination(FacesContext context){
		String url = (String)context.getExternalContext().getSessionMap().get(PRE_LOGIN_PAGE_VIEW);
		Map<String, List<String>> params = this.getParamsFromSessionMap(context);
		
		return context.getApplication().getViewHandler().getRedirectURL(context, url, params, params.size() >0);
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> getParamsFromSessionMap(
			FacesContext context) {
		Map<String, String[]> map = (Map<String, String[]>) context.getExternalContext().getSessionMap().get(PRE_LOGIN_PAGE_PARAMS);
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		for (Entry<String, String[]> entry :map.entrySet()){
			ArrayList<String> l = new ArrayList<String>();
			for (String s:entry.getValue()){
				l.add(s);
			}
			returnMap.put(entry.getKey(), l);
		}
		return returnMap;
	}

	/**
	 * Clears session map with previous page attribute.
	 *   
	 * @param context
	 */
	private void clearSessionMap(FacesContext context){
		context.getExternalContext().getSessionMap().remove(PRE_LOGIN_PAGE_VIEW);
		context.getExternalContext().getSessionMap().remove(PRE_LOGIN_PAGE_PARAMS);
	}
	
	/**
	 * Saves previous destination that user wanted to follow but login was required.
	 * Should be invoked before redirection to login page.
	 */
	private void saveCurrentDestination(FacesContext context){
		
		context.getExternalContext().getSessionMap().put(PRE_LOGIN_PAGE_VIEW, 
				context.getApplication().getViewHandler().getActionURL(context, context.getViewRoot().getViewId()));
		context.getExternalContext().getSessionMap().put(PRE_LOGIN_PAGE_PARAMS, 
				context.getExternalContext().getRequestParameterValuesMap());
	}
}
