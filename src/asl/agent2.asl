// Agent agent2 in project test1

/* Initial beliefs and rules */
/*visibleEdge_(V1, V2) :- visibleEdge(V1, V2).
visibleEdge_(V1, V2) :- visibleEdge(V2, V1).

can_walk:- energy(E) & E>1.*/

///////////
//beliefs  addiction
///////////
//+energy(0):
//	true <-
//		recharge;
///		.print("Recharging").

//+step(_) :
///	can_walk <- visibleEdge_(V1,V2); goto(V1);
//	.print("I'm going to explore the world!!!"). 
/* Initial goals */

!start.

/* Plans */

+!start : true <- recharge; energy(E);
		.print("recharging, now I have ", E , " energy").
