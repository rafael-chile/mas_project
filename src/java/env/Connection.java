package env;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import agent.AgentListener;

import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;

public class Connection extends Environment {
	

	private static final String CN = "massim.eismassim.EnvironmentInterface";
	private EnvironmentInterfaceStandard ei;
	private Map<String, AgentListener> marsAgentListeners;
	public void init(String args[]) {
	
		 ei = null;
		
			
		try {
			ei = EILoader.fromClassName(CN);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ei.registerAgent("agent2");
            
		} catch (AgentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ei.associateEntity("agent2", "connectionA1");
		} catch (RelationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ei.start();
		} catch (ManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void handlePercept(String agName, Percept percept) {
			Collection<Collection<Percept>> persEnts = null;
			try {
				persEnts = ei.getAllPercepts("agent2").values();
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Percept> percepts = new LinkedList<Percept>();
			for (Collection<Percept> persEnt : persEnts){
				System.out.println(persEnt.toString());
				percepts.addAll(persEnt);
			}
			handlePercepts(percepts);

		} 
	

	private String sanitizeTerm(String term) {
		if (term.isEmpty())
			return "emp";
		return term.substring(0, 1).toLowerCase() + term.substring(1);
	}
	
	private Literal literalOf(Percept percept) {
		Iterator<Parameter> paramsIt = percept.getParameters().iterator();
		String terms = percept.getParameters().isEmpty() ? "" : "(";
		while (paramsIt.hasNext()) {
			terms += sanitizeTerm(paramsIt.next().toString());
			terms += paramsIt.hasNext() ? "," : ")";
		}
		return Literal.parseLiteral(sanitizeTerm(percept.getName()) + terms);
	}

	
	private void handlePercepts(List<Percept> percepts) {
		List<Literal> literalPercepts = new LinkedList<Literal>();
		for (Percept percept : percepts)
			literalPercepts.add(literalOf(percept));
		notifyPercepts(literalPercepts);
	}
	
	private void notifyPercepts(List<Literal> percepts) {
		AgentListener marsAgentListener = marsAgentListeners.get("agent2");
		if (marsAgentListener != null)
			 marsAgentListener.notifyPercepts(percepts);
	}

	
	@Override
	public boolean executeAction(String agName, Structure action) {
		
		try {
			System.out.println(agName + " " + action.getFunctor());
			ei.performAction(agName, actionOf(action));
		} catch (ActException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Action actionOf(Structure sAction) {
		LinkedList<Parameter> parameters = new LinkedList<Parameter>();
		for (Term term : sAction.getTerms())
			parameters.add(new Identifier(term.toString()));
		return new Action(sAction.getFunctor(), parameters);
	}
}
