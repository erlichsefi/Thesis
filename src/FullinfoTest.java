import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import Negotiation.Algo;
import Negotiation.Fullinfo;
import tools.Agent;
import tools.options;
import tools.outcome;

public class FullinfoTest {
	int numberOfRuns=1;
	int MaxNumberOfPrefrence=3;
	int minNumberOfPrefrence=3;
	Random r=new Random(System.currentTimeMillis());





	@Test
	public  void Draw() {
		String[] out =new String[]{"o1","o2","o3"};
		String	per1="o1<o2<o3";
		String per2="o3<o2<o1";
		System.out.println("P2 "+per2);
		System.out.println("P1 "+per1);


		Agent otherAgnet=new Agent("P1",per1);
		Agent my=new Agent("P2",per2);
		options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);
		o1.getTree().print();
 		System.out.println();


	}


	//@Test
	public  void RandomTez() {
		boolean Random=true;
		String[] out;
		String per1;
		String per2;
		for (int i = 0; i < numberOfRuns; i++) {
			if (Random){
				int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
				out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
				per1=Algo.randomPrefrenceOver(out);
				per2=Algo.randomPrefrenceOver(out);
			}
			else{
				out =new String[]{"o1","o2","o3","o4","o5","o6","o7"};
				per1="o1<o2<o3<o4<o5<o6<o7";
				per2="o7<o4<o3<o6<o1<o5<o2";
			}
			if (per1!=null && per2!=null ){

				System.out.println("P2 "+per2);
				System.out.println("P1 "+per1);


				Agent otherAgnet=new Agent("P1",per1);
				Agent my=new Agent("P2",per2);
				options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);
				o1.getTree().print();
				otherAgnet=new Agent("P1",per1);
				my=new Agent("P2",per2);
				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);

				otherAgnet=new Agent("P1",per1);
				my=new Agent("P2",per2);
				System.out.println(Fullinfo.afterIntersection(out,my,otherAgnet));

				System.out.println("OLD :"+o1);
				System.out.println("NEW :"+o2);
				System.out.println(o1.longestPaths());
				System.out.println(o2.getPaths());

				//	o1.getTree().print();

				if(!o1.getResult().equals(o2.getResult())){
					assertTrue(false);
				}
				else{
					System.out.println("pass:"+(i+1)+"/"+numberOfRuns);
				}

				System.out.println("******************");
			}
		}
		System.out.println();
	}

	/**
	 * this junit suposse to show the new path to the equilibrium
	 * after adding outcome
	 */
	//	@Test
	public  void IndetcionAddOutcome() {
		boolean Random=false;
		String[] out;
		String per1;
		String per2;
		for (int i = 0; i < numberOfRuns; i++) {
			if (Random){
				int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
				out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
				per1=Algo.randomPrefrenceOver(out);
				per2=Algo.randomPrefrenceOver(out);
			}
			else{
				out =new String[]{"o1","o2","o3","o4"};
				per1="o1<o2<o3<o4";
				per2="o1<o3<o4<o2";
			}
			if (per1!=null && per2!=null ){

				System.out.println("P2 "+per2);
				System.out.println("P1 "+per1);


				Agent otherAgnet=new Agent("P1",per1);
				Agent my=new Agent("P2",per2);
				options o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);

				//define new outcome
				String  NewOutcome="o"+(out.length+1);
				String[] out2=Arrays.copyOf(out, out.length+1);
				out2[out2.length-1]=NewOutcome;

				System.out.println("** OLD **");
				System.out.println("Result "+o1.getResult());
				System.out.println("Path "+o1.getOrder(o1.longestPaths()));



				String[] p1Order=per1.split("<");

				String[] p2Order=per2.split("<");
				for (int j = 0; j <= p1Order.length; j++) {
					for (int j1 = 0; j1 <=p2Order.length; j1++) {

						String newP1="";
						for (int j2 = 0; j2 < p1Order.length; j2++) {
							if (j2==j){
								newP1=newP1+NewOutcome+"<";
							}
							newP1=newP1+p1Order[j2]+"<";
						}
						if (p1Order.length==j){
							newP1=newP1+NewOutcome+"<";
						}
						newP1=newP1.substring(0, newP1.length()-1);
						String newP2="";
						for (int j2 = 0; j2 < p2Order.length; j2++) {
							if (j2==j1){
								newP2=newP2+NewOutcome+"<";
							}
							newP2=newP2+p2Order[j2]+"<";
						}
						if (p2Order.length==j1){
							newP2=newP2+NewOutcome+"<";
						}
						newP2=newP2.substring(0, newP2.length()-1);

						otherAgnet=new Agent("P1",newP1);
						my=new Agent("P2",newP2);
						options o2=Fullinfo.FindBestByPrefernceTez(out2,my,otherAgnet);
						System.out.print("|");
						System.out.print("P1 "+newP1+",P2 "+newP2);
						System.out.print(" Path "+o2.getOrder(o2.longestPaths())+o2.getResult());

					}
					System.out.println();
					System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				}

				System.out.println("******************");
			}
		}
		System.out.println();
	}



	/**
	 * Testing if removing a outcome before the equilibrium , and add a new outcome in the end of
	 * the preference order of both players will affect the Path to the equilibrium. 
	 */
	//	@Test
	public  void AddingOutComeTest() {
		boolean Random=false;
		String[] out;
		String per1;
		String per2;
		for (int i = 0; i < numberOfRuns; i++) {
			if (Random){
				int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
				out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
				per1=Algo.randomPrefrenceOver(out);
				per2=Algo.randomPrefrenceOver(out);
			}
			else{
				out =new String[]{"o1","o2","o3","o4","o5","o6"};
				per1="o2<o3<o4<o1<o6<o5";
				per2="o5<o6<o1<o2<o3<o4";
			}
			if (per1!=null && per2!=null ){

				System.out.println("P2 "+per2);
				System.out.println("P1 "+per1);

				Agent otherAgnet=new Agent("P1",per1);
				Agent my=new Agent("P2",per2);

				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);

				System.out.println("The result is: "+o2.getResult());
				String[] StartingAgent= per1.split("<");
				boolean PassEquilibrium=false;
				boolean ExistOne=false;
				for (int j = StartingAgent.length-1; j >=0; j--) {
					if (PassEquilibrium){
						System.out.println(StartingAgent[j]);

						Agent CotherAgnet=new Agent("P1",per1);
						Agent Cmy=new Agent("P2",per2);
						if(Fullinfo.IsReplaceingOutcomeWorks(out, Cmy, CotherAgnet, StartingAgent[j])){
							ExistOne=true;
						}
					}
					else if (StartingAgent[j].equals(o2.getResult())){
						PassEquilibrium=true;
					}
				}
				assertTrue(ExistOne);


				System.out.println("pass:"+(i+1)+"/"+numberOfRuns);


				System.out.println("******************");
			}
		}
		System.out.println();
	}

	//@Test
	public void PrintStatistics(){
		boolean Random=false;
		String[] out;
		String per2;
		String Wanted="o3";
		if (Random){
			int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
			out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
			per2=Algo.randomPrefrenceOver(out);
		}
		else{
			out =new String[]{"o1","o2","o3","o4","o5","o6","o7"};
			per2="o2<o6<o4<o3<o5<o1<o7";
		}

		if (per2!=null ){
			ArrayList<String> BetterThenWantedInP2ShowInUpperP1=new ArrayList<String>();
			ArrayList<String> BetterThenWantedInP2NOTShowInUpperP1=new ArrayList<String>();



			ArrayList<String> Pper=Algo.AllPossiblePrefrence(out);
			for (int i = 0; i < Pper.size(); i++) {

				if (Fullinfo.IsBetterThenWantedInP2ShowInUpperP1(Pper.get(i),per2,Wanted,out.length)){
					BetterThenWantedInP2ShowInUpperP1.add(Pper.get(i));

				}
				else{
					BetterThenWantedInP2NOTShowInUpperP1.add(Pper.get(i));

				}

			}


			for (int i = 0; i < BetterThenWantedInP2NOTShowInUpperP1.size(); i++) {
				Agent p1=new Agent("P1",BetterThenWantedInP2NOTShowInUpperP1.get(i));
				Agent p2=new Agent("P2",per2);
				if (!Fullinfo.FindBestByPrefernceTez(out, p2, p1).getResult().equals(Wanted)){
					System.out.println(BetterThenWantedInP2NOTShowInUpperP1.get(i));

				}
			}




			int c=0;
			for (int i = 0; i < BetterThenWantedInP2ShowInUpperP1.size(); i++) {
				Agent p1=new Agent("P1",BetterThenWantedInP2ShowInUpperP1.get(i));
				Agent p2=new Agent("P2",per2);

				if (!Fullinfo.FindBestByPrefernceTez(out, p2, p1).getResult().equals(Wanted)){
					c++;

				}
			}
			System.out.println(c);
			System.out.println(BetterThenWantedInP2ShowInUpperP1.size());

		}
		System.out.println(out.length);
		System.out.println("Done");
	}
	//@Test
	public  void BuildPrefrenceTest() {
		boolean Random=false;
		String[] out;
		String per2;
		for (int i = 0; i < numberOfRuns; i++) {
			if (Random){
				int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
				out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
				per2=Algo.randomPrefrenceOver(out);
			}
			else{
				out =new String[]{"o1","o2","o3","o4","o5","o6"};
				per2="o2<o6<o4<o3<o5<o1";
			}
			if (per2!=null ){

				for (int j = 0; j < out.length; j++) {
					String Wantout=out[j];	

					Agent otherAgnet=new Agent("P2",per2);
					ArrayList<String> o1=Fullinfo.BuildWinningPrefrence(otherAgnet, Wantout, true);

					for (int k = 0; k < o1.size(); k++) {
						System.out.println("P2 "+per2);

						System.out.println("P1 "+o1.get(k));
						Agent CopyotherAgnet=new Agent(otherAgnet);
						Agent MyAgnet=new Agent("P1",o1.get(k));
						options op1=Fullinfo.FindBestByPrefernceTez(out, CopyotherAgnet, MyAgnet);
						assertTrue(op1.getResult().equals(Wantout));
					}

				}
			}
		}
		System.out.println();
	}


	//@Test
	public  void Compre() {
		boolean Random=false;
		String[] out;
		String per1;
		String per2;
		for (int i = 0; i < numberOfRuns; i++) {
			if (Random){
				int RandomResult=r.nextInt(minNumberOfPrefrence);
				out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
				per1=Algo.randomPrefrenceOver(out);
				per2=Algo.randomPrefrenceOver(out);
			}
			else{
				out =new String[]{"o1","o2","o3"};
				per1="o3<o1<o2";
				per2="o3<o2<o1";
			}
			if (per1!=null && per2!=null ){
				System.out.println("Before:");
				System.out.println("P2 "+per2);
				System.out.println("P1 "+per1);

				Agent otherAgnet=new Agent("P1",per1);
				Agent my=new Agent("P2",per2);
				options o1=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);

				otherAgnet=new Agent("P1",per1);
				my=new Agent("P2",per2);
				options _o1=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);

				System.out.println("OLD :"+_o1.getOrder(o1.longestPaths()));
				//3
				per2=PlaceBefore("o"+(out.length+1),_o1.getResult(),per2);
				per1=PlaceAfter("o"+(out.length+1),_o1.getResult(),per1);
				out=Arrays.copyOf(out, out.length+1);
				out[out.length-1]="o"+(out.length);

				System.out.println();
				System.out.println("After:");
				System.out.println("P2 "+per2);
				System.out.println("P1 "+per1);
				otherAgnet=new Agent("P1",per1);
				my=new Agent("P2",per2);
				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);


				otherAgnet=new Agent("P1",per1);
				my=new Agent("P2",per2);
				options _o2=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);


				System.out.println("NEW :"+_o2.getOrder(o2.longestPaths()));
				if ((out.length-1%2)==0){
					assertTrue(_o2.getResult().equals(out[out.length-1]));
				}
				else{
					assertTrue(_o2.getResult().equals(_o1.getResult()));
				}


				System.out.println("pass:"+(i+1)+"/"+numberOfRuns);


				System.out.println("******************");
			}
		}
		System.out.println();
	}


	private String PlaceIN(String add, int p, String per1) {
		String[] pr=per1.split("<");
		String ans=pr[0];
		for (int i = 1; i < pr.length; i++) {
			if (i==p){
				ans=ans+"<"+add;
			}
			ans=ans+"<"+pr[i];
		}
		if (pr.length==p){
			ans=ans+"<"+add;
		}
		return ans;
	}
	private String PlaceBefore(String add, String result, String per1) {
		String[] pr=per1.split("<");
		String ans=pr[0];
		int p=RandomPlaceBefore(result,per1);
		for (int i = 1; i < pr.length; i++) {
			if (i==p){
				ans=ans+"<"+add;
			}
			ans=ans+"<"+pr[i];
		}
		return ans;
	}
	public int RandomPlaceBefore(String result, String per1){
		String[] pr=per1.split("<");
		int i=0;
		for ( i = 0; i < pr.length; i++) {
			if (pr[i].equals(result)){
				break;
			}
		}
		return  r.nextInt(i)+1;
	}

	public int RandomPlaceAfter(String result, String per1){
		String[] pr=per1.split("<");
		int i=0;
		for ( i = 0; i < pr.length; i++) {
			if (pr[i].equals(result)){
				break;
			}
		}
		return  r.nextInt(i)+1+(pr.length-i);
	}

	private String PlaceAfter(String add, String result, String per1) {
		String[] pr=per1.split("<");
		int p=RandomPlaceAfter(result,per1);

		String ans=pr[0];
		for (int i = 1; i < pr.length; i++) {
			ans=ans+"<"+pr[i];
			if (i==p){
				ans=ans+"<"+add;
			}

		}
		return ans;
	}


	//@Test
	public  void WriteWorngsToFile() {

		BufferedWriter gw = null;
		FileWriter gfw = null;
		BufferedWriter bw = null;
		FileWriter bfw = null;

		try {

			gfw = new FileWriter("good.txt");
			gw = new BufferedWriter(gfw);
			bfw = new FileWriter("bad.txt");
			bw = new BufferedWriter(bfw);
			String[] out ={"o1","o2","o3","o4","o5","o6","o7"};

			ArrayList<String> per2=Algo.AllPossiblePrefrence(out);

			for (int j = 0; j < per2.size(); j++) {
				System.out.println((j+1)+"/"+per2.size());
				String p1="o1<o2<o3<o4<o5<o6<o7";

				String p2=per2.get(j);



				Agent otherAgnet=new Agent("P1",p1);
				Agent my=new Agent("P2",p2);
				options o1=Fullinfo.FindBestByPrefernceTezNoIntersection(out,my,otherAgnet);

				otherAgnet=new Agent("P1",p1);
				my=new Agent("P2",p2);
				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);


				otherAgnet=new Agent("P1",p1);
				my=new Agent("P2",p2);
				Agent[] a=Fullinfo.afterIntersection(out,my,otherAgnet);
				Agent[] a1=Fullinfo.afterUnionOneByOneFirstToLast(a[1], a[0]);

				if (o1.getResult().equals(o2.getResult())){
					gw.append("P1 "+p1+"    \nP2 "+p2+" Owers  "+o2.getResult()+"  Original  "+o1.getResult());
					gw.append(" ");
					gw.append("\n");
					gw.append("starting: "+a1[0].getAgentName()+" "+a1[0].getPrefrenceAboutCurrentOptions()+"\n");
					gw.append("secend: "+a1[1].getAgentName()+" "+a1[1].getPrefrenceAboutCurrentOptions());
					gw.append("\n");
					gw.append("\n");

				}
				else{


					bw.append("P1 "+p1+"    \nP2 "+p2+" Owers  "+o2.getResult()+"  Original  "+o1.getResult());
					bw.append(" ");
					bw.append("\n");
					bw.append("\n");
					bw.append("starting: "+a1[0].getAgentName()+" "+a1[0].getPrefrenceAboutCurrentOptions()+"\n");
					bw.append("secend: "+a1[1].getAgentName()+" "+a1[1].getPrefrenceAboutCurrentOptions());
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
