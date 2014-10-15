package env;

import jason.environment.Environment;

import java.io.IOException;
import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.RelationException;

public class Connection extends Environment {
	

	private static final String CN = "massim.eismassim.EnvironmentInterface";
	private static final String EISMASSIMCONFIG = "eismassimconfig.xml";
	
	public void init(String args[]) {
	
	EnvironmentInterfaceStandard ei = null;
	
		
	try {
		ei = EILoader.fromClassName(CN);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	for(String e: ei.getEntities()) {
        try {
			ei.registerAgent(e);
            try {
				ei.associateEntity(e, e);
			} catch (RelationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (AgentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	try {
		ei.start();
	} catch (ManagementException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
}
