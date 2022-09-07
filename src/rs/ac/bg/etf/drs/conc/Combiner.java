package rs.ac.bg.etf.drs.conc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Combiner extends Thread {

	Buffer<String> bufferIn, bufferOut;
	Map<String, Double> directorRating;

	public Combiner(Buffer<String> bufferIn, Buffer<String> bufferOut) {
		setName("Combiner");
		this.bufferIn = bufferIn;
		this.bufferOut = bufferOut;
		this.directorRating = new HashMap<String, Double>();
	}

	@Override
	public void run() {
		String line = null;
		while ((line=bufferIn.get()) != null) {
			String[] data = line.split("-");
			String director = data[0];
			double rating = Double.parseDouble(data[1]);

			if (!directorRating.containsKey(director)) {
				directorRating.put(director, rating);
			} else {
				double averageRating = (directorRating.get(director) + rating) / 2;
				directorRating.put(director, averageRating);
			}
		}

		for (Entry<String, Double> entry : directorRating.entrySet()) {
			line = entry.getKey() + "-" + entry.getValue();
			bufferOut.put(line);
		}

		System.out.println("Combiner done");
		
		bufferOut.put(null);
	}
}
