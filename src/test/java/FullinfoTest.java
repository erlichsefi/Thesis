import static org.testng.AssertJUnit.assertTrue;

import OtherPapers.UnanimityCompromise;
import org.testng.annotations.Test;

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


import DrawTree.DecisionTreePrinter;
import Negotiation.Algo;
import Negotiation.Fullinfo;
import tools.*;

public class FullinfoTest {
    int numberOfRuns=100;
    int MaxNumberOfPrefrence=9;
    int minNumberOfPrefrence=9;
    boolean Random=true;

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
                ArrayList<Agent> a=new ArrayList<>();
                a.add(new Agent("P2",per2));
                options o1=Fullinfo.FindBestByPrefernce(out,a,otherAgnet);
                new DecisionTreePrinter(o1.getTree()).show();
                otherAgnet=new Agent("P1",per1);
                options o2=Fullinfo.FindBestByPrefernceTez(out,new Agent("P2",per2),otherAgnet);

                otherAgnet=new Agent("P1",per1);
                Agent my=new Agent("P2",per2);
                System.out.println(Fullinfo.afterIntersection(out,my,otherAgnet));

                System.out.println("OLD :"+o1);
                System.out.println("NEW :"+o2);
                System.out.println(o1.longestPaths());
                System.out.println(o2.getPaths());

                if(!o1.getResult().equals(o2.getResult())){
                    assert(false);
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
     * this junit suposse to show the new Path to the equilibrium
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
                ArrayList<Agent> a=new ArrayList<>();
                a.add(new Agent("P2",per2));

                options o1=Fullinfo.FindBestByPrefernce(out,a,otherAgnet);

                //define new outcome
                String  NewOutcome="o"+(out.length+1);
                String[] out2=Arrays.copyOf(out, out.length+1);
                out2[out2.length-1]=NewOutcome;

                System.out.println("** OLD **");
                System.out.println("Result "+o1.getResult());
                System.out.println("Path "+o1.longestPaths());



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
                        Agent my=new Agent("P2",newP2);
                        options o2=Fullinfo.FindBestByPrefernceTez(out2,my,otherAgnet);
                        System.out.print("|");
                        System.out.print("P1 "+newP1+",P2 "+newP2);
                        System.out.print(" Path "+o2.longestPaths()+o2.getResult());

                    }
                    System.out.println();
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }

                System.out.println("******************");
            }
        }
        System.out.println();
    }



