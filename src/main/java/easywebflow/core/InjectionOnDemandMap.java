package easywebflow.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

public class InjectionOnDemandMap<K, V> extends ConcurrentHashMap<K, V> {
	
	/* map holds additional info about class type of injected object
	 * this is necessary becouse it seems impossible to retrieve class name from CDI proxy
	 * */
	private HashMap<K, Class<?>> classInfo = new HashMap<K, Class<?>>();
	
	@Inject @Any
	private Instance<Object> anyObject; 
	@Inject 
	BeanManager beanManager;
	
	// co z konkurencyjnościa
	
	// moze w ogóle wywalic wstrzykiwanie z flowa i wrzucic do takich map?? - tak
	// również w InjectedObjectData 
	
	// inny problemem spójność tzn. 
	// jezeli nie definiuje dataModel w xml to 
	// 1. sam xml robi się niespójny - mogą być odwołania do obiektów z dataModelu ale ich nie widać 
	// - nie mozna rozroznic z xml czy dataModel czy temp
	// 2. niezgodny ze standardem
	// 3. ale wygodniej
	// z drugiej strony twórca flowa może nie wiedziec czy to ma należeć do dataModelu czy tez nie - tymczasowe
	// hmm tutaj reguła jest prosta jezeli obiekt jest potrzebny w kliku stanach to dataModel
	// niekoniecznie jezeli obiekt typu sesja to co ???
	// nie mogę tutaj trzymać nazw klas itp. to w odzielnej mapie ??

	// co z rezultatem działania ? jezeli nazwa w dataModel to @Dependent 
	public InjectionOnDemandMap() {
		super();
	}

	public synchronized void createDataModel(ArrayList<K> data){
		Iterator<K> it = data.iterator();
		while (it.hasNext())
			this.get(it.next());
	}
	
	public Class<?> getClassInfo(Object key){
		if (this.classInfo.containsKey(key))
			return this.classInfo.get(key);
		return Object.class;
	}
	
	@Override
	public synchronized V get(Object key) {
		if (!this.containsKey(key)){
			this.createModel(key);
		}
		return super.get(key);
	
	}
	
	@Override
	public V put(K key, V value) {
		return super.put(key, value);
	}

	@SuppressWarnings("unchecked")
	private synchronized void createModel(Object key) {
		System.out.println("tworze model "+ key.toString());
		Set<Bean<?>> set = beanManager.getBeans(key.toString());
		if (set.isEmpty()){
			// TODO tutaj jakis exception bo CDI tego nie rozwiaze, moze nie wiedziec ze jest taki bean a wiec nie bedzie 
			// unsatisfied dependency

		}else{

				Bean<?> bean = set.iterator().next();
				Class<?> clazz = bean.getBeanClass();
				System.out.println("Class to create: "+clazz);
				System.out.println(bean.getStereotypes());
				System.out.println(bean.getTypes());
				System.out.println(bean.getQualifiers());
				System.out.println(bean.getScope());

				try {
					Object obj = clazz.newInstance();;
					super.put((K)key, (V) obj);
					this.classInfo.put((K)key, clazz);
				
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void injectExternal(K key){
		
		Set<Bean<?>> set = beanManager.getBeans(key.toString());
		if (set.isEmpty()){
			// TODO exception

		}else{
			Bean<?> bean = set.iterator().next();
			Class<?> clazz = bean.getBeanClass();
			try {
				Object obj = anyObject.select(clazz).get();
				super.put((K)key, (V) obj);
				this.classInfo.put((K)key, clazz);
				
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
