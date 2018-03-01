package Voting;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;



import org.testng.annotations.Test;
import tools.Agent;
import tools.outcome;

public class VotingTest {
	String[] out={"o1","o2","o3","o4"};
	String candidte;
	Agent[] a;
	outcome[] prePrefrnce;
   
	public void O1Fail(){
    	a=new Agent[3];
    	a[0]=new Agent("a1","o1<o2<o3<o4");
    	a[1]=new Agent("a2","o1<o3<o4<o2");
    	a[2]=new Agent("a3","o4<o3<o2<o1");
    	prePrefrnce=Voting.BuildGroupOrder(new VotingProtocol(), out, a);
    }
	
	
    @Test
	public  void testO1() {
    	O1Fail();
		candidte="o1";
		String pre= Voting.CanManipulte(new VotingProtocol(),Arrays.copyOfRange(a, 0, a.length-1),out, candidte);
		assertTrue(pre==null);

	}
    
	@Test
	public  void testO2() {
		O1Fail();
		candidte="o2";
		String pre= Voting.CanManipulte(new VotingProtocol(),Arrays.copyOfRange(a, 0, a.length-1),out, candidte);
		System.out.println("agent wants outcome: "+candidte);
		a[2]=new Agent("a3",pre);
		System.out.println("need to vote like: "+pre);
		System.out.println("new group prefrnce:");		

		outcome[] after=Voting.BuildGroupOrder(new VotingProtocol(), out, a);
		assertTrue(after[0].getName().equals(candidte));
	}
	
	

	@Test
	public  void testO3() {
		O1Fail();
		candidte="o3";
		String pre= Voting.CanManipulte(new VotingProtocol(),Arrays.copyOfRange(a, 0, a.length-1),out, candidte);
		System.out.println("agent wants outcome: "+candidte);
		a[2]=new Agent("a3",pre);
		System.out.println("need to vote like: "+pre);
		System.out.println("new group prefrnce:");		

		outcome[] after=Voting.BuildGroupOrder(new VotingProtocol(), out, a);
		assertTrue(after[0].getName().equals(candidte));
	}
	
	@Test
	public  void testO4() {
		O1Fail();
		candidte="o4";
		String pre= Voting.CanManipulte(new VotingProtocol(),Arrays.copyOfRange(a, 0, a.length-1),out, candidte);
		System.out.println("agent wants outcome: "+candidte);
		a[2]=new Agent("a3",pre);
		System.out.println("need to vote like: "+pre);
		System.out.println("new group prefrnce:");		

		outcome[] after=Voting.BuildGroupOrder(new VotingProtocol(), out, a);
		assertTrue(after[0].getName().equals(candidte));
	}
}
