package easywebflow.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import easywebflow.factory.MainFactory;
import easywebflow.state.StateContext;


public class FlowImpl implements Flow, Serializable {
	
	private HashMap<String, InjectedObjectData> dataMap = new HashMap<String, InjectedObjectData>();
	private StateContext stateContext;
	/* Attention: field available after creation methods invoked 
	 * and before PostCostruct method invocation */  
	private String flowName;
	
	@Inject 
	private Conversation conversation;
	@Inject 
	private BeanManager beanManager;
	@Inject @Any 
	private Instance<Object> anyObject;
	
	/*This method is called externally during bean registering.
	 * It is recommended not to call this method anywhere else.
	 * */
	@Override
	public void setFlowName(String name){
		flowName = name;
	}
	
	@PostConstruct
	public void initData(){
		//service = getInstance("myService", "getD"); 	
		System.out.println("Post construct:" + flowName);
		this.stateContext = MainFactory.getStateContext(this, flowName);
	}
	
	/*test*/
	public String start(){
		// if flow already exist
		if (conversation.isTransient()){
			conversation.begin();
		}
		this.stateContext.enterStartState();
		System.out.println( stateContext.getCurrentStateName());
		return stateContext.getCurrentStateName();
	}
	
	/*test*/
	@Override
	//public String transitionTo(String stateName) {
	public String transitionTo() {
		System.out.println( "transition");
		//String s = this.stateContext.transitionTo(stateName);
		String s = this.stateContext.transitionTo("outcome");
		if (s == null){
			System.out.println( "koncze flow");
			if (!conversation.isTransient()){
				conversation.end();
			}
			return "index";
		}
		return s;
	}

	@Override
	public Object invoke(String beanName, String methodName,
			String... paramName) {
		System.out.println("invoke by flow: " + beanName + " " +methodName + " " +paramName[0] );
		InjectedObjectData data = this.getExternalData(beanName);
		
		ArrayList<Object> paramObjects = new ArrayList<Object>();
		ArrayList<Class> paramClass = new ArrayList<Class>();		
		for (String s:paramName){
			if (s != null){
				System.out.println("dodaje: "+s);
				InjectedObjectData iod = this.getExternalData(s);
				paramObjects.add(iod.getObject());
				paramClass.add(iod.getClazz());
				System.out.println("dodalem: "+s);
			}
		}
		
		// build new Class array from ArrayList
		Class[] paramClazz = new Class[paramClass.size()];
		System.out.println("ok");
		paramClass.toArray(paramClazz);
		
		Object object = null;
		try {
			System.out.println("bean: " + data.getName() + " "+ data.getObject().toString() + " "+data.getClazz().toString());
			System.out.println("methodName: "+methodName);
			for (int i=0; i< paramClazz.length;i++){
				System.out.println("params: "+paramClazz[i] + " "+paramObjects.get(i));
			}
			
			object = data.getClazz().getDeclaredMethod(methodName, paramClazz).invoke(data.getObject(), paramObjects.toArray());
			System.out.println("Wolalem:" + data.getName() + " "+data.getObject() + " "+data.getClazz());
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
		
		return object;
	}

	@Override
	public void setResult(String resultName, Object result) {
		// jezeli obiekt o takiej nazwie juz istnieje zastap istniejacy
		dataMap.put(resultName, new InjectedObjectData(resultName, result));

	}

	@Override
	public boolean compareObjects(String comparedObjectName,
			String comparedObjectValue) {
		// TODO Auto-generated method stub
		System.out.println("Comparing: " + comparedObjectName + " "+ comparedObjectValue);
		return false;
	}
	
	private InjectedObjectData getExternalData(String name){
		if (!dataMap.containsKey(name)){
			injectExternal(name);
		}
		return dataMap.get(name);
	}
	
	private void injectExternal(String name){
		Set<Bean<?>> set = beanManager.getBeans(name);
		// może wyrzucić to i jeżeli nie wczyta lub ambiguous to podnieśc wyjatek
		// wyjatek jezeli pusty zbiór i jezeli nie znajde metody
		// jezeli wiecej niz jeden to nie potrzebuje - WELD sie tym zajmie
		if (set.isEmpty()){
			System.out.println("nie znalazlem beana o tej nazwie: "+ name);
		}else{
			Bean<?> bean = set.iterator().next();
			//System.out.println("getUserInstance znalazł klasy:");
			//System.out.println(bean.getBeanClass());
			try {
				InjectedObjectData ed = new InjectedObjectData(name, anyObject.select(bean.getBeanClass()).get(), bean.getBeanClass());
				dataMap.put(name, ed);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
