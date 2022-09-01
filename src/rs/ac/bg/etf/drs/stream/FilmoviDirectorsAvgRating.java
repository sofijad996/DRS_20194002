package rs.ac.bg.etf.drs.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilmoviDirectorsAvgRating {

	public static void main(String[] args) throws IOException {
		String filepathTitleCrew = "title_crew.tsv";
		String filepathTitleRatings = "title_ratings.tsv";
		
		List<String> titleCrew = new LinkedList<String>();
		List<String> titleRatings = new LinkedList<String>();
		Map<String, Double> directorsRating = new HashMap<String, Double>();
		
		try (Stream<String> titleCrewStream = Files.lines(Path.of(filepathTitleCrew)).parallel()) {
			titleCrew =  titleCrewStream
					.skip(1)
					.map(s -> new TitleCrew(s))
					.flatMap(s -> s.getFilmDirectors().stream())
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try (Stream<String> titleRatingsStream = Files.lines(Path.of(filepathTitleRatings)).parallel()) {
			titleRatings = titleRatingsStream
					.skip(1)
					.map(s -> new TitleRatings(s))
					.flatMap(s -> s.getFilmRatings().stream())
					.collect(Collectors.toList());
			
		//	for (String item : titleRatings) System.out.println(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		directorsRating = titleRatings
				.parallelStream()
				.forEach(tr -> {
					titleCrew.stream()
				     .filter(tc -> getFilmName(tc).equals(getFilmName(tr)))
				     .map(tr -> tr + "-" + getDirectorName(tc))
				     .get();
				})				
				.map(tr -> new DirectorRating(tr))
				.collect(Collectors.groupingByConcurrent(DirectorRating::getDirectorName, Collectors.averagingDouble(DirectorRating::getRating)));
		
	}
	
	
	static String getFilmName(String line) {
		String result = line.substring(0, 9);
		return result;
	}
	
	static String getDirectorName(String line) {
		String result = line.substring(9);
		return result;
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
			tconst = elements[0];
			rating = Double.parseDouble(elements[1]);
		}	
		
		List<String> getFilmRatings() {
			List<String> result = new LinkedList<>();
			String filmRating = tconst + "-" + rating;
			result.add(filmRating);
			return result;
		}
	}
	
	public static class DirectorRating {
		public String tconst;
		public Double rating;
		public String director;
		
		public DirectorRating(String line) {
			String[] elements = line.split("-");
			tconst = elements[0];
			rating = Double.parseDouble(elements[1]);
			director = elements[2];
		}
		
		 String getDirectorName() {
			return director;
		}
		
		 Double getRating() {
			 return rating;
		 }
	}
}
