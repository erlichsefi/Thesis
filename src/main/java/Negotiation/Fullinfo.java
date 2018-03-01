package Negotiation;
import java.lang.reflect.Array;
import java.util.*;

import DrawTree.DecisionNode;
import tools.*;

public class Fullinfo extends Algo{
	static int[][] sta=null;


	/**
	 * a game when both of the agents know the prefrences
	 * @param out
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
			ArrayList<Agent> others=new ArrayList<>();
			Agent myAgnet=new Agent("myagnet",prefrence);
			Agent otherAgent=new Agent(otheragnet);

			options o=null;
			if (IsOtherAgnetStarting){
				others.add(myAgnet);
				o=FindBestByPrefernce(out,others,otherAgent);
				int Startingindex=indexOf(out,o.getStartingOffer());
				int Resultingindex=indexOf(out,o.getResult());
				sta[Startingindex][Resultingindex]++;

				if (o.getResult().equals(offerAgentWhat)){
					op.add(new options(prefrence,o.getStartingOffer(),o.getResult()));
				}

			}else{
				others.add(otherAgent);
				ArrayList<options> o1=FindWantedOutcome(out,others,myAgnet,offerAgentWhat);
				op.addAll(o1);
			}




		}
		return op;
	}

	public static ArrayList<options> FindWantedOutcome(String[] out,ArrayList<Agent> Agent,Agent StartingAgent,String WantedResult ){
		ArrayList<options> op=new ArrayList<options>();
		DecisionNode tree=new DecisionNode(StartingAgent.getAgentName(),"Start",out.length);
		for (int k = 0; k < out.length; k++) {
			ArrayList<Agent> myAgnetCopt=new ArrayList<>(Agent);
			Agent otherAgentCopy=new Agent(StartingAgent);
			Path move=new Path();
			move.RejactAndOffer(out[k],otherAgentCopy.getAgentName());
			outcome o=FullInfoTurn(myAgnetCopt,otherAgentCopy,move,out[k],tree,true);

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
	public static outcome FullInfoTurn(ArrayList<Agent> AgentsGottenOffer2, Agent AgentSentOffer, Path path, String offer, DecisionNode FatherNode, boolean ToCutOff){
		ArrayList<Agent> AgentsGottenOffer=deepArrayList(AgentsGottenOffer2);
		//remove the offer that gotten
		AgentSentOffer.RemoveOutcome(offer);
		//get all agents my value for the offer
		outcome[] MyValueForOffer=new outcome[AgentsGottenOffer.size()];
		for(int p=0; p < MyValueForOffer.length;p++) {
			MyValueForOffer[p] = AgentsGottenOffer.get(p).RemoveOutcome(offer);
		}

		// there is noting to offer
		if (!AgentsGottenOffer.get(0).HasMoreOffers()){
			//FatherNode.Accept("all",offer,"last offer");
			Path path2=new Path(path);
			path2.Accept("all","last offer");
			MyValueForOffer[0].firstPath(path2);
			return MyValueForOffer[0];
		}
		else if (ToCutOff){
			// if all accept because they offered the best
			boolean allAccept=true;
			//there is no better thing to do
			for(int p=0; p < AgentsGottenOffer.size();p++) {
				outcome next = AgentsGottenOffer.get(p).CopyBestOutcome();
				if (next.getValue() > MyValueForOffer[p].getValue()) {
					allAccept=false;
				}
			}

			if (allAccept){
				Path path2 = new Path(path);
				path2.Accept("all", "better then: ");
//				FatherNode.Accept("all", offer, "better then: ") ;
				MyValueForOffer[0].firstPath(path2);
				return MyValueForOffer[0];
			}
		}

		DecisionNode current=new DecisionNode("all",offer,AgentsGottenOffer.get(0).NumberOfOfferLeft());
		current.AddTooption(new DecisionNode("all",offer) );
		// we need to calculate the other steps for all other players
		outcome[][] PossibleOut=new outcome[AgentsGottenOffer.size()][AgentsGottenOffer.get(0).NumberOfOfferLeft()];
		for(int p=0; p < AgentsGottenOffer.size();p++) {
			int o=0;
			for (String outcomeToOffer : AgentsGottenOffer.get(p).getOutComeOptions()) {
				// setting up the env for the recursion
				Path path2 = new Path(path);

				path2.RejactAndOffer(outcomeToOffer, AgentsGottenOffer.get(p).getAgentName());

				Agent new_p1=new Agent(AgentsGottenOffer.get(0));
				new_p1.RemoveOutcome(outcomeToOffer);
				ArrayList<Agent> new_to= deepArrayList(AgentsGottenOffer);
				new_to.remove(0);
				new_to.add(new Agent(AgentSentOffer));
				// if player p offer outcome o
				outcome OtherAgentOutCome = FullInfoTurn(new_to, new_p1, path2, outcomeToOffer, current, ToCutOff);

				//	current.addResultToLeaf(o+1,OtherAgentOutCome.getName());

				PossibleOut[p][o] = AgentsGottenOffer.get(p).copyOutcome(OtherAgentOutCome.getName());
				PossibleOut[p][o].InitPath(OtherAgentOutCome.getPathToout());
				o++;
			}
		}


		boolean SomeoneWillReject=false;
		ArrayList<String> rejects=new ArrayList<>();

		outcome[] maxs=new outcome[AgentsGottenOffer.size()];
		int[] maxs_id=new int[AgentsGottenOffer.size()];
		//find better outcome from current
		for(int p=0; p < AgentsGottenOffer.size();p++) {
			for (int o = 0; o < PossibleOut[p].length; o++) {
				if (maxs[p]==null || maxs[p].getValue()<PossibleOut[p][o].getValue()){
					maxs[p]=new outcome(PossibleOut[p][o]);
				}
				//if some one will reject
				if (PossibleOut[p][o].getValue() > MyValueForOffer[p].getValue()) {
					SomeoneWillReject = true;
					rejects.add(AgentsGottenOffer.get(p).getAgentName());
				}
			}
		}
		// all will accept
		if (!SomeoneWillReject){
			Path path2=new Path(path);
			path2.Accept("All","sub games are worst");
			MyValueForOffer[0].firstPath(path2);
			return MyValueForOffer[0];
		}else {

			// otherwise , the best for the player which got an offer
//		FatherNode.AddTooption(current);
			current.initselected(MyValueForOffer[0].getName());

			return maxs[0];
		}
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
	 * check if where is a Path that reault with to EQ being in the Intersection
	 * @param AgentSentOffer the agent that gave the offer
	 * @param AgentGottenOffer the agent got the offer
	 * @param offer the offer name, if it's the root set to null
	 * @param EQ the EQ name
	 * @param Pl 0 to check for all,1 to P1, 2 to P2
	 * @param paths array to add all Path to
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
	public static options FindBestByPrefernce(String[] out,ArrayList<Agent> p2, Agent p1){
		outcome bestoutcome=null;
		String startingOffer=null;

		DecisionNode tree=new DecisionNode(p1.getAgentName(),"Start",out.length);

		for (int k = 0; k < out.length; k++) {
			ArrayList<Agent> p2Copy=deepArrayList(p2);
			Agent p1Copy=new Agent(p1);
			Path move=new Path();
			move.Offer(out[k],p1Copy.getAgentName());
			outcome p2Outcome=FullInfoTurn(p2Copy,p1Copy,move,out[k],tree,true);
			outcome p1Outcome=p1.copyOutcome(p2Outcome.getName());

			p1Outcome.InitPath(p2Outcome.getPathToout());
//			tree.addResultToLeaf(k,p2Outcome.getName());

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

		options ans=new options(null,startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
		ans.setTree(tree);
		return ans ;
	}


	public static ArrayList<Agent> deepArrayList(ArrayList<Agent> copia) {
		ArrayList<Agent> a = new ArrayList<Agent>();
		for (Agent ag : copia) {
			a.add(new Agent(ag));
		}
		return a;
	}

	/**
	 * find the EQ by computing the sub game perfect (Tree)
	 * @param out
	 * @param p2
	 * @param p1
	 * @return
	 */
	public static options FindBestByPrefernceDissagremnt(String[] out,ArrayList<Agent> p2,Agent p1){
		outcome bestoutcome=p1.getdissagramntvalue();
		String startingOffer=null;

		DecisionNode tree=new DecisionNode(p1.getAgentName(),"Start",out.length);

		for (int k = 0; k < out.length; k++) {
			ArrayList<Agent> p2Copy=new ArrayList<>(p2);
			Agent p1Copy=new Agent(p1);
			Path move=new Path();
			move.Offer(out[k],p1Copy.getAgentName());
			outcome p2Outcome=FullInfoTurn(p2Copy,p1Copy,move,out[k],tree,true);
			outcome p1Outcome=p1.copyOutcome(p2Outcome.getName());

			p1Outcome.InitPath(p2Outcome.getPathToout());
			tree.addResultToLeaf(k,p2Outcome.getName());

			if (p1Outcome.getValue()>bestoutcome.getValue()){
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
//		tree.setPreprence(p1.getAgentName(),p2.getAgentName(),p1.getOriginalPrefrence(),p2.getOriginalPrefrence());

		options ans=new options(null,startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
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
	public static options FindBestByPrefernceNoCutOff(String[] out,ArrayList<Agent> p2,Agent p1){
		outcome bestoutcome=null;
		String startingOffer=null;

		DecisionNode tree=new DecisionNode(p1.getAgentName(),"Start",out.length);

		for (int k = 0; k < out.length; k++) {
			ArrayList<Agent> p2Copy=deepArrayList(p2);
			Agent p1Copy=new Agent(p1);
			Path move=new Path();
			move.RejactAndOffer(out[k],p1Copy.getAgentName());
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
		//tree.setPreprence(p1.getAgentName(),p2.getAgentName(),p1.getOriginalPrefrence(),p2.getOriginalPrefrence());

		options ans=new options(null,startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
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

		Path UoinonRemoved=new Path();
		//remove intersection
		String InterRemoved=RemoveIntesection(out,Agent,StartingAgent,"");

		//remove union
		if (InterRemoved.length()==0 || (InterRemoved.split(":").length)%2==0){
			RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,UoinonRemoved);
		}
		else{
			RemoveUnionOneByOneFirstToLast(StartingAgent,Agent,UoinonRemoved);
		}
		if (StartingAgent.numberOfOutcomeLeft()>1){
			System.exit(1);
		}


		return new options(null,null,StartingAgent.CopyBestOutcome().getName(),UoinonRemoved);

	}

	public static ArrayList<String> UcSize2WinningPrefrence(String[] out,Agent other,String goal){
		ArrayList<String> all=new ArrayList<String>();

		ArrayList<outcome> above_other_outcome=other.OutComesBeterThen(goal);

		int number_of_outcomes_above=above_other_outcome.size();
		int number_of_outcome_below=out.length-number_of_outcomes_above-1;
		if (number_of_outcome_below<number_of_outcomes_above){
			return all;
		}
		outcome[] below_other_loc=other.CopyNworst(number_of_outcome_below);
		ArrayList<String> below_other_loc_name=new ArrayList<>();
		for (outcome o:below_other_loc) {
			below_other_loc_name.add(o.getName());
		}

		List<Set<String>> allsubset = allsubsets(below_other_loc_name,number_of_outcomes_above);

		for (Set<String> set_above:allsubset) {
			set_above.add(goal);
			String[] set_above_array=set_above.toArray(new String[set_above.size()]);
			String[] sub=substract(out,set_above_array);
			List<String> allPossiblePrefrenceAbove = Fullinfo.AllPossiblePrefrence(set_above_array);
			List<String> allPossiblePrefrenceUnder = Fullinfo.AllPossiblePrefrence(sub);

			for (String a : allPossiblePrefrenceAbove) {
				for (String u : allPossiblePrefrenceUnder) {
					all.add(u +"<"+ a);
				}
			}
		}

		return all;

	}

	public static int lowersSize(int out,boolean offering){
		if (out%2==0){
			return offering ?  out/2-1: out/2;
		}
		else{
			return offering? out/2: out/2;
		}
	}



	public static List<Set<String>> allsubsets(ArrayList<String> pref, int below) {
		List<Set<String>> res = new ArrayList<>();
		getSubsets(pref, below, 0, new HashSet<String>(), res);
		return res;
	}

	private static void getSubsets(List<String> superSet, int k, int idx, Set<String> current,List<Set<String>> solution) {
		//successful stop clause
		if (current.size() == k) {
			solution.add(new HashSet<>(current));
			return;
		}
		//unseccessful stop clause
		if (idx == superSet.size()) return;
		String x = superSet.get(idx);
		current.add(x);
		//"guess" x is in the subset
		getSubsets(superSet, k, idx+1, current, solution);
		current.remove(x);
		//"guess" x is not in the subset
		getSubsets(superSet, k, idx+1, current, solution);
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


//	/**
//	 * finding the EQ by offering an element is the intersection if after
//	 * it will cause one of the common Goals to join the lowers
//	 * @param Agent
//	 * @param StartingAgent
//	 * @return
//	 */
//	public static options FindBestByPrefernceTezVersion2(Agent Agent,Agent StartingAgent,String order){
//		//Simulate the game with intersection
//		//the first agent is the starting one
//		int n=Agent.NumberOfOfferLeft();
//		if (n>1){
//			int numberOfPassStarting;
//			int numberOfPassWaiting;
//			if (n%2==0){
//				numberOfPassStarting=n/2-1;
//				numberOfPassWaiting=n/2;
//			}
//			else{
//				numberOfPassStarting=n/2;
//				numberOfPassWaiting=n/2;
//			}
//
//			outcome[] joinGoalsP2=JoinGoals(Agent,StartingAgent,Agent.getAgentName());
//
//			outcome[] lowersGoalStarting=StartingAgent.AllWorstFromWorstIn(joinGoalsP2);
//			outcome[] lowersGoalWaiting=Agent.AllWorstFromWorstIn(joinGoalsP2);
//			int InterSize=Fullinfo.IntesectGroup(lowersGoalStarting, lowersGoalWaiting).size();
//
//			int range=(int) (InterSize-Math.floor(InterSize/2));
//
//			outcome Removed=null;
//			outcome[] RangeOutcomes=StartingAgent.copyOutcomeInRange(numberOfPassStarting+1, numberOfPassStarting+range);
//			outcome[] RangeOutcomesP2=StartingAgent.copyOutcomeInRange(numberOfPassWaiting+1, numberOfPassWaiting+1);
//
//			if (!Fullinfo.IntesectGroup(RangeOutcomes, joinGoalsP2).isEmpty()){
//				ArrayList<outcome> I=Fullinfo.IntesectGroup(Agent.CopyNworst(numberOfPassWaiting), StartingAgent.CopyNworst(numberOfPassStarting));
//				if (I.isEmpty()){
//					Path p=new Path();
//					p=RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,p);
//					return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
//				}
//				Removed=Agent.RemoveOutcome(I.get(0).getName());
//				StartingAgent.RemoveOutcome(I.get(0).getName());
//			}
//			else if (!Fullinfo.IntesectGroup(RangeOutcomesP2, joinGoalsP2).isEmpty()){
//				ArrayList<outcome> I=Fullinfo.IntesectGroup(Agent.CopyNworst(numberOfPassWaiting), StartingAgent.CopyNworst(numberOfPassStarting));
//				if (I.isEmpty()){
//					RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
//					return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
//				}
//				Removed=Agent.RemoveOutcome(I.get(0).getName());
//				StartingAgent.RemoveOutcome(I.get(0).getName());
//
//			}
//			else{
//				outcome[] w=Agent.RemoveNworst(1);
//				Removed=StartingAgent.RemoveOutcome(w[0].getName());
//			}
//			//	System.out.println();
//			return FindBestByPrefernceTezVersion2(StartingAgent,Agent,order+":"+Removed.getName());
//		}
//		else{
//			//System.out.println();
//			return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
//
//		}
//
//	}

//
//	/**
//	 * finding the EQ by offering an element is the intersection if after
//	 * it will cause one of the common Goals to join the lowers
//	 * @param Agent
//	 * @param StartingAgent
//	 * @return
//	 */
//	public static options FindBestByPrefernceTezVersion3(Agent Agent,Agent StartingAgent,String order){
//		//Simulate the game with intersection
//		//the first agent is the starting one
//		int n=Agent.NumberOfOfferLeft();
//		outcome Removed=null;
//
//		if (n>1){
//			int numberOfPassStarting;
//			int numberOfPassWaiting;
//			if (n%2==0){
//				numberOfPassStarting=n/2-1;
//				numberOfPassWaiting=n/2;
//			}
//			else{
//				numberOfPassStarting=n/2;
//				numberOfPassWaiting=n/2;
//			}
//			if (IsThereComption(StartingAgent.getPrefrenceAboutCurrentOptions(),Agent.getPrefrenceAboutCurrentOptions())){
//				ArrayList<outcome> I=Fullinfo.IntesectGroup(Agent.CopyNworst(numberOfPassWaiting), StartingAgent.CopyNworst(numberOfPassStarting));
//				if (I.isEmpty()){
//					RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,"");
//					return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
//				}
//				Removed=Agent.RemoveOutcome(I.get(0).getName());
//				StartingAgent.RemoveOutcome(I.get(0).getName());
//			}
//			else {
//				outcome[] w=Agent.RemoveNworst(1);
//				Removed=StartingAgent.RemoveOutcome(w[0].getName());
//			}
//			//	System.out.println();
//			return FindBestByPrefernceTezVersion3(StartingAgent,Agent,order+":"+Removed.getName());
//		}
//		else{
//			//System.out.println();
//			return new options(null,null,StartingAgent.CopyBestOutcome().getName(),order);
//
//		}
//
//	}


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
		Path path=new Path();


		//remove union
		RemoveUnionOneByOneFirstToLast(Agent,StartingAgent,path);

		if (StartingAgent.numberOfOutcomeLeft()>1){
			System.exit(1);
		}

		return new options(null,null,StartingAgent.CopyBestOutcome().getName(),path);

	}
//	public static boolean IsReplaceingOutcomeWorks(String[] out,Agent Agent,Agent StartingAgent,String toRemoved){
//		options LastOut=FindBestByPrefernceTez(out,Agent,StartingAgent);
//		String newAgent=Agent.getOriginalPrefrence().replace("<"+toRemoved,"").replace("<"+toRemoved+"<", "<").replace(toRemoved+"<", "");
//		String newStartingAgent=StartingAgent.getOriginalPrefrence().replace("<"+toRemoved,"").replace("<"+toRemoved+"<", "<").replace(toRemoved+"<", "");
//		options NewOut=FindBestByPrefernce(out,new Agent(Agent.getAgentName(),toRemoved+"<"+newAgent),new Agent(StartingAgent.getAgentName(),toRemoved+"<"+newStartingAgent));
//		String prev=LastOut.longestPaths()+LastOut.getResult()+":";
//		System.out.println();
//		System.out.println("TRID TO REPLACE : "+toRemoved);
//		System.out.println("** old goal:"+ LastOut.getResult()+" **");
//
//		System.out.println("old Path:"+ prev);
//		System.out.println("*** new goal:"+ NewOut.getResult()+" **");
//		System.out.println("all paths:"+ NewOut.getOrders());
//
//		if (NewOut.getPathOf(prev)==null){
//			System.out.println("X");
//			return false;
//		}
//		System.out.println("V");
//		return true;
//	}



	private static void RemoveUnionOneByOneFirstToLast(Agent Agent,Agent StartingAgent,Path path){
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
				path.RejactAndOffer(a1.getName(),StartingAgent.getAgentName());
			else{
				path.RejactAndOffer(a1.getName(),StartingAgent.getAgentName());

			}

			Temp=StartingAgent;
			StartingAgent=Agent;
			Agent=Temp;
		}
	}


	public static String RemoveIntesection(String[] out,Agent Agent,Agent StartingAgent,String orderOfremovale){
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


	public static boolean isParetoOptimal(String outcome,Agent[] agents){
		ArrayList<outcome> pref=agents[0].OutComesBeterThen(outcome);
		for(int i=1; i<agents.length; i++){
			ArrayList<outcome> prefi=agents[i].OutComesBeterThen(outcome);
			pref=Fullinfo.IntesectGroup(pref.toArray(new outcome[pref.size()]),prefi.toArray(new outcome[prefi.size()]));
		}
		return pref.isEmpty();
	}
	public static ArrayList<String> allPartoOptimal(String[] out,Agent[] agent){
		ArrayList<String> result=new ArrayList<String>();
		for(String o:out){
			if (isParetoOptimal(o,agent)){
				result.add(o);
			}
		}

		return result;
	}


}
