package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Kmean {

	private HashMap<Double[], String> trainingData;
	private double tolerance, weigth;
	private List<Double[]> means;
	private int n;
	private int c;
	private Double[][] probabilities;
	
	public Kmean(HashMap<Double[], String> data) {
		this.trainingData = data;
		tolerance = 0;
		weigth = 0;
	}
	
	public Kmean() {
		// TODO Auto-generated constructor stub
	}

	public List<Double[]> getMeans() {
		return means;
	}
	
	public Double[][] getProbabilities() {
		return probabilities;
	}

	public boolean execute(double tol, double w) {
		this.tolerance = tol;
		this.weigth = w;
		boolean success = false;
		
		n = trainingData.size();
		c = nClass();
		
		means = initializeCenters();
		
		probabilities = computeProbablties();
		Double change = Double.MAX_VALUE;
		while(change > tolerance) {
			List<Double[]> newMeans = redoCenters(means, probabilities);
			probabilities = computeProbablties();
			change = computeBiggestChange(means, newMeans);
			means = newMeans;
		}
		
		return success;
	}
	
	private Double computeBiggestChange(List<Double[]> means2, List<Double[]> newMeans) {
		double change = 0.0;

		for (int i = 0; i < means2.size(); i++) {
			
			double distance = dist(means2.get(i), newMeans.get(i));
			if(distance > change) change = distance;
		}
		
		return change;
	}
		

	private List<Double[]> redoCenters(List<Double[]> originalMeans, Double[][] probabilities2) {
		
		double num = 0.0;
		double den = 0.0;
		List<Double[]> result = new ArrayList<Double[]>();
		//here the formula:
		//vi = ( sum[j=1...n](probabilidad(ci/xj))^b * xj ) / (sum[j=1...n](probabilidad(ci/xj))^b)
		
		for(int i = 0; i < c; i++) {
			Double[] value = new Double[originalMeans.get(0).length];
			
			for (int y  = 0; y < value.length; y++) {
				double position = 0.0;
				Iterator<Entry<Double[], String>> iterator = trainingData.entrySet().iterator();
				int j = 0;
				while(iterator.hasNext()) {
					Entry<Double[], String> current = iterator.next();
					position = Math.pow(probabilities2[i][j], weigth) * current.getKey()[y]; //X[y][j];
					j++;
				}
					
			}
		}
					
		return null;
	}

	public void setMeans(List<Double[]> e) {
		this.means = e;
	}

	private int nClass() {
		int p = 0;
		List<String> a = new ArrayList<String>();
		if(trainingData!=null) {
			trainingData.forEach((k,v)->{
				if(!a.contains(v)) a.add(v);
			});
			p = a.size();
		}
		
		return p;
	}
	
	private List<Double[]> initializeCenters() {
		List<Double[]> v = new ArrayList<Double[]>();
		Random r = new Random();
		while(v.size() < c) {
			int ind = r.nextInt(n-1);
			
			Iterator<Entry<Double[], String>> a = trainingData.entrySet().iterator();
			int j = 0;
			while(a.hasNext() && j<=ind) {
				
				if(j==ind) {
					Map.Entry<Double[], String> pair = (Map.Entry<Double[], String>) a.next();
					v.add(pair.getKey());
				} else {
					j++;
				}
			}

		}
		return v;
	}
	
	private Double[][] computeProbablties(){
		Double[][] result = new Double[c][n];
		Iterator<Entry<Double[], String>> iterator = trainingData.entrySet().iterator();
		int count = 0;
		while(iterator.hasNext()) {
			Entry<Double[], String> current = iterator.next();
			for (int i = 0; i < c; i++){
				result[i][count] = probablties(means.get(i), current.getKey());
			}
			count++;
		}
		
		return result;
	}

	private double probablties(Double[] center, Double[] value) {
		double exp = 1 / (weigth-1);
		double numer = Math.pow( (1/dist(value, center)) , exp);
		double denom = 0.0;
		for(int i = 0; i < c; i++) {
			denom += (1/dist(value, means.get(i)));
		}
		denom = Math.pow(denom, exp);
		
		return  numer/denom;
	}
	
	private double dist(Double[] i, Double[] j) {
		
		if(i.length!=j.length) throw new IllegalArgumentException();
		else {
			 double sum = 0.0;
		        for(int x=0;x<i.length;x++) {
		           sum = sum + Math.pow( (i[x]-j[x]) , 2.0);
		        }
		        return Math.sqrt(sum);
		}
	}

}
