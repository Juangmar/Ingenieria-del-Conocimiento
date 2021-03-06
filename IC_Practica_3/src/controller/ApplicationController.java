package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import business.Kmean;
import integration.DataManager;

/**
 * @author Juan Gomez-Martinho
 * License MIT, 2018
 */
public class ApplicationController {

	
	public HashMap<Double[], String> loadData(File e){
		DataManager manager = new DataManager();
		HashMap<Double[], String> result = manager.loadData(e);
		return result;
	}
	
	public HashMap<Double[], String> loadManyData(ArrayList<File> trainingCases) {
		HashMap<Double[], String> result = new HashMap<Double[], String>();
		for(File f:trainingCases) {
			HashMap<Double[], String> cases = loadData(f);
			result.putAll(cases);
		}
		return result;
	}
	
	public Kmean kmeans(File f, double tolerance, double weigth) {
		HashMap<Double[], String> data = loadData(f);
		if(data != null) {
			Kmean manager = new Kmean(data);
			boolean success = manager.execute(tolerance, weigth);
			if(success) {
				return manager;
			}
			else return null;
		}
		else return null;
	}
	
	
}
