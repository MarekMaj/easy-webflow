package easywebflow.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;

import easywebflow.factory.MainFactory;
import easywebflow.state.IllegalTransitionException;
import easywebflow.state.StateContext;

@ConversationScoped
public class FlowImpl implements Flow, Serializable {
	
	/* Tutaj jeszcze bym trzymal jedna mape z obiektami Map<String, Object> - czyli InjectableMap  
	 * zebym mógł się odwoływać do obiektów poprzez nazwaFlowa.map.bean.pole 
	 * a nie nazwaFlowa.map.bean.object.pole no i tam tylko trzymam referencje
	 * inicjalizacja flow contextu 
	 */
	// TODO czy to w ogóle potrzebne? 
	// TODO synchronizacja !!!
	// moze w srodku InjectionableMap?  
	@Inject
	private InjectionOnDemandMap<String, Object> map;

	/* Attention: field available after creation methods invoked 
	 * and before PostCostruct method invocation */  
	private String flowName;
	@Inject
	private StateContext stateContext;
	@Inject 
	private Conversation conversation;
	
	/** This method is called externally during bean registering.
	 *  It is recommended not to call this method anywhere else.
	 */
	@Override
	public void setFlowName(String name){
		flowName = name;
	}

	// albo @Produces
	@Override
	public String getFlowName(){
		return this.flowName;
	}
	
	public InjectionOnDemandMap<String, Object> getMap() {
		return map;
	}

	public void setMap(InjectionOnDemandMap<String, Object> map) {
		this.map = map;
	}

	@PostConstruct
	public void initializeFlowContext(){
		System.out.println("Flow post construct:" + flowName);
		// może lepiej wstrzyknąć StateContext - wtedy w nim odnoszę się do MainFactory i mogę definiować interceptory
		// tutaj chyba będę potrzebowal flowContext 
		// w którym oprócz stateContext będę miał dataModel
		// i tam zawarte pola sobie wstrzyknę na początku z adnotacją @New (bo bedą one wykorzystywane podczas całego flowa)

		// moze też udostępniąc @Produces te pola z nazwa np. "bindUser"
		// w sumie nazwy znam w fazie AnnotationProcessing więc teoretycznie możliwe
		// może warto udostępnic samą mape np. "bind"
		// dlaczego jak wołam pole, które jest @Produces tworzy sie nowy flow.... ?
		// według tutoriala z theserverside tak nie powinno być i z mojego testu TestClass wynika ze nie jest 
		// tutaj jest jakis blad
		// jezeli naprawie to moge @Produces na mapie i na nazwie flowa moze ?? 
		// ok i wtedy będzie ok metoda wolana za każdym razem ale zwracam obiekt ze scopem flowa
		// wydaje mi sie ze kontener nie wie ze instancja FlowImpl juz istnieje bo rezyduje pod inna nazwa
		// dlatego próbuje tworzyć instancje FlowImpl za każdym wywołaniem producenta
		// jezeli uda mi sie pokazac kontenerowi to moze moge wywalic anotacje @ConverstaionScope
		// HMM TYLKO CZY NIE BEDZIE PROBLEMU JAK ZAREJESTRUJE KILKA FLOWOW, KAZDY AKTYWNY I PRODUKUJE MAPE!!
		// narazie to zostaw (niech mapa bedzie dostepna poprzez flowName.map)- są ważniejsze rzeczy 
		
		// resztę będę wstrzykiwał tak jak teraz tzn ze scopem im przydzielonym np. servicy 
		// gereralnie trzeba zaoferować użytkownikowi te dwie możliwości
		// alternatywa: wstrzykuje z moim scopem jeśli jest wywołanie flow.map.user.password
		// lepsze! - jak to zrobić ?? trzeba zdefiniować swoją mapę dziedziczącą po hashMapie i tam
		// w get - jeżeli nie znajdę wstrzykiwać
		// fajnie - ominę konieczność definiowania dataModel
		if (flowName != null){
			FlowInitializingData fid = new MainFactory().getFlowContext(this, flowName);
			
			// inject datamodel beans 
			this.map.createDataModel(fid.getInitData());
			// init stateContext 
			this.stateContext.init(fid.getStateContext());
		}
	}
	
	// TODO protect start state 
	@Override
	public String start(){
		// If flow already exist
		if (conversation.isTransient()){
			conversation.begin();
		}
		this.stateContext.enterStartState();
		return new String (this.getFlowName() + '/' +stateContext.getCurrentStateName());
	}
	
