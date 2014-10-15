// Agent agent2 in project test1

/* Initial beliefs and rules */


visibleEdge_(V1, V2) :- visibleEdge(V1, V2).
visibleEdge_(V1, V2) :- visibleEdge(V2, V1).
can_walk:- energy(E) & E>1.

///////////
//beliefs  addiction
///////////
+energy(0):
	true <-
		recharge;
		.print("Recharging").

+step(_):
	position(P) & role(R) & can_walk<-
		graph.getPosToMove(P,R,V);
		goto(V);
		.print("I'm going to explore the world!!!").
		
+step(_):
	maxEnergy(ME) & energy(E) & E<ME <-
		recharge;
		.print("Recharging is the best that I can do now!!").

/* Initial goals */



/* Plans */

+!start : true <- .print("hello world.").


