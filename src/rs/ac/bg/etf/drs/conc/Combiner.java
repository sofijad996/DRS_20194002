package rs.ac.bg.etf.drs.conc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Combiner extends Thread {

	Buffer<String> bufferIn, bufferOut;
	Map<String, String> directorRatingSums;
	List<String> directorRatings;

	public Combiner(Buffer<String> bufferIn, Buffer<String> bufferOut) {
		setName("Combiner");
		this.bufferIn = bufferIn;
		this.bufferOut = bufferOut;
		this.directorRatingSums = new HashMap<String, String>();
		this.directorRatings = new ArrayList<String>();
	}

	@Override
	public void run() {
		String line = null;
		while ((line = bufferIn.get()) != null) {
			String[] data = line.split("-");
			String director = data[0];
			double sum = Double.parseDouble(data[1]);
			int cnt = Integer.parseInt(data[2]);

			if (!directorRatingSums.containsKey(director)) {
				directorRatingSums.put(director, sum + "-" + cnt);				
				
			} else {
				String[] value = directorRatingSums.get(director).split("-");
				double currSum = Double.parseDouble(value[0]) + sum;
				int currCnt = Integer.parseInt(value[1]) + cnt;
				
				directorRatingSums.replace(director, currSum + "-" + currCnt);
			}	
		}
		
		for (Entry<String, String> entry : directorRatingSums.entrySet()) {
			String director = entry.getKey();
			String value[] = entry.getValue().split("-");
			double rating = Double.parseDouble(value[0]);
			int count =  Integer.parseInt(value[1]);
			
			double avgRating = rating / count;
			directorRatings.add(director + "-" + avgRating);
		}


		for (String directorRating : directorRatings) {
			bufferOut.put(directorRating);
		}

		System.out.println("Combiner done");
		
		bufferOut.put(null);
	}
}
