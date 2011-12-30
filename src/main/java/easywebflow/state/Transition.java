package easywebflow.state;

public interface Transition {
	
	String getName();
	/* executes transition actions and returns new state name*/
	String transition();
	/* checks if event is appropriate for this transition or/and any business conditional rules are matched
	 * This method can be invoked at any time just to match appropriate transition.
	 * Do not invoke any business actions here (especially time consuming). Its aim is only to compare.
	 */
	Boolean isAllowed(String eventName);
	/* returns target state name without executing nor checking conditions*/
	String getTargetStateName();
}
