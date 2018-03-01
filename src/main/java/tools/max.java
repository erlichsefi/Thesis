package tools;

import Negotiation.GetWhatToOfferStrategy;

import java.util.*;

import static org.testng.AssertJUnit.assertTrue;

public class max implements GetWhatToOfferStrategy {

    @Override
    public HashMap<String,outcome> NoInfoTurn(Agent reciveing, Agent sending, GetWhatToOfferStrategy sendingSt, outcome offer) {
        HashMap<String,outcome> CurrentMinmuns=new HashMap<String,outcome>();

        // if reviced offer check the option to accept it
        if (offer != null) {
            reciveing.RemoveOutcome(offer.getName());
            CurrentMinmuns.put("accept",offer);
        }


        //checking the offering option
        String[] out = reciveing.getOutComeOptions();
        if (out.length!=0) {
            HashMap<String,HashMap<String,outcome>> l = new HashMap<String,HashMap<String,outcome>>();

            //adding all options to result
            for (String outcomeName : out) {
              //  assertTrue(reciveing.numberOfOutcomeLeft() == sending.numberOfOutcomeLeft());
                Agent newSending = new Agent(reciveing);
                HashMap<String,outcome> res=sendingSt.NoInfoTurn(new Agent(sending), newSending, this, newSending.RemoveOutcome(outcomeName));
                l.put(outcomeName,res );
            }
//            assertTrue(reciveing.numberOfOutcomeLeft() == sending.numberOfOutcomeLeft());
  //          assertTrue(out.length == sending.numberOfOutcomeLeft());

            System.out.println("=====Max=======");
            System.out.println("reciveing= "+reciveing);
            System.out.println("sending= "+sending);
            System.out.println("offer:"+offer);

            for (Map.Entry<String, HashMap<String, outcome>> tt : l.entrySet()) {

                String offerede=tt.getKey();
                HashMap<String,outcome> result= tt.getValue();
                System.out.println("BEFORE: "+offerede+"--> "+result);

                double tempMin = Double.MIN_VALUE;
                outcome min=null;
                for (String offered : result.keySet()) {
                    outcome gotten=result.get(offered);
                    outcome o2 = reciveing.CopyOutcome(gotten.getName());

                    if (tempMin < o2.getValue()) {
                        tempMin = o2.getValue();
                        min=new outcome(o2);
                    }
                }
                CurrentMinmuns.put(offerede,min);
                System.out.println("AFTER: "+offerede+"--> "+min);



            }
            System.out.println("=====Max=End===");

        }
        return CurrentMinmuns;
    }
}
