package tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



public class Agent {
	static int ids=0;
	int id=ids++;
	String[] pre;
	String strPrefrence;
	String agentname;
	HashMap<String,outcome> O;

	public Agent(String _agentname,String pref){
		strPrefrence=pref;
		pre=pref.split("<");
		O=new HashMap<String,outcome>();
		for (int i = 0; i < pre.length; i++) {
			O.put(pre[i], new outcome(_agentname,pre[i],i+1));

		}
		agentname=_agentname;
	}

	public Agent(Agent a){
		O=new HashMap<String,outcome>(a.O); 
		agentname=a.agentname;
		strPrefrence=a.strPrefrence;
		pre=a.pre;

	}




	/**
	 * return the outcome of the nego if the other agent know my preference and the offer i'm start with offer
	 * @param OtherAgent the agent that gave the offer
	 * @param offer the offer name
	 * @param FatherNode 
	 * @return
	 */
	public  outcome FullInfoTurn(Agent OtherAgent,String path,String offer, TNode FatherNode){
	
		//remove the offer that gotten
		OtherAgent.O.remove(offer);
		outcome MyValueForOffer=O.remove(offer);
		
		// there is noting to offer
		if (O.isEmpty()){
			FatherNode.Accept(agentname,offer,"last offer");
			MyValueForOffer.firstPath(path+":"+agentname+" accepts "+offer);
			return MyValueForOffer;
		}
		else{
			//there is no better thing to do 
			outcome next=CopyBestOutcome();
			if (next.getValue()<MyValueForOffer.getValue()){
				MyValueForOffer.firstPath(path+":"+agentname+" accepts "+offer+"  because it is best");
				FatherNode.Accept(agentname,offer,"better then: "+getPrefrenceAboutCurrentOptions());
				return MyValueForOffer;
			}
		}
		
		TNode current=new TNode(agentname,offer,O.size());
		current.initselected(MyValueForOffer.name);
		//build all possible outcomes
		outcome[] PossibleOut=new outcome[O.size()];
		int j=0;
		for (Map.Entry<String, outcome> entry : O.entrySet()) {
			String move=": "+ agentname+" reject "+offer+" and offer "+entry.getKey();
			outcome OtherAgentOutCome=new Agent(OtherAgent).FullInfoTurn(new Agent(this),path+move,entry.getKey(),current);
			PossibleOut[j++]=O.get(OtherAgentOutCome.name);
		}
		
		//find better outcome from current
		for (int i = 0; i < PossibleOut.length; i++) {
			//if it's better 
			if (PossibleOut[i].value>MyValueForOffer.value){
				MyValueForOffer=new outcome(PossibleOut[i]);
				current.initselected(PossibleOut[i].name);
			}else if (PossibleOut[i].value==MyValueForOffer.value){
				MyValueForOffer.addPath(PossibleOut[i].getPathToout());
				current.addselected(PossibleOut[i].name);
			}
		}
		
		FatherNode.Addoption(current);
		return MyValueForOffer;
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


	public outcome RemoveOutcome(String name) {
		return O.remove(name);
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

	public String getPrefrence() {
		return strPrefrence;
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


	public outcome copyOutcome(String name) {
		return O.get(name);
	}

	public String getAgentName() {
		return agentname;
	}


	public outcome[] RemoveNworst(int numberOfPassStarting) {
		outcome[] an=new outcome[numberOfPassStarting];
		for (int i = 0; i < numberOfPassStarting; i++) {
			an[i]=O.remove(pre[i]);
		}
		return an;
	}
	
	public outcome[] CopyNworst(int numberOfPassStarting) {
		outcome[] an=new outcome[numberOfPassStarting];
		for (int i = 0; i < numberOfPassStarting; i++) {
			an[i]=O.get(pre[i]);
		}
		return an;
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
		return "Agent [strPrefrence=" + strPrefrence + ", agentname=" + agentname
				+ ", getPrefrenceAboutCurrentOptions()=" + getPrefrenceAboutCurrentOptions() + "]";
	}

	
	public ArrayList<outcome> OutComesBeterThen(String n) {
		ArrayList<outcome> a=new ArrayList<outcome> ();
		double v=O.get(n).getValue();
		for (Entry<String, outcome> entry : O.entrySet()){
			if (v<entry.getValue().getValue()){
				a.add(entry.getValue());
			}
		}
		return a;
	}





}
