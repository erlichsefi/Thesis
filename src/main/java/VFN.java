import java.util.ArrayList;
import java.util.Arrays;

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
		String[] out ={"o1","o2","o3","o4","o5"};
		String per1="o1<o2<o3<o4<o5";
		Agent op=new Agent("op",per1);
		Agent[] a=new Agent[]{new Agent("a1","o4<o2<o3<o1<o5"),new Agent("a1","o2<o4<o3<o1<o5")};
		VotingProtocol p=new VotingProtocol();
		String[][] m=p.SWFasMatix(out,a);
		for(String[] m1:m){
			System.out.println(Arrays.toString(m1));
		}
		String[][] m2=p.SWFasMatix(out,a,new Agent("a1","o1<o2<o3<o4<o5"));
		for(String[] m1:m2){
			System.out.println(Arrays.toString(m1));
		}

		ArrayList<String> o=BFprefrenceInVotingToObtin(out,"o3",a,op,p);
		for (String str:o) {
			System.out.println(str);
		}
		System.out.println(o.size());
		System.out.println("Done");
	}
}
