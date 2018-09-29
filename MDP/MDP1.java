//Here We are using BURLAP Java based library to solve MDP problem
//Steps as follow:
//1. Choose relevant domain generator class for your problem
//2. Define Set of states to MDP
//3. Define initial state
//4. Add action to states
//5. Make reward class to attach reward to state
//6. Adding terminal function
//7. Value iteration for finding which one is best action
//8. Now you can call bestAction() method to see which one is best

import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.*;
import burlap.oomdp.singleagent.GroundAction;
import burlap.oomdp.singleagent.RewardFunction;

public class FirstMDP
{
	Domain domain;
	DomainGenerator dg;
	State initState;
	int numState;
	TerminalFunction tf;
	DiscreteStateHashFactory hashfactory;
	
	
	public FirstMDP(double p1,double p2,double p3,double p4){
		this.numStates = 6;
		this.dg = new GraphDefinedDomain(numStates);
		
		//.setTransition('starting_state','action_code','dest_state','probability')
		//For State 0
		((GraphDefinedDomain)this.dg).setTransition(0,0,1,1.);
		((GraphDefinedDomain)this.dg).setTransition(0,1,2,1.);
		((GraphDefinedDomain)this.dg).setTransition(0,2,3,1.);
		
		//For State 1
		((GraphDefinedDomain)this.dg).setTransition(1,0,1,1.);
		
		//For State 2
		((GraphDefinedDomain)this.dg).setTransition(2,0,4,1.);
		((GraphDefinedDomain)this.dg).setTransition(4,0,2,1.);
		
		//For State 3
		((GraphDefinedDomain)this.dg).setTransition(3,0,5,1.);
		((GraphDefinedDomain)this.dg).setTransition(5,0,5,1.);
		
		
		this.domain = this.dg.generateDomain(); 
		this.initState = GraphDefinedDomain.getState(this.domain,0);
		this.rf = new FourRewardRF(double p1,double p2,double p3,double p4);
		this.tf = new NullTermination();
		this.hashfactory = new DiscreteStateHashFactory();
	}
	
	public static class FourRewardRF implements RewardFunction{
		double p1;
		double p2;
		double p3;
		double p4;
		
		public FourRewardRF(double p1,double p2,double p3,double p4){
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
			this.p4 = p4;
			
		}
		
		@Override
		public double reward(State s, GroundAction a, State sPrime)
		{
			int sid = GroundAction.getNodeId(s);
			
			double r;
			if(sid == 0 || sid==3)
				r=0;
			else if (sid==1)
				r = this.p1;
			
			else if(sid==2)
				r = this.p2;
			else if(sid==4)
				r = this.p3;
			else if(sid==5)
				r = this.p4;
			else{
				throw new RuntimeException("Unknown State:"+sid);
				
			}
			
			return r;
		}
		
	}
	
	public Domain getDomain(){
		return this.domain;
	}
	
	public ValueInteration computevalue(double gamma){
		double maxDelta= 0.0001;
		int maxIteration = 1000;
		
		ValueInteration vi = new ValueInteration(this.domain,this.rf,this.tf,gamma,this.hashfactory,maxDelta,maxIteration);
		vi.planFromState(this.initState);
		return vi;
	}
	
	public String bestAction(double gammma){
		ValueInteration vi = computevalue(gamma);
		
		double[] v = new double[this.numStates];
		for (int i=0;i<this.numStates;i++)
		{
			State s = GraphDefinedDomain.getState(this.domain,i);
			v[i] = vi.value(s);
		}
		
		String actionname = null;
		if(v[1]>=v[2] && v[1] >=v[3])
			actionname = "a";
		else if(v[2]>=v[1] && v[2] >=v[3])
			actionname = "b";
		else if(v[3]>=v[1] && v[3] >=v[2])
			actionname = "c";
		return actionname;
		
	}
	
	public static void main(String [] args)
	{
		FirstMDP fmdp = new FirstMDP(5,3,6,7);
	}
	
}
