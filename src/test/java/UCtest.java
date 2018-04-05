import static org.testng.AssertJUnit.assertTrue;

import Negotiation.Algo;
import Negotiation.Fullinfo;
import OtherPapers.UnanimityCompromise;
import org.testng.annotations.Test;
import tools.Agent;
import tools.options;

import java.lang.reflect.Array;
import java.util.*;

public class UCtest {
    int numberOfRuns=100;
    int MaxNumberOfPrefrence=9;
    int minNumberOfPrefrence=9;
    boolean Random=true;
    java.util.Random r=new Random(System.currentTimeMillis());

    // @Test
    public void check_one_pred(){
        String[] out =new String[]{"o1","o2","o3","o4","o5","o6","o7"};
        String pref="o1<o2<o3<o4<o5<o6<o7";
        int[] true_count=count_true_one(out,pref);
        int[] pred_count=count_pred_one(out,pref);
        assertTrue(Arrays.equals(true_count,pred_count));
    }

    //  @Test
    public  void test_one_pred() {
        String[] out;
        String per2;
        for (int i = 0; i < numberOfRuns; i++) {
            if (Random){
                int RandomResult=r.nextInt(MaxNumberOfPrefrence-minNumberOfPrefrence+1);
                out=Algo.BuildOutComeArray(RandomResult+minNumberOfPrefrence-1);
                per2=Algo.randomPrefrenceOver(out);
            }
            else{
                out =new String[]{"o1","o2","o3","o4","o5"};
                per2="o2<o1<o3<o5<o4";
            }
            if (per2!=null ){
                System.out.println("** "+per2+" ** ");
                for (int j = 0; j < out.length; j++) {
                    System.out.println("Testing: "+out[j]);
                    String Wantout=out[j];

                    Agent otherAgnet=new Agent("P2",per2);
                    Set<String> o1= UnanimityCompromise.UcSize1WinningPrefrence(out,otherAgnet, Wantout);

                    for (String pr:o1) {
                        Agent CopyotherAgnet=new Agent(otherAgnet);
                        Agent MyAgnet=new Agent("P1",pr);

                        options op1=Fullinfo.FindBestByPrefernceTez(out, CopyotherAgnet, MyAgnet);
                        if (!op1.getResult().equals(Wantout)){
                            System.out.println(pr);
                            System.out.println(Wantout);
                            System.out.println(op1.getResult());

                            assertTrue(false);
                        }
                    }
                    System.out.println("Number of possible are: "+o1.size());
                }
            }
        }
        System.out.println("Done");
    }

    //    @Test
    public  void test_two_pred() {
        String[] out;
        String per2;
        boolean otherStarting;
        for (int i = 0; i < numberOfRuns; i++) {
            if (Random){
                int RandomResult=r.nextInt(MaxNumberOfPrefrence-minNumberOfPrefrence+1);
                out=Algo.BuildOutComeArray(RandomResult+minNumberOfPrefrence-1);
                per2=Algo.randomPrefrenceOver(out);
                otherStarting=r.nextBoolean();
            }
            else{
                out =new String[]{"o1","o2","o3","o4","o5"};
                per2="o1<o2<o3<o4<o5";
                otherStarting=true;
            }
            if (per2!=null ){
                System.out.println("** p2="+per2+" ** "+" when she starting= "+otherStarting);
                for (int j = 0; j < out.length; j++) {
                    System.out.println("Testing: "+out[j]);
                    String Wantout=out[j];

                    Agent otherAgnet=new Agent("P2",per2);
                    Set<String> o1= UnanimityCompromise.UcSize2WinningPrefrence(out,otherAgnet, Wantout,otherStarting);

                    for (String pr:o1) {
                        Agent CopyotherAgnet=new Agent(otherAgnet);
                        Agent MyAgnet=new Agent("P1",pr);
                        options op1=null;
                        if (otherStarting) {
                            op1 = Fullinfo.FindBestByPrefernceTez(out, MyAgnet,CopyotherAgnet);
                        }
                        else{
                            op1 = Fullinfo.FindBestByPrefernceTez(out, CopyotherAgnet, MyAgnet);

                        }

                        if (!op1.getResult().equals(Wantout)){
                            System.out.println(pr);
                            System.out.println(Wantout);
                            System.out.println(op1.getResult());

                            assertTrue(false);
                        }
                    }
                    System.out.println("Number of possible are: "+o1.size());
                }
            }
        }
        System.out.println("Done");
    }

