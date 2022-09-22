package rs.ac.bg.etf.drs.conc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Consumer extends Thread {

	Buffer<String> bufferTitleCrew, bufferTitleRatings;
	Buffer<String> bufferOut;
	Barrier barrier;
	Map<String, Double> directorRatings;
	Map<String, Double> titleRatings;
	Map<String, String> directorRatingSums;

	public Consumer(Buffer bufferTitleCrew, Buffer bufferTitleRatings, Barrier barrier, Buffer bufferOut) {
		this.bufferTitleCrew = bufferTitleCrew;
		this.bufferTitleRatings = bufferTitleRatings;
		this.barrier = barrier;
		this.bufferOut = bufferOut;
		this.directorRatings = new HashMap<String, Double>();
		this.titleRatings = new HashMap<String, Double>(); 
		this.directorRatingSums = new HashMap<String, String>();
	}

	@Override
	public void run() {
		String line = null;
		
		while (true) {
			String lineTitleRatings = bufferTitleRatings.get();
			if (lineTitleRatings == null) {
				break;
			}
			String[] data = lineTitleRatings.split("-");
			String nconst = data[0];
			double rating = Double.parseDouble(data[1]);
			
			titleRatings.put(nconst, rating);
		}
			
		while (true) {
			String lineTitleCrew = bufferTitleCrew.get();
			if (lineTitleCrew == null) {
				break;
			}
			String[] data = lineTitleCrew.split("-");
			String nconst = data[0];
			String director = data[1];	
			
			if (titleRatings.get(nconst) != null) { 
				directorRatings.put(director + "-" + nconst, titleRatings.get(nconst));
			}
			
		}
		
		for (Entry<String, Double> directorRating : directorRatings.entrySet()) {	
			String currDirector = directorRating.getKey().split("-")[0];
			double currRating = directorRating.getValue();					
			
			if (!directorRatingSums.containsKey(currDirector)) {
				directorRatingSums.put(currDirector, currRating + "-" + 1);				
				
			} else {
				String[] value = directorRatingSums.get(currDirector).split("-");
				double sum = Double.parseDouble(value[0]) + currRating;
				int cnt = Integer.parseInt(value[1]) + 1;
				
				directorRatingSums.replace(currDirector, sum + "-" + cnt);
			}			
		}		
		

		barrier.sync();

		for (Entry<String, String> entry : directorRatingSums.entrySet()) {
			line = entry.getKey() + "-" + entry.getValue();
			bufferOut.put(line);
		}

		barrier.sync();
		
		System.out.println("Consumer done");

		bufferOut.put(null);
	}
}
