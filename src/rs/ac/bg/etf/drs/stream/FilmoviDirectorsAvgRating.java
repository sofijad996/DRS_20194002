package rs.ac.bg.etf.drs.stream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FilmoviDirectorsAvgRating {

	public static void main(String[] args) throws IOException {
		String filepathTitleCrew = "title_crew.tsv";
		String filepathTitleRatings = "title_ratings.tsv";
		String outputFileName = "outputStream.tsv";

		Stream<String> titleCrewStream = Files.lines(Path.of(filepathTitleCrew)).parallel();
		List<DirectorRating> titleDirectors = titleCrewStream
				.skip(1)
				.map(s -> new TitleCrew(s))
				.flatMap(s -> s.getFilmDirectors().stream())
				.map(s -> new DirectorRating(s))
				.collect(Collectors.toList());
		System.out.println("Done processing titleCrew");

		Stream<String> titleRatingsStream = Files.lines(Path.of(filepathTitleRatings)).parallel();
		titleRatingsStream
				.skip(1)
				.map(s -> new TitleRatings(s))
				//.filter(s -> titleDirectors.stream().anyMatch(td -> td.tconst.equals(s.tconst)))
				.forEach(s -> {
					titleDirectors.stream()
					.forEach(td -> {
						if (td.getTitle().equals(s.getTitle())) {
							td.setRating(s.getRating());
						}
					});
				});
		
		System.out.println("Done processing titleRatings");
			
		Map<String, Double> directorsRating = titleDirectors.stream()
				.collect(Collectors.groupingByConcurrent(DirectorRating::getDirectorName, Collectors.averagingDouble(DirectorRating::getRating)));

		System.out.println("Writing to file...");
		
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(outputFileName));

	        for(Entry<String, Double> entry : directorsRating.entrySet()) {
	        	bf.write(entry.getKey() + ": " + entry.getValue());
	            bf.newLine();
	        }	            
	        bf.flush();	 
	        bf.close();
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
		
		System.out.println("Done writing to file!");		

	}

	public static class TitleCrew {
		public String tconst;
		String[] directors;

		public TitleCrew(String line) {
			String[] elements = line.split("\t");
			tconst = elements[0];
			if (elements[1].equals("\\N")) {
				directors = new String[0];
			} else {
				directors = elements[1].split(",");
			}
		}

		public List<String> getFilmDirectors() {
			List<String> result = new LinkedList<>();
			for (String director : directors) {
				String filmDirector = tconst + "-" + director;
				result.add(filmDirector);
			}
			return result;
		}
	}

	public static class TitleRatings {
		public String tconst;
		public Double rating;

		public TitleRatings(String line) {
			String[] elements = line.split("\t");
			this.tconst = elements[0];
			this.rating = Double.parseDouble(elements[1]);
		}

		List<String> getFilmRatings() {
			List<String> result = new LinkedList<>();
			String filmRating = tconst + "-" + rating;
			result.add(filmRating);
			return result;
		}

		Double getRating() {
			return rating;
		}

		String getTitle() {
			return this.tconst;
		}

	}

	public static class DirectorRating {
		public String tconst;
		public Double rating;
		public String director;

		public DirectorRating(String line) {
			String[] elements = line.split("-");
			this.tconst = elements[0];
			this.director = elements[1];
			this.rating = 5.0;
		}

		String getDirectorName() {
			return director;
		}

		String getTitle() {
			return this.tconst;
		}

		Double getRating() {
			return rating;
		}

		void setRating(double rating) {
			this.rating = rating;
		}

		@Override
		public String toString() {
			return tconst + " " + director + " " + rating;
		}
	}
}
