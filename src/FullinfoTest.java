import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

public class FullinfoTest {
	int numberOfRuns=100;
	int MaxNumberOfPrefrence=15;



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




	//@Test
	public  void Look() {

		BufferedWriter gw = null;
		FileWriter gfw = null;
		BufferedWriter bw = null;
		FileWriter bfw = null;

		try {

			gfw = new FileWriter("good.txt");
			gw = new BufferedWriter(gfw);
			bfw = new FileWriter("bad.txt");
			bw = new BufferedWriter(bfw);
			String[] out ={"o1","o2","o3","o4","o5"};

			ArrayList<String> per2=Algo.AllPossiblePrefrence(out);

			for (int j = 0; j < per2.size(); j++) {
				System.out.println((j+1)+"/"+per2.size());
				String p1="o1<o2<o3<o4<o5";
				String p2=per2.get(j);



				Agent otherAgnet=new Agent("P1",p1);
				Agent my=new Agent("P2",p2);
				options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);

				otherAgnet=new Agent("P1",p1);
				my=new Agent("P2",p2);
				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);

				if (o1.getResult().equals(o2.getResult())){
					gw.append("P1 "+p1+"   P2 "+p2+" Owers  "+o2.getResult()+"  Original  "+o1.getResult());
					gw.append(""+o2.longestPathsTurnNumebr());
					gw.append("\n");
				}
				else{
					bw.append("P1 "+p1+"   P2 "+p2+" Owers  "+o2.getResult()+"  Original  "+o1.getResult());
					bw.append(o1.getPaths());
					bw.append("\n");

				}
			}



		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (gw != null)
					gw.close();
				if (gfw != null)
					gfw.close();
				if (bw != null)
					bw.close();
				if (bfw != null)
					bfw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * check all the preference until given N
	 */
	//@Test
	public  void BigNcheckForTeza() {
		int RunTo=15;
		for (int i = 3; i <= RunTo; i++) {

			BufferedWriter gw = null;
			FileWriter gfw = null;
			BufferedWriter bw = null;
			FileWriter bfw = null;

			try {
				gfw = new FileWriter("good"+i+".txt");
				gw = new BufferedWriter(gfw);
				bfw = new FileWriter("bad"+i+".txt");
				bw = new BufferedWriter(bfw);
				String[] out =Algo.BuildOutComeArray(i);

				ArrayList<String> per2=Algo.AllPossiblePrefrence(out);

				for (int j = 0; j < per2.size(); j++) {
					System.out.println((j+1)+"/"+per2.size());
					String p1=Algo.BuildOneOrder(i);
					String p2=per2.get(j);

					Agent otherAgnet=new Agent("P1",p1);
					Agent my=new Agent("P2",p2);
					options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);

					otherAgnet=new Agent("P1",p1);
					my=new Agent("P2",p2);
					String[] o2=Fullinfo.FindBestByPrefernceTezTwo(out,my,otherAgnet);

					if (o1.getResult().equals(o2[0]) || (o2.length>1 && o1.getResult().equals(o2[1]))){
						gw.append("P1 "+p1+"   P2 "+p2+" NEW  "+Arrays.toString(o2)+"  OLD  "+o1.getResult()+" by: "+p2);
						gw.append("\n");
					}
					else{
						bw.append("P1 "+p1+"   P2 "+p2+" NEW  "+Arrays.toString(o2)+"  OLD  "+o1.getResult()+" by: "+p2);
						bw.append("\n");

					}
				}



			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (gw != null)
						gw.close();
					if (gfw != null)
						gfw.close();
					if (bw != null)
						bw.close();
					if (bfw != null)
						bfw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}


	

	/**
	 * a single round of full info negotiation when p1 is starting 
	 */
	//@Test
	public  void SingleTestFullInfo() {
		String[] out={"o1","o2","o3","o4","o5"};
		String per1="o1<o2<o3<o4<o5";
		String per2="o1<o3<o5<o2<o4";
		Agent P1=new Agent("p1",per1);
		Agent P2=new Agent("p2",per2);
		System.out.println(Fullinfo.FindBestByPrefernce(out,P2,P1));
	}
	
	
	
}
