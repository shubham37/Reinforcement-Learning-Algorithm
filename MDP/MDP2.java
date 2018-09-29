//Here We are using BURLAP Java based library to solve MDP problem
//Steps as follow:
//1. Choose relevant domain generator class for your problem
//2. Define Set of states to MDP
//3. Define initial state
//4. Add action to states
//5. Make reward class to attach reward to state
//6. Adding terminal function There is a Terminal State
//7. Value iteration for finding which one is best action
//8. Now you can call bestAction() method to see which one is best

import burlap.behavior.singleagent.policy;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.*;
import burlap.oomdp.singleagent.GroundAction;
import burlap.oomdp.singleagent.RewardFunction;

public class SecondMDP{
	
	public SecondMDP(){s
		
	}
	public static void main(String [] args){
		SecondMDP sm = new SecondMDP();
	}
}