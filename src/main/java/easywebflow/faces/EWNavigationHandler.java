package easywebflow.faces;

import javax.faces.FacesWrapper;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import easywebflow.configuration.StateIdentifier;
import easywebflow.el.ELFlowRecognizer;

public class EWNavigationHandler extends NavigationHandler implements FacesWrapper<NavigationHandler>{

	private NamingTranslator translator = new NamingTranslator();
	private NavigationHandler wrappedNavigationHandler;
	
	public EWNavigationHandler(NavigationHandler nh){
		this.wrappedNavigationHandler = nh;
	}
	
	@Override
	public void handleNavigation(FacesContext context, String fromAction,
			String outcome) {
		
		System.out.println("NavigationHandler start: " + outcome + " action: " +fromAction);
		// if outcome == null the same page should be redisplayed
		if (outcome == null){
			System.out.println("NavigationHandler przekazuje:" + outcome);
			this.wrappedNavigationHandler.handleNavigation(context, fromAction, outcome);
			return;
		}
		
		// if flow action invoked parse to retrieve flow name and state name and get view 
		// else view = null
		StateIdentifier stateID = ELFlowRecognizer.parse(fromAction, outcome);
		if (stateID != null){
			String view = translator.getViewForState(stateID);
			if (view != null){
				outcome = view;
			}
		}
		
		System.out.println("NavigationHandler przekazuje:" + outcome);
		// invoke defaultNavigationHandler
		this.wrappedNavigationHandler.handleNavigation(context, fromAction, outcome);

	}

	@Override
	public NavigationHandler getWrapped() {
		return wrappedNavigationHandler;
	}

}
