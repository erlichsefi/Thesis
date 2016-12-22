package Negotiation;
import java.util.ArrayList;

import tools.Agent;
import tools.options;
import tools.outcome;

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
			System.out.println(i+1+"/"+client1prefernce.size());
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
			if (o.getName().equals(offerAgentWhat)){
				op.add(new options(prefrence,null,offerAgentWhat));
			}
			int ResultIndex=indexOf(out,o.getName());
			stat[ResultIndex]++;
		}
		return op;
	}
	

	

	public static outcome NoInfoOutCome(Agent startingagent,Agent otheragnet){
		
		boolean Dealstatus=false;
		Agent offeringAgent=new Agent(startingagent);
		Agent offeringWaiting=new Agent(otheragnet);
		outcome OfferOntheTable=offeringAgent.RemoveBestOutcome();
		outcome waitingAgentValueToOffer=null;
		while (!Dealstatus){
			 waitingAgentValueToOffer=offeringWaiting.RemoveOutcome(OfferOntheTable.getName());
			outcome nextOnTheTable=offeringWaiting.RemoveBestOutcome();

			if (nextOnTheTable==null){
				return waitingAgentValueToOffer;
			}
			else if (waitingAgentValueToOffer.getValue()>=nextOnTheTable.getValue()){
				Dealstatus=true;
			}
			else{
				OfferOntheTable=nextOnTheTable;
				Agent temp= offeringWaiting;
				offeringWaiting=offeringAgent;
				offeringAgent=temp;
			}

		}
		return waitingAgentValueToOffer;
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
