
import java.util.ArrayList;

import org.junit.Test;

import Negotiation.Fullinfo;
import Negotiation.NoInfo;
import tools.Agent;
import tools.outcome;

public class NoInfoTest {
	int numberOfRuns=100;
	int MaxNumberOfPrefrence=15;

	/**
	 * look on a game when the agent with the NO information is stating
	 */
	@Test
	public void TestAsimetricGameAgentStarting(){
		String per1="o4<o1<o2<o3<o5";
		String per2="o3<o2<o4<o5<o1";

		Agent p1=new Agent("P1",per1);
		Agent p2=new Agent("P2",per2);

		outcome o=NoInfo.NoInfoOutCome(p1, p2);
		System.out.println(o);
	}

	
	public void print(Agent p1,Agent p2,String[] out){
	
		for (int i = 0; i < out.length; i++) {
			outcome p1o=p1.copyOutcome(out[i]);
			outcome p2o=p2.copyOutcome(out[i]);
			outcome[] o1=p1.CopyNbest((int) (out.length-p1o.getValue()));
			outcome[] o2=p2.CopyNbest((int) (out.length-p2o.getValue()));
			ArrayList<outcome> I=Fullinfo.IntesectGroup(o1, o2);
			


		}
	}
	
	
	
	
}
