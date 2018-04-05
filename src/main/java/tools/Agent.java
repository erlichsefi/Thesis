package tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;



public class Agent {
    static int ids=0;
    int id=ids++;
    String[] pre;
    String OriginalPrefrence;
    String agentname;
    Disagreement dissagrementValue;
    HashMap<String,outcome> O;

    public Agent(String _agentname,String pref){
        OriginalPrefrence=pref;
        pre=pref.split("<");
        O=new HashMap<String,outcome>();
        for (int i = 0; i < pre.length; i++) {
            String[] w=pre[i].split("~");
            for (String o:w) {
                O.put(o, new outcome(_agentname, o, i + 1));
            }

        }
        agentname=_agentname;
    }

    public Agent(Agent a){
        O=new HashMap<String,outcome>(a.O);
        agentname=a.agentname;
        OriginalPrefrence=a.OriginalPrefrence;
        pre=a.pre;
        this.dissagrementValue=a.dissagrementValue;
    }



    public outcome RemoveBestOutcome(){
        outcome best=null;
        String key=null;
        for (Entry<String, outcome> entry : O.entrySet()){
            if (best==null){
                best=entry.getValue();
                key=entry.getKey();
            }
            else if (entry.getValue().value>best.value){
                best=entry.getValue();
                key=entry.getKey();

            }

        }
        return O.remove(key);
    }
    public Disagreement getdissagramntvalue(){
        return new Disagreement(dissagrementValue);
    }


    public void setdissagramntvalue(Disagreement d){
        this.dissagrementValue=new Disagreement(d);
    }
    public int numberOfOutcomeNotPrefredover(String name){
        outcome best=O.get(name);
        int count=0;
        for (Entry<String, outcome> entry : O.entrySet()){
            if (entry.getValue().getValue()<best.getValue()){
                count++;
            }

        }
        return count;
    }



    public outcome RemoveSecondBestOutcome(){
        Agent a=new Agent(this);
        outcome best=a.RemoveBestOutcome();
        outcome secondbest=a.RemoveBestOutcome();
        if (secondbest!=null) {
            return O.remove(secondbest.getName());
        }else{
            return O.remove(best.getName());

        }
    }

    public outcome CopyBestOutcome(){
        outcome best=null;
        for (Entry<String, outcome> entry : O.entrySet()){
            if (best==null){
                best=entry.getValue();
            }
            else if (entry.getValue().value>best.value){
                best=entry.getValue();

            }

        }
        return best;
    }

    /**
     *
     * @param r the other agent offer,null if the first offering
     * @return null if accept, other offer if raject
     */
    public outcome NoInfoTurnVersion2(outcome r){
        outcome sec=RemoveSecondBestOutcome();
        if (r!=null && CopyBestOutcome().getName().equals(r.getName())){
            return null;
        }
        if (r!=null) {
            RemoveOutcome(r.getName());
        }
        return sec;

    }

    /**
     *
     * @param offer the other agent offer, null if the first offering
     * @return null if accept, other offer if raject
     */
    public outcome NoInfoTurnVersion1(outcome offer){
        outcome sec=RemoveBestOutcome();
        if (offer!=null && sec.getName().equals(offer.getName())){
            return null;
        }
        else {
            if (offer!=null) {
                RemoveOutcome(offer.getName());
            }
            return sec;
        }

    }


    public outcome RemoveOutcome(String name) {
        return O.remove(name);
    }
    public outcome CopyOutcome(String name) {
        return new outcome(O.get(name));
    }


    public boolean HasMoreOffers() {
        return !O.isEmpty();
    }


    public String[] getOutComeOptions() {
        String[] move=new String[O.size()];
        int i=0;
        for (Entry<String, outcome> entry : O.entrySet()){
            move[i++]=entry.getValue().name;
        }
        return move;
    }

    public ArrayList<String> getOutComeOptionsList() {
        ArrayList<String> move=new ArrayList<String>();
        int i=0;
        for (Entry<String, outcome> entry : O.entrySet()){
            move.add(entry.getValue().name);
        }
        return move;
    }

    public String getOriginalPrefrence() {
        return OriginalPrefrence;
    }

    public String getPrefrenceAboutCurrentOptions() {
        outcome[] move=new outcome[O.size()];
        int i=0;
        for (Entry<String, outcome> entry : O.entrySet()){
            move[i++]=entry.getValue();
        }
        Arrays.sort(move);
        return sortedOutcomeToPrefrnce(move);
    }

    private  String sortedOutcomeToPrefrnce(outcome[] a){
        if (a.length==0){
            return "";
        }
        String ans=a[a.length-1].getName();
        for (int i = a.length-2; i >= 0 ; i--) {
            ans=ans+"<"+a[i].getName();
        }
        return ans;
    }

    public String[] getPrefrenceArray() {
        return pre;
    }
    public outcome[] getOutComeOptionsOut() {
        outcome[] move=new outcome[O.size()];
        int i=0;
        for (Entry<String, outcome> entry : O.entrySet()){
            move[i++]=new outcome(entry.getValue());
        }
        return move;
    }


