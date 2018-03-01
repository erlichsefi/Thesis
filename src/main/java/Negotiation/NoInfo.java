package Negotiation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
		String staetingName=offeringAgent.getAgentName();
		Agent offeringWaiting=new Agent(otheragnet);
		outcome offer=offeringAgent.NoInfoTurnVersion1(null);
		outcome Tempoffer=offer;
		while (Tempoffer!=null){
			offer=Tempoffer;
			if (staetingName.equals(offeringWaiting)) {
				Tempoffer = offeringWaiting.NoInfoTurnVersion1(offer);
			}
			else{
				Tempoffer = offeringWaiting.NoInfoTurnVersion2(offer);

			}
			//swap
			Agent temp=offeringWaiting;
			offeringWaiting=offeringAgent;
			offeringAgent=temp;
		}
		return offer;
	}

	public static HashMap<String,outcome> StratrgyNoInfoOutCome(Agent _p1, Agent _p2, GetWhatToOfferStrategy s1, GetWhatToOfferStrategy s2){
		Agent p1=new Agent(_p1);
		Agent p2=new Agent(_p2);
		return s1.NoInfoTurn(p1,p2,s2,null);
	}

	public static Set<String> StratrgyNoInfoOutCome(Agent _p1, Agent _p2, GetResltsStrategy s1, GetResltsStrategy s2){
		Agent p1=new Agent(_p1);
		Agent p2=new Agent(_p2);
		return s1.NoInfoTurn(p1,p2,s2,null,"");
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

	public static void main(String[] args) {
		String[] out={"o1","o2","o3","o4","o5","o6","o7"};
		String per1="o1<o2<o3<o4<o5<o6<o7";
		Agent p2=new Agent("P2",per1);
		ArrayList<options> op=NoInfoGame(out,p2,false,"o1");
		for (int i = 0; i < out.length; i++) {
			System.out.println(op.get(i));
		}

	}


}
