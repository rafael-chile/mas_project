package env;

import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

import java.io.IOException;
import java.util.LinkedList;

import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class Connection extends Environment {
	

	private static final String CN = "massim.eismassim.EnvironmentInterface";
	private static final String EISMASSIMCONFIG = "eismassimconfig.xml";
	private EnvironmentInterfaceStandard ei;
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
