package tools;

import java.util.ArrayList;

import DrawTree.DecisionNode;

public class options{
	DecisionNode tree;
	String prefrence;
	String startingOffer;
	String resultOffer;
	ArrayList<Path> path;

	public options(String _prefrence, String _startingOfer,String _resultOffer) {
		super();
		this.prefrence = _prefrence;
		this.startingOffer = _startingOfer;
		this.resultOffer=_resultOffer;
		path=new ArrayList<Path>();
	}


	public options(String _prefrence, String _startingOfer,String _resultOffer,Path _path) {
		super();
		this.prefrence = _prefrence;
		this.startingOffer = _startingOfer;
		this.resultOffer=_resultOffer;
		path=new ArrayList<Path>();
		path.add(_path);
	}


	public options(String prefrence2, String startingOffer2, String name, ArrayList<Path> pathToout) {
		super();
		this.prefrence = prefrence2;
		this.startingOffer = startingOffer2;
		this.resultOffer=name;
		path=new ArrayList<Path>(pathToout);
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

	public String getPathsInSize(int size){
		ArrayList<String> sp=new ArrayList<String>();
		String ANS=" PATHS= \n";
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).size()==size){
				ANS=ANS+path.get(i)+"\n";
			}
		}
		return ANS;
	}
	public ArrayList<Path> getPathslist(){
		return path;
	}

	public Path longestPaths() {
		Path l=new Path(path.get(0));
		for (int i = 0; i < path.size(); i++) {
			if (l.size()<path.get(i).size()){
				l=new Path(path.get(i));
			}
		}
		return l;

	}

	public int ShortestPathLength() {
		Path l=new Path(path.get(0));
		for (int i = 0; i < path.size(); i++) {
			if (l.size()>path.get(i).size()){
				l=new Path(path.get(i));
			}
		}
		return l.size();

	}

	public Path ShortestPaths() {
		Path l=new Path(path.get(0));
		for (int i = 0; i < path.size(); i++) {
			if (l.size()>path.get(i).size()){
				l=new Path(path.get(i));
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

	public Path GetPathSizedN(int numeberOfOffers){
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).size()==(numeberOfOffers)){
				return new Path(path.get(i));
			}
		}
		return null;
	}

	public int longestPathsTurnNumebr() {
		Path str=longestPaths();
		return str.size();
	}

	public DecisionNode getTree() {
		return tree;
	}

	public void setTree(DecisionNode tree) {
		this.tree = tree;
	}

	public void setPath(Path string) {
		path.add(new Path(string));

	}

	public boolean IsAllPathsEndWith(String move){
		for (int i = 0; i < path.size(); i++) {
			if (!path.get(i).endsWith(move)){
				return false;
			}
		}
		return true;
	}

//	public String getPathOf(String prefix) {
//		for (int i = 0; i < path.size(); i++) {
//			String order=getOrder(path.get(i));
//			if (order.equals(longestPaths)){
//				return path.get(i);
//			}
//		}
//		return null;
//	}

	public void initPath(Path p) {
		path.clear();
		path.add(new Path(p));

	}

	public String getOrders() {
		String ans="";
		for (int i = 0; i < path.size(); i++) {
			ans=ans+path.get(i)+"\n";

		}
		return ans;
	}

	public int NumberOfOffersTo(String outcome) {
		int i=0;
		for (DecisionNode node:tree.getOptions()) {
			if (node != null && node.getResult().equals(outcome)){
				i++;
			}
		}
		return i;
	}


	public boolean ContainPathStartWith(outcome toremove) {
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).startsWith(toremove.name)){
				return true;
			}

		}
		return false;
	}
}