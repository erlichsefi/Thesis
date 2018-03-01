package Negotiation;

import tools.Agent;
import tools.options;
import tools.outcome;

import java.util.ArrayList;
import java.util.Set;

public interface GetResltsStrategy {


    public Set<String> NoInfoTurn(Agent reciveing, Agent sending, GetResltsStrategy sendingSt, outcome offer, String path) ;
}
