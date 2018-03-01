//package tools;
//
//import Negotiation.GetWhatToOfferStrategy;
//
//import java.util.Arrays;
//
//public class minMax_Star implements GetWhatToOfferStrategy {
//
//    @Override
//    public outcome NoInfoTurn(Agent reciveing, Agent sending,GetWhatToOfferStrategy sendingSt, outcome offer) {
//        int m=reciveing.O.size();
//        int toOffer=1;
//        int toAccept=m-1-toOffer;
//        outcome[] accepting= reciveing.CopyNbest(toAccept);
//        if (offer !=null && Arrays.asList(accepting).contains(offer)){
//
//            //accept
//            return reciveing.RemoveOutcome(offer.getName());
//        }
//        else{
//            //reject
//            outcome[] offering= reciveing.CopyNbest(toOffer);
//            reciveing.RemoveOutcome(offering[0].getName());
//            return offering[0];
//        }
//    }
//}