//	/**
//	 * Testing if removing a outcome before the equilibrium , and add a new outcome in the end of
//	 * the preference order of both players will affect the Path to the equilibrium.
//	 */
//	//	@Test
//	public  void AddingOutComeTest() {
//		boolean Random=false;
//		String[] out;
//		String per1;
//		String per2;
//		for (int i = 0; i < numberOfRuns; i++) {
//			if (Random){
//				int RandomResult=r.nextInt(minNumberOfPrefrence)+1;
//				out=Algo.BuildOutComeArray(RandomResult+MaxNumberOfPrefrence-minNumberOfPrefrence);
//				per1=Algo.randomPrefrenceOver(out);
//				per2=Algo.randomPrefrenceOver(out);
//			}
//			else{
//				out =new String[]{"o1","o2","o3","o4","o5","o6"};
//				per1="o2<o3<o4<o1<o6<o5";
//				per2="o5<o6<o1<o2<o3<o4";
//			}
//			if (per1!=null && per2!=null ){
//
//				System.out.println("P2 "+per2);
//				System.out.println("P1 "+per1);
//
//				Agent otherAgnet=new Agent("P1",per1);
//				Agent my=new Agent("P2",per2);
//
//				options o2=Fullinfo.FindBestByPrefernceTez(out,my,otherAgnet);
//
//				System.out.println("The result is: "+o2.getResult());
//				String[] StartingAgent= per1.split("<");
//				boolean PassEquilibrium=false;
//				boolean ExistOne=false;
//				for (int j = StartingAgent.length-1; j >=0; j--) {
//					if (PassEquilibrium){
//						System.out.println(StartingAgent[j]);
//
//						Agent CotherAgnet=new Agent("P1",per1);
//						Agent Cmy=new Agent("P2",per2);
//						if(Fullinfo.IsReplaceingOutcomeWorks(out, Cmy, CotherAgnet, StartingAgent[j])){
//							ExistOne=true;
//						}
//					}
//					else if (StartingAgent[j].equals(o2.getResult())){
//						PassEquilibrium=true;
//					}
//				}
//				assert(ExistOne);
//
//
//				System.out.println("pass:"+(i+1)+"/"+numberOfRuns);
//
//
//				System.out.println("******************");
//			}
//		}
//		System.out.println();
//	}



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
                ArrayList<Agent> a=new ArrayList<>();
                Agent my=new Agent("P2",per2);
                a.add(my);

                options o2=Fullinfo.FindBestByPrefernce(out,a,otherAgnet);
                ArrayList<Path> path=o2.getPathslist();
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
                ArrayList<Agent> a=new ArrayList<>();
                my=new Agent("P2",per2);
                a.add(my);
                options _o1=Fullinfo.FindBestByPrefernce(out,a,otherAgnet);

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

                 a=new ArrayList<>();
                my=new Agent("P2",per2);
                a.add(my);
                options _o2=Fullinfo.FindBestByPrefernce(out,a,otherAgnet);


                if ((out.length-1%2)==0){
                    assert(_o2.getResult().equals(out[out.length-1]));
                }
                else{
                    assert(_o2.getResult().equals(_o1.getResult()));
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
                ArrayList<Agent> a=new ArrayList<>();
                Agent p2=new Agent("P2",per2);
                a.add(p2);

                options Original=Fullinfo.FindBestByPrefernce(out,a,p1);

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

                    bw.append("as no Path to lowers\n");

                    for (String str:DontAsPathTo) {
                        bw.append(str+"\n");
                    }
                    bw.append("Path to the eq="+Original.getResult()+"\n");

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
            assert(IsInside);
            assert(lowerInstesectionSize+1==upperInstesectionSize);
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
        bw.write("Path= "+order+"\n");

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
                bw.write("Path= "+path+"\n");
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
  //  @Test
    public  void SingleTestFullInfo() {
        String[] out ={"o1","o2","o3","o4","o5"};
        String per2="o1<o3<o2<o4<o5";
        String per1="o2<o3<o1<o5<o4";
        Agent P1=new Agent("p1",per1);
        ArrayList<Agent> a=new ArrayList<>();
        a.add(new Agent("p2",per2));
        System.out.println(Fullinfo.FindBestByPrefernce(out,a,P1));
        System.out.println();
    }

    /**
     * a single round of full info negotiation when p1 is starting
     */
    //@Test
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
     * check if there is a game where a player must offer
     * the intersection.
     *
     * RESULT: THE PRINTED GAMES ARE GAME WHERE THE SHORTEST PATH
     * IS THE INTERSECTION, SO ANY ALGORITHM THAT FIND A SHORTER PATH MUST
     * USE OWER
     */
    //@Test
    public  void InAgameWithShortestPATHwithTheIntesection() {
        boolean otheragentstarts=false;
        String[] out ={"o1","o2","o3","o4","o5","o6"};
        String per1="o1<o2<o3<o4<o5<o6";
        Agent P1=new Agent("p1",per1);
        ArrayList<String> win=Fullinfo.AllPossiblePrefrence(out);
        for (String per2:win) {
            ArrayList<Agent> a=new ArrayList<>();
            Agent CP1=new Agent(P1);
            Agent CP2=new Agent("p2",per2);
            options outcome=null;
            int IntrestenionSize=0;
            if (otheragentstarts){
                a.add(CP1);
                outcome=Fullinfo.FindBestByPrefernce(out, a, CP2);
            }
            else{
                a.add(CP2);
                outcome=Fullinfo.FindBestByPrefernce(out, a, CP1);
            }
            CP1=new Agent(P1);
            CP2=new Agent("p2",per2);
            if (otheragentstarts){
                IntrestenionSize=Fullinfo.IntersectionSize(out,CP1,CP2);
            }
            else{
                IntrestenionSize=Fullinfo.IntersectionSize(out,CP2,CP1);
            }
            int Sp=outcome.ShortestPathLength();

            if (IntrestenionSize>0 && Sp==(IntrestenionSize+1)){
                System.out.println("*****************");
                System.out.println(Sp);

                System.out.println(per1);
                System.out.println(per2);

                System.out.println(outcome.getPathsInSize(Sp));
                System.out.println(outcome.getResult());
                //assert(false);
                //	break;
            }
        }
        System.out.println(win.size());

    }


    /**
     * check to see what comman in games with path size 1
     *
     * RESULT:
     */
    //@Test
    public  void GameWithPathSizeOne() {
        boolean otheragentstarts=false;
        String[] out ={"o1","o2","o3","o4","o5","o6","o7"};
        String per1="o1<o2<o3<o4<o5<o6<o7";
        Agent P1=new Agent("p1",per1);
        ArrayList<String> win=Fullinfo.AllPossiblePrefrence(out);
        for (String per2:win) {
            Agent CP1=new Agent(P1);
            Agent CP2=new Agent("p2",per2);
            options outcome=null;
            int IntrestenionSize=0;
            ArrayList<Agent> a=new ArrayList<>();
            if (otheragentstarts){
                a.add(CP1);
                outcome=Fullinfo.FindBestByPrefernce(out, a, CP2);
            }
            else{
                a.add(CP2);
                outcome=Fullinfo.FindBestByPrefernce(out, a, CP1);
            }
            CP1=new Agent(P1);
            CP2=new Agent("p2",per2);
            if (otheragentstarts){
                IntrestenionSize=Fullinfo.IntersectionSize(out,CP1,CP2);
            }
            else{
                IntrestenionSize=Fullinfo.IntersectionSize(out,CP2,CP1);
            }
            int Sp=outcome.ShortestPathLength();

            if (IntrestenionSize==2 && Sp==1){
                System.out.println("*****************");
                System.out.println("IntrestenionSize= "+IntrestenionSize);

                System.out.println(per1);
                System.out.println(per2);

                System.out.println(outcome.getPathsInSize(Sp));
                System.out.println(outcome.getResult());
                //assert(false);
                //	break;
            }
        }
        System.out.println(win.size());

    }

    /**
     * check the thesis that offering a non perto optimal JG below the EQ result in the EQ
     *
     * RESULT: THE CLAIM IS INCORRECT.
     */
  //  @Test
    public  void OfferJGlowerThenEQresultInEq() {
        boolean otheragentstarts=false;
        String[] out ={"o1","o2","o3","o4","o5","o6","o7","o8","o9"};
        String per1="o1<o2<o3<o4<o5<o6<o7<o8<o9";
        Agent P1=new Agent("p1",per1);
        ArrayList<String> win=Fullinfo.AllPossiblePrefrence(out);
        int count=0;
        for (String per2:win) {
            System.out.println((count++)+"/"+win.size());

            Agent CP1=new Agent(P1);
            Agent CP2=new Agent("p2",per2);
            options Original=null;
            Original=Fullinfo.FindBestByPrefernceTez(out, CP2, CP1);

            CP1=new Agent(P1);
            CP2=new Agent("p2",per2);
            //oringal value of the eq
            outcome p1eq=CP1.copyOutcome(Original.getResult());
            outcome p2eq=CP2.copyOutcome(Original.getResult());

            outcome[] jg1=Fullinfo.JoinGoals(CP1,CP2,CP1.getAgentName());

            //filtering the jg wtich is not prefred by both
            ArrayList<outcome> bothNotPrefred=new ArrayList<outcome>();
            for (int i=0; i<jg1.length;i++){
                outcome p1current=CP1.copyOutcome(jg1[i].getName());
                outcome p2current=CP2.copyOutcome(jg1[i].getName());
                if (p1current.getValue()<p1eq.getValue() && p2current.getValue()<p2eq.getValue() ){
                    bothNotPrefred.add(p2current);
                }
            }

            for (outcome Toremove:bothNotPrefred){
                CP1=new Agent("p1",Preferences.Remove(per1,Toremove.getName()));
                ArrayList<Agent> a=new ArrayList<>();
                CP2=new Agent("p2",Preferences.Remove(per2,Toremove.getName()));
                a.add(CP2);
                String[] newOUT=CP1.getOutComeOptions();
                options New=Fullinfo.FindBestByPrefernce(newOUT, a, CP2);

                CP1=new Agent(P1);
                CP2=new Agent("p2",per2);

                outcome p1neweq=CP1.copyOutcome(New.getResult());
                outcome p2neweq=CP2.copyOutcome(New.getResult());
                outcome p2offer=CP2.copyOutcome(Toremove.getName());

                //if (p2neweq.getValue()>p2offer.getValue()) {
                if ((p2offer.getValue() < p2neweq.getValue()) && (p2neweq.getValue() < p2eq.getValue())){
                    System.out.println("**************");
                    System.out.println(per1);
                    System.out.println(per2);

                    System.out.println("Original= "+Original);

                    System.out.println("Toremove= "+Toremove);
                    System.out.println("New= "+New);

                    System.out.println(CP1);
                    System.out.println(CP2);


                    assert(false);
                }
                //}



            }
        }

    }



    /**
     * check the equation version is correct
     *
     * RESULT: THE CLAIM IS INCORRECT.
     */
    //@Test
    public  void CheckEqutionVersion() {
        boolean otheragentstarts=false;
        String[] out ={"o1","o2","o3","o4","o5","o6","o7"};
        String per1="o1<o2<o3<o4<o5<o6<o7";
        Agent P1=new Agent("p1",per1);
        ArrayList<String> win=Fullinfo.AllPossiblePrefrence(out);
        for (String per2:win) {
            Agent CP1=new Agent(P1);
            Agent CP2=new Agent("p2",per2);
            options Original=null;
            ArrayList<Agent> a=new ArrayList<>();
            if (otheragentstarts){
                a.add(CP1);
                Original=Fullinfo.FindBestByPrefernce(out, a, CP2);
            }
            else{
                a.add(CP2);
                Original=Fullinfo.FindBestByPrefernce(out, a, CP1);
            }
            CP1=new Agent(P1);
            CP2=new Agent("p2",per2);

            int maxI=0;
            String eq="";
            for (String current:out){
                //get all worst then current
                int dj1=Algo.indexOf(per1.split("<"),current);
                int dj2=Algo.indexOf(per2.split("<"),current);

                outcome[] p1worst=CP1.CopyNworst(dj1);
                outcome[] p2worst=CP2.CopyNworst(dj2);

                ArrayList<outcome> worstl1= new ArrayList<outcome>(Arrays.asList(p1worst));
                List<outcome> worstl2=new ArrayList<outcome>(Arrays.asList(p2worst));


                int sizeBefore=worstl1.size();
                worstl1.removeAll(Fullinfo.IntesectGroup(p1worst,p2worst));
                worstl2.removeAll(Fullinfo.IntesectGroup(p2worst,p1worst));
                int i=sizeBefore-worstl1.size();
                int r1=worstl1.size();
                int r2=worstl2.size();
                System.out.println(current+" "+i);
                if (maxI<i){
                    maxI=i;
                    eq=current;
                }
                else if (maxI==i){
                    int left=out.length-i;
                    int border= (int) Math.floor(left/2);
                    System.out.println("left="+left);
                    System.out.println("border="+border);

                    int b1=border-((1-(out.length%2))*(1-i%2));
                    int b2=border-((out.length%2)*(i%2));
                    int i1=dj1-i;
                    int i2=dj2-i;
                    System.out.println(current);
                    System.out.println(b1+" "+b2+" i1="+i1+" i2="+i2);

                    if ((i1>=b1) &&
                            (i2>=b2)){
                        eq=current;
                    }
                }

            }
            if (!Original.getResult().equals(eq)){
                System.out.println("EQ= "+Original);
                System.out.println("ES EQ= "+eq);

                System.out.println(per1);
                System.out.println(per2);

                assert(false);


            }

        }
        System.out.println(win.size());

    }


    /**
     * check whteher the eq is in the only top 2 JG of the players
     *
     * RESULT: THE EQ not neccerly in the Top 2 JG
     * o1<o2<o3<o4<o5<o9<o10<o8<o6<o7
     * o1<o2<o3<o4<o5<o6<o7<o8<o9<o10
     *
     * EQ=o8
     */
    @Deprecated
    public  void IsEQonlyInTop2JG(){
        boolean otheragentstarts=false;
        String goal="o3";
        String[] out ={"o1","o2","o3","o4","o5","o6","o7","o8","o9","o10"};
        String per2="o1<o2<o3<o4<o5<o6<o7<o8<o9<o10";
        Agent P2=new Agent("p2",per2);
        ArrayList<String> win=Algo.AllPossiblePrefrence(out);
        for (String per1:win) {
            Agent CP1=new Agent("p1",per1);
            Agent CP2=new Agent(P2);
            outcome[] JG1=null;
            outcome[] JG2=null;

            if (Fullinfo.IntersectionSize(out,CP1,CP2)>0) {
                //FIND TOP 2 JG IN EACH AGENT
                CP1 = new Agent("p1", per1);
                CP2 = new Agent(P2);
                if (!otheragentstarts) {
                    JG1 = Fullinfo.JoinGoals(CP2, CP1, CP1.getAgentName());
                    JG2 = Fullinfo.JoinGoals(CP2, CP1, CP2.getAgentName());

                } else {
                    JG1 = Fullinfo.JoinGoals(CP1, CP2, CP1.getAgentName());
                    JG2 = Fullinfo.JoinGoals(CP1, CP2, CP2.getAgentName());
                }
                Arrays.sort(JG1);
                Arrays.sort(JG2);

                JG1 = Arrays.copyOfRange(JG1, 0, 2);
                JG2 = Arrays.copyOfRange(JG2, 0, 2);

                CP1 = new Agent("p1", per1);
                CP2 = new Agent(P2);
                options outcome = null;
                if (otheragentstarts) {
                    outcome = Fullinfo.FindBestByPrefernceTez(out, CP1, CP2);
                } else {
                    outcome = Fullinfo.FindBestByPrefernceTez(out, CP2, CP1);
                }
                System.out.println(per1);
                boolean In1 =JG1[0].getName().equals(outcome.getResult()) || JG1[1].getName().equals(outcome.getResult());
                boolean In2 = JG2[0].getName().equals(outcome.getResult()) || JG2[1].getName().equals(outcome.getResult());
                if (!In1 && !In2) {
                    System.out.println("********************");
                    System.out.println(per1);

                    System.out.println(per2);

                    System.out.println(Arrays.toString(JG1));
                    System.out.println(Arrays.toString(JG2));
                    System.out.println(outcome);

                    assert (false);
                    break;
                }
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
    //@Test
    public void paretoOptimal(){
        String[] out ={"o1","o2","o3","o4","o5"};
        ArrayList<String> Pper=Algo.AllPossiblePrefrence(out);
        int eq=0;
        int neq=0;
        String p="o1<o2<o3<o4<o5";
        Agent Cp1=new Agent("p1", p);
        for (String pref:Pper) {
            //pref="o5<o1<o4<o3<o2";
            Agent p1=new Agent(Cp1);
            Agent p2=new Agent("p2", pref);
            String actual=Fullinfo.FindBestByPrefernceTez(out,new Agent(p2),new Agent(p1)).getResult();

            Agent[] a = new Agent[]{new Agent(p1), new Agent(p2)};
            ArrayList<String> partoOptimal=Fullinfo.allPartoOptimal(out, a);
            ArrayList<String> cpartoOptimal=new ArrayList<String>(partoOptimal);
            for (String o: out){
                if (!partoOptimal.contains(o)) {
                    p1.RemoveOutcome(o);
                    p2.RemoveOutcome(o);
                }

            }

            int turnPass=out.length-partoOptimal.size();
            /// i have replaced it
            Agent _p1=new Agent(p1);
            Agent _p2=new Agent(p2);
            if (turnPass%2==0){
                _p1=new Agent(p2);
                _p2=new Agent(p1);
            }
            //System.out.println(_p1.getPrefrenceAboutCurrentOptions());
            //System.out.println(_p2.getPrefrenceAboutCurrentOptions());

            while (partoOptimal.size()!=1){
                String worst=_p1.CopyNworst(1)[0].getName();
                _p1.RemoveOutcome(worst);
                _p2.RemoveOutcome(worst);
                partoOptimal.remove(worst);

                Agent temp=new Agent(_p1);
                _p1=new Agent(_p2);
                _p2=new Agent(temp);
            }
            String estemation=_p1.CopyNworst(1)[0].getName();
            if (!estemation.equals(actual)){
                System.out.println("not eq: ");
                System.out.println(p);

                System.out.println(pref);
                System.out.println("before= "+cpartoOptimal);
                System.out.println("estemation="+estemation);

                System.out.println("actual= "+actual);
                neq++;
                System.out.println("**************");

            }
            else{
                //	System.out.println(" eq: ");
                eq++;
            }



        }
        System.out.println("**************");
        System.out.println("eq="+eq+" neq="+neq);


    }


}
