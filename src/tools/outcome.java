package tools;

import java.util.ArrayList;
import java.util.Arrays;

public class outcome implements Comparable<outcome>{
	String agentname;
	String name;
	int value;
	int index;
	ArrayList<String> pathToout;
	/**
	 * @param name
	 * @param value
	 */
	public outcome(String _agentname,String name, int value) {
		super();
		agentname=_agentname;
		this.name = name;
		this.value = value;
		pathToout=new ArrayList<String>();
	}


	public outcome(outcome o) {
		agentname=o.agentname;
		name=o.name;
		value=o.value;
		index=o.index;
		pathToout=new ArrayList<String>(o.pathToout);;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	@Override




	public String toString() {
		return "outcome [agentname="+agentname+", name=" + name + ", value=" + value + "]";
	}
	public String getName() {
		return name;
	}
	public int getValue() {
		return value;
	}


	@Override
	public int compareTo(outcome o) {
		if (this.value<o.value) return 1;
		else if (this.value>o.value) return -1;
		return 0;
	}


	public ArrayList<String> getPathToout() {
		return pathToout;
	}





	public void addPath(ArrayList<String> pathToout2) {
		pathToout.addAll(pathToout2);
		
	}

	public void firstPath(String pathToout2 ) {
		pathToout=new ArrayList<String>();
		pathToout.add(pathToout2);
		
	}

	public void InitPath(ArrayList<String> pathToout2) {
		pathToout.clear();
		addPath(pathToout2);
	}




}
