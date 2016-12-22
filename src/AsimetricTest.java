import static org.junit.Assert.*;


import java.util.Random;

import org.junit.Test;

import Negotiation.Algo;
import Negotiation.AsimetricInfo;
import tools.Agent;
import tools.outcome;

public class AsimetricTest {
	int numberOfRuns=100;
	int MaxNumberOfPrefrence=15;

	/**
	 * look on a game when the agent with the NO information is stating
	 */
	//@Test
	public void TestAsimetricGameAgentStarting(){
		String[] out={"o1","o2","o3","o4","o5","o6"};
		String per1="o1<o2<o3<o4<o5<o6";
		Agent A=new Agent("P1",per1);
		AsimetricInfo.AsimetricInfoGameStatistic(out, A, false);
		AsimetricInfo.printStatistic();
	}


	/**
	 * look on a game when the agent with the FULL information is stating
	 */
	//	@Test
	public void TestAsimetricGameAgentNotStarting(){
		String[] out={"o1","o2","o3","o4","o5","o6","o7"};
		String per1="o1<o2<o3<o4<o5<o6<o7";
		Agent A=new Agent("P1",per1);
		AsimetricInfo.AsimetricInfoGameStatistic(out, A, true);
		AsimetricInfo.printStatistic();
	}
	
	
	@Test
	public  void TestAsimetricGameAgentStartingTez() {
		Random r=new Random();
		for (int i = 0; i < numberOfRuns; i++) {
			String[] out=Algo.BuildOutComeArray(new Random().nextInt(MaxNumberOfPrefrence)+2);

			String per1=Algo.randomPrefrenceOver(out);
			String per2=Algo.randomPrefrenceOver(out);
			

			System.out.println(per1);
			System.out.println(per2);
			if (per1!=null && per2!=null ){
				boolean knowStarting=false;
                if (r.nextDouble()>0.5)
                	knowStarting=true;
				
				Agent otherAgnet=new Agent("P2",per2);
				Agent my=new Agent("P1",per1);
				outcome o1=AsimetricInfo.AsimetricInfoGame(my,otherAgnet,knowStarting);

				otherAgnet=new Agent("P2",per2);
				my=new Agent("P1",per1);
				outcome o2=AsimetricInfo.AsimetricInfoGameTeza(my, otherAgnet, knowStarting);

			
				if(!o1.getName().equals(o2.getName()) ){
					assertTrue(false);
				}
				else{
					System.out.println("pass:"+(i+1)+"/"+numberOfRuns);
				}

				System.out.println("******************");
			}
			else{
				i--;
			}
		}

	}
	
	
	
	
}
