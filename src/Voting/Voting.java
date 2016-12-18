package Voting;
import java.util.ArrayList;
import java.util.Arrays;

import tools.Agent;
import tools.outcome;

public class Voting {

	


	public static String CanManipulte(VotingProtocol p,Agent[] agent,String[] out,String outcomeWanted){
		String[] MyPreference=new String[out.length];
		//place c in the best location
		int k=out.length-1;
		MyPreference[k]=outcomeWanted;
        
		outcome[] Order=BuildGroupOrder(p,out,agent);
		ArrayList<outcome> ManipulatorPrefrnce=new ArrayList<outcome> ();
		outcome bestout=new outcome("mani",outcomeWanted,p.getScore(MyPreference,k--));
		ManipulatorPrefrnce.add(bestout);

		
		ArrayList<String> UnasinddOut=new ArrayList<String> ();
		for (int i = 0; i < out.length; i++) {
			if (!outcomeWanted.equals(out[i]))
				UnasinddOut.add(out[i]);
		}
	//	System.out.println(Arrays.toString(Order));

		boolean fail=false;
		int j=0;
		int numberOfFail=0;
		while (!fail && !UnasinddOut.isEmpty()){
			//define next value in preference order
			MyPreference[k]=UnasinddOut.get(j);
			outcome tryToPlace=new outcome("mani",MyPreference[k],p.getScore(MyPreference,k));
			ManipulatorPrefrnce.add(tryToPlace);

			//compute with the new preference sum
			outcome[] NewOrder=SumArrayOutOfOrder(Order,ManipulatorPrefrnce);
			Arrays.sort(NewOrder);
			//System.out.println(Arrays.toString(NewOrder));
			//System.out.println(Arrays.toString(MyPreference));
			//if the wanted outcome is still the chosen
			if (!NewOrder[0].getName().equals(outcomeWanted) && NewOrder[0].getValue()>NewOrder[1].getValue()){

				ManipulatorPrefrnce.remove(ManipulatorPrefrnce.size()-1);
				numberOfFail++;
				if (numberOfFail==UnasinddOut.size()){
					fail=true;
				}
			}
			else {
				k--;
				numberOfFail=0;
			    UnasinddOut.remove(j);
				
				if (!UnasinddOut.isEmpty())j=(j+1)%UnasinddOut.size();

			}
		}

		if (fail) return null;
		return ArrayToPrefrence(MyPreference);

	}





	private static String ArrayToPrefrence(String[] myPre) {
		String ans=myPre[0];
		for (int i = 1; i < myPre.length; i++) {
			ans=ans+"<"+myPre[i];
		}
		return ans;
	}





	final public static outcome[] BuildGroupOrder(VotingProtocol p,String[] out,Agent[] agent2) {
		outcome[] sum=null;
		for (int i = 0; i < agent2.length; i++) {
			outcome[] OutComeScore=p.getScore(agent2[i]);
			if (sum==null){
				sum=OutComeScore;
			}
			else{
				sum=SumOutcomes(sum,OutComeScore);
			}
		}
		Arrays.sort(sum);
		return sum;
	}
	
	public static String sortedOutcomeToPrefrnce(outcome[] a){
		String ans=a[a.length-1].getName();
		for (int i = a.length-2; i >= 0 ; i--) {
			ans=ans+"<"+a[i].getName();
		}
		return ans;
	}
	
	

	final private static  outcome[] SumOutcomes(outcome[] a, outcome[] b) {
		outcome[] answer=new outcome[b.length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i].getName().equals(b[j].getName())){
					answer[i]=new outcome("group",a[i].getName(),a[i].getValue()+b[j].getValue());
					break;
				}
			}
		}
		return answer;
	}

	private static  outcome[] SumArrayOutOfOrder(outcome[] dest, ArrayList<outcome>  source){
		outcome[] clone = dest.clone();
		for (int i = 0; i < clone.length; ++i) {
			for (int j = 0; j < source.size(); j++) {
				if (clone[i].getName().equals(source.get(j).getName())){
					clone[i]=new outcome("group",clone[i].getName(),clone[i].getValue() +source.get(j).getValue());
					break;
				}
			}
		}
		return clone;
	}



}
