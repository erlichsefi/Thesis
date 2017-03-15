package tools;

import java.util.Arrays;


import Extra.GuiTreePrinter;


public class TNode{
	private static final String SELECTED = "selected";
	private static final String NOTSELECTED = "not selected";
	private static final String ACCEPT="accept";
	private static final String REJECT="reject";
	
	String order;
	String player;
	String offerGotten;
	String status=REJECT;
	String selection=NOTSELECTED;
	String reason;
	String resultFromThisTree;

	
	int index=0;
	TNode[] options;


	public TNode(String _player,String _offer,int numberOfOffers){
		offerGotten=_offer;
		options=new TNode[numberOfOffers+1];
		player=_player;
		resultFromThisTree=_offer;
	}
	public TNode(String _player,String _offer){
		offerGotten=_offer;
		options=new TNode[0];
		player=_player;
		resultFromThisTree=_offer;
	}

	public void Addoption(TNode n){
		options[index++]=n;
	}


	public void Accept(String agentname,String _offer, String _reason) {
		TNode t=new TNode(agentname,_offer,0);
		t.status=ACCEPT;
		reason=_reason;
		Addoption(t);
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
	
	public void addselected(String name){
		for (int i = 0; i < options.length; i++) {
			if (options[i]!=null)
			if (options[i].resultFromThisTree.equals(name)){
				options[i].selection=SELECTED;
			}
		}	
	}


	@Override
	public String toString() {
		return "TNode [player=" + player + ", offerGotten=" + offerGotten + ", status=" + selection +", resultFromThisTree=" + resultFromThisTree + ", options="
				+ Arrays.toString(options) + "]";
	}



	public String StepName() {
		return " R="+resultFromThisTree;

	}
	public String getOfferOffered(){
		return offerGotten;
	}
	public void print() {
		final TNode root=this;
		new Thread(){
			public void run(){
				GuiTreePrinter.createSampleTree(root);

			}
		}.start();
	}





	public TNode[] getOPtions() {
		return options;
	}


	public String getReason() {
		return reason;
	}

	public void setPreprence(String startingName,String secendagentName,String prefrence, String prefrence2) {
		order=startingName+":"+prefrence+"\n"+secendagentName+":"+prefrence2;

	}

	public String getOrder() {
		return order;
	}

	public String getPlayerId() {
		return player;
	}

	public String getResult() {
		return resultFromThisTree;
	}

	public void setResult(String result) {
		this.resultFromThisTree = result;
	}

	public void addResultToLeaf(int k, String name) {
		this.options[k].setResult(name);
		
	}

	public String getOffer() {
		return offerGotten;
	}

	public boolean IsSelected(){
		return this.selection.equals(SELECTED);
	}





}

