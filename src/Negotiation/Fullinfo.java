package Negotiation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import tools.Agent;
import tools.TNode;
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
		TNode tree=new TNode(StartingAgent.getAgentName(),"Start",out.length);
		for (int k = 0; k < out.length; k++) {
			Agent myAgnetCopt=new Agent(Agent);
			Agent otherAgentCopy=new Agent(StartingAgent);
			outcome o=myAgnetCopt.FullInfoTurn(otherAgentCopy,"",out[k],tree);

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
		TNode tree=new TNode(StartingAgent.getAgentName(),"Start",out.length);
		for (int k = 0; k < out.length; k++) {
			Agent myAgnetCopt=new Agent(Agent);
			Agent otherAgentCopy=new Agent(StartingAgent);
			outcome outcomeName=new Agent(myAgnetCopt).FullInfoTurn(new Agent(otherAgentCopy),otherAgentCopy.getAgentName()+"  offering "+out[k],out[k],tree);
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
		tree.Setselected(bestoutcome.getName());
		tree.print();
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
		int turnPass=-1;
		int eliminteCount=numberOfPassStarting+numberOfPassWaiting;
		int intresect=Intesect(SAgentWorst,WAgentWorst);
		int uonion=(numberOfPassWaiting+numberOfPassStarting-2*intresect);

		if (even){
			outcome startingNextBad=StartingAgent.CopyWorstOutcome();
			if (IsInlist(startingNextBad,WAgentWorst)){
				eliminteCount++;
			}
			turnPass=(eliminteCount);
		}
		else{
			turnPass=(eliminteCount-intresect);

		}

		for (int i = 0; i < WAgentWorst.length; i++) {
			StartingAgent.RemoveOutcome(WAgentWorst[i].getName());
		}
		//	
		for (int i = 0; i < SAgentWorst.length; i++) {
			Agent.RemoveOutcome(SAgentWorst[i].getName());
		}

		//		System.out.println("##########");
		//		System.out.println("eliminteCount: "+eliminteCount);
		//		System.out.println("intresect :"+intresect);
		//		System.out.println("sameRange: "+sameRange);




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

	public static String[] FindBestByPrefernceTezTwo(String[] out,Agent Agent,Agent StartingAgent){
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
		int turnPass=-1;
		int eliminteCount=numberOfPassStarting+numberOfPassWaiting;
		int intresect=Intesect(SAgentWorst,WAgentWorst);
		int uonion=(numberOfPassWaiting+numberOfPassStarting-2*intresect);

		if (even){
			outcome startingNextBad=StartingAgent.CopyWorstOutcome();
			if (IsInlist(startingNextBad,WAgentWorst)){
				eliminteCount++;
			}
			turnPass=(eliminteCount);
		}
		else{
			turnPass=(eliminteCount-intresect);

		}

		for (int i = 0; i < WAgentWorst.length; i++) {
			StartingAgent.RemoveOutcome(WAgentWorst[i].getName());
		}
		for (int i = 0; i < SAgentWorst.length; i++) {
			Agent.RemoveOutcome(SAgentWorst[i].getName());
		}


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
				return FindBestByPrefernceTezTwo(unionArray,new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()),new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()));
			}
			else{
				return FindBestByPrefernceTezTwo(unionArray,new Agent(StartingAgent.getAgentName(),StartingAgent.getPrefrenceAboutCurrentOptions()),new Agent(Agent.getAgentName(),Agent.getPrefrenceAboutCurrentOptions()));

			}
		}
		else{
			return unionArray;

		}
	}


	public static options FindBestByPrefernceTez2(String[] out,Agent Agent,Agent StartingAgent){
		Agent otherC=new Agent(StartingAgent);
		outcome[] o=new outcome[out.length];
		Map<String,outcome> o2=new HashMap<String,outcome> ();

		int i=0;
		while (Agent.HasMoreOffers()){
			outcome a1=Agent.RemoveBestOutcome();
			outcome a2=StartingAgent.RemoveOutcome(a1.getName());
			o2.put(a1.getName(), new outcome("both",a1.getName(),Math.abs(a1.getValue()-a2.getValue())));
			o[i++]=new outcome("both",a1.getName(),a1.getValue()*a2.getValue());
		}
		Arrays.sort(o);

		if (o[0].getValue()!=o[1].getValue()){
			return new options(null,null,o[0].getName());
		}
		else{
			ArrayList<outcome> t=new ArrayList<outcome> ();
			int m=0;
			while (m<o.length && o[m].getValue()==o[0].getValue()){
				t.add(o2.get(o[m].getName()));
				m++;
			}

			t.sort(null);
			if (!(t.size()>1 && t.get(0).getValue()==t.get(1).getValue())){
				return new options(null,null,t.get(0).getName());
			}
			else{
				if (otherC.RemoveOutcome( t.get(0).getName()).getValue()>otherC.RemoveOutcome( t.get(1).getName()).getValue()){
					return new options(null,null,t.get(0).getName());
				}
				else{
					return new options(null,null,t.get(1).getName());

				}
			
			
			}



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
