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
	 * @return
	 */
	public  outcome FullInfoTurn(Agent OtherAgent,String offer){
		//System.out.println("level= "+id+" agentname= "+agentname+" got an offer : "+offer);
		OtherAgent.O.remove(offer);
		outcome max_reject=O.remove(offer);
		System.out.print(".");
		//System.out.println("level= "+id+" agentname= "+agentname+" possible offers "+O.entrySet());
		for (Map.Entry<String, outcome> entry : O.entrySet()) {
			//System.out.println("level= "+id+" agentname= "+agentname+" looking for other options with : "+entry.getKey());
			outcome otheragent=new Agent(OtherAgent).FullInfoTurn(new Agent(this),entry.getKey());
			outcome myoutcome=O.get(otheragent.name);

			if (myoutcome.value>max_reject.value){
				max_reject=myoutcome;
				//System.out.println("level= "+id+" agentname= "+agentname+" got better offer: "+myoutcome);

			}
		}
		//System.out.println(max_reject.name);
		return max_reject;
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
		// TODO Auto-generated method stub
		return O.remove(name);
	}

	
	public boolean HasMoreOffers() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return strPrefrence;
	}

	
	
}
