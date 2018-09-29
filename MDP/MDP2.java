//Problem Statement is finding best action for state S0 and S1
//Here We are using BURLAP Java based library to solve MDP problem

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
	
	DomainGenerator dg;
	Domain domain;
	int numStates;
	State initState;
	RewardFunction rf;
	TerminalFunction tf;
	DiscreteStateHashFactory hashFactory;
	double p1 = p1;
	double p2 = p2;
		
	public SecondMDP(double p1, double p2){
		this.p1 = p1;
		this.p2 = p2;
		
		this.numStates = 5;
		this.dg = new GraphDefinedDomain(numStates);
		
		//Actions for initial state
		((GraphDefinedDomain)this.dg).setTransition(0,0,0,p1);
		((GraphDefinedDomain)this.dg).setTransition(0,0,1,1.-p1);
		((GraphDefinedDomain)this.dg).setTransition(0,1,2,1.);
		
		//Actions for 1 state
		((GraphDefinedDomain)this.dg).setTransition(1,0,3,1.-p2);
		((GraphDefinedDomain)this.dg).setTransition(1,1,4,1.);
		((GraphDefinedDomain)this.dg).setTransition(1,0,5,p2);
		
		//Actions for 2 state
		((GraphDefinedDomain)this.dg).setTransition(2,0,1,1.);
		
		//Actions for 3 state
		((GraphDefinedDomain)this.dg).setTransition(3,0,1,1.);
		
		//Actions for 4 state
		((GraphDefinedDomain)this.dg).setTransition(4,0,5,1.);
		
		this.domain = this.dg.generateDomain();
		this.initState = GraphDefinedDomain.getState(this.domain,0);
		
		this.rf = new RewardFunctionRF();
		this.tf = new TerminalFunctionTF(5);//That define which one is terminal or not
		this.hashFactory = new DiscreteStateHashFactory();
	}
	
	
	public static class RewardFunctionRF implements RewardFunction{
		
		@Override
		public double reward(State s, GroundAction a, State t)
		{
			int sid = GraphDefinedDomain.getNodeId(s);
			int tid = GraphDefinedDomain.getNodeId(s);
			
			double r=0;

			if (sid == 0){
				if (tid==0)
					r=-1;
				else if(tid==1)
					r=3;
				else if(tid==2)
					r=1;
			}
			else if(sid==1)
			{
				if(tid==3)
					r=1;
				else if(tid==4)
					r=2;
				else if(tid==5)
					r=0;
			}
			else if(sid>=2 && sid <=4)
			{	
				r=0;
			}
			else if(sid==5)
				throw new RuntimeException("No Transition :"+sid);
			else
				new RuntimeException("Unkowon State:"+sid);
		}
		
	}
	
	public static TerminalFunctionTF implements TerminalFunction{
		int tid;
		public TerminalFunctionTF(int tid)
		{
			this.tid = tid;
		}
		
		public boolean isTerminal(State s)
		{
			int sid = GraphDefinedDomain.getNodeId(s);
			return sid==this.tid;
		}
	}
	
	public ValueIteration computeValue(double gamma){
		double maxDelta = 0.0001;
		double maxIteration = 1000;
		ValueIteration vi = new ValueIteration(this.domain,this.rf,this.tf,gamma,this.hashFactory,maxDelta,maxIteration);
		vi.planStateFrom(this.initState);
		return vi;
	}
	
	public String bestAction(double gamma){
		ValueIteration vi = computeValue(vi);
		Policy p = new GreedyQPolicy(vi);//It take ValueIteration object and give which action is best for particular state
		State s0 = GraphDefinedDomain.getState(this.domain,0);
		State s1 = GraphDefinedDomain.getState(this.domain,1);
		
		String action0 = p.getAction(s0).actionName().replaceAll("action0","a").replaceAll("action1","b");
		String action1 = p.getAction(s1).actionName().replaceAll("action0","c").replaceAll("action1","d");
		return action0+", "+action1;
	}
	
	public Domain getDomain(){
		return this.domain;
	}
	
	public static void main(String [] args){
		SecondMDP sm = new SecondMDP(.5,.7);
	}
}
