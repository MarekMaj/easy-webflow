package easywebflow.state;

public interface State {
	void onStart();
	String onTransition(String name);
	void onExit();
	String getName();
}
