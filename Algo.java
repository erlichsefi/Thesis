import java.util.ArrayList;
import java.util.Arrays;



public class Algo {

	



	

	protected static ArrayList<String> AllPossiblePrefrence(String[] out){

		ArrayList<String> out2=new ArrayList<String>();
		for (int i = 0; i < out.length; i++) {
			out2.add(out[i]);
		}
		String pre="";
		ArrayList<String> result=new ArrayList<String>();
		AllPossible(out2,result,pre);
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


	protected static int indexOf(String[] out,String Sout){
		for (int i = 0; i < out.length; i++) {
			if (out[i].endsWith(Sout))
				return i;
		}
		return -1;
	}

	public static void FullINFOsTATISTIC(){
		String[] out={"o1","o2","o3"};//,"o4","o5","o6","o7"};
		String otherAgnetpref="o1<o2<o3";//<o4<o5<o6<o7";

		int[][] statMath=new int[out.length][out.length];
		System.out.println("        "+otherAgnetpref+"             "+Arrays.toString(out));
		ArrayList<String> client1prefernce=Algo.AllPossiblePrefrence(out);

		for (int i = 0; i < client1prefernce.size(); i++) {
			String prefrence=client1prefernce.get(i);

			System.out.print("agent prefernce is : "+prefrence+" ");
			for (int k = 0; k < out.length; k++) {
				Agent myAgnet=new Agent("agent1",prefrence);
				Agent otherAgent=new Agent("agent2",otherAgnetpref);
				outcome o=otherAgent.FullInfoTurn(myAgnet,out[k]);
				System.out.print(o.name+"  ");
				int outindex=indexOf(out,o.name);
				statMath[k][outindex]++;
			}
			System.out.println();
		}



		System.out.println("**************************************************");

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


}
