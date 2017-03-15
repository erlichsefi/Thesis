package tools;

import java.util.ArrayList;

import DrawTree.DecisionNode;

public class options{
	DecisionNode tree;
	String prefrence;
	String startingOffer;
	String resultOffer;
	ArrayList<String> path;
	/**
	 * @param prefrence
	 * @param startingOfer
	 */
	public options(String _prefrence, String _startingOfer,String _resultOffer) {
		super();
		this.prefrence = _prefrence;
		this.startingOffer = _startingOfer;
		this.resultOffer=_resultOffer;
		path=new ArrayList<String>();
	}
	
	/**
	 * @param prefrence
	 * @param startingOfer
	 */
	public options(String _prefrence, String _startingOfer,String _resultOffer,String _path) {
		super();
		this.prefrence = _prefrence;
		this.startingOffer = _startingOfer;
		this.resultOffer=_resultOffer;
		path=new ArrayList<String>();
		path.add(_path);
	}
	

	public options(String prefrence2, String startingOffer2, String name, ArrayList<String> pathToout) {
		super();
		this.prefrence = prefrence2;
		this.startingOffer = startingOffer2;
		this.resultOffer=name;
		path=new ArrayList<String>(pathToout);
	}

	public String getResult() {
		return resultOffer;
	}
	public String getStartingOffer() {
		// TODO Auto-generated method stub
		return startingOffer;
	}


	public String getPrefrence() {
		return prefrence;
	}


	@Override
	public String toString() {
		return "options [prefrence=" + prefrence + ", startingOffer=" + startingOffer + ", resultOffer=" + resultOffer;
	}
	
	public String getPaths(){
		String ANS=" PATHS= \n";
		for (int i = 0; i < path.size(); i++) {
			ANS=ANS+path.get(i)+"\n";
		}
		return ANS;
	}

	
	public String longestPaths() {
		String l="";
		for (int i = 0; i < path.size(); i++) {
			if (l.split(":").length<path.get(i).split(":").length){
				l=path.get(i);
			}
		}
		return l;
		
	}
	
	public String getOrder(String path){
		String[] l=path.split(":");
		String a="";
		for (int i = 0; i < l.length; i++) {
			if (!l[i].contains("accept"))
			a=a+l[i].substring(l[i].length()-2,l[i].length())+":";
		}
		return a;
	}
	
	public String GetPathSizedN(int numeberOfOffers){
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).split(":").length==(numeberOfOffers+1)){
				return new String(path.get(i));
			}
		}
		return null;
	}

	public int longestPathsTurnNumebr() {
		String str=longestPaths();
		return str.split(":").length;
	}

	public DecisionNode getTree() {
		return tree;
	}

	public void setTree(DecisionNode tree) {
		this.tree = tree;
	}

	public void setPath(String string) {
		path.add(string);
		
	}

	public boolean IsAllPathsEndWith(String move){
		for (int i = 0; i < path.size(); i++) {
			if (!getOrder(path.get(i)).endsWith(move+":")){
				return false;
			}
		}
		return true;
	}
	
	public String getPathOf(String longestPaths) {
		for (int i = 0; i < path.size(); i++) {
			String order=getOrder(path.get(i));
			if (order.equals(longestPaths)){
				return path.get(i);
			}
		}
		return null;
	}

	public void initPath(String p) {
		path.clear();
		path.add(p);
		
	}

	public String getOrders() {
		String ans="";
		for (int i = 0; i < path.size(); i++) {
			ans=ans+getOrder(path.get(i))+"\n";
		
		}
		return ans;
	}
	
	
}