package rs.ac.bg.etf.drs.stream;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
		
		long start = System.currentTimeMillis();
		
		Stream<String> titleRatingsStream = Files.lines(Path.of(filepathTitleRatings)).parallel();
		Map<String, Double> titleRatings = titleRatingsStream
				.skip(1)
				.map(s -> new TitleRatings(s))
				.collect(Collectors.toMap(TitleRatings::getTitle, TitleRatings::getRating));
		
		System.out.println("Done processing titleRatings");

		Stream<String> titleCrewStream = Files.lines(Path.of(filepathTitleCrew)).parallel();
		Map<String, Double> directorsRating = titleCrewStream
				.skip(1)
				.map(s -> new TitleCrew(s))
				.flatMap(s -> s.getFilmDirectors().stream())
				.map(s -> {
					String tconst = s.split("-")[0];
					String director = s.split("-")[1];
					if (titleRatings.get(tconst) != null) {
						Double rating = titleRatings.get(tconst);
						String directorRating = director + "-"  + rating;
						return directorRating;
					} else {
						return director + "-" + "null";
					}
				})
				.filter(s -> !s.substring(s.length() - 4).equals("null"))
				.collect(Collectors.groupingByConcurrent(s -> s.split("-")[0], Collectors.averagingDouble(s -> Double.parseDouble(s.split("-")[1]))));
		
		System.out.println("Done processing titleCrew");	
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println("Vreme izvrsavanja je: " + duration);

		try {
			System.out.println("Writing to file...");
			
			BufferedWriter bf = new BufferedWriter(new FileWriter(outputFileName));

	        for(Entry<String, Double> entry : directorsRating.entrySet()) {
	        	bf.write(entry.getKey() + ": " + entry.getValue());
	            bf.newLine();
	        }	            
	        bf.flush();	 
	        bf.close();
	        
	        System.out.println("Done writing to file!");	
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 			

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

		Double getRating() {
			return rating;
		}

		String getTitle() {
			return this.tconst;
		}

	}

}
