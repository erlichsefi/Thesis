
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


import Negotiation.Algo;
import Negotiation.Fullinfo;
import Negotiation.NoInfo;
import org.testng.annotations.Test;
import tools.*;

public class NoInfoTest {
	int numberOfRuns=100;
	int MaxNumberOfPrefrence=15;
	selection maxMinSelection = new selection() {

		@Override
		public int ToAccept(int m) {
			if (m % 2 == 0) {
				return (int) (Math.floor((m - 2) / 2)) + 1;
			} else {
				return (int) (Math.floor((m - 3) / 2)) + 2;

			}
		}

		@Override
		public int ToOffer(int m) {
			if (m % 2 == 0) {
				return (int) (Math.floor((m - 2) / 2)) + 2;
			} else {
				return (int) (Math.floor((m - 1) / 2)) + 1;

			}
		}
	};
	selection maxMinSelectionPlus = new selection() {

		@Override
		public int ToAccept(int m) {
			if (m % 2 == 0) {
				return (int) (Math.floor((m - 2) / 2)) + 1;
			} else {
				return (int) (Math.floor((m - 3) / 2)) + 2;

			}		}

		@Override
		public int ToOffer(int m) {
			return 1;
		}
	};

	/**
	 * min-m
	 */
	//@Test
	public void MinMaxVsMin(){
		String per1="o1<o2<o3<o4<o5<o6";
		String per2="o6<o5<o4<o3<o2<o1";
		Agent p1=new Agent("P1",per1);
		Agent p2=new Agent("P2",per2);
		HashMap<String,outcome> o=NoInfo.StratrgyNoInfoOutCome(p1, p2,new max(),new min());
		System.out.println("can offer= "+o);
	}


	//@Test
	public void MinMaxVsMinMax() throws IOException {

		String per1="o5<o4<o3<o2<o1";
		String[] out=new String[]{"o1","o2","o3","o4","o5"};

		FileWriter gfw = new FileWriter("over_"+out.length+".csv");
		BufferedWriter gw = new BufferedWriter(gfw);
		ArrayList<String> all=Algo.AllPossiblePrefrence(out);
		for (String per2:all) {
			Agent p1 = new Agent("P1", per1);
			Agent p2 = new Agent("P2", per2);
			Set<String> o = NoInfo.StratrgyNoInfoOutCome(p1, p2, new minMax(maxMinSelection), new minMax(maxMinSelection));
			Set<String> o1 = NoInfo.StratrgyNoInfoOutCome(p1, p2, new minMax(maxMinSelectionPlus), new minMax(maxMinSelection));
			Set<String> o2 = NoInfo.StratrgyNoInfoOutCome(p1, p2, new minMax(maxMinSelection), new minMax(maxMinSelectionPlus));
			Set<String> o3 = NoInfo.StratrgyNoInfoOutCome(p1, p2, new minMax(maxMinSelectionPlus), new minMax(maxMinSelectionPlus));


			gw.write(" "+per1+"Vs "+per2+" \n");
			gw.write("offers:,");
			for(String ou:out){
				gw.write(ou+",");
			}
			gw.write("\n");
			gw.write("min-max vs min-max, ");
			SetToResult(gw,o,out);
			gw.write("min-max-plus vs min-max,");
			SetToResult(gw,o1,out);
			gw.write("min-max vs min-max-plus ,");
			SetToResult(gw,o2,out);
			gw.write("min-max-plus vs min-max-plus,");
			SetToResult(gw,o3,out);
			gw.flush();
			gw.write("******,**********,************,**********\n");

		}
	}

	private void SetToResult(BufferedWriter gw,Set<String> o,String[] out) throws IOException {
		HashMap<String,ArrayList<String>> results=new HashMap<String,ArrayList<String>>();
		o.forEach(new Consumer<String>() {
			@Override
			public void accept(String s) {

				String[] split=s.split(",");
				String result=split[0];
				String starting=split[1].split(":")[1];
				if (results.containsKey(starting)){
					if (!results.get(starting).contains(result)) {
						results.get(starting).add(result);
					}
				}
				else{
					ArrayList<String> r=new ArrayList<String >();
					r.add(result);
					results.put(starting,r);
				}
			}
		});
		String result="";
		for (String key:out){
			ArrayList<String> s=results.get(key);
			if (s!=null) {
				result = result + results.get(key).toString().replaceAll(",", " ") + ",";
			}
			else{
				result = result + "X,";

			}
		}
		gw.write(result+"\n");
	}


	public void print(Agent p1,Agent p2,String[] out){

		for (int i = 0; i < out.length; i++) {
			outcome p1o=p1.copyOutcome(out[i]);
			outcome p2o=p2.copyOutcome(out[i]);
			outcome[] o1=p1.CopyNbest((int) (out.length-p1o.getValue()));
			outcome[] o2=p2.CopyNbest((int) (out.length-p2o.getValue()));
			ArrayList<outcome> I=Fullinfo.IntesectGroup(o1, o2);



		}
	}




}
