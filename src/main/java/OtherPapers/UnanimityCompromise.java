package OtherPapers;

import Negotiation.Fullinfo;
import tools.*;

import java.util.*;

import static org.testng.AssertJUnit.assertTrue;

public class UnanimityCompromise {


    /**
     * get the group P(B)
     * @param p1
     * @param p2
     * @param out
     * @return
     */
    public static ArrayList<String> getPreto(Agent p1, Agent p2,String[] out){
        ArrayList<String> res=new ArrayList<String>();
        for (String o:out){
            outcome[] p1o=p1.BetterThen(o);
            outcome[] p2o=p2.BetterThen(o);
            ArrayList<outcome> both= Fullinfo.IntesectGroup(p1o,p2o);
            if (both.isEmpty()){
                res.add(o);
            }
        }
        return res;
    }


    public static int Vi(Agent pi,String o,String out[]){
        outcome[] wd= pi.WorstThen(o);
        int count=0;
        for(String o2:out){
            for (outcome o1:wd){
                if (o1.getName().equals(o2)){
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public static ArrayList<String> Fun(Agent p1,Agent p2,String[] out) {
        ArrayList<String> res = new ArrayList<String>();
        int max = 0;
        for (String o : out) {
            int v1 = Vi(p1, o, out);
            int v2 = Vi(p2, o, out);
            int min = Math.min(v1, v2);
            if (min > max) {
                max = min;
                res = new ArrayList<String>();
                res.add(o);
            } else if (min == max) {
                res.add(o);
            }
        }
        return res;
    }


    public static ArrayList<String> UnanimityCompromise(Agent p1, Agent p2, String[] out){
        return Fun(p1,p2,out);
    }

    public static Set<String> UcSize1WinningPrefrence(String[] out,Agent other,String goal){

        //finding the outcomes that preferd over the goal
        int under_include_other_outcome=out.length-other.OutComesBeterThen(goal).size();

        // THE GOAL IS IN THE LOWERS - STOP
        Set<String> all=new HashSet<>();
        if (under_include_other_outcome<=Fullinfo.lowersSize(out.length,true)){
            return all;
        }
        //running on all location rc will stop ( i = below the goal +goal)
        for (int rc_index_stop=Fullinfo.lowersSize(out.length,false); rc_index_stop<=under_include_other_outcome;rc_index_stop++) {

            ArrayList<String> below_other_loc_name = new ArrayList<>();
            for (outcome o : other.CopyNworst(rc_index_stop-1)) {
                below_other_loc_name.add(o.getName());
            }

            int above_my_goal = out.length - rc_index_stop ;
            List<Set<String>> allsubset = Fullinfo.allsubsets(below_other_loc_name, above_my_goal);

            for (Set<String> set_above : allsubset) {
                set_above.add(goal);
                String[] set_above_array = set_above.toArray(new String[set_above.size()]);
                String[] sub = Fullinfo.substract(out, set_above_array);
                sub = Fullinfo.substract(sub,set_above_array);

                List<String> allPossiblePrefrenceAbove = Fullinfo.AllPossiblePrefrence(set_above_array);
                List<String> allPossiblePrefrenceUnder = Fullinfo.AllPossiblePrefrence(sub);

                for (int i=0; i< allPossiblePrefrenceAbove.size();i++) {
                    for (int j=0; j<allPossiblePrefrenceUnder.size();j++  ) {
                        all.add(allPossiblePrefrenceUnder.get(j)+"<"+allPossiblePrefrenceAbove.get(i));

                    }
                }

            }
        }
        return all;
    }






    public static Set<String> UcSize2WinningPrefrence(String[] out,Agent other,String goal,boolean otherStarting) {
        int under_other_outcome=out.length-other.OutComesBeterThen(goal).size();

        // THE GOAL IS IN THE LOWERS - STOP
        Set<String> all = new HashSet<>();
        if (under_other_outcome <= Fullinfo.lowersSize(out.length, otherStarting)) {
            return all;
        }
        // must be at least two steps of a tie, that's why stop when -1
        for (int rc_loc_stop = Fullinfo.lowersSize(out.length, !otherStarting); rc_loc_stop <= under_other_outcome ; rc_loc_stop++) {
            int numer_of_outcomes_above_my_goal = out.length - rc_loc_stop ;// -1 for adding the goal

            ArrayList<String> below_other_loc_name = new ArrayList<>();
            ArrayList<String> above_other_loc = new ArrayList<>(Arrays.asList(out));

            for (outcome o : other.CopyNworst(rc_loc_stop - 1)) {
                below_other_loc_name.add(o.getName());
                above_other_loc.remove(o.getName());
            }


            String tie=other.copyOutcomeIn(rc_loc_stop).getName();
            if (!tie.equals(goal)) {

                List<Set<String>> allsubset = Fullinfo.allsubsets(below_other_loc_name, numer_of_outcomes_above_my_goal-1);

                for (Set<String> set_above : allsubset) {
                    set_above.add(tie);
                    String[] set_above_array = set_above.toArray(new String[set_above.size()]);
                    String[] set_below = Fullinfo.substract(out, set_above_array);
                    set_below = Fullinfo.substract(set_below, set_above_array);
                    set_below = Fullinfo.substract(set_below, new String[]{goal});

                    int inter = 0;
                    for (String t : Arrays.asList(set_below)) {
                        if (below_other_loc_name.contains(t)) {
                            inter++;
                        }
                    }
                    if (out.length%2==1) {
                        if (!otherStarting && inter % 2 == 1) {
                            continue;
                        }
                    }
                    else{
                        if (otherStarting && inter % 2 == 0) {
                            continue;
                        }
                    }


                    List<String> allPossiblePrefrenceAbove = Fullinfo.AllPossiblePrefrence(set_above_array);
                    List<String> allPossiblePrefrenceUnder = Fullinfo.AllPossiblePrefrence(set_below);
                    for (int i = 0; i < allPossiblePrefrenceAbove.size(); i++) {
                        for (int j = 0; j < allPossiblePrefrenceUnder.size(); j++) {
                            all.add(allPossiblePrefrenceUnder.get(j) + "<" + goal + "<" + allPossiblePrefrenceAbove.get(i));
                        }
                    }
                }
            }
            else{
                for(String tobetie:above_other_loc){
                    if (tobetie.equals(goal)){
                        continue;
                    }

                    List<Set<String>> allsubset = Fullinfo.allsubsets(below_other_loc_name, numer_of_outcomes_above_my_goal-1);

                    for (Set<String> set_above : allsubset) {
                        set_above.add(goal);
                        String[] set_above_array = set_above.toArray(new String[set_above.size()]);
                        String[] set_below = Fullinfo.substract(out, set_above_array);
                        set_below = Fullinfo.substract(set_below, set_above_array);
                        set_below = Fullinfo.substract(set_below, new String[]{tobetie});


                        int inter = 0;
                        for (String t : Arrays.asList(set_below)) {
                            if (below_other_loc_name.contains(t)) {
                                inter++;
                            }
                        }
                        if (out.length%2==1) {
                            if (otherStarting && inter % 2 == 1) {
                                continue;
                            }
                        }
                        else{
                            if (!otherStarting && inter % 2 == 0) {
                                continue;
                            }
                        }

                        List<String> allPossiblePrefrenceAbove = Fullinfo.AllPossiblePrefrence(set_above_array);
                        List<String> allPossiblePrefrenceUnder = Fullinfo.AllPossiblePrefrence(set_below);

                        for (int i = 0; i < allPossiblePrefrenceAbove.size(); i++) {
                            for (int j = 0; j < allPossiblePrefrenceUnder.size(); j++) {
                                all.add(allPossiblePrefrenceUnder.get(j) + "<" + tobetie + "<" + allPossiblePrefrenceAbove.get(i));
                            }
                        }
                    }

                }
            }
        }


        return all;
    }

    /**
     * the same as UnanimityCompromise because we assume the players rank 'd' lowers
     * @param p1
     * @param p2
     * @param out
     * @return
     */
    public static ArrayList<String> RationalCompromise(Agent p1,Agent p2,String[] out){
        return Fun(p1,p2,out);
    }

    public static ArrayList<String> ImputationalCompromise(Agent p1,Agent p2,String[] out){
        ArrayList<String> po=  getPreto(p1,p2,out);
        return Fun(p1,p2,po.toArray(new String[po.size()]));
    }

    public static int Dif(String o,String[]  out,Agent p1,Agent p2){
        return Math.abs(Vi(p1,o,out)-Vi(p2,o,out));
    }

    public static ArrayList<String> EQLa(Agent p1,Agent p2,String[] out){
        ArrayList<String> res=new ArrayList<String>();

        ArrayList<String> po=  getPreto(p1,p2,out);
        for(String s:po){
            boolean pass=true;
            for(String t:po){
                if (Dif(s,out,p1,p2)>Dif(t,out,p1,p2)){
                    pass=false;
                }
            }
            if (pass){
                res.add(s);
            }
        }

        return  res;
    }

    public static ArrayList<String> NashLike(Agent p1,Agent p2,String[] out){
        ArrayList<String> res=new ArrayList<String>();
        int max=0;
        for(String o:out){
            int v=Vi(p1,o,out)*Vi(p2,o,out);
            if (v>max) {
                max = v;
                res = new ArrayList<String>();
                res.add(o);
            }
            else if(v==max){
                res.add(o);
            }
        }
        return res;
    }


    public  static ArrayList<String> Utilitarianlike(Agent p1,Agent p2,String[] out){
        ArrayList<String> res=new ArrayList<String>();
        int max=0;
        for(String o:out){
            int v=Vi(p1,o,out)+Vi(p2,o,out);
            if (v>max) {
                max = v;
                res = new ArrayList<String>();
                res.add(o);
            }
            else if(v==max){
                res.add(o);
            }
        }
        return res;
    }

    public static ArrayList<String> Egalitarianlike(Agent p1,Agent p2,String[] out) {
        return RationalCompromise(p1,p2,out);

    }

    public void main() {
        test();
        String[] out ={"o1","o2","o3","o4","o5","o6","o7","o8","o9","o10"};
        String per1="o1<o2<o3<o4<o5<o6<o7<o8<o9<o10";
        Agent P1=new Agent("p1",per1);

        //getting all results
        ArrayList<String> win=Fullinfo.AllPossiblePrefrence(out);
        int factorial=1;
        int n=1;
        while(n<=out.length){
            factorial=factorial*n;
            n++;
        }
        if (win.size()!=factorial){
            System.exit(1);
        }

        // run over all them
        for (String per2:win){
            Agent P2=new Agent("p2",per2);

            //algorithms
            options eqP1Starts=Fullinfo.FindBestByPrefernceTez(out,new Agent(P2),new Agent(P1));
            options eqP2Starts=Fullinfo.FindBestByPrefernceTez(out,new Agent(P1),new Agent(P2));


            ArrayList<String> eq=UnanimityCompromise(new Agent(P2),new Agent(P1),out);
            String p1Starts=null;
            String p2Starts=null;
            if (eq.size()==2){
                if (out.length%2==0) {
                    p1Starts = (P1.copyOutcome(eq.get(0)).getValue() < P1.copyOutcome(eq.get(1)).getValue())
                            ? eq.get(0):eq.get(1);
                    p2Starts = (P2.copyOutcome(eq.get(0)).getValue() < P2.copyOutcome(eq.get(1)).getValue())
                            ?  eq.get(0):eq.get(1);
                }
                else{
                    p1Starts = (P1.copyOutcome(eq.get(0)).getValue() > P1.copyOutcome(eq.get(1)).getValue())
                            ? eq.get(0):eq.get(1);
                    p2Starts = (P2.copyOutcome(eq.get(0)).getValue() > P2.copyOutcome(eq.get(1)).getValue())
                            ? eq.get(0):eq.get(1);
                }

                //another test
                if (p1Starts.equals(p2Starts)){
                    System.exit(1);
                }

            }else if (eq.size()==1){
                p2Starts=p1Starts=eq.get(0);
            }
            else{
                System.err.println("3 results?? ");
                System.exit(1);
            }


            if (!p1Starts.equals(eqP1Starts.getResult()) || !p2Starts.equals(eqP2Starts.getResult())) {
                System.out.println("G-O-O-D");
                System.out.println("Unanimity Compromise result for p1 is: " + p1Starts);
                System.out.println("Unanimity Compromise result for p2 is: " + p2Starts);

                System.out.println("ower algoritem result when p1 starts is: " + eqP1Starts.getResult());
                System.out.println("ower algoritem result when p2 starts is: " + eqP2Starts.getResult());
                System.exit(1);
            }
        }

    }
    public static void main(String[] arg){
        m_t_is_odd_UC_return_1();
    }
    public  static void  m_t_is_odd_UC_return_1 (){
        String[] out ={"o1","o2","o3","o4","o5"};
        String per1="o5<o4<o3<o2<o1";
        ArrayList<String> win=Fullinfo.AllPossiblePrefrence(out);

        for (String per2:win) {


            Agent P1 = new Agent("p1", per1);
            Agent P2 = new Agent("p1", per2);

            // get number of outcomes the removed in the intersection
            Path InterRemoved = Fullinfo.RemoveIntesection(out, new Agent(P2), new Agent(P1), new AlgoPath());
            int intsize = InterRemoved.size();

            //get the equilibrium
            options eqP1Starts = Fullinfo.FindBestByPrefernceTez(out, new Agent(P2), new Agent(P1));
            //get number of outcomes not prefred
            int M_T = out.length - intsize;
            int limit = (int) Math.floor(M_T / 2) - 1;
            int p1NotPrefers = P1.numberOfOutcomeNotPrefredover(eqP1Starts.getResult());
            int p2NotPrefers = P2.numberOfOutcomeNotPrefredover(eqP1Starts.getResult());

            ArrayList<String> eq = UnanimityCompromise(P1, P2, out);

            if (((out.length % 2 == 1 && intsize % 2 == 1) || (out.length % 2 == 0 && intsize % 2 == 0))) {
                if (eq.size()==1){
                    System.out.println("M_T="+M_T);
                    System.out.println("limit="+limit);
                    System.out.println(per1);
                    System.out.println(per2);
                    System.out.println(InterRemoved+" -> "+intsize);
                }
            }

//
//                if ((out.length % 2 == 1 && intsize % 2 == 0) || (out.length % 2 == 0 && intsize % 2 == 1)) {
//                assertTrue(eq.size() == 1);
//            } else {
//                if (eq.size() == 2) {
////                System.out.println("M_T="+M_T);
////                System.out.println("limit="+limit);
////
////                System.out.println(per1);
////                System.out.println(per2);
////                System.out.println(InterRemoved+" -> "+intsize);
//
//                    if (out.length % 2 == 1 && intsize % 2 == 1) {
//                        String a = (P1.copyOutcome(eq.get(0)).getValue() > P1.copyOutcome(eq.get(1)).getValue())
//                                ? eq.get(0) : eq.get(1);
//                        assertTrue(a.equals(eqP1Starts.getResult()));
//                    } else if (out.length % 2 == 0 && intsize % 2 == 0) {
//                        String a = (P2.copyOutcome(eq.get(0)).getValue() > P2.copyOutcome(eq.get(1)).getValue())
//                                ? eq.get(0) : eq.get(1);
//                        assertTrue(a.equals(eqP1Starts.getResult()));
//                    }
//
//                }
//
//            }
        }



    }


    public static void test(){
        String[] out ={"o1","o2","o3","o4","o5"};
        String per1="o5<o4<o3<o2<o1";
        String per2="o1<o2<o3<o4<o5";
        Agent P1=new Agent("p1",per1);
        Agent P2=new Agent("p1",per2);
        ArrayList<String> eq=UnanimityCompromise(P1,P2,out);
        assertTrue(eq.size()==1 && eq.get(0).equals("o3"));


        per2="o1<o2<o4<o5<o3";
        P1=new Agent("p1",per1);
        P2=new Agent("p1",per2);
        eq=EQLa(P1,P2,out);
        assertTrue(eq.size()==2 );
        assertTrue( (eq.get(0).equals("o3") &&  eq.get(1).equals("o2")) || (eq.get(0).equals("o2") &&  eq.get(1).equals("o3")));


        P1=new Agent("p1",per1);
        P2=new Agent("p1",per2);
        eq=UnanimityCompromise(P1,P2,out);
        assertTrue(eq.size()==1 );
        assertTrue(eq.get(0).equals("o3"));

        P1=new Agent("p1",per1);
        P2=new Agent("p1",per2);
        eq=ImputationalCompromise(P1,P2,out);
        assertTrue(eq.size()==1 );
        assertTrue(eq.get(0).equals("o2"));
    }
}
