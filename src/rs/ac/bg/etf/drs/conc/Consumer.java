package rs.ac.bg.etf.drs.conc;

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
			Double rating = Double.parseDouble(data[1]);

			for (Film f : films) {
				if (nconst == f.nconst) {
					f.setRating(rating);
				}

				if (!directorRating.containsKey(f.director)) {
					directorRating.put(f.director, f.rating);
				} else {
					double averageRating = (directorRating.get(f.director) + f.rating) / 2;
					directorRating.put(f.director, averageRating);
				}
			}

		}

		barrier.sync();

		for (Entry<String, Double> entry : directorRating.entrySet()) {
			line = entry.getKey() + "-" + entry.getValue();
			bufferOut.put(line);
		}

		barrier.sync();

		bufferOut.put(null);
	}

	public class Film {
		String nconst;
		String director;
		Double rating;

		public Film(String film) {
			String[] data = film.split("-");
			String nconst = data[0];
			this.nconst = nconst;
			String director = data[1];
			this.director = director;
			this.rating = 0.0;
		}

		public Film() {
		}

		public String getTitle() {
			return nconst;
		}

		public void setRating(Double rating) {
			this.rating = rating;
		}
	}

}
