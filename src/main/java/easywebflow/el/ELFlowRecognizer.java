package easywebflow.el;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import easywebflow.configuration.StateIdentifier;

public final class ELFlowRecognizer {

	private static final String word = "\\w+";
	private static final String outcomePattern = word+"/" + word;
	private static final String transitionPattern ="#\\{"+word+"\\.transitionTo\\(\'"+word+"\'\\)\\}";
	private static final String startPattern = "#\\{"+word+"\\.start\\}";
	
	// Suppress default constructor for noninstantiability
	private ELFlowRecognizer() {
		throw new AssertionError();
	}
	
	public static StateIdentifier parse(String fromAction, String outcome){
		String flowName = null;
		String stateName = null;
		Scanner scanner = new Scanner(fromAction);
		
		// if fromAction matches start pattern
		Pattern pattern = Pattern.compile(startPattern);
		Matcher matcher = pattern.matcher(fromAction);
		if (matcher.matches()){
			scanner.useDelimiter("#\\{|.start\\}");
			flowName = scanner.next();
			
			// retrieve stateName from outcome
			pattern = Pattern.compile(outcomePattern);
			matcher = pattern.matcher(outcome);
			if (matcher.matches()){
				scanner = new Scanner(outcome);
				scanner.useDelimiter("/");
				scanner.next();
				stateName = scanner.next();
				
				return new StateIdentifier(flowName, stateName);
			}
			
			return null;
		}
		
		// else if fromAction matches transition pattern
		pattern = Pattern.compile(transitionPattern);
		matcher = pattern.matcher(fromAction);
		if (matcher.matches()){
			scanner.useDelimiter("#\\{|\\.transitionTo\\(");
			flowName = scanner.next();
			stateName = outcome;
			return new StateIdentifier(flowName, stateName);
		}
		
		// else return null
		return null;
		}
}
