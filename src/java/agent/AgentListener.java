package agent;

import jason.asSyntax.Literal;

import java.util.List;

public interface AgentListener {

	public void notifyPercepts(List<Literal> percepts);
	
}
