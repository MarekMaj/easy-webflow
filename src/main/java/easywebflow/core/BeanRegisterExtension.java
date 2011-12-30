package easywebflow.core;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;

import easywebflow.configuration.Configuration;
import easywebflow.factory.MainFactory;
import easywebflow.security.SecurityInterceptor;

public class BeanRegisterExtension implements Extension {
	
	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
		
		// find an interceptor bean
		/*beanManager.
		// register it without XML
		// add Interceptor if required
		if (!Configuration.getInstance().getConfigAttributeByName("security").equalsIgnoreCase("false")){
			abd.addBean(new Interceptor<SecurityInterceptor>());
		}
		*/
		// resolver analizuje wpisy i w petli
		// powiedzmy ze resolver zdecydowal ze utworzony flow bedzie z polami i mial nazwe name
		//use this to read annotations of the class
		
		AnnotatedType<FlowImpl> at = beanManager.createAnnotatedType(FlowImpl.class);
		//use this to instantiate the class and inject dependencies
		final InjectionTarget<FlowImpl> it = beanManager.createInjectionTarget(at);
		
		MainFactory main = new MainFactory();
		for (String s:main.getFlowNames()){
			System.out.println("rejestruje :" +s);
			abd.addBean(new StatefullFlowBean<FlowImpl>(it, s));
		}

		//System.out.println(StatefullFlow.ile);
	
		//Set<Bean<?>> set = beanManager.getBeans("newUser");
		// może wyrzucić to i jeżeli nie wczyta lub ambiguous to podnieśc wyjatek
		// wyjatek jezeli pusty zbiór i jezeli nie znajde metody
		// jezeli wiecej niz jeden to nie potrzebuje - WELD sie tym zajmie
		/*for (Bean<?> b: set){
			System.out.println("znalazlem poprzez EL");
			System.out.println(b.getBeanClass());
		}*/
		/*najpierw decyduję jakie beany muszę utworzyć poprzez wołanie parsera
		rodzaje beanów-flowów:
		każdy ma zbiór stanów 
		niektóre stany mogą być secured
		każdy stan ma nazwę 
		mogą mieć prywatne pola / wstrzyknięte servisy 
		
		w ten sposób będę dodawal pola:
		Class actualClass = Class.forName(classValueInXML): 
		Object actualObject = actualClass.newInstance();  // tutaj powinno zadziałać CDI a nie newInstance
		Field acutalField = actualClass.getDeclaredField("Stranger"); 
		actualField.set(actualObject, "I am an stranger"); 
 
		*/
		/*
		Class actualClass;
		StatefullFlow flow = new StatefullFlow();
		Method[] methods = flow.getClass().getDeclaredMethods();
		for (Method m:methods){
			System.out.println(m.getName());
		}
		
		try {
			actualClass = Class.forName("User");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Nie znaleziono");
		}
		*/
		
		// i to trzeba dodac do mapy 
		// i pozniej napisac metode ktora wola metode 
		// Method method = getDeclaredMethod("persist");  jezeli nie ma to wyjatek
		// method.invoke();
		// choć najlepiej byloby tworzyc normalne pola 
		// bo wtedy łatwiej wolac poprzez EL
		// zrobic interceptor ktory wychwytuje wywolanie metody i wola troche inna
		// czy to zadziala ????
		// a moze po prostu wolac zmiane stanu a bean pozniej wola handle() na nowym stanie i bean sam wola alikacje !!!!!
		// a jezeli nie to wolam tak w JSF: flowName.invoke(methodName) lub flowName.invoke(serviceName, serviceMetohd, parameters ?)
		// nie mozna poprzez getDeclaredMethodes na tym etapie bo niektóre metody i pola deklarowane at runtime
		// moze zamiast flowName.invoke(methodName) cos w stylu flowName.transistion(String destination)
		//StatefullFlow flow = new StatefullFlow();
		//flow.getDeclaredMethods();
	
		//System.out.println("po...");
		}
	
}