    @Test
    public void check_two_pred(){
        String[] out =new String[]{"o1","o2","o3","o4","o5","o6","o7","o8"};
        String pref="o1<o2<o3<o4<o5<o6<o7<o8";
        int[] true_count=count_true_two(out,pref);
        int[] pred_count=count_pred_two(out,pref);
        if(!Arrays.equals(true_count,pred_count)){
            System.out.println(Arrays.toString(true_count));
            System.out.println(Arrays.toString(pred_count));
            assertTrue(false);
        }
    }

    private int[] count_true_two(String[] out,String pref1){
        Agent other=new Agent("p2",pref1);
        ArrayList<String> all=Algo.AllPossiblePrefrence(out);
        int[] count_two=new int[out.length];

        for (String pref:all) {
            Agent me=new Agent("p1",pref);
            ArrayList<String> result=UnanimityCompromise.RationalCompromise(me,other,out);

            if (result.size()==2){
                System.out.println(pref+"-> "+result);

                count_two[Algo.indexOf(out,result.get(0))]++;
                count_two[Algo.indexOf(out,result.get(1))]++;

            }
        }
        return count_two;
    }


    private int[] count_pred_two(String[] out,String otherpref) {
        Agent other=new Agent("p2",otherpref);
        int[] count_one=new int[out.length];
        int index=0;
        for (String o:out) {
            System.out.println("Trying to: "+o);
            Set<String> all = UnanimityCompromise.UcSize2WinningPrefrence(out, other, o,true);
            System.out.println("Checking thus predications with other starting:");

            for(String pref2:all) {
                System.out.println("my: "+pref2);
                checkPred(pref2,otherpref,true,out,o);
            }
            count_one[index]=all.size();
            all = UnanimityCompromise.UcSize2WinningPrefrence(out, other, o,false);
            System.out.println("Checking thus predications with my starting:");
            for(String pref2:all) {
                System.out.println("my: "+pref2);
                checkPred(pref2,otherpref,false,out,o);
            }
            count_one[index]+=all.size();
            index++;
          //  break;
        }
        return count_one;
    }

    private void checkPred(String pref2, String pref1, boolean otherStarting, String[] out,String goal) {
        Agent MyAgnet=new Agent("P2",pref1);
        Agent CopyotherAgnet=new Agent("P1",pref2);
        options op1;
        if (otherStarting) {
            op1 = Fullinfo.FindBestByPrefernceTez(out,CopyotherAgnet, MyAgnet);
        }
        else{
            op1 = Fullinfo.FindBestByPrefernceTez(out, MyAgnet,CopyotherAgnet);
        }
        assertTrue(op1.getResult().equals(goal));
    }

    private int[] count_true_one(String[] out,String pref1){
        Agent other=new Agent("p2",pref1);
        ArrayList<String> all=Algo.AllPossiblePrefrence(out);
        int[] count_one=new int[out.length];
        int[] count_two=new int[out.length];

        for (String pref:all) {
            Agent me=new Agent("p1",pref);
            ArrayList<String> result=UnanimityCompromise.RationalCompromise(me,other,out);
            if (result.size()==1){
                count_one[Algo.indexOf(out,result.get(0))]++;
            }

        }
        return count_one;
    }


    private int[] count_pred_one(String[] out,String pref1){
        Agent other=new Agent("p2",pref1);
        int[] count_one=new int[out.length];
        int index=0;
        for (String o:out) {
            Set<String> all = UnanimityCompromise.UcSize1WinningPrefrence(out, other, o);
            count_one[index]=all.size();
            index++;
        }
        return count_one;
    }


}


