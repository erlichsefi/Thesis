package tools;

import Negotiation.GetResltsStrategy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class minMax implements GetResltsStrategy {
    private selection be;

    public minMax(selection _be){
        be=_be;
    }



    @Override
    public Set<String> NoInfoTurn(Agent reciveing, Agent sending, GetResltsStrategy sendingSt, outcome offer,String path) {

        Set<String> allSubGameResult=new HashSet<String>();
        if (offer !=null) {

            int m = reciveing.O.size();

            int toAccept=be.ToAccept(m);
            outcome[] accepting = reciveing.CopyNbest(toAccept);
            reciveing.RemoveOutcome(offer.getName());

            if (m==0 || Arrays.asList(accepting).contains(offer)) {
                //accept
                Set<String> on=new HashSet<String>();
                on.add(offer.name+","+path+"");
                return on;
            }
        }
        int m=reciveing.O.size();
        int toOffer=be.ToOffer(m);
       // System.out.println("AGENT= "+reciveing.agentname+" m= "+ m+"  Offer LIMIT ="+toOffer);

        //reject and offer one of the following
        outcome[] options=reciveing.CopyNbest(toOffer);
      // System.out.println(Arrays.toString(options));
        for(outcome op:options){
            Agent _newSending=new Agent(reciveing);
            _newSending.RemoveOutcome(op.getName());
            Agent _newReciveing=new Agent(sending);
            Set<String> res=sendingSt.NoInfoTurn(_newReciveing,_newSending,this,op,path+":"+op.getName());
            allSubGameResult.addAll(res);

        }

        return allSubGameResult;

    }
}
