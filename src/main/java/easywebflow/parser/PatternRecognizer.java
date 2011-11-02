package easywebflow.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import easywebflow.config.*;

public class PatternRecognizer {
	
	private static final String whitespace = "\\s+";
	private static final String white = "\\s*";
	private static final String word = "\\w+";
	private static final String name = "\"("+word+")\"";

	private static final String secured = "secured:"+ whitespace + name + white;

	private static final String invoke = white + "-\"(" + word + ")\\.(" + word + ")\\(("+word+")?\\)\"" + white; 
	private static final String invoke2 = white + "\"(" + word + ")\\.(" + word + ")\\(("+word+")?\\)\"" + white; 
	private static final String result = "result="+name + white;
	private static final String trans = white + name+ white;
	private static final String destName = "to" + whitespace +name  +white ;
	private static final String decision = "decision:" + white;
	private static final String ifPattern = white +":if"+whitespace + name +"=="+name + white ;
	private static final String elsePattern = white+":else"+ whitespace;

	private static final String ifP = white + name +"=="+name + white+"(?:" +destName+ ")?" + white ;
	private static final String elseP = white+ destName;
	
	private static final String view = "view=" +name + white;
	private static final String simpleState = white + name + whitespace+ "(?:"+view+")?" + white;
	
	/*description:	:else to "stateName"	*/
	private static final String elseGroup = "(?:("+elsePattern + destName+ "))";
	
	/*description:	:if "resultName"=="resultValue" [to "stateName"]	*/ 
	private static final String ifGroup = "(?:("+ifPattern +"(?:" +destName+ ")?"+"))";
	
	/*description:	-"beanName.methodName([paramName])" [result="resultName"] */
	private static final String invokeGroup = "(?:("+ invoke +"(?:"+result+")?" +"))";
	private static final String inv = "(?:(?:"+ invoke2 +"(?:"+result+")?" +"))";
	
	/*description:	*/
	private static final String decisionGroup = "(?:("+"(?:"+invokeGroup+")?" +"(?:"+ifGroup+")+" + elseGroup +"))";
	



	
	public static HashMap<String, FlowConfig> parseFile(String fileName) throws FileNotFoundException{
		HashMap<String, FlowConfig> hm = new HashMap<String, FlowConfig>();
		Scanner scanner = new Scanner(new File(fileName));
		scanner.useDelimiter("Flow:");
		try {
			while (scanner.hasNext()){
				//System.out.println("flow:");
				FlowConfig fc = parseFlow(scanner.next());
				hm.put(fc.getFlowName(), fc);
				//System.out.println(fc.getFlowName());  
			}
		} finally {
			/*ensure the underlying stream is always closed 
			 * this only has any effect if the item passed to the Scanner 
			 * constructor implements Closeable (which it does in this case).
		    */
			scanner.close();
		}
		
		System.out.println("KONIEC");
		return hm; 
	}
	
	private static FlowConfig parseFlow(String str){
		FlowConfigBuilder fcb = new FlowConfigBuilder();
		Scanner scanner = new Scanner(str);
		scanner.useDelimiter("state:");
		
		// tutaj parsuje intro flowa
		if (!scanner.hasNext()){
			// TODO wyjatek
		}
		
		String intro = scanner.next();
		Pattern pattern = Pattern.compile(white+name+secured);
		Matcher matcher = pattern.matcher(intro);

		if (matcher.matches()){
			fcb.setFlowName(matcher.group(1));
			// TODO parsuj secured
		} else if (matcher.usePattern(Pattern.compile(white + name + white)).matches()){
			fcb.setFlowName(matcher.group(1)); 
			//System.out.println("flowname parsera: " + fcb.getFlowName());
		}
		else {
			// TODO wyjatek
		}

		//tutaj parsuje wlasciwa część - stany
		while (scanner.hasNext()){
			//System.out.println("stan:");
			fcb.addStates(parseState(scanner.next()));
		}
	
		return fcb.build();
	}
	
	private static StateConfig parseState(String str){
		StateConfigBuilder scb = new StateConfigBuilder();
		Scanner scanner = new Scanner(str);
		scanner.useDelimiter(white + ":on");
		
		String grupa = scanner.next();
		
		Scanner introScanner = new Scanner(grupa);
		introScanner.useDelimiter("-");
		Pattern pattern = Pattern.compile(simpleState+secured);
	    Matcher matcher = pattern.matcher(introScanner.next());
		// tutaj poczatek stanu - intro
		if (matcher.matches()){
			// TODO jezeli secured state
		}
		else {
			matcher.usePattern(Pattern.compile(simpleState));
			
			if (matcher.matches()){
				scb.setName(matcher.group(1));
				
				if (matcher.groupCount() == 2){
					scb.setViewName(matcher.group(2));
				}
			}
			else {
				// TODO wyjatek zle state intro
			}
		}

		
		while (introScanner.hasNext()){
			scb.addOnStartCommands(parseInvokation(introScanner.next()));
		}
		
		while (scanner.hasNext()){	
				scb.addTransitions(parseTransition(scanner.next()));
		}	
		// TODO wyjatek jezeli nei ma przejsc
		
		
		return scb.build();
	}
	
