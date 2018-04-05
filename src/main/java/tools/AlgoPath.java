package tools;

public class AlgoPath extends Path {


    public AlgoPath() {
        super();
    }

    public void addIntersection(String str){
        this.CorePath.add(str);
    }

    public void addUnion(String str){
        this.desciption.add(str);
    }
    public int size() {
        return CorePath.size()+desciption.size();
    }

    public boolean asIntersection(){
        return !CorePath.isEmpty();
    }
    public String offerNumber(int i){
        if (i<CorePath.size()) {
            return CorePath.get(i);
        }
        else{
            return  desciption.get(i-CorePath.size());
        }
    }
}
