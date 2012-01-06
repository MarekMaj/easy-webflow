package easywebflow.state;

public interface State {
	void onStart();
	String onTransition(String name) throws IllegalTransitionException;
	String getStateNameForTransition(String name) throws IllegalTransitionException;
	void onExit();
	String getName();
	Boolean isFinal();
}
