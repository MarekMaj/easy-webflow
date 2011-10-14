package easywebflow.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;

import easywebflow.core.Flow;

public class StatefullFlowBean<T> implements Bean<T> {

	private final String name;
	private final InjectionTarget<T> it;
	
	public StatefullFlowBean(InjectionTarget<T> it, String name) {
		this.name = name;
		this.it = it;
	}

	@Override
	public T create(
			CreationalContext<T> creationalContext) {
		T instance = it.produce(creationalContext);
		it.inject(instance, creationalContext);
		if (instance instanceof Flow){
			((Flow) instance).setFlowName(name);
		}
		it.postConstruct(instance);
		return instance;
	}

	@Override
	public void destroy(T instance,
			CreationalContext<T> creationalContext) {
		it.preDestroy(instance);
		it.dispose(instance);
		creationalContext.release();
		
	}

	@Override
	public Set<Type> getTypes() {
		Set<Type> types = new HashSet<Type>();
		types.add(FlowImpl.class);
		types.add(Object.class);
		return types;
	}

	@Override
	public Set<Annotation> getQualifiers() {
		Set<Annotation> qualifiers = new HashSet<Annotation>();
		qualifiers.add( new AnnotationLiteral<Default>() {} );
		qualifiers.add( new AnnotationLiteral<Any>() {} );
		return qualifiers;
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return ConversationScoped.class;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<Class<? extends Annotation>> getStereotypes() {
		return Collections.emptySet();
	}

	@Override
	public Class<?> getBeanClass() {
		return FlowImpl.class;
	}

	@Override
	public boolean isAlternative() {
		return false;
	}

	@Override
	public boolean isNullable() {
		return false;
	}

	@Override
	public Set<InjectionPoint> getInjectionPoints() {
		return it.getInjectionPoints();
	}
	
}

