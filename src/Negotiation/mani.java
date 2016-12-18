package Negotiation;

import java.util.ArrayList;
import java.util.Arrays;

import tools.Agent;
import tools.options;
import tools.outcome;

public class mani {


	public static void main(String[] args) {
		String[] out={"o1","o2","o3","o4","o5","o6"};
		ArrayList<String>[] stat=new ArrayList[out.length];
		for (int i = 0; i < stat.length; i++) {
			stat[i]=new ArrayList<String>();	
		}
		//ArrayList<String> p1=Algo.AllPossiblePrefrence(out);
		ArrayList<String> p2=Algo.AllPossiblePrefrence(out);
		//for (int i = 0; i < p1.size(); i++) {
			for (int j = 0; j < p2.size(); j++) {
				String per1="o1<o2<o3<o4<o5<o6";//<o4<o5";
				String per2=p2.get(j);

				Agent otherAgnet=new Agent("other agent",per1);
				Agent my=new Agent("me",per2);
				outcome o=NoInfo.NoInfoOutCome(my,otherAgnet);
				stat[Algo.indexOf(out, o.getName())].add(per2);
			}
	//	}
			for (int i = 0; i < stat.length; i++) {
				System.out.println(out[i]+" "+stat[i].size()+":");
				System.out.println(stat[i]);
			}

		System.out.println();
		System.out.println();

	}
}
