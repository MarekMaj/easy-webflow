package easywebflow.core;

public class InjectedObjectData {
	private String name; 	//unique name
	private Object object;
	//optional
	private Class clazz;	

	
	public InjectedObjectData(String name, Object object, Class clazz) {
		super();
		this.name = name;
		this.object = object;
		this.clazz = clazz;
	}
	
	public InjectedObjectData(String name, Object object) {
		super();
		this.name = name;
		this.object = object;
		this.clazz = Object.class;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
