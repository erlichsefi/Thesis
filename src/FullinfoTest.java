import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import DrawTree.DecisionTreePrinter;
import Negotiation.Algo;
import Negotiation.Fullinfo;
import tools.Agent;
import tools.Preferences;
import tools.options;
import tools.outcome;

public class FullinfoTest {
	int numberOfRuns=100;
	int MaxNumberOfPrefrence=9;
	int minNumberOfPrefrence=9;
	Random r=new Random(System.currentTimeMillis());


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
				new DecisionTreePrinter(o1.getTree()).show();
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
	public  void NumberOfPaths() {
		boolean Random=true;
		String[] out;
		String per1;
		String per2;
		for (int i = 0; i < numberOfRuns; i++) {
			if (Random){
				int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
				out=Algo.BuildOutComeArray(MaxNumberOfPrefrence);
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

				options o2=Fullinfo.FindBestByPrefernce(out,my,otherAgnet);
				ArrayList<String> path=o2.getPathslist();
				int number=Fullinfo.NumberOfElementRemoveAtIntersection(out,my,otherAgnet);

				int fact = 1; // this  will be the result
				for (int j = 1; j <= number; j++) {
					fact *= j;
				}
				System.out.println(number);
				for (int j = 0; j < path.size(); j++) {
					System.out.println(path.get(j));
				}
				assertTrue(fact<=path.size());


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
				per2=Preferences.PlaceBefore("o"+(out.length+1),_o1.getResult(),per2);
				per1=Preferences.PlaceAfter("o"+(out.length+1),_o1.getResult(),per1);
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





	//@Test
	public  void WriteWorngsToFile() {
		BufferedWriter gw = null;
		FileWriter gfw = null;
		BufferedWriter bw = null;
		FileWriter bfw = null;
		int Work=0;
		int NotWork=0;
		try {

			gfw = new FileWriter("good.txt");
			gw = new BufferedWriter(gfw);
			bfw = new FileWriter("bad.txt");
			bw = new BufferedWriter(bfw);
			String[] out ={"o1","o2","o3","o4","o5"};//,"o6","o7","o8","o9"};

			ArrayList<String> allper2=Algo.AllPossiblePrefrence(out);

			for (int j = 0; j < allper2.size(); j++) {
				System.out.println((j+1)+"/"+allper2.size());
				String per1="o1<o2<o3<o4<o5";//<o6<o7<o8"<o9";
				String per2=allper2.get(j);


				Agent p1=new Agent("P1",per1);
				Agent p2=new Agent("P2",per2);

				options Original=Fullinfo.FindBestByPrefernce(out,p2,p1);

				p1=new Agent("P1",per1);
				p2=new Agent("P2",per2);

				options newO=Fullinfo.FindBestByPrefernceTezNoIntersection(out,p2,p1);

				ArrayList<String> DontAsPathTo=new ArrayList<String>();
				for (int i = 0; i < out.length; i++) {
					if (!out[i].equals(Original.getResult())){
						p1=new Agent("P1",per1);
						p2=new Agent("P2",per2);
						ArrayList<String> pathTo=new ArrayList<String>();
						Fullinfo.FullInfoFindPathOutcomeInIntersection(p1,p2,"",null,out[i],1,pathTo);
						if (pathTo.isEmpty()){
							DontAsPathTo.add(out[i] );
						}
					}
				}
				ArrayList<String> pathTOeq=new ArrayList<String>();
				Fullinfo.FullInfoFindPathOutcomeInIntersection(p1,p2,"",null,Original.getResult(),2,pathTOeq);

				if (!newO.getResult().equals(Original.getResult())){

					//if (!DontAsPathTo.isEmpty() || !pathTOeq.isEmpty()){
					bw.append("P1 "+per1+"    \nP2 "+per2);//'+" Owers  "+o2.getResult()+"  Original  "+o1.getResult());
					bw.append("\n");
					bw.append("\n");
					bw.append(Original.getResult()+"\n");
					bw.append("**\n");

					bw.append("as no path to lowers\n");

					for (String str:DontAsPathTo) {
						bw.append(str+"\n");
					}
					bw.append("path to the eq="+Original.getResult()+"\n");

					for (String str:pathTOeq) {
						bw.append(str+"\n");
					}
					bw.append("\n");

					NotWork++;
				}
				else{
					gw.append("P1 "+per1+"    \nP2 "+per2);//'+" Owers  "+o2.getResult()+"  Original  "+o1.getResult());
					gw.append("\n");
					gw.append("\n");
					gw.append(Original.getResult()+"\n");
					gw.append("**\n");

					for (String str:DontAsPathTo) {
						gw.append(str+"\n");
					}
					gw.append("\n");
					Work++;
				}
				//CriticalSteps(out,per1,per2,bw);
			}
			System.out.println("Work= "+Work+" Not Work= "+NotWork);

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
	 * testing the lemma that if the intersection is size x 
	 * the intersection of the upper is x+1
	 */
	//@Test
	public  void TestIresults() {


		String[] out ={"o1","o2","o3","o4","o5","o6","o7","o8","o9","o10"};

		ArrayList<String> per22=Algo.AllPossiblePrefrence(out);

		for (int j = 0; j < per22.size(); j++) {
			String per1="o1<o2<o3<o4<o5<o6<o7<o8<o9<o10";
			String per2=per22.get(j);


			Agent p1=new Agent("P1",per1);
			Agent p2=new Agent("P2",per2);
			int numberOfPassStarting;
			int numberOfPassWaiting;
			if (out.length%2==0){
				numberOfPassStarting=out.length/2-1;
				numberOfPassWaiting=out.length/2;
			}
			else{
				numberOfPassStarting=out.length/2;
				numberOfPassWaiting=out.length/2;
			}

			outcome[] SAgentWorst=p1.CopyNworst(numberOfPassStarting);
			outcome[] WAgentWorst=p2.CopyNworst(numberOfPassWaiting);
			int lowerInstesectionSize=Fullinfo.IntesectGroup(SAgentWorst, WAgentWorst).size();
			outcome[] SAgentBest=p1.CopyNbest(out.length-numberOfPassStarting);
			outcome[] WAgentBest=p2.CopyNbest(out.length-numberOfPassWaiting);

			ArrayList<outcome> up=Fullinfo.IntesectGroup(WAgentBest, SAgentBest);
			int upperInstesectionSize=up.size();

			options o=Fullinfo.FindBestByPrefernceTez(out, p2, p1);
			boolean IsInside=false;
			for (outcome o1:up){
				if (o1.getName().equals(o.getResult())){
					IsInside=true;
				}
			}
			assertTrue(IsInside);
			assertTrue(lowerInstesectionSize+1==upperInstesectionSize);
			System.out.println("**"+j+"**");
		}

	}


	public  void CriticalSteps(String[] out,String per1,String per2, BufferedWriter bw) throws IOException {
		Agent P1=new Agent("p1",per1);
		Agent P2=new Agent("p2",per2);
		Agent SWAP=null;
		options o=Fullinfo.FindBestByPrefernceTez(out,new Agent(P2),new Agent(P1));
		String result=o.getResult();
		String order=o.getOrders();
		bw.write("Comp= "+Fullinfo.IsThereComption(P1.getPrefrenceAboutCurrentOptions(),P2.getPrefrenceAboutCurrentOptions())+"\n");
		bw.write("Original Result= "+result+"\n");
		bw.write("path= "+order+"\n");

		//WHILE THERE IS ITERSECTION
		while (Fullinfo.IntersectionSize(out,new Agent(P2),new Agent(P1))>0){

			String removed=P2.RemoveNworst(1)[0].getName();
			P1.RemoveOutcome(removed);
			out=Remove(out,removed);
			SWAP=new Agent(P1);
			P1=new Agent(P2);
			P2=new Agent(SWAP);
			o=Fullinfo.FindBestByPrefernceTez(out,new Agent(P2),new Agent(P1));
			String resultAfterStep=o.getResult();
			String path=o.getOrders();
			if (!resultAfterStep.equals(result)){
				bw.write("************X*****************\n");
				//	bw.write("Comp= "+IsThereComption(P1.getPrefrenceAboutCurrentOptions(),P2.getPrefrenceAboutCurrentOptions())+"\n");
				bw.write("removed= "+removed+"\n");
				bw.write("path= "+path+"\n");
				bw.write(P1.getAgentName()+" "+P1.getPrefrenceAboutCurrentOptions()+""+"\n");
				bw.write(P2.getAgentName()+" "+P2.getPrefrenceAboutCurrentOptions()+""+"\n");
				bw.write("Current result= "+resultAfterStep+"\n");
				bw.write("*****************************"+"\n");
				bw.write(""+"\n");
				result=resultAfterStep;

			}
			else{
				bw.write("************V*****************"+"\n");
				bw.write("Comp= "+Fullinfo.IsThereComption(P1.getPrefrenceAboutCurrentOptions(),P2.getPrefrenceAboutCurrentOptions())+"\n");

				bw.write("removed= "+removed+"\n");
				bw.write(P1.getAgentName()+" "+P1.getPrefrenceAboutCurrentOptions()+""+"\n");
				bw.write(P2.getAgentName()+" "+P2.getPrefrenceAboutCurrentOptions()+""+"\n");
				bw.write("*****************************"+"\n");
				bw.write(""+"\n");

			}

		}
		o=Fullinfo.FindBestByPrefernceTez(out,new Agent(P2),new Agent(P1));
		String resultAfterStep=o.getResult();
		if (!resultAfterStep.equals(result)){
			bw.write("************X*****************"+"\n");


		}
		else{
			bw.write("************V*****************"+"\n");

		}
		bw.write("Comp= "+Fullinfo.IsThereComption(P1.getPrefrenceAboutCurrentOptions(),P2.getPrefrenceAboutCurrentOptions())+"\n");
		bw.write(P1.getAgentName()+" "+P1.getPrefrenceAboutCurrentOptions()+""+"\n");
		bw.write(P2.getAgentName()+" "+P2.getPrefrenceAboutCurrentOptions()+""+"\n");
		bw.write("*****************************"+"\n");
		bw.write("################################################################"+"\n");

	}



	private String[] Remove(String[] out, String removed) {
		String[] out2=new String[out.length-1];
		int o2=0;
		for (int i = 0; i < out.length; i++) {
			if (!out[i].equals(removed)){
				out2[o2++]=out[i];
			}
		}
		return out2;
	}

	/**
	 * a single round of full info negotiation when p1 is starting 
	 */
	//@Test
	public  void SingleTestFullInfo() {
		String[] out ={"o1","o2","o3","o4","o5"};
		String per2="o1<o3<o2<o4<o5";
		String per1="o2<o3<o1<o5<o4";
		Agent P1=new Agent("p1",per1);
		Agent P2=new Agent("p2",per2);
		System.out.println(Fullinfo.FindBestByPrefernce(out,P2,P1));
		System.out.println();
	}
	
	/**
	 * a single round of full info negotiation when p1 is starting 
	 */
	@Test
	public  void ManiTest() {
		String[] out ={"o1","o2","o3","o4","o5"};
		String per1="o2<o3<o1<o5<o4";
		String per2="o1<o2<o3<o4<o5";
		Agent P1=new Agent("p1",per1);
		Agent P2=new Agent("p2",per2);
		options op=Fullinfo.FindBestByPrefernceTez(out,P2,P1);
		System.out.println(op);
		System.out.println(Fullinfo.WinningPrefrenceV2(per1, per2, false, out, "o5"));
	}

	/**
	 * a single round of full info negotiation when p1 is starting 
	 */
	//@Test
	public  void goalprefrence() {
		boolean otheragentstarts=false;
		String goal="o3";
		String[] out ={"o1","o2","o3","o4","o5"};
		String per2="o1<o2<o3<o4<o5";
		Agent P2=new Agent("p2",per2);
		ArrayList<String> win=Fullinfo.WinningPrefrence(P2, otheragentstarts, out, goal);
		for (String per1:win) {
			Agent CP1=new Agent("p1",per1);
			Agent CP2=new Agent(P2);
			options outcome=null;
			if (otheragentstarts){
				outcome=Fullinfo.FindBestByPrefernceTez(out, CP1, CP2);
			}
			else{
				outcome=Fullinfo.FindBestByPrefernceTez(out, CP2, CP1);
			}
			System.out.println(per1);
			if (!outcome.getResult().equals(goal)){

				System.out.println(outcome.longestPaths());
				System.out.println(outcome.getResult());
				Assert.assertTrue(false);
				break;
			}
		}
		System.out.println(win.size());

	}



	//@Test
	public  void isallgoalprefrence() {
		boolean otheragentstarts=false;
		String goal="o4";
		String[] out ={"o1","o2","o3","o4","o5"};
		Agent P2=new Agent("p2","o1<o2<o3<o4<o5");
							   //o1<o4<o3<o5<o2		 
		ArrayList<String> win=Fullinfo.WinningPrefrence(P2, otheragentstarts, out, goal);
		// add elements to al, including duplicates
		Set<String> hs = new HashSet<String>();
		hs.addAll(win);
		win.clear();
		win.addAll(hs);
		System.out.println("mani count= "+win.size());

		ArrayList<String> bf=new ArrayList<String>();
		ArrayList<String> Pper=Algo.AllPossiblePrefrence(out);

		for (String per1:Pper) {
			Agent CP1=new Agent("p1",per1);
			Agent CP2=new Agent(P2);
			String outcome=null;

			if (otheragentstarts){
				outcome=Fullinfo.FindBestByPrefernceTez(out, CP1, CP2).getResult();
			}
			else{
				outcome=Fullinfo.FindBestByPrefernceTez(out, CP2, CP1).getResult();
			}

			if(outcome.equals(goal)){
				bf.add(per1);
			}
		}
		System.out.println("BruteForcecount= "+bf.size());


		if (bf.size()>win.size()){
			ArrayList<String> Cwin=new ArrayList<String>(win);
			Cwin.removeAll(bf);
			System.out.println("in algo but not in  brute fource = "+Cwin);
			bf.removeAll(win);
			System.out.println("missing from algo:");
			System.out.println(bf);
		}
		else if (bf.size()<win.size()){
			System.err.println("E-R-R-O-R");
			System.err.println("algo can't be larger then brute fource");
			ArrayList<String> Cwin=new ArrayList<String>(win);
			Cwin.removeAll(bf);
			System.err.println("in algo but not in  brute fource = "+Cwin);
			bf.removeAll(win);
			System.err.println("missing from algo:");
			System.err.println(bf);
		}
		else{
			System.out.println("E-Q-U-L-E");
		}


	}



}
