package easywebflow.faces;

import java.io.IOException;
import java.security.Principal;

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
	private SecurityData securityData = new SecurityData(); 
	private NamingTranslator translator = new NamingTranslator();

	@Override
	public void afterPhase(PhaseEvent event) {}

	@Override
	public void beforePhase(PhaseEvent event) {
		System.out.println("SECURITY LISTENER: START...");
		FacesContext facesContext = event.getFacesContext();
		String view = facesContext.getViewRoot().getViewId();
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
		else if (!substringView(view).equals(Configuration.getConfigAttributeByName("login-page"))){
			// user resigned from login
			System.out.println("not login page - clear map");
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
		String viewId = substringView(view);
		System.out.println("SECURITY checking ViewId: "+viewId); 

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

	private String substringView(String view){
		int position = ((view.indexOf(".xhtml") > 0) ? view.indexOf(".xhtml") 
				: (view.indexOf(".jsf") > 0) ? view.indexOf(".jsf") 
						: 0);
		
		return view.substring(0, position);
	}
	
	/**
	 * Sets redirection to login page. After invokation of this method response should be completed.
	 * @param context
	 */
	private void redirectToLoginPage(FacesContext context){
		System.out.println("SECURITY - redirect to login Page");
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
		System.out.println("SECURITY - Loading previous view");
		
		String url = (String)context.getExternalContext().getSessionMap().get(PRE_LOGIN_PAGE_VIEW);
		//Map<String, List<String>> params = (Map<String, List<String>>) context.getExternalContext().getSessionMap().get(PRE_LOGIN_PAGE_VIEW);

		//if (params!=null && !params.isEmpty()){
			try {
				// TODO params do not work
				//context.getExternalContext().redirect(context.getExternalContext().encodeRedirectURL(url, params));
				context.getExternalContext().redirect(context.getExternalContext().encodeActionURL(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		//}
		
		return url;
	}
	
	/**
	 * Clears session map with previous page attribute.
	 *   
	 * @param context
	 */
	private void clearSessionMap(FacesContext context){
		context.getExternalContext().getSessionMap().remove(PRE_LOGIN_PAGE_VIEW);
		
	}

	/**
	 * Saves previous destination that user wanted to follow but login was required.
	 * Should be invoked before redirection to login page.
	 */
	private void saveCurrentDestination(FacesContext context){
		context.getExternalContext().getSessionMap().put(PRE_LOGIN_PAGE_VIEW, 
				context.getApplication().getViewHandler().getActionURL(context, context.getViewRoot().getViewId()));
		// TODO context.getExternalContext().getSessionMap().put(PRE_LOGIN_PAGE_VIEW, context.getApplication().getViewHandler().getRedirectURL(context, context.getViewRoot().getViewId(),createMapFromRequestParams(context), (createMapFromRequestParams(context).size() >0)));

	}
}
