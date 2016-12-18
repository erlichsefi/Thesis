package Voting;

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

}
