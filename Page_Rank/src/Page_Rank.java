import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Page_Rank {
	
	
	
	
	
	
	public static ArrayList<Sommet> adjacencyArray = new ArrayList<>();
	public static ArrayList<Integer> indexTableList;
	public static Set<Integer> indexTableSet = new HashSet<>();
	public static Map<Integer, Integer> map = new HashMap<>();
	public static Map<Integer, Integer> mapDegSortant = new HashMap<>();
	public static String path = "/Vrac/CPA-PageRank/alr21--dirLinks--enwiki-20071018.txt";
	//public static String path = "test.txt";
	
	public static void readFile(String path) {
		
		BufferedReader lecteurAvecBuffer = null;
	    String ligne = "";
	    try{
			lecteurAvecBuffer = new BufferedReader(new FileReader(path));
			int s_old = -1;
			int index = -1;
			int cpt = 0;
			while((ligne = lecteurAvecBuffer.readLine()) != null) {
				String [] aretes = new String[2];
				aretes = ligne.split("\\s+", 2);
				String s = aretes[0];
				if(s_old == Integer.parseInt(s)) {
					adjacencyArray.get(index).neighbor.add(Integer.parseInt(aretes[1]));
				}else {
					ArrayList<Integer> nei = new ArrayList<>();
					nei.add(Integer.parseInt(aretes[1]));
					adjacencyArray.add(new Sommet(Integer.parseInt(s), nei));
					s_old = Integer.parseInt(s);
					index++;
				}
				
				if(mapDegSortant.containsKey(Integer.parseInt(aretes[1]))){
					mapDegSortant.put(Integer.parseInt(aretes[1]), mapDegSortant.get(Integer.parseInt(aretes[1]))+1);
				}else {
					mapDegSortant.put(Integer.parseInt(aretes[1]), 1);
				}
				
				
				int v = Integer.parseInt(aretes[1]);
				int k = Integer.parseInt(aretes[0]);
//				if(!indexTable.contains(k) && !indexTable.contains(v)) {
//					indexTable.add(k);
//					indexTable.add(v);
//				}else if(!indexTable.contains(k)){
//					indexTable.add(k);
//				}else if(!indexTable.contains(v)){
//					indexTable.add(v);
//				}
				indexTableSet.add(k);
				indexTableSet.add(v);
				
				
			}
			System.out.println("reading finished");
			indexTableList = new ArrayList<>(indexTableSet);
			Collections.sort(indexTableList);
			for(int i = 0; i<indexTableList.size();i++) {
				map.put(indexTableList.get(i), i);
			}
			lecteurAvecBuffer.close();
		}
	    
	    
	    catch(IOException exc){
	    	System.out.println("Erreur d'ouverture");
	    }
	}
	
	
	public static float [] prodMatVect(ArrayList<Sommet> mat, float [] A){

		float [] B = new float[indexTableList.size()];
		for(int i = 0; i<mat.size(); i++) {
			float degSortant = (float)mat.get(i).neighbor.size();
			for(int j=0; j<mat.get(i).neighbor.size();j++) {
				int ii = map.get(mat.get(i).neighbor.get(j));
				B[ii] += (float)(A[i] / degSortant);
			}	
		}
		
		return B;
	}
	
	
	public static float[] powerIteration(float alpha, int t) {
		
		float [] A = new float[indexTableList.size()];
		
		for(int i=0; i<A.length; i++) {
			A[i] = (float)1/adjacencyArray.size();
		}
		
		float[] produit = new float[indexTableList.size()];
		
		for(int i=0; i<A.length; i++) {
			produit[i] = 0;
		}
		
		for(int k =0; k<t; k++) {
			
			produit = prodMatVect(adjacencyArray,A);
			
//			System.out.println("iteration :"+k+" P[0] "+produit[0]);
//			System.out.println("iteration :"+k+" P[size - 1] "+produit[produit.length -1]);
			float sommeDesEltDuTabProduit = 0;
			
			for(int i=0; i<produit.length; i++) {
				produit[i] = (1-alpha) * produit[i] + (alpha/adjacencyArray.size());
				sommeDesEltDuTabProduit += produit[i];
			}
			
			
			//Normalisation
			for(int i=0; i<produit.length; i++) {
				produit[i] += (1-sommeDesEltDuTabProduit) / adjacencyArray.size();
				A[i] = produit[i];
			}
						
			
		}
		
		
		return produit;
		
	}
	
	
	public static String getCountry(int index) {
		String path2 = "/Vrac/CPA-PageRank/alr21--pageNum2Name--enwiki-20071018.txt";
		String res = "Not found";
		BufferedReader lecteurAvecBuffer = null;
	    String ligne = "";
	    try{
	    	lecteurAvecBuffer = new BufferedReader(new FileReader(path2));
	    	while((ligne = lecteurAvecBuffer.readLine()) != null) {
				String [] aretes = new String[2];
				aretes = ligne.split("\\s+", 2);
				String s = aretes[0];
				try {
					if(Integer.parseInt(s) == index) {
						res = aretes[1];
						break;
					}
				}catch (NumberFormatException e) {
					
				}
				
			}
			lecteurAvecBuffer.close();
			return res;
		}
	    
	    catch(IOException exc){
	    	System.out.println("Erreur d'ouverture");
	    }
		return res;
	}
	
	
	
	public static void main(String [] args) {
		
		readFile(path);
		
		ArrayList<Float> res = new ArrayList<>();
		//ArrayList<Float> res1 = new ArrayList<>();
		
		float[] p = powerIteration((float) 0.15, 4);
		//float[] p1 = powerIteration((float) 0.9, 15);
		
		for(int i=0; i<p.length; i++) {
			//System.out.println("p[i] : "+p[i]);
			res.add(p[i]);
			//res1.add(p1[i]);
		}
		
		
		
		ArrayList<Float> clone = (ArrayList<Float>) res.clone();
		Collections.sort(clone);
		

		for (int i = 1; i < 6; i++) {
			Float highest = clone.get(clone.size() - i);
			int inde = res.indexOf(highest);
			System.out.println(getCountry(indexTableList.get(inde)));
		}
		
		
		System.out.println("------------------------------------");
		
		
		for (int i = 0; i < 5; i++) {
			Float lowest = clone.get(i);
			int inde = res.indexOf(lowest);
			res.remove(inde);
			System.out.println(getCountry(indexTableList.get(inde)));
		}
		
		

		
		

		
//		try {
//			BufferedWriter bf1 = new BufferedWriter(new FileWriter("X1.txt"));
////			BufferedWriter bf2 = new BufferedWriter(new FileWriter("X2.txt"));
////			BufferedWriter bf3 = new BufferedWriter(new FileWriter("X3.txt"));
////			BufferedWriter bf4 = new BufferedWriter(new FileWriter("X4.txt"));
////			BufferedWriter bf5 = new BufferedWriter(new FileWriter("X5.txt"));
//			BufferedWriter bf6 = new BufferedWriter(new FileWriter("X6.txt"));
//			for(int i=0; i<adjacencyArray.size();i++) {
//				float PG = res.get(map.get(adjacencyArray.get(i).s));
//				float PG1 = res1.get(map.get(adjacencyArray.get(i).s));
//				bf1.write(adjacencyArray.get(i).neighbor.size()+"    "+PG+"\n");
//				
////				if(mapDegSortant.get(adjacencyArray.get(i).s) == null) {
////					bf2.write("0"+ "   " + +PG+"\n");
////				}else {
////					bf2.write(mapDegSortant.get(adjacencyArray.get(i).s)+"    "+PG+"\n");
////				}
//				bf6.write(PG + "   "+ PG1+"\n");
//			}
//			
//			
//			
//			bf1.close();
////			bf2.close();
////			bf3.close();
////			bf4.close();
////			bf5.close();
//			bf6.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
	}

}
