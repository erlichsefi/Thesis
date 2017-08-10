package Negotiation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import DrawTree.DecisionNode;
import tools.Agent;
import tools.Preferences;
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
			outcome o=FullInfoTurn(myAgnetCopt,otherAgentCopy,"",out[k],tree,true);

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
	public static outcome FullInfoTurn(Agent AgentGottenOffer,Agent AgentSentOffer,String path,String offer, DecisionNode FatherNode,boolean ToCutOff){

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
		else if (ToCutOff){
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

			outcome OtherAgentOutCome=FullInfoTurn(new Agent(AgentSentOffer),new Agent(AgentGottenOffer),path+move,entry,current,ToCutOff);

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




	public static ArrayList<String> WinningPrefrence(Agent otheragnet,boolean isotherstarting,String[] out,String goal){
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

		List<String> outlist=Arrays.asList(out);
		ArrayList<String> result=new ArrayList<String>();


		int P1lowerSizeAtStart=0;
		int GapInOtherLowers=0;
		int NumberOfTotalpossibleIntrasection=0;
		//compute the intersection values
		int LocationOfOtherInOther=Algo.indexOf(out, goal);
		if (isotherstarting){
			GapInOtherLowers=LocationOfOtherInOther-numberOfPassStarting;
			NumberOfTotalpossibleIntrasection=2*GapInOtherLowers;
			P1lowerSizeAtStart=numberOfPassWaiting;
		}
		else{
			GapInOtherLowers=LocationOfOtherInOther-numberOfPassWaiting;
			NumberOfTotalpossibleIntrasection=2*GapInOtherLowers+1;
			P1lowerSizeAtStart=numberOfPassStarting;

		}
		if (GapInOtherLowers<0){
			return new ArrayList<String>();
		}
		else{
			outcome[] OtherBelowGoal=otheragnet.CopyNworst(LocationOfOtherInOther);
			outcome[] OtherAboveGoaltemp=otheragnet.CopyNbest(out.length-LocationOfOtherInOther-1);

			ArrayList<String> OtherAboveGoal=new ArrayList<String>();
			for (int i = 0; i < OtherAboveGoaltemp.length; i++) {
				OtherAboveGoal.add(OtherAboveGoaltemp[i].getName());
			}

			int MinimumOutcomeBelowMyGoal=0;
			for (int i = 0; i <= NumberOfTotalpossibleIntrasection; i++) {
				if (!isotherstarting && i%2==1){
					MinimumOutcomeBelowMyGoal=(int) (P1lowerSizeAtStart+i/2+1);
				}
				else{
					MinimumOutcomeBelowMyGoal=(int) (P1lowerSizeAtStart+i/2);

				}

				ArrayList<ArrayList<String>> AllPossibleIntresection=Algo.AllSubGroupsWithOutReturn(OtherBelowGoal,i);
				ArrayList<String> BelowMyGoal=null;
				for (ArrayList<String> crruntIntersection:AllPossibleIntresection) {

					BelowMyGoal=new ArrayList<String>(crruntIntersection);
					BelowMyGoal.addAll(OtherAboveGoal);
					System.out.println("for below "+BelowMyGoal);

					if (MinimumOutcomeBelowMyGoal<=BelowMyGoal.size()){
						ArrayList<String> AllOrderOfBelowMyGoal=Algo.AllPossiblePrefrence(BelowMyGoal.toArray(new String[BelowMyGoal.size()]));
						//get reminder
						ArrayList<String> LeftToAboveMyGoal=new ArrayList<String>(outlist);
						//LeftToAboveMyGoal.remove(goal);
						LeftToAboveMyGoal.removeAll(BelowMyGoal);

						System.out.println("for above "+LeftToAboveMyGoal);

						ArrayList<String> AllOrderOfAboveMyGoal=Algo.AllPossiblePrefrence(LeftToAboveMyGoal.toArray(new String[LeftToAboveMyGoal.size()]));

						for (String currentAbove:AllOrderOfAboveMyGoal) {

							for (String CurrentBelow:AllOrderOfBelowMyGoal) {
								result.add(CurrentBelow+"<"+currentAbove);
								//System.out.println(below+"<"+goal+"<"+obove);
								if ("o1<o4<o3<o2".equals(CurrentBelow+"<"+currentAbove)){
									System.out.println();
								}
							}
						}
					}
				}
			}

			return result;
		}
	}


	public static int WinningPrefrenceV2(String p1,String p2,boolean isotherstarting,String[] out,String goal){
		String[] p1p=p1.split("<");
		String[] p2p=p2.split("<");

		for (int SGS = 2; SGS < out.length; SGS++) {
			int numberOfPassStarting;
			int numberOfPassWaiting;
			if (SGS%2==0){
				numberOfPassStarting=SGS/2-1;
				numberOfPassWaiting=SGS/2;
			}
			else{
				numberOfPassStarting=SGS/2;
				numberOfPassWaiting=SGS/2;
			}
			int numberOfoutcomeremoved=out.length-SGS;
			String[] p1l;
			String[] p2l;
			if (numberOfoutcomeremoved%2==0){
				p1l=Arrays.copyOfRange(p1p, out.length-SGS,  out.length-SGS+numberOfPassStarting);
				p2l=Arrays.copyOfRange(p2p, out.length-SGS,  out.length-SGS+numberOfPassWaiting);
			}
			else{
				p1l=Arrays.copyOfRange(p1p, out.length-SGS,  out.length-SGS+numberOfPassWaiting);
				p2l=Arrays.copyOfRange(p2p, out.length-SGS,  out.length-SGS+numberOfPassStarting);
			}
			//there is intersection
			if ( !ArrayContin(p1l,goal) || !ArrayContin(p2l,goal)){
				System.out.println("p1: "+Arrays.toString(Arrays.copyOfRange(p1p, out.length-SGS,  out.length)));
				System.out.println("p2  "+Arrays.toString(Arrays.copyOfRange(p2p, out.length-SGS,  out.length)));
				return  SGS;
			}
//			else if (ArrayContin(p1l,goal) || ArrayContin(p2l,goal)){
//				
//			}
//			else{
//				System.out.println("Do something here:");
//				System.out.println("p1: "+Arrays.toString(Arrays.copyOfRange(p1p, out.length-SGS,  out.length)));
//				System.out.println("p2  "+Arrays.toString(Arrays.copyOfRange(p2p, out.length-SGS,  out.length)));
//
//			}
		}
		return -1;

	}
	
	public static boolean ArrayContin(String[] arr,String elm){
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(elm)){
				return true;
			}
		}
		return false;
	}

	/**
	 * check if where is a path that reault with to EQ being in the Intersection
	 * @param AgentSentOffer the agent that gave the offer
	 * @param AgentGottenOffer the agent got the offer
	 * @param offer the offer name, if it's the root set to null
	 * @param EQ the EQ name
	 * @param Pl 0 to check for all,1 to P1, 2 to P2
	 * @param paths array to add all path to
	 * @return
	 */
	public static boolean FullInfoFindPathOutcomeInIntersection(Agent AgentGottenOffer,Agent AgentSentOffer,String path,String offer,String EQ,int Pl,ArrayList<String> paths){
		if (offer!=null){
			//remove the offer that gotten
			AgentSentOffer.RemoveOutcome(offer);
			//get my value for it
			AgentGottenOffer.RemoveOutcome(offer);
		}
		String[] out=outcomeLeft(AgentGottenOffer, AgentSentOffer);

		//compute the intersection values
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

		//get values from agents and intersect them
		outcome[] SAgentWorst=AgentGottenOffer.CopyNworst(numberOfPassStarting);
		outcome[] WAgentWorst=AgentSentOffer.CopyNworst(numberOfPassWaiting);
		ArrayList<outcome> el=IntesectGroup(SAgentWorst,WAgentWorst);

		boolean IsInLowers=false;
		for (outcome o:WAgentWorst) {
			if (o.getName().equals(EQ)){
				IsInLowers=true;
			}

		}
		for (outcome o:SAgentWorst) {
			if (o.getName().equals(EQ)){
				IsInLowers=true;
			}

		}
		if (IsInLowers){
			paths.add(path+"-> to "+EQ+" lowers");
			return true;
		}
		boolean R=false;
		// if there is not intersection
		if (el.size()==0){
			System.out.println(path);
			// looking in both agents lowers
			if (Pl==0){
				for (outcome o:el) {
					if (o.equals(EQ)){
						R= true;
					}
				}
			}
			// look only in player 1 lowers
			else if (Pl==1){
				if (AgentGottenOffer.getAgentName().equals("P1")){
					for (int i = 0; i < SAgentWorst.length; i++) {
						if (SAgentWorst[i].getName().equals(EQ)){
							R= true;
						}
					}
				}
				else{
					for (int i = 0; i < WAgentWorst.length; i++) {
						if (WAgentWorst[i].getName().equals(EQ)){
							R= true;
						}
					}
				}
				// look only in player 2 lowers
			}
			else if (Pl==2){{
				if (AgentSentOffer.getAgentName().equals("P2")){
					for (int i = 0; i < WAgentWorst.length; i++) {
						if (WAgentWorst[i].getName().equals(EQ)){
							R= true;
						}
					}
				}
				else{
					for (int i = 0; i < SAgentWorst.length; i++) {
						if (SAgentWorst[i].getName().equals(EQ)){
							R= true;
						}
					}
				}
			}
			}
			if (R){
				paths.add(path);

			}
			return R;

		}

		//build all possible outcomes

		boolean TotalResult=false;
		for (String entry : AgentGottenOffer.getOutComeOptions()) {
			if ( AgentSentOffer.copyOutcome(entry).getValue()<AgentSentOffer.copyOutcome(EQ).getValue()){
				String move=": "+ AgentGottenOffer.getAgentName()+" reject and offer "+entry;
				boolean OtherAgentOutCome=FullInfoFindPathOutcomeInIntersection(new Agent(AgentSentOffer),new Agent(AgentGottenOffer),path+move,entry,EQ,Pl,paths);
				if (OtherAgentOutCome){
					TotalResult=true;
				}
			}
		}
		return TotalResult;

	}




	/**
	 * find the EQ by computing the sub game perfect (Tree)
	 * @param out
	 * @param p2
	 * @param p1
	 * @return
	 */
	public static options FindBestByPrefernce(String[] out,Agent p2,Agent p1){
		outcome bestoutcome=null;
		String startingOffer=null;

		DecisionNode tree=new DecisionNode(p1.getAgentName(),"Start",out.length);

		for (int k = 0; k < out.length; k++) {
			Agent p2Copy=new Agent(p2);
			Agent p1Copy=new Agent(p1);
			String move=p1Copy.getAgentName()+"  offering "+out[k];
			outcome p2Outcome=FullInfoTurn(p2Copy,p1Copy,move,out[k],tree,true);

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
		tree.setResult(bestoutcome.getName());
		tree.initselected(bestoutcome.getName());
		tree.setPreprence(p1.getAgentName(),p2.getAgentName(),p1.getOriginalPrefrence(),p2.getOriginalPrefrence());

		options ans=new options(p2.getOriginalPrefrence(),startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
		ans.setTree(tree);
		return ans ;
	}


	/**
	 * find the EQ by computing the sub game perfect (Tree)
	 * @param out
	 * @param p2
	 * @param p1
	 * @return
	 */
	public static options FindBestByPrefernceNoCutOff(String[] out,Agent p2,Agent p1){
		outcome bestoutcome=null;
		String startingOffer=null;

		DecisionNode tree=new DecisionNode(p1.getAgentName(),"Start",out.length);

		for (int k = 0; k < out.length; k++) {
			Agent p2Copy=new Agent(p2);
			Agent p1Copy=new Agent(p1);
			String move=p1Copy.getAgentName()+"  offering "+out[k];
			outcome p2Outcome=FullInfoTurn(p2Copy,p1Copy,move,out[k],tree,false);

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
		tree.setResult(bestoutcome.getName());
		tree.initselected(bestoutcome.getName());
		tree.setPreprence(p1.getAgentName(),p2.getAgentName(),p1.getOriginalPrefrence(),p2.getOriginalPrefrence());

		options ans=new options(p2.getOriginalPrefrence(),startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
		ans.setTree(tree);
		return ans ;
	}

	/**
	 * finding the EQ by offering the intersection while there is , and then offering the lowest 
	 * of the other player.
	 * @param out
	 * @param Agent
	 * @param StartingAgent
	 * @return
	 */
	public static options FindBestByPrefernceTez(String[] out,Agent Agent,Agent StartingAgent){

		String UoinonRemoved="";
		//remove intersection
		String InterRemoved=RemoveIntesection(out,Agent,StartingAgent,"");

		//remove union
		if (InterRemoved.length()==0 || (InterRemoved.split(":").length)%2==0){
			UoinonRemoved=	RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
		}
		else{
			UoinonRemoved=RemoveUnionOneByOneFirstToLast(StartingAgent,Agent,"");
		}
		if (StartingAgent.numberOfOutcomeLeft()>1){
			System.exit(1);
		}		
		String p=InterRemoved;
		if (InterRemoved.length()>0) p=p+":";
		p=p+"L"+UoinonRemoved;

		return new options(null,null,StartingAgent.CopyBestOutcome().getName(),p);

	}

	/**
	 * finding the EQ by offering the intersection while there is , and then offering the lowest 
	 * of the other player.
	 * @param out
	 * @param Agent
	 * @param StartingAgent
	 * @return
	 */
	public static int NumberOfElementRemoveAtIntersection(String[] out,Agent Agent,Agent StartingAgent){

		//remove intersection
		String InterRemoved=RemoveIntesection(out,Agent,StartingAgent,"");

		return InterRemoved.split(":").length;

	}


	/**
	 * finding the EQ by offering an element is the intersection if after 
	 * it will cause one of the common Goals to join the lowers
	 * @param out
	 * @param Agent
	 * @param StartingAgent
	 * @return
	 */
	public static options FindBestByPrefernceTezVersion2(Agent Agent,Agent StartingAgent,String order){
		//Simulate the game with intersection 
		//the first agent is the starting one
		int n=Agent.NumberOfOfferLeft();
		if (n>1){
			int numberOfPassStarting;
			int numberOfPassWaiting;
			if (n%2==0){
				numberOfPassStarting=n/2-1;
				numberOfPassWaiting=n/2;
			}
			else{
				numberOfPassStarting=n/2;
				numberOfPassWaiting=n/2;
			}

			outcome[] joinGoalsP2=JoinGoals(Agent,StartingAgent,Agent.getAgentName());

			outcome[] lowersGoalStarting=StartingAgent.AllWorstFromWorstIn(joinGoalsP2);
			outcome[] lowersGoalWaiting=Agent.AllWorstFromWorstIn(joinGoalsP2);
			int InterSize=Fullinfo.IntesectGroup(lowersGoalStarting, lowersGoalWaiting).size();

			int range=(int) (InterSize-Math.floor(InterSize/2));

			outcome Removed=null;
			outcome[] RangeOutcomes=StartingAgent.copyOutcomeInRange(numberOfPassStarting+1, numberOfPassStarting+range);
			outcome[] RangeOutcomesP2=StartingAgent.copyOutcomeInRange(numberOfPassWaiting+1, numberOfPassWaiting+1);

			if (!Fullinfo.IntesectGroup(RangeOutcomes, joinGoalsP2).isEmpty()){
				ArrayList<outcome> I=Fullinfo.IntesectGroup(Agent.CopyNworst(numberOfPassWaiting), StartingAgent.CopyNworst(numberOfPassStarting));
				if (I.isEmpty()){
					RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
					return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
				}
				Removed=Agent.RemoveOutcome(I.get(0).getName());
				StartingAgent.RemoveOutcome(I.get(0).getName());
			}
			else if (!Fullinfo.IntesectGroup(RangeOutcomesP2, joinGoalsP2).isEmpty()){
				ArrayList<outcome> I=Fullinfo.IntesectGroup(Agent.CopyNworst(numberOfPassWaiting), StartingAgent.CopyNworst(numberOfPassStarting));
				if (I.isEmpty()){
					RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
					return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
				}
				Removed=Agent.RemoveOutcome(I.get(0).getName());
				StartingAgent.RemoveOutcome(I.get(0).getName());

			}
			else{
				outcome[] w=Agent.RemoveNworst(1);
				Removed=StartingAgent.RemoveOutcome(w[0].getName());
			}
			//	System.out.println();
			return FindBestByPrefernceTezVersion2(StartingAgent,Agent,order+":"+Removed.getName());
		}
		else{
			//System.out.println();
			return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);

		}

	}


	/**
	 * finding the EQ by offering an element is the intersection if after 
	 * it will cause one of the common Goals to join the lowers
	 * @param out
	 * @param Agent
	 * @param StartingAgent
	 * @return
	 */
	public static options FindBestByPrefernceTezVersion3(Agent Agent,Agent StartingAgent,String order){
		//Simulate the game with intersection 
		//the first agent is the starting one
		int n=Agent.NumberOfOfferLeft();
		outcome Removed=null;

		if (n>1){
			int numberOfPassStarting;
			int numberOfPassWaiting;
			if (n%2==0){
				numberOfPassStarting=n/2-1;
				numberOfPassWaiting=n/2;
			}
			else{
				numberOfPassStarting=n/2;
				numberOfPassWaiting=n/2;
			}
			if (IsThereComption(StartingAgent.getPrefrenceAboutCurrentOptions(),Agent.getPrefrenceAboutCurrentOptions())){
				ArrayList<outcome> I=Fullinfo.IntesectGroup(Agent.CopyNworst(numberOfPassWaiting), StartingAgent.CopyNworst(numberOfPassStarting));
				if (I.isEmpty()){
					RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
					return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
				}
				Removed=Agent.RemoveOutcome(I.get(0).getName());
				StartingAgent.RemoveOutcome(I.get(0).getName());
			}
			else {
				outcome[] w=Agent.RemoveNworst(1);
				Removed=StartingAgent.RemoveOutcome(w[0].getName());
			}
			//	System.out.println();
			return FindBestByPrefernceTezVersion3(StartingAgent,Agent,order+":"+Removed.getName());
		}
		else{
			//System.out.println();
			return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);

		}

	}


	/**
	 * 
	 * @param out
	 * @param Agent
	 * @param StartingAgent
	 * @return an array of the agents where the first agent is the starting one
	 */
	public static int IntersectionSize(String[] out,Agent Agent,Agent StartingAgent){
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
		return el.size();

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
			UoinonRemoved=	RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
		}
		else{
			UoinonRemoved=RemoveUnionOneByOneFirstToLast(StartingAgent,Agent,"");
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



	private static String RemoveUnionOneByOneFirstToLast(Agent Agent,Agent StartingAgent,String orderofremoval){
		Agent Temp;
		while (StartingAgent.getOutComeOptions().length>1){
			String[] out=outcomeLeft(Agent,StartingAgent);

			//System.out.println(StartingAgent);
			//	System.out.println(Agent);
			outcome[] a=Agent.CopyNworst(1);




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
			boolean IsInInter=false;
			for (int i = 0; i < el.size(); i++) {
				if (el.get(i).getName().equals(a[0].getName())){
					IsInInter=true;
				}
			}
			a=Agent.RemoveNworst(1);
			if(a.length!=1){
				System.err.println("something worng");
			}
			outcome a1=StartingAgent.RemoveOutcome(a[0].getName());

			if (!a[0].getName().equals(a1.getName())){
				System.err.println("!");
			}
			if (!IsInInter)
				orderofremoval=orderofremoval+a1.getName()+":";
			else{
				orderofremoval=orderofremoval+a1.getName()+"*:";

			}

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



	public static outcome[] JoinGoals(Agent Agent,Agent StartingAgent,String agentName){
		int numberOfPassStarting;
		int numberOfPassWaiting;
		int n=Agent.numberOfOutcomeLeft();
		if (n%2==0){
			numberOfPassStarting=n/2-1;
			numberOfPassWaiting=n/2;
		}
		else{
			numberOfPassStarting=n/2;
			numberOfPassWaiting=n/2;
		}
		outcome[] SAgentBest=StartingAgent.CopyNbest(n-numberOfPassStarting);
		outcome[] WAgentBest=Agent.CopyNbest(n-numberOfPassWaiting);
		ArrayList<outcome> up=null;
		if (Agent.getAgentName().equals(agentName)){
			up=Fullinfo.IntesectGroup(WAgentBest, SAgentBest);
		}
		else{
			up=Fullinfo.IntesectGroup(SAgentBest,WAgentBest);

		}
		outcome[] join=new outcome[up.size()];
		return up.toArray(join);
	}

	public static boolean IsFlip(Agent Agent,Agent StartingAgent,outcome[] o){
		outcome[] out2=JoinGoals(Agent, StartingAgent, Agent.getAgentName());
		outcome[] out1=JoinGoals(Agent, StartingAgent, StartingAgent.getAgentName());
		Arrays.sort(out1);
		Arrays.sort(out1);
		for (int i = 0; i < out1.length; i++) {
			if (!out1[i].getName().equals(out2[out2.length-1-i].getName())){
				return false;
			}
		}
		return true;

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



	/**
	 * return the intersection of the two groups,
	 * the elements are of the first array
	 * @param sAgentWorst
	 * @param wAgentWorst
	 * @return
	 */
	public static ArrayList<outcome> IntesectGroup(outcome[] sAgentWorst, outcome[] wAgentWorst) {
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


	public static boolean IsThereComption(String per1,String per2){
		Agent P1=new Agent("p1",per1);
		Agent P2=new Agent("p2",per2);
		String[] out=P1.getOutComeOptions();

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


		outcome[] joinGoalsP2=Fullinfo.JoinGoals(P2,P1,P1.getAgentName());
		outcome[] p1V=new outcome[joinGoalsP2.length];
		outcome[] p2V=new outcome[joinGoalsP2.length];
		for (int i = 0; i < p2V.length; i++) {
			int p1Index=Preferences.IndexOf(joinGoalsP2[i].getName(),per1);
			int p2Index=Preferences.IndexOf(joinGoalsP2[i].getName(),per2);
			p2V[i]=new outcome("StepToLowers",joinGoalsP2[i].getName(),(p2Index-(numberOfPassWaiting)));
			p1V[i]=new outcome("StepToLowers",joinGoalsP2[i].getName(),(p1Index-(numberOfPassStarting)));
		}
		Arrays.sort(p2V);
		Arrays.sort(p1V);
		int i1=p1V.length-1 ,i2=p2V.length-1;
		boolean bottel=false;
		while ( i1 >=0 &&  i2 >=0) {
			if (!p1V[i1].getName().equals(p2V[i2].getName()) && p1V[i1].getValue()==p2V[i2].getValue()){
				bottel=true;
				i2--;
			}
			else if (p1V[i1].getValue()>p1V[i2].getValue()){
				i2--;
			}
			else{
				i1--;
			}
		}
		return bottel;

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
