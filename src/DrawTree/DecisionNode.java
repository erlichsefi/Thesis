package DrawTree;

import java.util.Arrays;


public class DecisionNode{
	private static final String SELECTED = "selected";
	private static final String NOTSELECTED = "not selected";
	
	private	String selection=NOTSELECTED;
	private String prefrenceInNode;
	private String playerName;
	private String offerGotten;
	private String reasonForResult;
	private	String resultFromThisTree;
	private int index=0;
	private DecisionNode[] options;


	public DecisionNode(String _player,String _offer,int numberOfOffers){
		offerGotten=_offer;
		options=new DecisionNode[numberOfOffers+1];
		playerName=_player;
		resultFromThisTree=_offer;
	}
	public DecisionNode(String _player,String _offer){
		offerGotten=_offer;
		options=new DecisionNode[0];
		playerName=_player;
		resultFromThisTree=_offer;
	}

	public void AddTooption(DecisionNode n){
		options[index++]=n;
	}


	public void Accept(String agentname,String _offer, String _reason) {
		DecisionNode t=new DecisionNode(agentname,_offer,0);
		reasonForResult=_reason;
		AddTooption(t);
	}
	public void setPreprence(String startingName,String secendagentName,String prefrence, String prefrence2) {
		prefrenceInNode=startingName+":"+prefrence+"\n"+secendagentName+":"+prefrence2;

	}
	public void addResultToLeaf(int k, String name) {
		this.options[k].resultFromThisTree=name;
		
	}
	

	public void initselected(String name) {
		for (int i = 0; i < options.length; i++) {
			if (options[i]!=null)
			if (options[i].resultFromThisTree.equals(name)){
				options[i].selection=SELECTED;
			}
			else{
				options[i].selection=NOTSELECTED;
			}
		}	

	}
	

	public DecisionNode[] getOptions() {
		return options;
	}


	public String getReason() {
		return reasonForResult;
	}

	

	public String getPrefrenceInNode() {
		return prefrenceInNode;
	}

	public String getPlayerId() {
		return playerName;
	}

	public String getResult() {
		return resultFromThisTree;
	}


	

	public String getOfferGotten() {
		return offerGotten;
	}

	public boolean IsSelected(){
		return this.selection.equals(SELECTED);
	}

	@Override
	public String toString() {
		return "TNode [player=" + playerName + ", offerGotten=" + offerGotten + ", status=" + selection +", resultFromThisTree=" + resultFromThisTree + ", options="
				+ Arrays.toString(options) + "]";
	}





}

