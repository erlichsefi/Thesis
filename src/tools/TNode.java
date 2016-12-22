package tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TNode{
	final String ACCEPT="accept";
	final String REJECT="reject";
	String player;
	String offer;
	String status;
	int i=0;
	boolean[] chosen;
	TNode[] options;


	public TNode(String _player,String _offer,int numberOfOffers){
		offer=_offer;
		options=new TNode[numberOfOffers];
		player=_player;
	}


	public void Addoption(TNode n){
		options[i++]=n;
	}


	public void Accept(String agentname,String _offer) {
		TNode t=new TNode(agentname,_offer,0);
		t.status=ACCEPT;
		Addoption(t);
	}



	public void Setselected(String name) {
		chosen=new boolean[options.length];
		for (int i = 0; i < options.length; i++) {
			if (options[i].offer.equals(name)){
				chosen[i]=true;
			}
		}			
	}


	@Override
	public String toString() {
		return "TNode [player=" + player + ", offer=" + offer + ", status=" + status + ", options="
				+ Arrays.toString(options) + "]";
	}


	public void toPrint() {
		if (status != null && status.equals(this.ACCEPT)){
			System.err.print(this.offer+" || ");
		}
		else{
			System.out.print(this.offer+" || ");
		}
	}
	public void print() {
		int el=1;
		int n=0;
		Queue<TNode> q = new LinkedList<TNode>();
		q.add(this);
		System.out.print(this.player+" : ");
		while (!q.isEmpty()){
			TNode c=q.poll();
			el--;
			c.toPrint();
			if (c!=null){
				for (int i = 0; i < c.options.length; i++) {
					q.add(c.options[i]);
				}
				if (el>=0){
					n=n+c.options.length;
				}
				if (el==0){
					el=n;
					n=0;
					System.out.println();
					System.out.print(c.player+" : ");

				}
			}
		}

	}



}

