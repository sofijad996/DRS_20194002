package rs.ac.bg.etf.drs.conc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Consumer extends Thread {

	Buffer<String> bufferTitleCrew, bufferTitleRatings;
	Buffer<String> bufferOut;
	Barrier barrier;
	List<Film> films;
	Map<String, Double> directorRating;

	public Consumer(Buffer bufferTitleCrew, Buffer bufferTitleRatings, Barrier barrier, Buffer bufferOut) {
		this.bufferTitleCrew = bufferTitleCrew;
		this.bufferTitleRatings = bufferTitleRatings;
		this.barrier = barrier;
		this.bufferOut = bufferOut;
		this.films = new ArrayList<>();
		this.directorRating = new HashMap<String, Double>();
	}

	@Override
	public void run() {
		String line = null;
		while (true) {
			String lineTitleCrew = bufferTitleCrew.get();
			if (lineTitleCrew == null) {
				break;
			}
			Film film = new Film(lineTitleCrew);
			films.add(film);
		}

		while (true) {
			String lineTitleRatings = bufferTitleRatings.get();
			if (lineTitleRatings == null) {
				break;
			}
			String[] data = lineTitleRatings.split("-");
			String nconst = data[0];
			BigDecimal rating = (new BigDecimal(Double.parseDouble(data[1]))).setScale(2, RoundingMode.HALF_UP);

			for (Film f : films) {				
				if (nconst.equals(f.getTitle())) {
					if (!directorRating.containsKey(f.getDirector())) {
						directorRating.put(f.getDirector(), rating.doubleValue());
					} else {
						double currentAverage = directorRating.get(f.getDirector());
						double averageRatingDouble = (currentAverage + rating.doubleValue()) / 2;
						BigDecimal averageRating = (new BigDecimal(averageRatingDouble).setScale(2, RoundingMode.HALF_UP));
						directorRating.put(f.getDirector(), averageRating.doubleValue());
					}
				}
				
			}

		}

		barrier.sync();

		for (Entry<String, Double> entry : directorRating.entrySet()) {
			line = entry.getKey() + "-" + entry.getValue();
			bufferOut.put(line);
		}

		barrier.sync();
		
		System.out.println("Consumer done");

		bufferOut.put(null);
	}

	public class Film {
		String nconst;
		String director;

		public Film(String film) {
			String[] data = film.split("-");
			String nconst = data[0];
			this.nconst = nconst;
			String director = data[1];
			this.director = director;
		}

		public Film() {
		}

		public String getTitle() {
			return nconst;
		}
		
		public String getDirector() {
			return director;
		}
	}

}
