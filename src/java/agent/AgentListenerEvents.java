package agent;

import jason.asSyntax.Literal;

public interface AgentListenerEvents { 
    public void notifyPercepts(Literal... literals);
    public void addBelief(Literal belief);
}
