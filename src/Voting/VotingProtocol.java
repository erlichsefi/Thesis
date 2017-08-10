package Voting;

import java.util.Arrays;
import java.util.Comparator;

import tools.Agent;
import tools.outcome;

public class VotingProtocol {

	public VotingProtocol(){

	}


	final public outcome[] getScore(Agent agent2) {	
		return ValuePrefrence(agent2.getPrefrenceArray(),agent2.getAgentName());

	}


	final private outcome[] ValuePrefrence(String[] out, String agentName) {
		outcome[] answer=new outcome[out.length];
		for (int i = 0; i < answer.length; i++) {
			answer[i]=new outcome(agentName,out[i],ValueToPlaceInArray(out.length,i));
		}
		return answer;
	}


	private int ValueToPlaceInArray(int length, int i) {
		return i+1;
	}


	final public int getScore(String[] myPre, int i) {
		return ValueToPlaceInArray(myPre.length,i);
	}




	public String asSWF(String[] out,Agent[] agents,Agent me, Agent op) {
		Agent[] all=Arrays.copyOf(agents, agents.length+1);
		all[all.length-1]=new Agent(me);
		outcome[] jp=Voting.BuildGroupOrder(this,out,all);
		Arrays.sort(jp, new Comparator<outcome>(){

			@Override
			public int compare(outcome arg0, outcome arg1) {
				// TODO Auto-generated method stub
				return (int) (arg0.getValue()-arg1.getValue());
			}

		});
		for (int j = 0; j < jp.length; j++) {
			double Vmem=-1;
			for (int i = 0; i < jp.length-j; i++) {
				if (Vmem==jp[i].getValue()){
					if (op.copyOutcome(jp[i-1].getName()).getValue()>op.copyOutcome(jp[i].getName()).getValue()){
						outcome mem=new outcome(jp[i-1]);
						jp[i-1]=new outcome(jp[i]);
						jp[i]=new outcome(mem);
					}
					else{
						break;
					}
				}

				Vmem=jp[i].getValue();

			}
		}
		String result=jp[0].getName();
		for (int i = 1; i < jp.length; i++) {
			result=result+"<"+jp[i];
		}
		return result;
	}

}
