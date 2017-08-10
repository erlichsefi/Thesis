import java.util.ArrayList;

import Negotiation.Algo;
import Negotiation.Fullinfo;
import Voting.VotingProtocol;
import tools.Agent;
import tools.options;
import tools.outcome;

public class VFN {

	
	public static ArrayList<String> BFprefrenceInVotingToObtin(String[] out,String goal,Agent[] agents,Agent op,VotingProtocol p){
		ArrayList<String> result=new ArrayList<String>();
		ArrayList<String>  all=Algo.AllPossiblePrefrence(out);
		for (String pref:all) {
			String jp=p.asSWF(out,agents,new Agent("p1",pref),new Agent(op));
			Agent p1=new Agent("jp",jp);
			options o=Fullinfo.FindBestByPrefernceTez(out, new Agent(op), p1);
			if (o.getResult()!=null && o.getResult().equals(goal)){
				result.add(pref+" jp: "+jp);
			}

		}
		return result;
	}
	
	public static void main(String[] args) {
		String[] out ={"o1","o2","o3","o4","o5","o6","o7","o8"};
		String per1="o1<o2<o3<o4<o5<o6<o7<o8";
		Agent op=new Agent("op",per1);
		Agent[] a=new Agent[]{new Agent("a1","o1<o2<o3<o4<o5<o6<o7<o8"),new Agent("a1","o1<o2<o3<o4<o5<o6<o7<o8")};
		ArrayList<String> o=BFprefrenceInVotingToObtin(out,"o7",a,op,new VotingProtocol());
		for (String str:o) {
			System.out.println(str);
		}
		System.out.println(o.size());
		System.out.println("Done");
	}
}
