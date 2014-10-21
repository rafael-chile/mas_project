package env;

import jason.JasonException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Translator;
import eis.AgentListener;
import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import agent.AgentListenerEvents;

public class Connection extends Environment implements AgentListener {
	

	private static final String CN = "massim.eismassim.EnvironmentInterface";
	private static Map<String, AgentListenerEvents> listeners = new HashMap<String, AgentListenerEvents>();
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
		ei.attachAgentListener("agent2", this);
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
	
	public static void addAgentListenerEvents(String agName, AgentListenerEvents agArch) {
        listeners.put(agName, agArch);
        System.out.println("Agent " + agName + " added!");
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
	
	public void handlePercept(String agent, Collection<Percept> percepts) {        
        try {
            clearPercepts(agent);
            
            if (listeners.containsKey(agent)) {
                Literal[] jasonPers = new Literal[percepts.size()];
                int i = 0;
                for (Percept p: percepts) {
                    jasonPers[i++] = Translator.perceptToLiteral(p);
                }
            
                listeners.get(agent).notifyPercepts(jasonPers);
                
                //add the massim belief
                //if (agToMassimContest.containsKey(agToMassimContest.get(listeners.get(agent)))) {
                listeners.get(agent).addBelief(Literal.parseLiteral("myNameInContest(agent2)"));
                //}
            }
        } catch (JasonException e) {
            e.printStackTrace();
        }
    }

	public void handlePercept(String arg0, Percept arg1) {
		/*try {
			System.out.println("Agent: " + arg0 + "perceiving percept " + Translator.perceptToLiteral(arg1));
		} catch (JasonException e) {
			e.printStackTrace();
		}*/
	}
	
	@Override
    public List<Literal> getPercepts(String agName) {
        return addEISPercept(super.getPercepts(agName),agName);
    }
    
    protected List<Literal> addEISPercept(List<Literal> percepts, String agName) {
        clearPercepts(agName);
        if (percepts == null) 
            percepts = new ArrayList<Literal>();
        
        if (ei != null) {
            try {
                Map<String,Collection<Percept>> perMap = ei.getAllPercepts(agName);
                for (String entity: perMap.keySet()) {
                    Structure strcEnt = ASSyntax.createStructure("entity", ASSyntax.createAtom(entity));
                    for (Percept p: perMap.get(entity)) {
                        percepts.add(Translator.perceptToLiteral(p).addAnnots(strcEnt));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return percepts;        
    }
}
