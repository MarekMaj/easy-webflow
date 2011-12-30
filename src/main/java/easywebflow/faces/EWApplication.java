package easywebflow.faces;

import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.application.NavigationHandler;

import easywebflow.configuration.Configuration;

public class EWApplication extends ApplicationWrapper {

	private Application wrappedApplication;
	
	public EWApplication(Application wrappedApplication) {
		this.wrappedApplication = wrappedApplication;
	}

	@Override
	public NavigationHandler getNavigationHandler() {
		return super.getNavigationHandler();
	}
	
	@Override
	public void setNavigationHandler(NavigationHandler handler) {
		if (!Configuration.getInstance().getConfigAttributeByName("navigation").equalsIgnoreCase("false")){
			handler = new EWNavigationHandler(handler);
		}
		super.setNavigationHandler(handler);
	}
	
	@Override
	public Application getWrapped() {
		return wrappedApplication;
	}

}
