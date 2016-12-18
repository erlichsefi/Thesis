import java.util.ArrayList;
import java.util.Arrays;

import tools.Agent;
import tools.outcome;

public class Voting {
	VotingProtocol p=new VotingProtocol();
	Agent[] agent;
	
	/**
	 * @param agent
	 * @param manipulator
	 */
	public Voting(Agent[] agent, String outcome) {
		super();
		this.agent = agent.clone();
	}
	
	
	public String CanManipulte(String[] out,String outcomeWanted){
		String[] MyPre=new String[out.length];
		int k=out.length-1;
		String ResultPrefrnce=outcomeWanted;
		outcome[] Order=BuildGroupOrder(out,agent);
		MyPre[k]=outcomeWanted;
		ArrayList<outcome> ManipulatorPrefrnce=new ArrayList<outcome> ();
		outcome bestout=new outcome("mani",outcomeWanted,p.getScore(MyPre,k--));
		ManipulatorPrefrnce.add(bestout);
		
		ArrayList<String> UnasinddOut=new ArrayList<String> ();
		for (int i = 0; i < out.length; i++) {
			if (!outcomeWanted.equals(out[i]))
				UnasinddOut.add(out[i]);
		}
		
		boolean fail=false;
		int j=0;
		int numberOfFail=0;
		while (!fail && !UnasinddOut.isEmpty()){
			//define next value in preference order
			MyPre[k]=UnasinddOut.get(j);
			outcome tryToPlace=new outcome("mani",MyPre[k],p.getScore(MyPre,k));
			ManipulatorPrefrnce.add(tryToPlace);
			
			//compute with the new preference sum
			outcome[] NewOrder=SumArrayOutOfOrder(Order,ManipulatorPrefrnce);
			Arrays.sort(NewOrder);
			
			//if the wanted outcome is still the chosen
			if (!NewOrder[NewOrder.length-1].getName().equals(outcomeWanted)){
				
				ManipulatorPrefrnce.remove(ManipulatorPrefrnce.size()-1);
				numberOfFail++;
				if (numberOfFail==UnasinddOut.size()){
					fail=true;
				}
			}
			else{
				k--;
				numberOfFail=0;
				String added=UnasinddOut.remove(j);
				ResultPrefrnce=added+"<"+ResultPrefrnce;
				j=(j+1)%UnasinddOut.size();
				Order=NewOrder;
			}
		}
		
		if (fail) return null;
		return ResultPrefrnce;
		
	}
	
	
	


	private outcome[] BuildGroupOrder(String[] out,Agent[] agent2) {
		outcome[] sum=new outcome[out.length];
		for (int i = 0; i < agent2.length; i++) {
			outcome[] OutComeScore=p.getScore(agent2);
			sum=SumArray(sum,OutComeScore);
		}
		Arrays.sort(sum);
		return sum;
	}
	
	private outcome[] SumArray(outcome[] a, outcome[] b){
		outcome[] c = new outcome[a.length];
		for (int i = 0; i < a.length; ++i) {
		    c[i] =new outcome("group",a[i].getName(),a[i].getValue() + b[i].getValue());
		}
		return c;
	}
	
	private outcome[] SumArrayOutOfOrder(outcome[] dest, ArrayList<outcome>  source){
		outcome[] clone = dest.clone();
		for (int i = 0; i < clone.length; ++i) {
			for (int j = 0; j < source.size(); j++) {
				if (clone[i].getName().equals(source.get(i).getName())){
					clone[i]=new outcome("group",clone[i].getName(),clone[i].getValue() +source.get(i).getValue());
					break;
				}
			}
		}
		return clone;
	}
	
	

}