    public outcome copyOutcome(String name) {
        return new outcome(O.get(name));
    }

    public String getAgentName() {
        return agentname;
    }


    public outcome[] RemoveNworst(int numberOfPassStarting) {
        outcome[] an=CopyNworst(numberOfPassStarting);
        for (int i = 0; i < an.length; i++) {
            an[i]=O.remove(an[i].getName());
        }
        return an;
    }

    public outcome[] CopyNworst(int numberOfPassStarting) {
        outcome[] an=new outcome[O.size()];
        int i=0;
        for (outcome value : O.values()) {
            an[i++]=value;
        }
        Arrays.sort(an, new Comparator<outcome>(){

            @Override
            public int compare(outcome o1, outcome o2) {
                if (o1.value<o2.value) return -1;
                else if (o1.value>o2.value) return 1;
                return 0;
            }

        });
        return Arrays.copyOfRange(an,0,numberOfPassStarting);
    }

    public outcome CopyWorstOutcome() {
        outcome worst=null;
        String key=null;
        for (Entry<String, outcome> entry : O.entrySet()){
            if (worst==null){
                worst=entry.getValue();
                key=entry.getKey();
            }
            else if (entry.getValue().value<worst.value){
                worst=entry.getValue();
                key=entry.getKey();
            }
        }
        return O.get(key);
    }

    @Override
    public String toString() {
        return "Agent [OriginalPrefrence=" + OriginalPrefrence + ", agentname=" + agentname
                + ", getPrefrenceAboutCurrentOptions()=" + getPrefrenceAboutCurrentOptions() + "]";
    }


    public ArrayList<outcome> OutComesBeterThen(String n) {
        ArrayList<outcome> a=new ArrayList<outcome> ();
        if (O.containsKey(n)) {
            double v = O.get(n).getValue();
            for (Entry<String, outcome> entry : O.entrySet()) {
                if (v < entry.getValue().getValue()) {
                    a.add(new outcome(entry.getValue()));
                }
            }
        }
        return a;
    }


    public ArrayList<outcome> OutComeWorstThen(String n) {
        ArrayList<outcome> a=new ArrayList<outcome> ();
        double v=O.get(n).getValue();
        for (Entry<String, outcome> entry : O.entrySet()){
            if (v>entry.getValue().getValue()){
                a.add(entry.getValue());
            }
        }
        return a;
    }

    public int NumberOfOfferLeft() {
        return O.size();
    }

    public outcome[] CopyNbest(int n) {
        outcome[] an=new outcome[O.size()];
        int i=0;
        for (outcome value : O.values()) {
            an[i++]=value;
        }
        Arrays.sort(an);
        return Arrays.copyOfRange(an,0,n);
    }

    public int numberOfOutcomeLeft() {
        return O.size();
    }

    public outcome copyOutcomeIn(int n) {

        return this.copyOutcome(pre[n-1]);
    }


    public outcome[] WorstThen(String out) {
        outcome o=copyOutcome(out);
        ArrayList<outcome> re=new ArrayList<outcome>();
        for (outcome value : O.values()) {
            if (value.getValue()<o.getValue()){
                re.add(new outcome(value));
            }
        }
        return re.toArray(new outcome[re.size()]);

    }

    public outcome[] BetterThen(String out) {
        outcome o=copyOutcome(out);
        ArrayList<outcome> re=new ArrayList<outcome>();
        for (outcome value : O.values()) {
            if (value.getValue()>o.getValue()){
                re.add(new outcome(value));
            }
        }
        return re.toArray(new outcome[re.size()]);

    }

    public outcome[] copyOutcomeInRange(int s, int e) {
        outcome[] an=new outcome[O.size()];
        int i=0;
        for (outcome value : O.values()) {
            an[i++]=value;
        }
        Arrays.sort(an, new Comparator<outcome>(){

            @Override
            public int compare(outcome o1, outcome o2) {
                if (o1.value<o2.value) return -1;
                else if (o1.value>o2.value) return 1;
                return 0;
            }

        });
        return Arrays.copyOfRange(an,s-1,e);
    }


    public outcome[] AllWorstFromWorstIn(outcome[] joinGoals) {
        outcome worst=null;
        String key=null;
        for ( outcome entry : joinGoals){
            if (worst==null){
                worst=entry;
                key=entry.getName();
            }
            else if (entry.getValue()<worst.value){
                worst=entry;
                key=entry.getName();
            }
        }
        ArrayList<outcome> w=new ArrayList<outcome>();
        for (Entry<String, outcome> entry : O.entrySet()){
            if (entry.getValue().getValue()<O.get(key).getValue()){
                w.add(new outcome(entry.getValue()));
            }
        }
        return w.toArray(new outcome[0]);
    }

    @Override
    public Object clone(){
        return new Agent(this);
    }

    public outcome getoutcomeIn(int rc_index_stop) {
        outcome[] w=CopyNworst(rc_index_stop);
        return  w[w.length-1];
    }
}
