package tools;

import java.util.ArrayList;

public class Path {
    ArrayList<String> CorePath;
    ArrayList<String> desciption;


    public Path(){
        CorePath=new ArrayList<String>();
        desciption=new ArrayList<String>();
    }

    public Path(Path p){
        CorePath=new ArrayList<String>(p.CorePath);
        desciption=new ArrayList<String>(p.desciption);
    }


    public int RejactAndOffer(String outcome, String player){
        CorePath.add(outcome);
        desciption.add(player +" reject and  offer ");
        return CorePath.size();
    }

    public void desciption(int loc,String why){
        desciption.get(loc).replace("###",why);
    }

    public void Offer(String outcome, String player){
        CorePath.add(outcome);
        desciption.add(player +" offer ");
    }



    public void Accept(String player,String reson){
        desciption.add(player + " accept "+" because "+reson);
    }


    public int size() {
       return CorePath.size();
    }

    public boolean endsWith(String move) {
        return CorePath.get(CorePath.size()-1).equals(move);
    }

    public String toString(){
        if (!desciption.isEmpty()) {
            String pa = "";
            int i;
            for ( i = 0; i < CorePath.size(); i++) {
                pa = pa + desciption.get(i) + " " + CorePath.get(i) + ":";
            }
            for (; i < desciption.size(); i++) {
                pa = pa + desciption.get(i) + " ";
            }
            return pa;
        }
        return  "";
    }

    public boolean startsWith(String move) {
        return CorePath.get(0).equals(move);

    }

}
