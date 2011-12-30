package easywebflow.faces;

import java.util.HashMap;

import easywebflow.configuration.Configuration;
import easywebflow.configuration.StateIdentifier;

public class NamingTranslator {

	/* TODO pytanie czy potrzebuje to per flow czyt nie 
	// czy nazwy stanów są unikalne w calym pliku conf ?
	// przyjme ze sa unikalne w calej konfiguracji - de facto widok i stan sa w relacji 1-1
	// no bo stan ukazuje w ktorym punkcie jest konwersacja z uzytkownikiem prowadzona za posrednictwem widoku
	// nieunikalne widoki moglyby prowadzic do sytuacji w ktorej na jednym widoku sa przyciski wywolujace akcje dla kilku konwersacji
	 * wniosek : musze sprawdzac unikalnosc nazw widokow i nazw stanów które nie maja opcjonalnych nazw widoków w obrębie konfiguracji
	// 
	 * w momencie gdy doszlismy do tego stanu idąc sciążką jednej konwersacji i klikamy na akcje z innej moze wywolac bledy
	// wlasnie!! :  łączenie konwersacji w widoku - ciekawy pomysl do przemyslenia ale nie na teraz
	// wstępnie : jak pogodzic zarówno facesListenera(nazwa widoku) jak i obsługę security poprzez interceptory(stateContext - obiekt state)
	// zlozony problem narazie wazniejsze rzeczy
	 * 
	// wniosek2 : gdy trzymam securityData dla stanów muszę tez miec nazwę flowa, bo mogą sie zdarzyć dwa stany o tej samej nazwie i róznych nazwach widoków
	// moze tworzy unikalne identyfikatory dla stanow i umieszczam je w obiekcie stan wtedy je moge sobie wyluskac w interceptorze ze stateContextu
	// a w facesListenerze korzystam z nazwy widoku
	 *  
	 */
	public NamingTranslator(){}
	
	public String getViewForState(StateIdentifier stateID){
		return Configuration.getInstance().getStateToViewMap().get(stateID);
	}
	
	public StateIdentifier getStateForView(String viewName){
		return Configuration.getInstance().getViewToStateMap().get(viewName);
	}
}
