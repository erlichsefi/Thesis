package tools;

import java.util.ArrayList;

public class options{
	TNode tree;
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
		return "options [prefrence=" + prefrence + ", startingOffer=" + startingOffer + ", resultOffer=" + resultOffer+"\n"+getPaths();
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

	public int longestPathsTurnNumebr() {
		String str=longestPaths();
		return str.split(":").length;
	}

	public TNode getTree() {
		return tree;
	}

	public void setTree(TNode tree) {
		this.tree = tree;
	}
	
	
}