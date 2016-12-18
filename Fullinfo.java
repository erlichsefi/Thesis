import java.util.ArrayList;

public class Fullinfo extends Algo{
	static int[][] sta=null;

	
	
	/**
	 * a game when both of the agents know the prefrences
	 * @param out
	 * @param startingagent
	 * @param otheragnet
	 * @param offerAgentWhat
	 * @return
	 */
	public static ArrayList<options> FullInfoGame(String[] out,Agent otheragnet,boolean IsOtherAgnetStarting,String offerAgentWhat){
		sta=new int[out.length][out.length];
		ArrayList<options> op=new ArrayList<options>();
		ArrayList<String> client1prefernce=AllPossiblePrefrence(out);
		for (int i = 0; i < client1prefernce.size(); i++) {
			String prefrence=client1prefernce.get(i);
			for (int k = 0; k < out.length; k++) {
				Agent myAgnet=new Agent("myagnet",prefrence);
				Agent otherAgent=new Agent(otheragnet);
				
				outcome o=null;
				if (IsOtherAgnetStarting){
					 o=myAgnet.FullInfoTurn( otherAgent,out[k]);
				}else{
					 o=otherAgent.FullInfoTurn(myAgnet,out[k]);
				}
				
				if (o.name.equals(offerAgentWhat)){
					op.add(new options(prefrence,out[k]));
				}
				int Resultindex=indexOf(out,o.name);
				sta[k][Resultindex]++;
			}
		}
		return op;
	}
	
	public static int[][] getStatistic(){
		return sta;
	}


}
