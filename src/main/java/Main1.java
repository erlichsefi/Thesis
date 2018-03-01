import java.util.Arrays;

import Negotiation.NoInfo;
import Voting.Voting;
import Voting.VotingProtocol;
import tools.Agent;
import tools.outcome;

public class Main1 {

	public static void main(String[] args){
	    Agent otherAgnet=new Agent("other agent","o1<o2<o3<o4");

		String Candidte="o4";
		String[] out={"o1","o2","o3","o4"};
		Agent[] a=new Agent[4];
		a[0]=new Agent("a1","o1<o2<o3<o4");
		a[1]=new Agent("a2","o1<o3<o4<o2");
		a[2]=new Agent("a3","o4<o3<o2<o1");
	   
		System.out.println("mainpultor want:");
		System.out.println(Candidte);
	    System.out.println("*******************************************");

		outcome[] beforeMani=Voting.BuildGroupOrder(new VotingProtocol(), out, Arrays.copyOfRange(a, 0, a.length-1));
	    String GroupP= Voting.sortedOutcomeToPrefrnce(beforeMani);
	    
	    System.out.println("before manipultion group prefrence are: ");
	    System.out.println(GroupP);
	    System.out.println("*******************************************");
	    
	    Agent group=new Agent("group",GroupP);
	    System.out.println("with Negotiation the result will be: ");
	    System.out.println(NoInfo.NoInfoOutCome(otherAgnet, group));
		String Mainprfence= Voting.CanManipulte(new VotingProtocol(),Arrays.copyOfRange(a, 0, a.length-1),out, Candidte);
		System.out.println("*******************************************");
		if (Mainprfence!=null){
		System.out.println("the mainfultoe need to vote as: "+Mainprfence);
		System.out.println("*******************************************");

		a[3]=new Agent("manipultor",Mainprfence);
	    outcome[] AfterMani=Voting.BuildGroupOrder(new VotingProtocol(), out, Arrays.copyOfRange(a, 0, a.length));
	    System.out.println("after manipultion group prefrence are: ");
	    System.out.println(Voting.sortedOutcomeToPrefrnce(AfterMani));


	     group=new Agent("group",Voting.sortedOutcomeToPrefrnce(AfterMani));
	    outcome FinalOut=NoInfo.NoInfoOutCome(otherAgnet, group);
		System.out.println("*******************************************");

	    System.out.println("The final Outcome is:");
	     System.out.println(FinalOut.getName());
		}
		else{
			System.err.println("can't manipulte");
		}

	}



}
