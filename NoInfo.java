import java.util.ArrayList;

public class NoInfo extends Algo{
	static int[] stat;
	
	
	
	/**
	 * no info sharing, a1 do not know a2 and a2 do not know a1
	 * @param out
	 * @param Otheragent
	 * @param offerAgentWhat
	 * @return
	 */
	public static ArrayList<options> NoInfoGame(String[] out,Agent Otheragent,boolean IsOtherAgnetStarting,String offerAgentWhat){
		stat=new int[out.length];
		ArrayList<options> op=new ArrayList<options>();

		ArrayList<String> client1prefernce=AllPossiblePrefrence(out);
		for (int i = 0; i < client1prefernce.size(); i++) {
			String prefrence=client1prefernce.get(i);
			Agent otherAgent  =new Agent(Otheragent);
			Agent myAgnet=new Agent("myagent",prefrence);
			outcome o=null;
			if (IsOtherAgnetStarting){
				o=NoInfoOutCome(otherAgent,myAgnet);

			}
			else{
				o=NoInfoOutCome(myAgnet,otherAgent);


			}
			if (o.name.equals(offerAgentWhat)){
				op.add(new options(prefrence,null));
			}
			int ResultIndex=indexOf(out,o.name);
			stat[ResultIndex]++;
		}
		return op;
	}
	

	public static int[] getStatistic(){
		return stat;
	}
	

	private static outcome NoInfoOutCome(Agent startingagent,Agent otheragnet){
		boolean Dealstatus=false;
		Agent offeringAgent=startingagent;
		Agent offeringWaiting=otheragnet;
		outcome OfferOntheTable=startingagent.RemoveBestOutcome();
		while (!Dealstatus){
			outcome waitingAgentValueToOffer=offeringWaiting.RemoveOutcome(OfferOntheTable.name);
			outcome nextOnTheTable=offeringWaiting.RemoveBestOutcome();

			if (nextOnTheTable==null){
				return waitingAgentValueToOffer;
			}
			else if (waitingAgentValueToOffer.value>=nextOnTheTable.value){
				Dealstatus=true;
			}
			else{
				OfferOntheTable=nextOnTheTable;
				Agent temp= offeringWaiting;
				offeringWaiting=offeringAgent;
				offeringAgent=temp;
			}

		}
		return OfferOntheTable;
	}


	
	
}
