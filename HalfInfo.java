import java.util.ArrayList;

public class HalfInfo extends Algo{

	
	

	/**
	 * get all the couples of Preference X order of offers that will lead to the deal . 
	 * KnowenStartingAgnet 
	 * @param out
	 * @param KnowenStartingAgnet
	 * @param offerAgentWhat
	 * @return
	 */
	public static ArrayList<options> HalfInfoGame(String[] out,Agent KnowenAgnet,boolean IsOtherAgnetStarting,String offerAgentWhat){
		ArrayList<options> op=new ArrayList<options>();
		ArrayList<String> client1prefernce=AllPossiblePrefrence(out);
		for (int i = 0; i < client1prefernce.size(); i++) {
			String prefrence=client1prefernce.get(i);
			Agent otherAgent  =new Agent(KnowenAgnet);
			Agent myAgnet=new Agent("myagent",prefrence);
			if (IsOtherAgnetStarting){
				FindOrderOfOffersWhenUnknownStarting(myAgnet,otherAgent,"",op,offerAgentWhat);

			}else{
				FindOrderOfOffersWhenKnownStarting(myAgnet,otherAgent,"",op,offerAgentWhat);

			}
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
	 */
	private static void FindOrderOfOffersWhenKnownStarting(Agent KnowenAgnet,Agent otheragnet,String PastMove,ArrayList<options> op, String offerAgentWhat){
		if (KnowenAgnet.HasMoreOffers()){

			String[] moves=KnowenAgnet.getOutComeOptions();


			for (int k = 0; k < moves.length; k++) {
				Agent KnowenAgnetCopy=new Agent(KnowenAgnet);
				Agent otheragnetCopy=new Agent(otheragnet);
				String presentMove=PastMove+moves[k]+"->";

				KnowenAgnetCopy.RemoveOutcome(moves[k]);
				outcome OtherAgnetOffer=otheragnetCopy.RemoveOutcome(moves[k]);

				outcome OtherAgnetNextOnTheTable=otheragnetCopy.RemoveBestOutcome();

				if (OtherAgnetNextOnTheTable==null){
					if (OtherAgnetOffer.name.equals(offerAgentWhat)){
						op.add(new options(KnowenAgnetCopy.getPrefrence(),presentMove));

					}
					break;
				}


				//check if the other agent preferred, if gotten to a deal and the deal is not fit , go back
				if (OtherAgnetOffer.value>=OtherAgnetNextOnTheTable.value){
					if ( OtherAgnetOffer.name.equals(offerAgentWhat)){
						op.add(new options(KnowenAgnetCopy.getPrefrence(),presentMove));
					}
					break;

				}

				outcome KnowenAgentValueForOffer=KnowenAgnetCopy.RemoveOutcome(OtherAgnetNextOnTheTable.name);
				otheragnetCopy.CopyBestOutcome();
				presentMove=presentMove+OtherAgnetNextOnTheTable.name+"->";

				//KnowenAgentValueForOffer.value>=KnowenAgentNextOnTheTable.value &&
				if ( KnowenAgentValueForOffer.name.equals(offerAgentWhat)){
					op.add(new options(KnowenAgnetCopy.getPrefrence(),presentMove));
				}
				else{
					FindOrderOfOffersWhenKnownStarting(KnowenAgnetCopy,otheragnetCopy,presentMove,op,offerAgentWhat);
				}

			}
		}
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
	private static void FindOrderOfOffersWhenUnknownStarting(Agent KnowenAgnet,Agent otheragnet,String PastMove,ArrayList<options> op, String offerAgentWhat){
		if (KnowenAgnet.HasMoreOffers()){

			outcome OtherAgnetNextOnTheTable=otheragnet.RemoveBestOutcome();

			outcome KnowenAgentValueForOffer=KnowenAgnet.RemoveOutcome(OtherAgnetNextOnTheTable.name);
			outcome KnowenAgentNextOnTheTable=KnowenAgnet.CopyBestOutcome();
			String presentMove=PastMove+OtherAgnetNextOnTheTable.name+"->";


			if (KnowenAgentNextOnTheTable==null){
				if (OtherAgnetNextOnTheTable.name.equals(offerAgentWhat)){
					op.add(new options(KnowenAgnet.getPrefrence(),presentMove));

				}
				return;
			}

			//KnowenAgentValueForOffer.value>=KnowenAgentNextOnTheTable.value &&
			if ( KnowenAgentValueForOffer.name.equals(offerAgentWhat)){
				op.add(new options(KnowenAgnet.getPrefrence(),presentMove));
				return;
			}



			String[] moves=KnowenAgnet.getOutComeOptions();

			for (int k = 0; k < moves.length; k++) {
				Agent KnowenAgnetCopy=new Agent(KnowenAgnet);
				Agent otheragnetCopy=new Agent(otheragnet);
				String presentMove2=presentMove+moves[k]+"->";

				KnowenAgnetCopy.RemoveOutcome(moves[k]);
				outcome OtherAgnetOffer=otheragnetCopy.RemoveOutcome(moves[k]);
				OtherAgnetNextOnTheTable=otheragnetCopy.CopyBestOutcome();


				//check if the other agent preferred, if gotten to a deal and the deal is not fit , go back
				if (OtherAgnetOffer.value>=OtherAgnetNextOnTheTable.value){
					if ( OtherAgnetOffer.name.equals(offerAgentWhat)){
						op.add(new options(KnowenAgnetCopy.getPrefrence(),presentMove2));
					}
					return;

				}
				else{
					FindOrderOfOffersWhenUnknownStarting(KnowenAgnetCopy,otheragnetCopy,presentMove2,op,offerAgentWhat);
				}



			}
		}
	}



}