	@Override
	public String transitionTo(String transitionName) {
		String outcome = null;
		/** wołam: transitionTo(stateName)
		 * Jezeli zwrócona zostanie nazwa != null to przechodzę do tego widoku (sprawdzam jeszcze czy to był endState)
		 * Jeżeli endState to kończę konwersację
		 * Jeżeli zwrócony zostanie IllegalTransitionException to pozostaję w tym widoku i tworzę log ?
		 * 		// uwaga tutaj na pczycisk wstecz - uzytkownik wraca sobie  do poprzedniego widoku w przedladarce, 
		// raz jeszcze chce przejsc do stanu w którym jest i state context nie znajdzie takiego przejscia
		 */
		
		/* Process transition and return new state name */
		try {
			outcome = this.stateContext.transitionTo(transitionName);
		} catch (easywebflow.security.SecurityException e){
			/* If user is not permitted to enter transition return new stateName 
			 * but DO NOT enter this state nor invoke any actions related with transition, state.
			 * After succesful login user can try reinvoke transistion
			 */
			return e.getStateName();
		} catch (IllegalTransitionException e){
			// if transition is illegal then redisplay the same page
			return null;
		}
		
		// if this is last state end flow and return param name
		// this way user can define view that has to be displayed after flow finishes
		if (outcome == null){
			System.out.println( "koncze flow");
			// If flow already finished 
			if (!conversation.isTransient()){
				conversation.end();
			}
			return transitionName;
		}
		// otherwise return new state name
		return outcome;
	}

	@Override
	public Object invoke(String beanName, String methodName,
			String... paramName) {
		System.out.println("*******************");
		System.out.println("invoke by flow: " + beanName + " " +methodName + " " +paramName );
		
		// if bean does not exist in flow data inject external service
		if (!this.map.contains(beanName))
			this.map.injectExternal(beanName);
		// get instance of bean
		Object bean = this.map.get(beanName);
		Class<?> beanClass = this.map.getClassInfo(beanName);
		
		ArrayList<Object> paramObjects = new ArrayList<Object>();
		ArrayList<Class> paramClass = new ArrayList<Class>();		
		for (String s:paramName){
			if (s != null){
				System.out.println("dodaje: "+s);
				
				paramObjects.add(this.map.get(s));
				paramClass.add(this.map.getClassInfo(s));
				//System.out.println("dodalem: "+s);
			}
		}
		
		// TODO tutaj chyba w tej samej metodzie
		return invokeInternally(bean, beanClass, methodName, paramObjects, paramClass);
	}

	private Object invokeInternally(Object bean, Class<?> beanClass, String methodName, 
			ArrayList<Object> paramObjects, ArrayList<Class> paramClazz) {
		Object object = null;
		try {
			System.out.println("bean: " + bean.toString()  + " "+beanClass.toString());
			System.out.println("methodName: "+methodName);
			for (int i=0; i< paramClazz.size();i++){
				System.out.println("params: "+paramClazz.get(i) + " "+paramObjects.get(i));
			}
			
			System.out.println(paramClazz.toString());
			object = beanClass.getDeclaredMethod(methodName, paramClazz.toArray(new Class[paramClazz.size()])).invoke(bean, paramObjects.toArray()); 
			
			//object = data.getClazz().getDeclaredMethod(methodName, paramClazz).invoke(data.getObject(), paramObjects.toArray());
			System.out.println("Wolalem:" + bean.toString()  + " "+beanClass.toString());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("*******************");
		return object;
	}

	// TODO i cannot use result as param!!!!
	// lepiej wolac z poziomu komendy od razu z resultem
	@Override
	public void setResult(String resultName, Object result) {
		// jezeli obiekt o takiej nazwie juz istnieje zastap istniejacy
		System.out.println("Setting result: " + resultName + " "+ result.toString());
		map.put(resultName, result);

	}

	// TODO BIGGG dodac wychwytywanie metod np bean.metoda == costam
	@Override
	public boolean compareObjects(String comparedObjectName,
			String comparedObjectValue) {
		// TODO uzupelnic dla innych typów danych / wlasny interfejs comparable ??
		System.out.println("Comparing: " + comparedObjectName + " "+ comparedObjectValue);
		Object obj = this.map.get(comparedObjectName);
		if ( obj.toString().equals(comparedObjectValue) ){
			return true;
		}
		return false;
	}
	
	@Override
	public Object get(String expression) {
		String[] exp = expression.split("\\.");
		// TODO zastanów sie wczy wstzykiwać jeżeli nie istnieje w dataMap na
		if (!map.containsKey(exp[0])){
			return null;
		}

		System.out.print("Wywoluje internally: ");
		for (String e:exp) System.out.print(e + " ");
		
		if (exp.length ==1 ){
			return this.map.get(exp[0]);
		}else if (exp.length == 2 ){
			// tutaj lepiej zastosowac coś podobnego do EL zeby wolal membera 
			// wtedy nie musiałbym flowName.get(user.getField) tylko flowName.get(user.field)
			return invokeInternally(this.map.get(exp[0]), this.map.getClassInfo(exp[0]), exp[1] , new ArrayList<Object>(), new ArrayList<Class>());
		}
		
		return null;
	}

}
