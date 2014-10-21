package arch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import agent.AgentListenerEvents;
import env.Connection;
import jason.architecture.AgArch;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class AgentArch extends AgArch implements AgentListenerEvents {

	private Literal[] lastPercepts;

	public AgentArch() {
		super();
	}
	
	 @Override
    public void init() throws Exception {
        super.init();
        
        Connection.addAgentListenerEvents(getAgName(), this);
//        getTS().getAg().getBB().add(Literal.parseLiteral("MyNameIsAgent2"));
        //logOn = getAgName().equals("explorer1");
    }

	public void notifyPercepts(Literal... percepts) {
        lastPercepts = percepts;
        getArchInfraTier().wake();
    }

    public void addBelief(Literal belief) {
        getTS().getAg().getBB().add(belief);
    }

}
