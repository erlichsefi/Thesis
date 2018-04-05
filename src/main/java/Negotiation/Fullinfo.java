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

    public static int[] lowers(int numberOfPlayers, int numberofoutcomes){
        int player_turn=0;
        int[] result=new int[numberOfPlayers];
        while (numberofoutcomes>1){
            for (int i=0; i<numberOfPlayers;i++){
                if (i!=player_turn){
                    result[i]++;
                }
            }
            numberofoutcomes--;
            player_turn=(player_turn+1)%numberOfPlayers;
        }
        return result;
    }

    public static ArrayList<options> FindWantedOutcome(String[] out,ArrayList<Agent> Agent,Agent StartingAgent,String WantedResult ){
        ArrayList<options> op=new ArrayList<options>();
        DecisionNode tree=new DecisionNode(StartingAgent.getAgentName(),"Start",out.length);
        for (int k = 0; k < out.length; k++) {
            ArrayList<Agent> myAgnetCopt=new ArrayList<>(Agent);
            Agent otherAgentCopy=new Agent(StartingAgent);
            Path move=new Path();
            move.RejactAndOffer(out[k],otherAgentCopy.getAgentName());
            outcome o= FullInfoTurnNplayers(myAgnetCopt,otherAgentCopy,move,out[k],tree,true);

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
    public static outcome FullInfoTurnNplayers(ArrayList<Agent> AgentsGottenOffer, Agent AgentSentOffer, Path path, String offer, DecisionNode FatherNode, boolean ToCutOff){
        //remove the offer that gotten
        AgentSentOffer.RemoveOutcome(offer);
        //get all agents my value for the offer
        outcome[] AllWaitingAgentsValue=new outcome[AgentsGottenOffer.size()];
        for(int p=0; p < AgentsGottenOffer.size();p++) {
            AllWaitingAgentsValue[p] = AgentsGottenOffer.get(p).RemoveOutcome(offer);
        }


        ArrayList<Agent> AgentsGottenOfferCopy=deepArrayList(AgentsGottenOffer);
        // there is noting to offer
        if (!AgentsGottenOfferCopy.get(0).HasMoreOffers()){
            FatherNode.Allaccept(offer,"last offer");
            Path path2=new Path(path);
            path2.Accept("all","last offer");
            AllWaitingAgentsValue[0].firstPath(path2);
            return AllWaitingAgentsValue[0];
        }
        else if (ToCutOff){
            // if all accept because they offered the best
            boolean allAccept=true;
            //there is no better thing to do
            for(int p=0; p < AgentsGottenOfferCopy.size();p++) {
                outcome player_best = AgentsGottenOfferCopy.get(p).CopyBestOutcome();
                if (player_best.getValue() > AllWaitingAgentsValue[p].getValue()) {
                    allAccept=false;
                }

            }

            if (allAccept){
                FatherNode.Allaccept( offer, "better then: ") ;
                Path path2 = new Path(path);
                path2.Accept("all", "better then: ");
                AllWaitingAgentsValue[0].firstPath(path2);
                return AllWaitingAgentsValue[0];
            }
        }

        DecisionNode current=new DecisionNode("all",offer,AgentsGottenOfferCopy.get(0).NumberOfOfferLeft());
        current.addBranch(new DecisionNode("all",offer) );
        // we need to calculate the other steps for all other players
        String[] PossibleOffers=AgentsGottenOffer.get(0).getOutComeOptions();

        outcome[] PossibleOut=new outcome[PossibleOffers.length];
        for (int o=0;o< AgentsGottenOffer.get(0).getOutComeOptions().length;o++) {
            String current_offer=PossibleOffers[o];
            // setting up the env for the recursion
            Path new_path = new Path(path);

            new_path.RejactAndOffer(current_offer, AgentsGottenOffer.get(0).getAgentName());

            ArrayList<Agent> new_to= deepArrayList(AgentsGottenOfferCopy);
            new_to.add(new Agent(AgentSentOffer));

            Agent new_p1=new Agent(new_to.remove(0));
            new_p1.RemoveOutcome(current_offer);

            // if player p offer outcome o
            outcome OtherAgentOutCome = new outcome(FullInfoTurnNplayers(new_to, new_p1, new_path, current_offer, current, ToCutOff));

            //	current.addBranchResult(o+1,OtherAgentOutCome.getName());

            PossibleOut[o] = AgentsGottenOffer.get(0).copyOutcome(OtherAgentOutCome.getName());
            PossibleOut[o].InitPath(OtherAgentOutCome.getPathToout());

        }

        outcome CurrentSelection=new outcome(PossibleOut[0]);
        for (int i = 0; i < PossibleOut.length; i++) {
            //if it's better
            if (PossibleOut[i].getValue()>CurrentSelection.getValue()){
                CurrentSelection=new outcome(PossibleOut[i]);
            }else if (PossibleOut[i].getValue()==CurrentSelection.getValue()){
                CurrentSelection.addPath(PossibleOut[i].getPathToout());
            }
        }


        boolean SomeoneWillReject=false;

        try {
            //find better outcome from current
            for (int p = 0; p < AgentsGottenOfferCopy.size(); p++) {
                double Player_value_for_deeper_result = AgentsGottenOfferCopy.get(p).copyOutcome(CurrentSelection.getName()).getValue();
                double Player_value_for_acceptance_result = AllWaitingAgentsValue[p].getValue();

                if (Player_value_for_deeper_result > Player_value_for_acceptance_result) {
                    SomeoneWillReject = true;
                    // FatherNode.Reject(AgentsGottenOfferCopy.get(p).getAgentName(), offer, "better then: ") ;

                }else{
                    //FatherNode.Accept(AgentsGottenOfferCopy.get(p).getAgentName(), offer, "better then: "); ;

                }
            }
        }catch (Exception e){
            System.exit(1);
        }
        // all will accept
        if (!SomeoneWillReject){
            Path path2=new Path(path);
            path2.Accept("All","sub games are worst");
            AllWaitingAgentsValue[0].firstPath(path2);
            return AllWaitingAgentsValue[0];
        }else {

            // otherwise , the best for the player which got an offer
            FatherNode.addBranch(current);
            current.setSelected(PossibleOut[0].getName());

            return CurrentSelection;
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
            //copy players
            ArrayList<Agent> p2Copy=deepArrayList(p2);
            Agent p1Copy=new Agent(p1);
            //create a path to this branch
            Path move=new Path();
            move.Offer(out[k],p1Copy.getAgentName());

            //
            outcome ResultedOutcome= FullInfoTurnNplayers(p2Copy,p1Copy,move,out[k],tree,true);
            outcome MyOutcome=p1.copyOutcome(ResultedOutcome.getName());

            MyOutcome.InitPath(ResultedOutcome.getPathToout());
//			tree.addBranchResult(k,MyOutcome.getName());

            if (bestoutcome==null){
                bestoutcome=new outcome(MyOutcome);
                startingOffer=out[k];
            }
            else if (MyOutcome.getValue()>bestoutcome.getValue()){
                bestoutcome=new outcome(MyOutcome);
                startingOffer=out[k];
            }
            else if (MyOutcome.getValue()==bestoutcome.getValue()){
                bestoutcome.addPath(MyOutcome.getPathToout());

            }

        }
        tree.setSelected(bestoutcome.getName());

        options ans=new options(null,startingOffer,bestoutcome.getName(),bestoutcome.getPathToout());
        ans.setTree(tree);
        return ans ;
    }


    public static outcome FullInfoTurnTwoPlayers(Agent AgentGottenOffer, Agent AgentSentOffer, Path path, String offer, DecisionNode FatherNode, boolean ToCutOff){

        //remove the offer that gotten
        AgentSentOffer.RemoveOutcome(offer);
        //get my value for it
        outcome MyValueForOffer=AgentGottenOffer.RemoveOutcome(offer);

        // there is noting to offer
        if (!AgentGottenOffer.HasMoreOffers()){
            FatherNode.Accept(AgentGottenOffer.getAgentName(),offer,"last offer");
            Path path2=new Path(path);
            path2.Accept(AgentGottenOffer.getAgentName(),"last offer");
            MyValueForOffer.firstPath(path2);
            return MyValueForOffer;
        }
        else if (ToCutOff){
            //there is no better thing to do
            outcome next=AgentGottenOffer.CopyBestOutcome();
            if (next.getValue()<MyValueForOffer.getValue()){
                Path path2=new Path(path);
                path2.Accept(AgentGottenOffer.getAgentName(),"better then: "+AgentGottenOffer.getPrefrenceAboutCurrentOptions());
                FatherNode.Accept(AgentGottenOffer.getAgentName(),offer,"better then: "+AgentGottenOffer.getPrefrenceAboutCurrentOptions());
                MyValueForOffer.firstPath(path2);
                return MyValueForOffer;
            }
        }

        DecisionNode current=new DecisionNode(AgentGottenOffer.getAgentName(),offer,AgentGottenOffer.NumberOfOfferLeft());
        current.addBranch(new DecisionNode(AgentGottenOffer.getAgentName(),offer) );
        //build all possible outcomes
        outcome[] PossibleOut=new outcome[AgentGottenOffer.NumberOfOfferLeft()];
        int j=0;
        for (String entry : AgentGottenOffer.getOutComeOptions()) {


            Path path2=new Path(path);
            path2.RejactAndOffer(entry,AgentGottenOffer.getAgentName());
            outcome OtherAgentOutCome=FullInfoTurnTwoPlayers(new Agent(AgentSentOffer),new Agent(AgentGottenOffer),path2,entry,current,ToCutOff);

            if (!OtherAgentOutCome.getPlayer().equals(AgentSentOffer.getAgentName())){
                throw new IllegalArgumentException("worng player.");
            }

            current.addBranchResult(j+1,OtherAgentOutCome.getName());

            PossibleOut[j]=AgentGottenOffer.copyOutcome(OtherAgentOutCome.getName());
            PossibleOut[j++].InitPath(OtherAgentOutCome.getPathToout());
        }

//		if (path.size()==2 && path.startsWith("o2") && path.endsWith("o4")){
//			System.out.println();
//		}

        boolean IsBetterInSubGames=false;
        //find better outcome from current
        for (int i = 0; i < PossibleOut.length; i++) {
            //if it's better
            if (PossibleOut[i].getValue()>MyValueForOffer.getValue()){
                MyValueForOffer=new outcome(PossibleOut[i]);
                IsBetterInSubGames=true;
            }else if (PossibleOut[i].getValue()==MyValueForOffer.getValue()){
                MyValueForOffer.addPath(PossibleOut[i].getPathToout());
            }
        }
        //accepting the offer
        if (!IsBetterInSubGames){
            Path path2=new Path(path);
            path2.Accept(AgentGottenOffer.getAgentName(),"sub games are worst");

            MyValueForOffer.firstPath(path2);
        }

        FatherNode.addBranch(current);
        current.setSelected(MyValueForOffer.getName());
        return MyValueForOffer;
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
            outcome p2Outcome= FullInfoTurnNplayers(p2Copy,p1Copy,move,out[k],tree,true);
            outcome p1Outcome=p1.copyOutcome(p2Outcome.getName());

            p1Outcome.InitPath(p2Outcome.getPathToout());
            tree.addBranchResult(k,p2Outcome.getName());

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
        //tree.setResult(bestoutcome.getName());
        tree.setSelected(bestoutcome.getName());
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
            outcome p2Outcome= FullInfoTurnNplayers(p2Copy,p1Copy,move,out[k],tree,false);

            outcome p1Outcome=p1.copyOutcome(p2Outcome.getName());

            p1Outcome.InitPath(p2Outcome.getPathToout());
            tree.addBranchResult(k,p2Outcome.getName());

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
        //  tree.setResult(bestoutcome.getName());
        tree.setSelected(bestoutcome.getName());
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
        assert (Agent.getOutComeOptions().length==StartingAgent.getOutComeOptions().length);

        //remove intersection
        AlgoPath UoinonRemoved=RemoveIntesection(out,Agent,StartingAgent,new AlgoPath());

        //remove union
        if (UoinonRemoved.size()%2==0){
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

    public static String FindBestByPrefernceTezNplayerSelect(ArrayList<String> out,ArrayList<Agent> Agent,Agent StartingAgent) {
        String	o=null;
        if (out.size()%2==1) {
            o = LowestOthersNotDominated(out.toArray(new String[out.size()]), new ArrayList<Agent>(Agent), new Agent(StartingAgent));
        }
        else{
            o= LowestOtherWithMysNotDominated(out.toArray(new String[out.size()]), new ArrayList<Agent>(Agent), new Agent(StartingAgent));
        }
        return o;
    }

    public static String FindBestByPrefernceTezNplayerSelectV2(ArrayList<String> out,ArrayList<Agent> Agent,Agent StartingAgent) {
        String	o=null;
        if (out.size()==1){
            o=out.get(0);
        }
        else if (out.size()==2){
            ArrayList<String> po=Fullinfo.allPartoOptimal(out.toArray(new String[out.size()]),Agent.toArray(new Agent[Agent.size()]));
            if (po.isEmpty()){
                System.err.println("error! ");
                return null;
            }
            else if (po.size()==1){
                //offer either
                ArrayList<String> E=new ArrayList<>(out);
                E.removeAll(po);
                o=E.get(0);
            }else{ //po.size==2
                o=StartingAgent.CopyWorstOutcome().getName();
            }
        }
        else if (out.size()==3){
            ArrayList<String> po=Fullinfo.allPartoOptimal(out.toArray(new String[out.size()]),new Agent[]{StartingAgent,Agent.get(1)});
            if (po.isEmpty()){
                System.err.println("error! ");
                return null;
            }
            else if (po.size()==1) {
                //offer either
                ArrayList<String> E=new ArrayList<>(out);
                E.removeAll(po);
                o=E.get(0);
            }else if (po.size()==2){

                o=lowersAction(out,StartingAgent,Agent.get(0));
            }else{ //po.size==3
                o= FindWorstIn(po,Agent.get(0));
            }
        }
        else{
            System.err.println("not defined yet");
            return null;
        }
        return o;
    }


    public static String lowersAction(ArrayList<String> out,Agent AgentGottenOffer,Agent AgentSentOffer){
        int numberOfPassStarting;
        int numberOfPassWaiting;
        if (out.size()%2==0){
            numberOfPassStarting=out.size()/2-1;
            numberOfPassWaiting=out.size()/2;
        }
        else{
            numberOfPassStarting=out.size()/2;
            numberOfPassWaiting=out.size()/2;
        }

        //get values from agents and intersect them
        outcome[] SAgentWorst=AgentGottenOffer.CopyNworst(numberOfPassStarting);
        outcome[] WAgentWorst=AgentSentOffer.CopyNworst(numberOfPassWaiting);
        ArrayList<outcome> el=IntesectGroup(SAgentWorst,WAgentWorst);
        if (el.isEmpty()){
            ArrayList<String> out2=new ArrayList<String> (out);
            for (outcome a:SAgentWorst){out2.remove(a.getName());}
            for (outcome a:WAgentWorst){out2.remove(a.getName());}
            return out2.get(0);
        }else{
            return el.get(0).getName();
        }

    }
    public static String FindWorstIn(ArrayList<String> options,Agent by){
        String res=options.get(0);
        for (int i=1;i<options.size();i++){
            if (by.CopyOutcome(res).getValue()>by.CopyOutcome(options.get(i)).getValue()){
                res=options.get(i);
            }
        }
        return res;
    }
    public static String FindBestIn(ArrayList<String> options,Agent by){
        String res=options.get(0);
        for (int i=1;i<options.size();i++){
            if (by.CopyOutcome(res).getValue()<by.CopyOutcome(options.get(i)).getValue()){
                res=options.get(i);
            }
        }
        return res;
    }

    public static options FindBestByPrefernceTezNplayer(ArrayList<String> out,ArrayList<Agent> Agent,Agent StartingAgent){
        assert (StartingAgent.getOutComeOptions().length==Agent.get(0).getOutComeOptions().length);

        if (StartingAgent.numberOfOutcomeLeft()==1){
            return new options(null,null,StartingAgent.CopyBestOutcome().getName());
        }
        else{

            String o=FindBestByPrefernceTezNplayerSelectV2(out,Agent,StartingAgent);
            options result=null;
            System.out.println(o);
            ArrayList<String> new_out=new ArrayList<>(out);
            new_out.remove(o);

            ArrayList<Agent> new_Agent=deepArrayList(Agent);
            new_Agent.add(new Agent(StartingAgent));
            for(Agent a:new_Agent) {
                a.RemoveOutcome(o);
            }
            Agent new_starting=new Agent(new_Agent.remove(0));
            assert (new_starting.getOutComeOptions().length==new_Agent.get(0).getOutComeOptions().length);

            options o1=FindBestByPrefernceTezNplayer(new_out,new_Agent,new_starting);
            if (result==null){
                result=o1;
            }
            else{
                try {
                    if (!result.getResult().equals(o1.getResult())){
                        System.out.println();
                    }
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }
            }


            if (result==null){
                System.exit(0);
            }
            return result;
        }



    }
    private static String LowestOthersNotDominated(String[] all, ArrayList<Agent> agent, Agent starting) {
        ArrayList<Agent> all_agent=new ArrayList<>(agent);
        all_agent.add(new Agent(starting));


        Agent minimizer=new Agent(all_agent.get(all.length%all_agent.size()));
        String lowest=minimizer.CopyWorstOutcome().getName();
        boolean eval=false;
        for (Set<Agent> Subs:allsubsets(agent,2)){
            ArrayList<String> allist=new ArrayList<>(Arrays.asList(all));
            ArrayList<String> po=Fullinfo.allPartoOptimal(all,Subs.toArray(new Agent[Subs.size()]));
            allist.removeAll(po);
            for (String dominted:allist) {
                eval=true;
                if (minimizer.CopyOutcome(lowest).getValue() < minimizer.CopyOutcome(dominted).getValue()) {
                    lowest = dominted;
                }

            }
        }
        if (!eval){
            lowest=starting.CopyWorstOutcome().getName();
        }
        return lowest;

    }

    private static String LowestOtherWithMysNotDominated(String[] all, ArrayList<Agent> agent, Agent starting) {
//        ArrayList<Agent> all_agent=new ArrayList<>(agent);
//        all_agent.add(new Agent(starting));
        boolean allPertoOptimal=true;
        Agent from=new Agent(agent.get(0));
        String offer=from.CopyWorstOutcome().getName();
        for (Set<Agent> Subs:allsubsets(agent,2)){
            ArrayList<String> allist=new ArrayList<>(Arrays.asList(all));
            ArrayList<String> po=Fullinfo.allPartoOptimal(all,Subs.toArray(new Agent[Subs.size()]));
            allist.removeAll(po);
            System.out.println("po= "+po);
            for (String dominted:allist) {
                String temp=null;
                allPertoOptimal=false;
                ArrayList<outcome> S=from.OutComeWorstThen(dominted);
                if (S.isEmpty()){
                    temp=dominted;
                }
                else{
                    temp=S.get(0).getName();
                }
                if( from.CopyOutcome(temp).getValue()>from.CopyOutcome(offer).getValue()){
                    offer=temp;
                }
            }
        }
        // all are pareto optimal
        if (allPertoOptimal){
            offer=starting.CopyWorstOutcome().getName();
        }
        return offer;

    }


    private static String MaxIntersection(String[] all,ArrayList<Agent> agent,Agent starting) {
        assert (starting.getOutComeOptions().length==agent.get(0).getOutComeOptions().length);
        String startingoffer=null;
        int[] lowers=Fullinfo.lowers(agent.size()+1,all.length);
        for (int i=0;i<agent.size();i++){
            Agent current=new Agent(agent.get(i));
            ArrayList<outcome> To1=Fullinfo.IntesectGroup(current.CopyNworst(lowers[i+1]),starting.CopyNworst(lowers[0]));

            if (!To1.isEmpty()){
                return To1.get(0).getName();
            }
        }
        return startingoffer;


    }
    private static ArrayList<String> InIntresectinOfN(String[] all,ArrayList<Agent> agent,Agent starting) {
        ArrayList<String> res=new ArrayList<>();

        assert (starting.getOutComeOptions().length==agent.get(0).getOutComeOptions().length);

        Agent maxStarting=new Agent(starting);
        Agent maxOffering=new Agent(agent.get(0));
        options maxt=Fullinfo.FindBestByPrefernceTez(all,new Agent(maxOffering),new Agent(maxStarting));
        AlgoPath p= (AlgoPath) maxt.firstPath();
        String maxO=null;
        if (!p.asIntersection()) {
            maxO = agent.get(0).CopyWorstOutcome().getName();
            res.add(maxO);
        }
        else{
            maxO=maxt.getResult();

        }
        for (int i=1;i<agent.size();i++){
            Agent current=new Agent(agent.get(i));
            options To1=Fullinfo.FindBestByPrefernceTez(all,new Agent(current),new Agent(starting));

            p= (AlgoPath) To1.firstPath();
            if (p.asIntersection()){
                if(starting.CopyOutcome(To1.getResult()).getValue()>starting.CopyOutcome(maxO).getValue()) {
                    maxO=To1.getResult();
                    res=new ArrayList<>();
                }
                if (starting.CopyOutcome(To1.getResult()).getValue()==starting.CopyOutcome(maxO).getValue()){
                    res.add(agent.get(i).CopyWorstOutcome().getName());

                }
            }


        }
        return res;

    }
    private static ArrayList<String> InIntresectinOfN(ArrayList<String> all,ArrayList<Agent> agent, int i) {
        List<Set<Agent>> allSubs= allsubsets(agent,i);
        ArrayList<String> result=new ArrayList<String>();
        for (Set<Agent> aSub:allSubs) {
            List<String> in=new ArrayList<>(all);
            for(Agent agen:aSub){
                List<String> list=new ArrayList<>();
                for (outcome o:agen.CopyNworst((int)Math.floor(all.size()/3))){
                    list.add(o.getName());
                }
                in=intersection(in,list);
            }
            result.addAll(in);
        }
        return result;
    }
    public static  <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
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


    public static <T> List<List<T>> allsubsetsWithRep(ArrayList<T> pref, int below) {
        if (below==0){
            List<List<T>> res=new ArrayList<>();
            res.add(new ArrayList<>());
            return res;
        }

        List<List<T>> res = new ArrayList<>();
        for (T v:pref){
            List<List<T>> rep=allsubsetsWithRep(pref,below-1);
            for (List<T> l:rep){
                l.add(v);
                res.add(l);
            }
        }
        return res;
    }
    public static <T> List<Set<T>> allsubsets(ArrayList<T> pref, int below) {
        List<Set<T>> res = new ArrayList<>();
        getSubsets(pref, below, 0, new HashSet<T>(), res);
        return res;
    }

    private static <T> void getSubsets(List<T> superSet, int k, int idx, Set<T> current,List<Set<T>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        T x = superSet.get(idx);
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
        Path InterRemoved=RemoveIntesection(out,Agent,StartingAgent,new AlgoPath());

        return InterRemoved.size();

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
        Path InterRemoved=RemoveIntesection(out,Agent,StartingAgent,new AlgoPath());
        if (InterRemoved.size()%2==0){
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
        AlgoPath path=new AlgoPath();


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



    private static void RemoveUnionOneByOneFirstToLast(Agent Agent,Agent StartingAgent,AlgoPath path){
        assert (Agent.getOutComeOptions().length==StartingAgent.getOutComeOptions().length);
        Agent Temp;
        while (StartingAgent.getOutComeOptions().length>1){
            String[] out=Agent.getOutComeOptions();

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
            path.addUnion(a1.getName());


            Temp=StartingAgent;
            StartingAgent=Agent;
            Agent=Temp;
        }
    }


    public static ArrayList<outcome> IntresectLowers(String[] out,Agent Agent,Agent StartingAgent){
        int numberOfPassStarting;
        int numberOfPassWaiting;
        if (out.length%2==0){
            numberOfPassStarting=out.length/2;
            numberOfPassWaiting=out.length/2;
        }
        else{
            numberOfPassStarting=out.length/2;
            numberOfPassWaiting=out.length/2;
        }

        outcome[] SAgentWorst=StartingAgent.CopyNworst(numberOfPassStarting);
        outcome[] WAgentWorst=Agent.CopyNworst(numberOfPassWaiting);
        return IntesectGroup(SAgentWorst,WAgentWorst);
    }

    public static AlgoPath RemoveIntesection(String[] out,Agent Agent,Agent StartingAgent,AlgoPath p){
        assert (Agent.getOutComeOptions().length==StartingAgent.getOutComeOptions().length);
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
            p.addIntersection(o.getName());
        }
        String[] unionArray = outcomeLeft(Agent,StartingAgent);


        if (!el.isEmpty()){
            if (el.size()%2==0){
                return RemoveIntesection(unionArray,Agent,StartingAgent,p);
            }
            else{
                return RemoveIntesection(unionArray,StartingAgent,Agent,p);
            }
        }
        else{
            return p;
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


    public static ArrayList<outcome> isParetoOptimal(String outcome,Agent[] agents){
        ArrayList<outcome> pref=agents[0].OutComesBeterThen(outcome);
        for(int i=1; i<agents.length; i++){
            ArrayList<outcome> prefi=agents[i].OutComesBeterThen(outcome);
            pref=Fullinfo.IntesectGroup(pref.toArray(new outcome[pref.size()]),prefi.toArray(new outcome[prefi.size()]));
        }
        return pref;
    }
    public static ArrayList<String> allPartoOptimal(String[] out,Agent[] agent){
        ArrayList<String> result=new ArrayList<String>();
        for(String o:out){
            if (isParetoOptimal(o,agent).isEmpty()){
                result.add(o);
            }
        }

        return result;
    }


    public static ArrayList<String> allPartoDominted(String[] out,Agent[] agent){
        ArrayList<String> result=new ArrayList<String>(Arrays.asList(out));
        ArrayList<String> po=allPartoOptimal(out,agent);
        result.removeAll(po);
        return result;
    }

    public static ArrayList<String> allPartoDominting(String[] out,Agent[] agent){
        ArrayList<String> result=new ArrayList<String>();
        for(String o:out){
            isParetoOptimal(o,agent).forEach(outcome -> result.add(outcome.getName()));
        }

        return result;
    }


}
