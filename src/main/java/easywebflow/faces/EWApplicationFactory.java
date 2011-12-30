package easywebflow.faces;

import javax.faces.FacesWrapper;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

public class EWApplicationFactory extends ApplicationFactory implements FacesWrapper<ApplicationFactory>{

	private ApplicationFactory wrappedApplicationFactory;
	
	public EWApplicationFactory(ApplicationFactory wrappedApplicationFactory) {
		this.wrappedApplicationFactory = wrappedApplicationFactory;
	}
	
	@Override
	public Application getApplication() {
		/* 
				// to jest bez sensu - za kazdym razem kiedy biore get app tworze nowego handlera !
				// tutaj problem jest taki ze application.getNH zwraca mi null
				// sam nh jest ustawiany gdzies indziej
				// prawdopodobnie pobierana jest instancja application ta metoda
				// i poźniej ustawiany nh
				// wiec musze nadpisac Application i tutaj ja tworzyc, dodac EL
				// tak naprawde wystarczyloby nadpisac aplikacje ale nie ma takiego interfejsu - brak pola w faces-config.xml

		*/
		return new EWApplication(this.wrappedApplicationFactory.getApplication());
	}

	@Override
	public void setApplication(Application application) {	
		this.wrappedApplicationFactory.setApplication(application);
	}
	
	@Override
	public ApplicationFactory getWrapped() {
		return this.wrappedApplicationFactory;
	}

	
}
