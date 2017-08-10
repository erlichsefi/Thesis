package Negotiation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import tools.outcome;





public class Algo {
	static String[] _out;



	public static ArrayList<String[]> AllSubGroupsWithOutReturn(String[] superSet, int k) {
		ArrayList<String[]> res = new ArrayList<>();
		getSubsets(Arrays.asList(superSet), k, 0, new ArrayList<String>(), res);
		return res;
	}

	private static void getSubsets(List<String> superSet, int k, int idx, ArrayList<String> current,ArrayList<String[]> solution) {
		//successful stop clause
		if (current.size() == k) {
			solution.add(current.toArray(new String[k]));
			return;
		}
		//unseccessful stop clause
		if (idx == superSet.size()) return;
		String x = superSet.get(idx);
		current.add(x);
		//"guess" x is in the subset
		getSubsets(superSet, k, idx+1, current, solution);
		current.remove(x);
		//"guess" x is not in the subset
		getSubsets(superSet, k, idx+1, current, solution);
	}


	public static ArrayList<ArrayList<String>> AllSubGroupsWithOutReturn(outcome[] superSet, int k) {
		ArrayList<ArrayList<String>> res = new ArrayList<>();
		getSubsets2(Arrays.asList(superSet), k, 0, new ArrayList<String>(), res);
		return res;
	}

	private static void getSubsets2(List<outcome> superSet, int k, int idx, ArrayList<String> current,ArrayList<ArrayList<String>> solution) {
		//successful stop clause
		if (current.size() == k) {
			solution.add(new ArrayList<String>(current));
			return;
		}
		//unseccessful stop clause
		if (idx == superSet.size()) return;
		String x = superSet.get(idx).getName();
		current.add(x);
		//"guess" x is in the subset
		getSubsets2(superSet, k, idx+1, current, solution);
		current.remove(x);
		//"guess" x is not in the subset
		getSubsets2(superSet, k, idx+1, current, solution);
	}

	



	//	public static ArrayList<String[]> AllSubGroupsWithOutReturn(String[] out,int k){
	//		ArrayList<String> out2=new ArrayList<String>();
	//		for (int i = 0; i < out.length; i++) {
	//			out2.add(out[i]);
	//		}
	//		ArrayList<String[]> result=new ArrayList<String[]>();
	//		ArrayList<String> tempout=new ArrayList<String>(Arrays.asList(out));
	//		AllPossibleSubGroups(tempout,result, k,0,new String[k]);
	//		return result;
	//	}
	//
	//	private static void AllPossibleSubGroups(ArrayList<String> out,ArrayList<String[]> result,int k,int j,String[] pre){
	//		if (j<k){
	//			ArrayList<String> tempout=new ArrayList<String>(out);
	//			while (!tempout.isEmpty()) {
	//				pre[j]=tempout.remove(0);
	//				AllPossibleSubGroups(tempout,result, k,j+1,pre);
	//			}
	//		}
	//		else{
	//			result.add(Arrays.copyOf(pre, pre.length));
	//		}
	//	}
	//
	public static ArrayList<String> AllPossiblePrefrence(String[] out){
		ArrayList<String> out2=new ArrayList<String>();
		for (int i = 0; i < out.length; i++) {
			out2.add(out[i]);
		}
		ArrayList<String> result=new ArrayList<String>();

		if (out.length>0){
			String pre="";
			AllPossible(out2,result,pre);
		}
		return result;
	}
	private static void AllPossible(ArrayList<String> out,ArrayList<String> result,String pre){
		if (out.size()!=0){
			for (int i = 0; i < out.size(); i++) {
				ArrayList<String> tempout=new ArrayList<String>(out);
				String o=tempout.remove(i);
				AllPossible(tempout,result,pre+o+"<");
			}
		}
		else{
			result.add(pre.substring(0,pre.lastIndexOf("<")));
		}
	}

	public static String randomPrefrenceOver(String[] out){
		ArrayList<String> out2 = new ArrayList<String>(Arrays.asList(out));
		String result=null;
		while (!out2.isEmpty()){
			Random Dice = new Random(); 
			int random_index = Dice.nextInt(out2.size()); 
			if (result==null){
				result=out2.remove(random_index);
			}
			else{
				result=result+"<"+out2.remove(random_index);
			}
		}
		return result;

	}

	public static String[] substract(String[] From, String[] the) {
		if (From.length==0 || the.length==0){
			return From;
		}
		String[] an=new String[From.length-the.length];
		int j=0;
		for (int i = 0; i < From.length; i++) {
			boolean Toremove=false;
			for (int k = 0; k < the.length; k++) {
				if (the[k].equals(From[i])){
					Toremove=true;	
				}
			}
			if (!Toremove){
				an[j++]=From[i];
			}
		}
		return an;
	}

	public static String[] append(String[] strings, String[] lowerThen2) {
		String[] result = Arrays.copyOf(strings, strings.length+lowerThen2.length);
		int offset = strings.length;
		System.arraycopy(lowerThen2, 0, result, offset, lowerThen2.length);
		return result;		
	}
	public static int indexOf(String[] out,String Sout){
		for (int i = 0; i < out.length; i++) {
			if (out[i].endsWith(Sout))
				return i;
		}
		return -1;
	}


	static void printStatistic(String[] out,int[][] statMath){
		System.out.println("Statictics:");
		System.out.println("    "+Arrays.toString(out));

		for (int i = 0; i < statMath.length; i++) {
			System.out.print(out[i]+"   ");
			for (int j = 0; j < statMath.length; j++) {
				System.out.print(statMath[i][j]+"   ");
			}
			System.out.println();
		}
	}

	static void printStatistic(String[] out,int[] statMath){
		System.out.println("start with:       "+Arrays.toString(out));
		System.out.println("numer of options: "+Arrays.toString(statMath));

	}




	public static String[] BuildOutComeArray(int runTo) {
		String[] out=new String[runTo];
		for (int i = 0; i < runTo; i++) {
			out[i]="o"+(i+1);
		}
		return out;
	}

	public static String BuildOneOrder(int runTo) {
		String out="o1";
		for (int i = 1; i < runTo; i++) {
			out=out+"<o"+(i+1);
		}
		return out;
	}




	public static String opossiteOrder(String per1) {
		String[] out=per1.split("<");
		String ans=out[0];
		for (int i = 1; i < out.length; i++) {
			ans=out[i]+"<"+ans;
		}
		return ans;
	}


	
	

}
