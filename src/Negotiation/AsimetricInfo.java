package Negotiation;
import java.util.ArrayList;


import tools.Agent;
import tools.options;
import tools.outcome;

public class AsimetricInfo extends Algo{
	static int[] stat;
	static String pref="";


	/**
	 * get all the couples of Preference X order of offers that will lead to the deal . 
	 * KnowenStartingAgnet 
	 * @param out
	 * @param KnowenStartingAgnet
	 * @param offerAgentWhat
	 * @return
	 */
	public static ArrayList<options> AsimetricInfoGameStatistic(String[] out,Agent KnowenAgnet,boolean IsOtherAgnetStarting){
		_out=out;
		stat=new int[out.length];


		ArrayList<options> op=new ArrayList<options>();
		ArrayList<String> client1prefernce=AllPossiblePrefrence(out);
		outcome o=null;

		for (int i = 0; i < client1prefernce.size(); i++) {
			System.out.println((i+1)+"/"+client1prefernce.size());
			String prefrence=client1prefernce.get(i);
			Agent P1=new Agent(KnowenAgnet);
			Agent P2=new Agent("P2",prefrence);
			if (IsOtherAgnetStarting){
				o=FindOrderOfOffersWhenUnknownStarting(P1,P2,"");

			}else{
				o=FindOrderOfOffersWhenKnownStarting(P1,P2,"");

			}
			stat[indexOf(out,o.getName())]++;
			op.add(new options(prefrence,null,o.getName(),o.getPathToout()));
		}
		return op;
	}

	/**
	 * finding the order of offers that will result in the outcome the agent want
	 * where the agent without the information responding, and agent with perfect information  starting
	 * @param KnowenAgnet
	 * @param otheragnet
	 * @param PastMove
	 * @param op
	 * @param offerAgentWhat
	 * @return 
	 */
	private static outcome FindOrderOfOffersWhenKnownStarting(Agent KnowenAgnet,Agent otheragnet,String PastMove){
		//get all moves
		String[] moves=KnowenAgnet.getOutComeOptions();
		outcome[] possibleOutForKnow=new outcome[moves.length];

		//for each move look for the best 
		for (int k = 0; k < moves.length; k++) {

			Agent KnowenAgnetCopy=new Agent(KnowenAgnet);
			Agent otheragnetCopy=new Agent(otheragnet);

			String presentMove=PastMove+" : "+KnowenAgnet.getAgentName()+" offer "+moves[k];
			//remove the outcome from my options
			outcome KnowOffer=KnowenAgnetCopy.RemoveOutcome(moves[k]);
			//the other agent value
			outcome OtherAgnetOffer=otheragnetCopy.RemoveOutcome(moves[k]);
			//the other agent next look on is next option
			outcome OtherAgnetNextOnTheTable=otheragnetCopy.RemoveBestOutcome();

			//if there is no other option accept it.
			if (OtherAgnetNextOnTheTable==null){
				KnowOffer.firstPath(presentMove+" - "+otheragnet.getAgentName()+" accept, last options");
				possibleOutForKnow[k]= new outcome(KnowOffer);
			}
			// if the next out come for the other agent as lower value aceept
			else if (OtherAgnetOffer.getValue()>OtherAgnetNextOnTheTable.getValue()){
				KnowOffer.firstPath(presentMove+" - "+otheragnet.getAgentName()+" accept, the best option (left "+otheragnetCopy.getPrefrenceAboutCurrentOptions()+")");
				possibleOutForKnow[k]=new outcome(KnowOffer);
			}
			//he will offer me the outcome
			else{
				//i look on my value to it
				outcome KnowenAgentValueForOffer=KnowenAgnetCopy.RemoveOutcome(OtherAgnetNextOnTheTable.getName());
				presentMove=presentMove+" : "+otheragnetCopy.getAgentName()+" offer "+OtherAgnetNextOnTheTable.getName();

				outcome bestKnow=KnowenAgnetCopy.CopyBestOutcome();
				//will see what i can get from the other options
				if (!(bestKnow==null || bestKnow.getValue()<KnowenAgentValueForOffer.getValue())){
					//get my value to other option
					outcome otherAgentout=FindOrderOfOffersWhenKnownStarting(KnowenAgnetCopy,otheragnetCopy,presentMove);
					outcome OutcomeFromOther=KnowenAgnetCopy.RemoveOutcome(otherAgentout.getName());
					//if the outcome is preferred swap
					if ( OutcomeFromOther.getValue()>KnowenAgentValueForOffer.getValue()){
						OutcomeFromOther.InitPath(otherAgentout.getPathToout());
						possibleOutForKnow[k]= new outcome(OutcomeFromOther);
					}
					else{
						OutcomeFromOther.InitPath(otherAgentout.getPathToout());
						possibleOutForKnow[k]=new outcome(OutcomeFromOther);
					}
				}else{
					KnowenAgentValueForOffer.firstPath(presentMove+" - "+otheragnet.getAgentName()+" accept, the best option (left "+otheragnetCopy.getPrefrenceAboutCurrentOptions()+")");
					possibleOutForKnow[k]=new outcome(KnowenAgentValueForOffer);

				}


			}

		}
		outcome max=new outcome(possibleOutForKnow[0]);
		for (outcome outcome : possibleOutForKnow) {
			if (outcome.getValue()>max.getValue()){
				max=new outcome(outcome);
			}
		}
		return max;


	}

