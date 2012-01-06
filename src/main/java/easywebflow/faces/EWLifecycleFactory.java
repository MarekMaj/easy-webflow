package easywebflow.faces;

import java.util.Iterator;

import javax.faces.FacesWrapper;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import easywebflow.configuration.Configuration;

public class EWLifecycleFactory extends LifecycleFactory implements FacesWrapper<LifecycleFactory>{

	private LifecycleFactory wrappedLifecycleFactory = null;

	public EWLifecycleFactory() {
		super();
	}

	public EWLifecycleFactory(LifecycleFactory wrappedLifecycleFactory) {
		super();
		this.wrappedLifecycleFactory = wrappedLifecycleFactory;

		// if security present
		if (!Configuration.getInstance().getConfigAttributeByName("security").equalsIgnoreCase("false")){
			// add listeners to existing/defaults lifecycles
			Iterator<String> it = this.getWrapped().getLifecycleIds();
			while (it.hasNext()){
				this.getWrapped().getLifecycle(it.next()).addPhaseListener(new SecurityListener());
			}
		}
		// if PRG present
		if (!Configuration.getInstance().getConfigAttributeByName("PRG").equalsIgnoreCase("false")){
			// add listeners to existing/defaults lifecycles
			Iterator<String> it = this.getWrapped().getLifecycleIds();
			while (it.hasNext()){					
				this.getWrapped().getLifecycle(it.next()).addPhaseListener(new PRGListener());
			}
		}
	}

	@Override
	public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
		if (!Configuration.getInstance().getConfigAttributeByName("security").equalsIgnoreCase("false")){
			lifecycle.addPhaseListener(new SecurityListener());
		}
		if (!Configuration.getInstance().getConfigAttributeByName("PRG").equalsIgnoreCase("false")){
			lifecycle.addPhaseListener(new PRGListener());
		}
		this.getWrapped().addLifecycle(lifecycleId, lifecycle);
	}

	@Override
	public Lifecycle getLifecycle(String lifecycleId) {
		return this.getWrapped().getLifecycle(lifecycleId);
	}

	@Override
	public Iterator<String> getLifecycleIds() {
		return this.getWrapped().getLifecycleIds();
	}

	@Override
	public LifecycleFactory getWrapped() {
		return this.wrappedLifecycleFactory;
	}

}
