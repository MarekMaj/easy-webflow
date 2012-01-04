package easywebflow.faces;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

public class PRGListener implements PhaseListener{

	@Override
	public void afterPhase(PhaseEvent event) {}

	@Override
	public void beforePhase(PhaseEvent event) {
		// TODO to wciaz nie chroni przed kilkukrotnym wyslaniem danych jezeli uzytkownik odswiezy POST request
		
		FacesContext facesContext = event.getFacesContext();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		System.out.println("PRG LISTENER: START...");
		// if Method is POST
		if (request.getMethod().equalsIgnoreCase("post")){
			System.out.println("To jest POST");
			System.out.println("partial: " +facesContext.getPartialViewContext().isPartialRequest());
			System.out.println("walidacja failed: " +facesContext.isValidationFailed());
		
			// if validation ok and not an ajax request try redirect
			if (!facesContext.isValidationFailed() && !facesContext.getPartialViewContext().isPartialRequest()){
				// TODO save all assigned and unassigned messages
				// redirect to appropriate URL
				String url = facesContext.getApplication().getViewHandler().getActionURL(facesContext, facesContext.getViewRoot().getViewId());
				try {
					facesContext.getExternalContext().redirect(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("PRG LISTENER: END");
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
