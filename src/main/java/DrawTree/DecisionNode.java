package DrawTree;

import java.util.Arrays;
import java.util.HashMap;


public class DecisionNode{
	private static final String SELECTED = "selected";
	private static final String NOTSELECTED = "not selected";
	
	private	String selection=NOTSELECTED;
	private String prefrenceInNode;
	private String playerName;
	private String offerGotten;
	private String reasonForResult;
	private	String resultFromThisTree;
	private	String resultAfterResponse;
	private int index=0;
	private DecisionNode[] options;
	private HashMap<String,String> agentsResponsd;


	public DecisionNode(String _player,String _offer,int numberOfOffers){
		offerGotten=_offer;
		options=new DecisionNode[numberOfOffers+1];
		agentsResponsd=new HashMap<String,String>();
		playerName=_player;
		resultFromThisTree=_offer;
	}
	public DecisionNode(String _player,String _offer){
		offerGotten=_offer;
		options=new DecisionNode[0];
		playerName=_player;
		resultFromThisTree=_offer;
	}

	public DecisionNode(DecisionNode tree) {
		if (tree==null) return;
		this.selection = tree.selection;
		this.prefrenceInNode = tree.prefrenceInNode;
		this.playerName = tree.playerName;
		this.offerGotten = tree.offerGotten;
		this.reasonForResult = tree.reasonForResult;
		this.resultFromThisTree = tree.resultFromThisTree;
		this.index = tree.index;
		this.options = tree.options;
	}


    public void addBranch(DecisionNode n){
		options[index++]=n;
	}


	public void Allaccept(String _offer, String _reason) {
		DecisionNode t=new DecisionNode("all",_offer,0);
		reasonForResult=_reason;
		agentsResponsd.put("all","accept");
		addBranch(t);
	}

	public void Accept(String agentname,String _offer, String _reason) {
		DecisionNode t=new DecisionNode(agentname,_offer,0);
		reasonForResult=_reason;
		agentsResponsd.put(agentname,"accept");
		addBranch(t);
	}

	public void Reject(String agentName, String offer, String _reason) {
		DecisionNode t=new DecisionNode(agentName,offer,0);
		reasonForResult=_reason;
		agentsResponsd.put(agentName,"reject");

		addBranch(t);
	}

	public void setPreprence(String startingName,String secendagentName,String prefrence, String prefrence2) {
		prefrenceInNode=startingName+":"+prefrence+"\n"+secendagentName+":"+prefrence2;

	}
	public void addBranchResult(int branchNumber, String result) {
		this.options[branchNumber].resultFromThisTree=result;
		
	}
	

	public void setSelected(String name) {
		this.resultFromThisTree=name;
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

