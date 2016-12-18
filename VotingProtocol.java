
import tools.Agent;
import tools.outcome;

public class VotingProtocol {
	
	public VotingProtocol(){
		
	}
	 

	public outcome[] getScore(Agent[] agent2) {
		outcome[] answer = null;
		for (int i = 0; i < agent2.length; i++) {
			String[] out=agent2[i].getPrefrenceArray();
			outcome[] s=ValuePrefrence(out,agent2[i].getAgentName());
			
			if (answer==null){
				answer=s;
			}
			else{
				answer=SumOutcomes(answer,s);
			}
		}
		return answer;
	}




	final private outcome[] SumOutcomes(outcome[] a, outcome[] b) {
		outcome[] answer=new outcome[b.length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i].getName().equals(b[j].getName())){
					answer[i]=new outcome("group",a[i].getName(),a[i].getValue()+b[i].getValue());
					break;
				}
			}
		}
		return answer;
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