	/**
	 * finding the order of offers that will result in the outcome the agent want
	 * where the agent without the information starting, and agent with perfect information responding 
	 * @param KnowenAgnet
	 * @param otheragnet
	 * @param PastMove
	 * @param op
	 * @param offerAgentWhat
	 */
	private static outcome FindOrderOfOffersWhenUnknownStarting(Agent KnowenAgnet,Agent otheragnet,String PastMove){

		//take the other agent best offer
		outcome OtherAgnetNextOnTheTable=otheragnet.RemoveBestOutcome();
		//look at my value for it
		outcome myvalueforoffer=KnowenAgnet.RemoveOutcome(OtherAgnetNextOnTheTable.getName());
		//set the path
		String CurrentMove=PastMove+" : "+otheragnet.getAgentName()+" offer "+OtherAgnetNextOnTheTable.getName();
		//find the best outcome if i'm starting without the offer gotten
		outcome other=FindOrderOfOffersWhenKnownStarting(KnowenAgnet,otheragnet,CurrentMove);
		//return the best
		if (other.getValue()>myvalueforoffer.getValue()){
			return other;
		}
		else{
			return myvalueforoffer;
		}
	}
	
	/**
	 * compute a asymtric game and get the result
	 * @param KnowenAgnet the agent that know the other values
	 * @param otheragnet the agent that do not know the other agent value
	 * @param KnowStarting true if the agent the know starting
	 * @return
	 */
	public static outcome AsimetricInfoGame(Agent KnowenAgnet,Agent otheragnet,boolean KnowStarting){
		if (KnowStarting){
			return FindOrderOfOffersWhenKnownStarting(KnowenAgnet,otheragnet,"");
		}
		else{
			return FindOrderOfOffersWhenUnknownStarting(KnowenAgnet,otheragnet,"");

		}
	}

	/**
	 * algoritem offer outcomes that will result in the best outcome
     * @param KnowenAgnet the agent that know the other values
	 * @param otheragnet the agent that do not know the other agent value
	 * @param KnowStarting true if the agent the know starting
	 * @return
	 */
	public static outcome AsimetricInfoGameTeza(Agent KnowenAgnet,Agent otheragnet,boolean KnowStarting){
		if (KnowStarting){
			return FindOrderOfOffersWhenKnownStartingTeza(KnowenAgnet,otheragnet,"");
		}
		else{
			return FindOrderOfOffersWhenUnknownStartingTeza(KnowenAgnet,otheragnet,"");

		}
	}
	
	
	private static outcome  FindOrderOfOffersWhenKnownStartingTeza(Agent KnowenAgnet,Agent otheragnet,String PastMove){
		String offer=KnowenAgnet.CopyBestOutcome().getName();
		//if even number and the first outcome is the the worst for the other
		//try to get the second best
		if (KnowenAgnet.getOutComeOptions().length%2==0){
			if (otheragnet.CopyWorstOutcome().getName().equals(offer)){
				Agent KnowenAgnetC=new Agent(KnowenAgnet);
				KnowenAgnetC.RemoveBestOutcome();
				offer=KnowenAgnetC.RemoveBestOutcome().getName();
			}
		}
		
		ArrayList<outcome> better=otheragnet.OutComesBeterThen(offer);
		better.sort(null);
		String Currentoffer=null;
		while (better.size()>1){
			Currentoffer=better.remove(better.size()-1).getName();
			KnowenAgnet.RemoveOutcome(Currentoffer);

			outcome other=otheragnet.RemoveOutcome(Currentoffer);
			outcome best=otheragnet.RemoveBestOutcome();

			if (best==null || best.getValue()<other.getValue()){
				return other;
			}

			KnowenAgnet.RemoveOutcome(best.getName());
			if (best.getName().equals(offer)){
				return best;
			}
			better=otheragnet.OutComesBeterThen(offer);
			better.sort(null);
		}

		if (better.size()==1){
			Currentoffer=otheragnet.CopyWorstOutcome().getName();
			KnowenAgnet.RemoveOutcome(Currentoffer);

			outcome other=otheragnet.RemoveOutcome(Currentoffer);
			outcome best=otheragnet.RemoveBestOutcome();

			if (best==null || best.getValue()<other.getValue()){
				return other;
			}

			KnowenAgnet.RemoveOutcome(best.getName());
			if (best.getName().equals(offer)){
				return best;
			}
			better=otheragnet.OutComesBeterThen(offer);
		}
		KnowenAgnet.RemoveOutcome(offer);

		outcome other=otheragnet.RemoveOutcome(offer);
		outcome best=otheragnet.RemoveBestOutcome();

		if (best==null || best.getValue()<other.getValue()){
			return other;
		}

		return null;
	}

	private static outcome FindOrderOfOffersWhenUnknownStartingTeza(Agent KnowenAgnet,Agent otheragnet,String PastMove){
        String wanted=KnowenAgnet.CopyBestOutcome().getName();
    	if (KnowenAgnet.getOutComeOptions().length%2==1){
			if (otheragnet.CopyWorstOutcome().getName().equals(wanted)){
				Agent KnowenAgnetC=new Agent(KnowenAgnet);
				KnowenAgnetC.RemoveBestOutcome();
				wanted=KnowenAgnetC.RemoveBestOutcome().getName();
			}
		}
        
        
        //take the other agent best offer
		outcome OtherAgnetNextOnTheTable=otheragnet.RemoveBestOutcome();
		KnowenAgnet.RemoveOutcome(OtherAgnetNextOnTheTable.getName());
		if (OtherAgnetNextOnTheTable.getName().equals(wanted)){
			return OtherAgnetNextOnTheTable;
		}
		else{
			return FindOrderOfOffersWhenKnownStartingTeza(KnowenAgnet,otheragnet,"");
		}
	}

	
	public static void printStatistic() {
		for (int i = 0; i < _out.length; i++) {
			System.out.print(_out[i]+" ");
		}
		System.out.println();
		for (int i = 0; i < stat.length; i++) {
			System.out.print(stat[i]+"  ");
		}

	}



}
