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
		// jezeli POST to:
		// sprawdz czy pojawily sie jakies bledy
		// jezeli tak to nic nie rob 
		// jezeli nie to PGR do nowego widoku
		// TODO to wciaz nie chroni przed kilkukrotnym wyslaniem danych jezeli uzytkownik odswiezy POST request
		
		FacesContext facesContext = event.getFacesContext();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		System.out.println("PRG LISTENER: START...");
		// if Method is POST
		if (request.getMethod().equalsIgnoreCase("post")){
			System.out.println("To jest POST");
			// if none of components (clients) have been assigned messages and ALSO there are no unassigned messages 
			if (facesContext.getMessageList().isEmpty()){
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
