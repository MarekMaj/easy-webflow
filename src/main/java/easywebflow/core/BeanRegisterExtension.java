package easywebflow.core;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;

import easywebflow.factory.MainFactory;

public class BeanRegisterExtension implements Extension {

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {

		//use this to read annotations of the class

		AnnotatedType<FlowImpl> at = beanManager.createAnnotatedType(FlowImpl.class);
		//use this to instantiate the class and inject dependencies
		final InjectionTarget<FlowImpl> it = beanManager.createInjectionTarget(at);

		MainFactory main = new MainFactory();
		for (String s:main.getFlowNames()){
			//System.out.println("rejestruje :" +s);
			abd.addBean(new StatefullFlowBean<FlowImpl>(it, s));
		}
	}

}
