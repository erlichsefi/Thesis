package tools;

import java.util.ArrayList;

public class outcome implements Comparable<outcome>{
	String agentname;
	String name;
	double value;
	int index;
	ArrayList<Path> pathToout;
	boolean NOinter=false;
	/**
	 * @param name
	 * @param value
	 */
	public outcome(String _agentname,String name, double value) {
		super();
		agentname=_agentname;
		this.name = name;
		this.value = value;
		pathToout=new ArrayList<Path>();
	}

	


	public outcome(outcome o) {
		agentname=o.agentname;
		name=o.name;
		value=o.value;
		index=o.index;
		pathToout=new ArrayList<Path>(o.pathToout);;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	@Override




	public String toString() {
		return name ;
	}
	public String getName() {
		return name;
	}
	public double getValue() {
		return value;
	}


	@Override
	public int compareTo(outcome o) {
		if (this.value<o.value) return 1;
		else if (this.value>o.value) return -1;
		return 0;
	}


	public ArrayList<Path> getPathToout() {
		return new ArrayList<Path>(pathToout);
	}


	public boolean equals(Object o){
		return ((outcome)o).name.equals(this.name);
	}

	
	public void addPath(ArrayList<Path> pathToout2) {
		pathToout.addAll(pathToout2);
		
	}

	public void firstPath(Path pathToout2) {
		pathToout=new ArrayList<Path>();
		pathToout.add(new Path(pathToout2));
		
	}

	public void InitPath(ArrayList<Path> pathToout2) {
		pathToout=new ArrayList<Path>();
		addPath(pathToout2);
	}


	

	public String getPlayer() {
		return agentname;
	}




	public boolean IsAllPathStartWith(String move) {
		for (int i = 0; i < pathToout.size(); i++) {
			if (!pathToout.get(i).startsWith(move)){
				return false;
			}
		}
		return true;
	}




	public void NoInters() {
		NOinter=true;		
	}




}
