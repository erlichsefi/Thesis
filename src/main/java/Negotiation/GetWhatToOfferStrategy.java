package Negotiation;

import tools.Agent;
import tools.outcome;

import java.util.HashMap;

public interface GetWhatToOfferStrategy {

    /**
     *
     * @param reciveing
     * @param sending
     * @param sendingSt
     * @param offer
     * @param p
     * @return what to offer to get the min/max/etc
     */
    public HashMap<String,outcome> NoInfoTurn(Agent reciveing, Agent sending, GetWhatToOfferStrategy sendingSt, outcome offer);
}
