import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import Negotiation.Algo;
import Negotiation.AsimetricInfo;
import Negotiation.Fullinfo;
import tools.Agent;
import tools.options;
import tools.outcome;

public class TezTest {
	int numberOfRuns=100;
	int MaxNumberOfPrefrence=7;



	//@Test
	public  void RandomTez() {
		for (int i = 0; i < numberOfRuns; i++) {
			//	String[] out=BuildPrefrncesArray(new Random().nextInt(MaxNumberOfPrefrence));
			String[] out ={"o1","o2","o3","o4","o5","o6"};

			String per1=Algo.randomPrefrenceOver(out);
			String per2=Algo.randomPrefrenceOver(out);
			//String[] out ={"o1","o2","o3","o4"};
			//String per2="o4<o1<o6<o2<o5<o3";
			//String per1="o1<o2<o3<o5<o4<o6";

			if (per1!=null && per2!=null ){

				System.out.println("P2 "+per2);
				System.out.println("P1 "+per1);


				Agent otherAgnet=new Agent("P1",per1);
				Agent my=new Agent("P2",per2);
				options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);

				otherAgnet=new Agent("P1",per1);
				my=new Agent("P2",per2);
				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);

				System.out.println("OLD :"+o1);
				System.out.println("NEW :"+o2);
				System.out.println(o1.getPaths());
				if(!o1.getResult().equals(o2.getResult()) ){
					//System.out.println("NEW :"+o2);
					assertTrue(false);
				}
				else{
					System.out.println("pass:"+(i+1)+"/"+numberOfRuns);
				}

				System.out.println("******************");
			}
		}

	}




	//	private String[] BuildPrefrncesArray(int nextInt) {
	//		String[] out=new String[nextInt];
	//		for (int i = 0; i < out.length; i++) {
	//			out[i]="o"+(i+1);
	//		}
	//		return out;
	//	}



	//@Test
	public  void SingleTestFullInfo() {
		String[] out={"o1","o2","o3"};
		String per1="o3<o2<o1";
		String per2="o2<o3<o1";
		Agent otherAgnet=new Agent("other agent",per1);
		Agent my=new Agent("me",per2);
		options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);
		System.out.println(o1);

	}

	//@Test
	public void TestAsimetricGameAgentStarting(){
		String[] out={"o1","o2","o3","o4","o5"};
		String per1="o1<o2<o3<o4<o5";
		Agent A=new Agent("P1",per1);
		System.out.println(AsimetricInfo.AsimetricInfoGame(out, A, false));
		AsimetricInfo.printStatistic();
	}
	
	@Test
	public void TestAsimetricGameAgentNotStarting(){
		String[] out={"o1","o2","o3","o4","o5"};
		String per1="o1<o2<o3<o4<o5";
		Agent A=new Agent("P1",per1);
		AsimetricInfo.AsimetricInfoGame(out, A, true);
		AsimetricInfo.printStatistic();
	}
}
