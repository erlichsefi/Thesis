package Negotiation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import DrawTree.DecisionNode;
import junit.framework.Assert;
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
		DecisionNode tree=new DecisionNode(StartingAgent.getAgentName(),"Start",out.length);
		for (int k = 0; k < out.length; k++) {
			Agent myAgnetCopt=new Agent(Agent);
			Agent otherAgentCopy=new Agent(StartingAgent);
			outcome o=FullInfoTurn(myAgnetCopt,otherAgentCopy,"",out[k],tree);

			//set statistic
			int Resultindex=indexOf(out,o.getName());
			sta[k][Resultindex]++;

			if (o.getName().equals(WantedResult)){
				op.add(new options(StartingAgent.getOriginalPrefrence(),out[k],WantedResult)); 
			}

		}
		return op;

	}


	public static boolean IsBetterThenWantedInP2ShowInUpperP1(String P1,String P2,String Wanted,int OutLength){
		ArrayList<outcome> betterThenWantedP2=new Agent("P2",P2).OutComesBeterThen(Wanted);
		List<outcome> betterThenWantedP1=Arrays.asList(new Agent("P2",P1).CopyNbest((int)Math.floor(OutLength/2)-1));
		for (int j = 0; j < betterThenWantedP2.size(); j++) {
			for (int i = 0; i < betterThenWantedP1.size(); i++) {
				if (betterThenWantedP1.get(i).getName().equals(betterThenWantedP2.get(j).getName())){
					return true;
				}

			}

		}
		return false;

	}

	public static boolean IsBetterThenWantedInP2ShowInUpperP1Old(String P1,String P2,String Wanted,int OutLength){
		ArrayList<outcome> betterThenWantedP2=new Agent("P2",P2).OutComesBeterThen(Wanted);
		List<outcome> betterThenWantedP1=Arrays.asList(new Agent("P2",P1).CopyNbest((int)Math.floor(OutLength/2)-1));
		for (int j = 0; j < betterThenWantedP2.size(); j++) {
			for (int i = 0; i < betterThenWantedP1.size(); i++) {
				if (betterThenWantedP1.get(i).getName().equals(betterThenWantedP2.get(j).getName())){
					return true;
				}
			}

		}
		return false;

	}

	/**
	 * return the outcome of the nego if the other agent know my preference and the offer i'm start with offer
	 * @param AgentSentOffer the agent that gave the offer
	 * @param offer the offer name
	 * @param FatherNode 
	 * @return
	 */
	public static outcome FullInfoTurn(Agent AgentGottenOffer,Agent AgentSentOffer,String path,String offer, DecisionNode FatherNode){

		//remove the offer that gotten
		AgentSentOffer.RemoveOutcome(offer);
		//get my value for it
		outcome MyValueForOffer=AgentGottenOffer.RemoveOutcome(offer);

		// there is noting to offer
		if (!AgentGottenOffer.HasMoreOffers()){
			FatherNode.Accept(AgentGottenOffer.getAgentName(),offer,"last offer");
			MyValueForOffer.firstPath(path+":"+AgentGottenOffer.getAgentName()+" accepts ");
			return MyValueForOffer;
		}
		else{
			//there is no better thing to do 
			outcome next=AgentGottenOffer.CopyBestOutcome();
			if (next.getValue()<MyValueForOffer.getValue()){
				MyValueForOffer.firstPath(path+":"+AgentGottenOffer.getAgentName()+" accepts ");
				FatherNode.Accept(AgentGottenOffer.getAgentName(),offer,"better then: "+AgentGottenOffer.getPrefrenceAboutCurrentOptions());
				return MyValueForOffer;
			}
		}

		DecisionNode current=new DecisionNode(AgentGottenOffer.getAgentName(),offer,AgentGottenOffer.NumberOfOfferLeft());
		current.AddTooption(new DecisionNode(AgentGottenOffer.getAgentName(),offer) );
		//build all possible outcomes
		outcome[] PossibleOut=new outcome[AgentGottenOffer.NumberOfOfferLeft()];
		int j=0;
		for (String entry : AgentGottenOffer.getOutComeOptions()) {
			String move=": "+ AgentGottenOffer.getAgentName()+" reject and offer "+entry;

			outcome OtherAgentOutCome=FullInfoTurn(new Agent(AgentSentOffer),new Agent(AgentGottenOffer),path+move,entry,current);

			if (!OtherAgentOutCome.getPlayer().equals(AgentSentOffer.getAgentName())){
				throw new IllegalArgumentException("worng player.");
			}

			current.addResultToLeaf(j+1,OtherAgentOutCome.getName());

			PossibleOut[j]=AgentGottenOffer.copyOutcome(OtherAgentOutCome.getName());
			PossibleOut[j++].InitPath(OtherAgentOutCome.getPathToout());
		}


		//find better outcome from current
		for (int i = 0; i < PossibleOut.length; i++) {
			//if it's better 
			if (PossibleOut[i].getValue()>MyValueForOffer.getValue()){
				MyValueForOffer=new outcome(PossibleOut[i]);
				//current.initselected(PossibleOut[i].getName());
			}else if (PossibleOut[i].getValue()==MyValueForOffer.getValue()){
				MyValueForOffer.addPath(PossibleOut[i].getPathToout());
				//current.addselected(PossibleOut[i].getName());
			}
		}

		FatherNode.AddTooption(current);
		current.initselected(MyValueForOffer.getName());
		return MyValueForOffer;
	}


	public static options FindBestByPrefernceTezFunction(String[] out,Agent Agent,Agent StartingAgent){
		Agent otherC=new Agent(Agent);
		Agent staringC=new Agent(StartingAgent);
		outcome[] o=new outcome[out.length];
		Map<String,outcome> o2=new HashMap<String,outcome>();

		int i=0;
		while (Agent.HasMoreOffers()){
			outcome a1=Agent.RemoveBestOutcome();
			outcome a2=StartingAgent.RemoveOutcome(a1.getName());
			o2.put(a1.getName(),new outcome("both",a1.getName(),(out.length-(1.1)*Math.abs(a1.getValue()-a2.getValue()))));
			o[i++]=new outcome("both",a1.getName(),a1.getValue()+a2.getValue());
		}
		Arrays.sort(o);
		ArrayList<outcome> a=new ArrayList<outcome>();
		double max=o[0].getValue();
		for (int j = 0; j < o.length; j++) {
			if (o[j].getValue()==max){
				a.add(o2.get(o[j].getName()));
			}
		}
		a.sort(null);
		max=a.get(0).getValue();
		ArrayList<outcome> diff=new ArrayList<outcome>();
		for (int j = 0; j <a.size(); j++) {
			if (a.get(j).getValue()==max){
				diff.add(a.get(j));
			}
		}
		if (a.size()==1)
			return new options("","",a.get(0).getName(),"");
		else{
			outcome o1;
			outcome o3;
			if ((out.length-2)%2==0){
				o1=staringC.copyOutcome(a.get(0).getName());
				o3=staringC.copyOutcome(a.get(1).getName());

			}
			else{
				o1=otherC.copyOutcome(a.get(0).getName());
				o3=otherC.copyOutcome(a.get(1).getName());
			}
			if (o1.getValue()<o3.getValue()){
				return new options("","",o1.getName(),"");
			}
			else{
				return new options("","",o3.getName(),"");

			}

		}

	}




	public static options FindBestByPrefernce(String[] out,Agent p2,Agent p1){
		outcome bestoutcome=null;
		String startingOffer=null;

		DecisionNode tree=new DecisionNode(p1.getAgentName(),"Start",out.length);
		tree.setPreprence(p1.getAgentName(),p2.getAgentName(),p1.getOriginalPrefrence(),p2.getOriginalPrefrence());

		for (int k = 0; k < out.length; k++) {
			Agent p2Copy=new Agent(p2);
			Agent p1Copy=new Agent(p1);
			String move=p1Copy.getAgentName()+"  offering "+out[k];
			outcome p2Outcome=FullInfoTurn(p2Copy,p1Copy,move,out[k],tree);

			outcome p1Outcome=p1.copyOutcome(p2Outcome.getName());
			
			p1Outcome.InitPath(p2Outcome.getPathToout());
			tree.addResultToLeaf(k,p2Outcome.getName());

			if (bestoutcome==null){
				bestoutcome=new outcome(p1Outcome);
				startingOffer=out[k];
				//tree.initselected(out[k]);
			}
			else if (p1Outcome.getValue()>bestoutcome.getValue()){
				bestoutcome=new outcome(p1Outcome);
				startingOffer=out[k];
				//tree.initselected(out[k]);
			}
			else if (p1Outcome.getValue()==bestoutcome.getValue()){
				bestoutcome.addPath(p1Outcome.getPathToout());
				//tree.addselected(out[k]);	

			}
		}
		tree.initselected(bestoutcome.getName());

		options ans=new options(p2.getOriginalPrefrence(),startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
		ans.setTree(tree);
		return ans ;
	}

	public static options FindBestByPrefernceTez(String[] out,Agent Agent,Agent StartingAgent){

		String UoinonRemoved="";
		//remove intersection
		String InterRemoved=RemoveIntesection(out,Agent,StartingAgent,"");

		//remove union
		if (InterRemoved.length()==0 || (InterRemoved.split(":").length)%2==0){
			UoinonRemoved=	RemoveUnionOneByOneFirstToLast(outcomeLeft(Agent,StartingAgent),Agent,StartingAgent,"");
		}
		else{
			UoinonRemoved=RemoveUnionOneByOneFirstToLast(outcomeLeft(Agent,StartingAgent),StartingAgent,Agent,"");
		}
		if (StartingAgent.numberOfOutcomeLeft()>1){
			System.exit(1);
		}		
		String p=InterRemoved;
		if (InterRemoved.length()>0) p=p+":";
		p=p+UoinonRemoved;
		return new options(null,null,StartingAgent.CopyBestOutcome().getName(),p);

	}
	/**
	 * 
	 * @param out
	 * @param Agent
	 * @param StartingAgent
	 * @return an array of the agents where the first agent is the starting one
	 */
	public static Agent[] afterIntersection(String[] out,Agent Agent,Agent StartingAgent){
		String InterRemoved=RemoveIntesection(out,Agent,StartingAgent,"");
		if (InterRemoved.length()==0 || (InterRemoved.split(":").length)%2==0){
			return new Agent[]{Agent,StartingAgent};
		}
		else{
			return new Agent[]{StartingAgent,Agent};
		}		
	}
	
	/**
	 * 
	 * @param Agent
	 * @param StartingAgent
	 * @return an array of the agents where the first agent is the starting one
	 */
	public static Agent[] afterUnionOneByOneFirstToLast(Agent Agent,Agent StartingAgent){
		Agent Temp;

		while (StartingAgent.getOutComeOptions().length>2){
			outcome[] a=Agent.RemoveNworst(1);
			if(a.length!=1){
				System.err.println("something worng");
			}
			outcome a1=StartingAgent.RemoveOutcome(a[0].getName());

			if (!a[0].getName().equals(a1.getName())){
				System.err.println("!");
			}
			Temp=StartingAgent;
			StartingAgent=Agent;
			Agent=Temp;
		}
		return new Agent[]{StartingAgent,Agent};
	}
	
	
	public static options FindBestByPrefernceTezNoIntersection(String[] out,Agent Agent,Agent StartingAgent){
		String UoinonRemoved="";

		//remove intersection
		String InterRemoved="";
		//remove union
		if (InterRemoved.length()==0 || (InterRemoved.split(":").length)%2==0){
			UoinonRemoved=	RemoveUnionOneByOneFirstToLast(outcomeLeft(Agent,StartingAgent),Agent,StartingAgent,"");
		}
		else{
			UoinonRemoved=RemoveUnionOneByOneFirstToLast(outcomeLeft(Agent,StartingAgent),StartingAgent,Agent,"");
		}
		if (StartingAgent.numberOfOutcomeLeft()>1){
			System.exit(1);
		}		
		String p=InterRemoved;
		if (InterRemoved.length()>0) p=p+":";
		p=p+UoinonRemoved;
		return new options(null,null,StartingAgent.CopyBestOutcome().getName(),p);

	}
	public static boolean IsReplaceingOutcomeWorks(String[] out,Agent Agent,Agent StartingAgent,String toRemoved){	
		options LastOut=FindBestByPrefernceTez(out,Agent,StartingAgent);
		String newAgent=Agent.getOriginalPrefrence().replace("<"+toRemoved,"").replace("<"+toRemoved+"<", "<").replace(toRemoved+"<", "");
		String newStartingAgent=StartingAgent.getOriginalPrefrence().replace("<"+toRemoved,"").replace("<"+toRemoved+"<", "<").replace(toRemoved+"<", "");
		options NewOut=FindBestByPrefernce(out,new Agent(Agent.getAgentName(),toRemoved+"<"+newAgent),new Agent(StartingAgent.getAgentName(),toRemoved+"<"+newStartingAgent));
		String prev=LastOut.longestPaths()+LastOut.getResult()+":";
		System.out.println();
		System.out.println("TRID TO REPLACE : "+toRemoved);
		System.out.println("** old goal:"+ LastOut.getResult()+" **");

		System.out.println("old path:"+ prev);
		System.out.println("*** new goal:"+ NewOut.getResult()+" **");
		System.out.println("all paths:"+ NewOut.getOrders());

		if (NewOut.getPathOf(prev)==null){			
			System.out.println("X");
			return false;
		}
		System.out.println("V");
		return true;
	}

	private static String RemoveUnionOneByOneFirstToLast(String[] out,Agent Agent,Agent StartingAgent,String orderofremoval){
		Agent Temp;

		while (StartingAgent.getOutComeOptions().length>1){
			outcome[] a=Agent.RemoveNworst(1);
			if(a.length!=1){
				System.err.println("something worng");
			}
			outcome a1=StartingAgent.RemoveOutcome(a[0].getName());

			if (!a[0].getName().equals(a1.getName())){
				System.err.println("!");
			}
			orderofremoval=orderofremoval+a1.getName()+":";

			Temp=StartingAgent;
			StartingAgent=Agent;
			Agent=Temp;
		}
		return orderofremoval;
	}



	private static String RemoveIntesection(String[] out,Agent Agent,Agent StartingAgent,String orderOfremovale){
		int numberOfPassStarting;
		int numberOfPassWaiting;
		if (out.length%2==0){
			numberOfPassStarting=out.length/2-1;
			numberOfPassWaiting=out.length/2;
		}
		else{
			numberOfPassStarting=out.length/2;
			numberOfPassWaiting=out.length/2;
		}

		outcome[] SAgentWorst=StartingAgent.CopyNworst(numberOfPassStarting);
		outcome[] WAgentWorst=Agent.CopyNworst(numberOfPassWaiting);
		ArrayList<outcome> el=IntesectGroup(SAgentWorst,WAgentWorst);
		for (outcome o: el){
			StartingAgent.RemoveOutcome(o.getName());
			Agent.RemoveOutcome(o.getName());
			orderOfremovale=orderOfremovale+o.getName()+":";
		}
		int SizeOfHighers=el.size()+1;

		ArrayList<outcome> el2=IntesectGroup(StartingAgent.getOutComeOptionsOut(),Agent.getOutComeOptionsOut());
		boolean Samerange=true;
		if (el2.size()<=SizeOfHighers){

		}
		String[] unionArray = outcomeLeft(Agent,StartingAgent); 


		if (!el.isEmpty()){
			if (el.size()%2==0){
				return RemoveIntesection(unionArray,Agent,StartingAgent,orderOfremovale);
			}
			else{
				return RemoveIntesection(unionArray,StartingAgent,Agent,orderOfremovale);
			}
		}
		else{
			return (orderOfremovale.length()>0)? orderOfremovale.substring(0,orderOfremovale.length()-1):orderOfremovale;
		}

	}






	private static int IntesectCount(outcome[] sAgentWorst, outcome[] wAgentWorst) {
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


	private static ArrayList<outcome> IntesectGroup(outcome[] sAgentWorst, outcome[] wAgentWorst) {
		ArrayList<outcome> res=new ArrayList<outcome>();
		for (int i = 0; i < sAgentWorst.length; i++) {
			for (int j = 0; j < wAgentWorst.length; j++) {
				if (sAgentWorst[i].getName().equals(wAgentWorst[j].getName())){
					res.add(sAgentWorst[i]);
					break;
				}
			}
		}
		return res;
	}





	private static String[] outcomeLeft(Agent a1,Agent a2){
		String[] o=a1.getOutComeOptions();
		String[] o2=a2.getOutComeOptions();

		//push the arrays in the list.
		ArrayList<String> list1 = new ArrayList<String>(Arrays.asList(o));
		ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(o2));

		HashSet <String> set = new HashSet <String>();

		//add the lists in the set.
		set.addAll(list1);
		set.addAll(list2);

		//convert it back to array.
		return set.toArray(new String[0]);  
	}

}