	private static TransitionConfig parseTransition(String str){
		//System.out.println(str);
		TransitionConfigBuilder tcb = new TransitionConfigBuilder();
		//muszę sprawdzić jaki to typ transition
		Pattern pattern = Pattern.compile(trans);
	    Matcher matcher = pattern.matcher(str);
	    
		if (matcher.matches()){
			//System.out.println("zwykle:" + str);
			tcb.setOn(matcher.group(1));
		}
		else if (matcher.usePattern(Pattern.compile(trans + destName)).matches()){
			tcb.setOn(matcher.group(1)).setTo(matcher.group(2));
			//System.out.println("dest:" + matcher.group(1) + matcher.group(2));
		}
		else if (matcher.usePattern(Pattern.compile(trans + decision + decisionGroup)).matches()){
			//matcher.usePattern(Pattern.compile(decisionGroup)).
			Scanner scanner = new Scanner(str);
			scanner.useDelimiter(decision);
			tcb.setOn(scanner.next());
			
			tcb = parseDecision(tcb, scanner.next());
		}
		else {
			// TODO 
		}

		return tcb.build();
	}
	
	private static TransitionConfigBuilder parseDecision(TransitionConfigBuilder tcb, String str){
		//System.out.println("decision:" + str);
		Scanner scanner = new Scanner(str);
		scanner.useDelimiter(":if|:else");
		/*while (scanner.hasNext()){
			System.out.println("g "+scanner.next());
		}*/
		String grupa = null;
		if (scanner.hasNext()){
			grupa = scanner.skip("-").next();
			Matcher matcher = Pattern.compile(white+inv).matcher("");
			matcher.reset(grupa);

			if (matcher.matches() && scanner.hasNext()){
				//System.out.println("invokation:" + grupa);
				tcb.addOnDecision(parseInvokation(grupa));
				grupa = scanner.next();
				//System.out.println("grupa:" + grupa);
			}
			
			matcher.usePattern(Pattern.compile(ifP));
			matcher.reset(grupa);
			//System.out.println("pattern:" + matcher.pattern());
			while (matcher.matches() && scanner.hasNext()){
				//System.out.println("if:" + grupa);
				// TODO tworze if-y
				tcb = parseIf(tcb, grupa);
				grupa = scanner.next();
				//System.out.println("grupa:" + grupa);
			}
			
			matcher.usePattern(Pattern.compile(elseP));
			matcher.reset(grupa);
			//System.out.println("pattern:" + matcher.pattern());
			if (matcher.matches()){
				//System.out.println("else:" + grupa);
				if (matcher.groupCount() == 1){
					tcb.setTo(matcher.group());
				}
				else {
					// TODO wyjatek
				}
			}

		}
		return tcb;
	}
	
	private static TransitionConfigBuilder parseIf(TransitionConfigBuilder tcb, String str){
		Pattern pattern = Pattern.compile(ifP);
		Matcher matcher = pattern.matcher( str);
		//System.out.println("iF: " +str);	
		if (!matcher.matches()){
			//TODO podnies wyjatek
		}
		CommandConfig cc = new CommandConfigBuilder().setVariableName(matcher.group(1)).setVariableValue(matcher.group(2)).build();
	
		if (matcher.groupCount() == 3){
			tcb.addIfz(cc, matcher.group(3));
		}
		else{
			tcb.addIfz(cc, cc.getVariableValue());
		}
		
		return tcb;
	}
	
	private static CommandConfig parseInvokation(String str){
		Pattern pattern = Pattern.compile(inv);
		Matcher matcher = pattern.matcher( str);
		CommandConfigBuilder builder = new CommandConfigBuilder();
		//System.out.println("command: " +str);
		
		if (!matcher.matches()){
			//TODO podnies wyjatek
		}
		builder.setBeanName(matcher.group(1)).setMethodName(matcher.group(2));
		int n = matcher.groupCount();
		//sprawdz czy jest result prze find?? jezeli tak to zmniejsz n o 1
		//builder.setResultName(matcher.group(matcher.groupCount()));
		// n--;
		for (int i=3; i <=n; i++){
			// TODO: tutaj cos nie tak, dodaje nieistniejące puste wartosci
			//System.out.println("dodaje parametr "+ matcher.group(i));
			builder.addParamName(matcher.group(i));
		}
		
		return builder.build();
	}
}
