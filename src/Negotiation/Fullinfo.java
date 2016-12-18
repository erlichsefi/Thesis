package Negotiation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import tools.Agent;
import tools.options;
import tools.outcome;

public class Fullinfo extends Algo{
	static int[][] sta=null;



	/**
	 * a game when both of the agents know the prefrences
	 * @param out
	 * @param startingagent
	 * @param otheragnet
	 * @param offerAgentWhat
	 * @return
	 */
	public static ArrayList<options> FullInfoGame(String[] out,Agent otheragnet,boolean IsOtherAgnetStarting,String offerAgentWhat){
		sta=new int[out.length][out.length];

		ArrayList<options> op=new ArrayList<options>();
		ArrayList<String> client1prefernce=AllPossiblePrefrence(out);

		for (int i = 0; i < client1prefernce.size(); i++) {
			System.out.println(i+1+"/"+client1prefernce.size());
			String prefrence=client1prefernce.get(i);
			Agent myAgnet=new Agent("myagnet",prefrence);
			Agent otherAgent=new Agent(otheragnet);

			options o=null;
			if (IsOtherAgnetStarting){
				o=FindBestByPrefernce(out,myAgnet,otherAgent);
				int Startingindex=indexOf(out,o.getStartingOffer());
				int Resultingindex=indexOf(out,o.getResult());
				sta[Startingindex][Resultingindex]++;

				if (o.getResult().equals(offerAgentWhat)){
					op.add(new options(prefrence,o.getStartingOffer(),o.getResult()));
				}

			}else{
				ArrayList<options> o1=FindWantedOutcome(out,otherAgent,myAgnet,offerAgentWhat);
				op.addAll(o1);
			}




		}
		return op;
	}

	public static ArrayList<options> FindWantedOutcome(String[] out,Agent Agent,Agent StartingAgent,String WantedResult ){
		ArrayList<options> op=new ArrayList<options>();
		for (int k = 0; k < out.length; k++) {
			Agent myAgnetCopt=new Agent(Agent);
			Agent otherAgentCopy=new Agent(StartingAgent);
			outcome o=myAgnetCopt.FullInfoTurn(otherAgentCopy,"",out[k]);

			//set statistic
			int Resultindex=indexOf(out,o.getName());
			sta[k][Resultindex]++;

			if (o.getName().equals(WantedResult)){
				op.add(new options(StartingAgent.getPrefrence(),out[k],WantedResult)); 
			}

		}
		return op;

	}

	public static options FindBestByPrefernce(String[] out,Agent Agent,Agent StartingAgent){
		outcome bestoutcome=null;
		String startingOffer=null;
		for (int k = 0; k < out.length; k++) {
			Agent myAgnetCopt=new Agent(Agent);
			Agent otherAgentCopy=new Agent(StartingAgent);
			outcome outcomeName=new Agent(myAgnetCopt).FullInfoTurn(new Agent(otherAgentCopy),otherAgentCopy.getAgentName()+"  offering "+out[k],out[k]);
			outcome o=otherAgentCopy.copyOutcome(outcomeName.getName());
			if (bestoutcome==null){
				bestoutcome=new outcome(o);
				startingOffer=out[k];
			}
			else if (o.getValue()>bestoutcome.getValue()){
				bestoutcome=new outcome(o);
				startingOffer=out[k];

			}
		}

		return new options(Agent.getPrefrence(),startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
	}

	public static options FindBestByPrefernceTez(String[] out,Agent Agent,Agent StartingAgent){
		int numberOfPassStarting;
		int numberOfPassWaiting;
		boolean even=false;
		if (out.length%2==0){
			even=true;
			numberOfPassStarting=out.length/2-1;
			numberOfPassWaiting=out.length/2;
		}
		else{
			numberOfPassStarting=out.length/2;
			numberOfPassWaiting=out.length/2;
		}

		outcome[] SAgentWorst=StartingAgent.RemoveNworst(numberOfPassStarting);
		outcome[] WAgentWorst=Agent.RemoveNworst(numberOfPassWaiting);

		int sameRange=0;
		for (int i = 0; i < SAgentWorst.length; i++) {
			if (SAgentWorst[i].getName().equals(WAgentWorst[i].getName())){
				sameRange++;
			}
		}

		int eliminteCount=numberOfPassStarting+numberOfPassWaiting;
		int intresect=Intesect(SAgentWorst,WAgentWorst);
		int uonion=(numberOfPassWaiting+numberOfPassStarting-2*intresect);
		int extra=0;

		if (even){
			outcome startingNextBad=StartingAgent.CopyWorstOutcome();
			if (IsInlist(startingNextBad,WAgentWorst)){
				extra++;
			}
		}
		else{

		}

		for (int i = 0; i < WAgentWorst.length; i++) {
			StartingAgent.RemoveOutcome(WAgentWorst[i].getName());
		}
//	
		for (int i = 0; i < SAgentWorst.length; i++) {
			Agent.RemoveOutcome(SAgentWorst[i].getName());
		}
		
		System.out.println("##########");
		System.out.println("eliminteCount: "+eliminteCount);
		System.out.println("extra: "+extra);
		System.out.println("intresect :"+intresect);
		System.out.println("sameRange: "+sameRange);
		int turnPass=(eliminteCount-intresect-extra);


		

		String[] o=Agent.getOutComeOptions();
		String[] o2=StartingAgent.getOutComeOptions();

		//push the arrays in the list.
		ArrayList<String> list1 = new ArrayList<String>(Arrays.asList(o));
		ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(o2));

		HashSet <String> set = new HashSet <String>();

		//add the lists in the set.
		set.addAll(list1);
		set.addAll(list2);

		//convert it back to array.
		String[] unionArray = set.toArray(new String[0]);  
		if (unionArray.length>2){
			if (turnPass%2==0){
				return FindBestByPrefernceTez(unionArray,new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()),new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()));
			}
			else{
				return FindBestByPrefernceTez(unionArray,new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()),new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()));

			}
		}
		else{
			//return unionArray;
			if (turnPass%2==0){
				return FindBestByPrefernce(unionArray,new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()),new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()));
			}
			else{
				return FindBestByPrefernce(unionArray,new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()),new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()));

			}
			//return FindBestByPrefernce(unionArray,new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()),new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()));
			//	return unionArray;
			//return new options(Agent.getPrefrence(),startingOffer,bestoutcome.getName());
		}
	}




	private static int Intesect(outcome[] sAgentWorst, outcome[] wAgentWorst) {
		int count=0;
		for (int i = 0; i < sAgentWorst.length; i++) {
			for (int j = 0; j < wAgentWorst.length; j++) {
				if (sAgentWorst[i].getName().equals(wAgentWorst[j].getName())){
					count++;
					break;
				}
			}
		}
		return count;
	}

	private static boolean IsInlist(outcome lastLeft, outcome[] wAgentWorst) {
		for (int i = 0; i < wAgentWorst.length; i++) {
			if (wAgentWorst[i].getName().equals(lastLeft.getName())){
				return true;
			}
		}
		return false;
	}

	public static int[][] getStatistic(){
		return sta;
	}


}
