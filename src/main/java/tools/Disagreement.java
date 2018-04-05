package tools;

public class Disagreement extends outcome {


    public Disagreement(String _agentname, String name, double value) {
        super(_agentname, name, value);
    }

    public Disagreement(Disagreement d){
        super(d);
    }
}
