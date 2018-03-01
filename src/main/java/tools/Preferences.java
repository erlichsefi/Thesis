package tools;

import java.util.Random;

public class Preferences {
	static Random r=new Random(System.currentTimeMillis());

	

	public static String PlaceIN(String add, int p, String per1) {
		String[] pr=per1.split("<");
		String ans=pr[0];
		for (int i = 1; i < pr.length; i++) {
			if (i==p){
				ans=ans+"<"+add;
			}
			ans=ans+"<"+pr[i];
		}
		if (pr.length==p){
			ans=ans+"<"+add;
		}
		return ans;
	}
	public static String PlaceBefore(String add, String result, String per1) {
		String[] pr=per1.split("<");
		String ans=pr[0];
		int p=RandomPlaceBefore(result,per1);
		for (int i = 1; i < pr.length; i++) {
			if (i==p){
				ans=ans+"<"+add;
			}
			ans=ans+"<"+pr[i];
		}
		return ans;
	}
	public static  int RandomPlaceBefore(String result, String per1){
		String[] pr=per1.split("<");
		int i=0;
		for ( i = 0; i < pr.length; i++) {
			if (pr[i].equals(result)){
				break;
			}
		}
		return  r.nextInt(i)+1;
	}

	public static  int RandomPlaceAfter(String result, String per1){
		String[] pr=per1.split("<");
		int i=0;
		for ( i = 0; i < pr.length; i++) {
			if (pr[i].equals(result)){
				break;
			}
		}
		return  r.nextInt(i)+1+(pr.length-i);
	}

	public static  String PlaceAfter(String add, String result, String per1) {
		String[] pr=per1.split("<");
		int p=RandomPlaceAfter(result,per1);

		String ans=pr[0];
		for (int i = 1; i < pr.length; i++) {
			ans=ans+"<"+pr[i];
			if (i==p){
				ans=ans+"<"+add;
			}

		}
		return ans;
	}


	
	
	
	public static String Remove(String pre,String to ){
		return pre.replace("<"+to+"<","<");
		
	}
	
	public static int IndexOf(String name, String per) {
		String[] o=per.split("<");
		for (int i = 0; i < o.length; i++) {
			if (o[i].equals(name)){
				return i+1;
			}
		}
		return -1;
	}
}
