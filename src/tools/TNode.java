package tools;

import java.util.Arrays;


import Extra.TreePrinter;


public class TNode{
	private static final String SELECTED = "selected";
	private static final String NOTSELECTED = "not selected";
	private static final String ACCEPT="accept";
	private static final String REJECT="reject";

	String order;
	String player;
	String offer;
	String status=REJECT;
	String selection=NOTSELECTED;
	String reason;
	int index=0;
	TNode[] options;


	public TNode(String _player,String _offer,int numberOfOffers){
		offer=_offer;
		options=new TNode[numberOfOffers];
		player=_player;
		
	}

	public void Addoption(TNode n){
		options[index++]=n;
	}


	public void Accept(String agentname,String _offer, String _reason) {
		TNode t=new TNode(agentname,_offer,0);
		t.status=ACCEPT;
		reason=_reason;
		Addoption(t);
		initselected(_offer);
	}



	public void initselected(String name) {
		for (int i = 0; i < options.length; i++) {
			if (options[i]!=null)
			if (options[i].offer.equals(name)){
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
			if (options[i].offer.equals(name)){
				options[i].selection=SELECTED;
			}
		}	
	}


	@Override
	public String toString() {
		return "TNode [player=" + player + ", offer=" + offer + ", status=" + selection + ", options="
				+ Arrays.toString(options) + "]";
	}



	public String StepName() {
		return player+" "+status+" "+offer+" - "+selection;

	}
	public void print() {
		new TreePrinter(this);
	}





	public TNode[] getOPtions() {
		return options;
	}


	public String getReason() {
		return reason;
	}

	public void setPreprence(String startingName,String secendagentName,String prefrence, String prefrence2) {
		order=startingName+":"+prefrence+"  "+secendagentName+":"+prefrence2;

	}

	public String getOrder() {
		return order;
	}

	public String getPlayerId() {
		return player;
	}








}

