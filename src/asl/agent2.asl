// Agent agent2 in project test1

/* Initial beliefs and rules */
visibleEdge_(V1, V2) :- visibleEdge(V1, V2).
visibleEdge_(V1, V2) :- visibleEdge(V2, V1).

can_walk:- energy(E) & E>1.

///////////
//beliefs  addition
///////////
+energy(0):
	true <-
    recharge;
    .print("Recharging").

+step(_) :
	can_walk <- visibleEdge_(V1,V2); goto(V1);
	.print("I'm going to explore the world!!!"). 