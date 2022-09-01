package rs.ac.bg.etf.drs.spark;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.api.java.function.PairFunction;

import rs.ac.bg.etf.drs.stream.FilmoviDirectorsAvgRating.DirectorRating;
import rs.ac.bg.etf.drs.stream.FilmoviDirectorsAvgRating.TitleCrew;
import rs.ac.bg.etf.drs.stream.FilmoviDirectorsAvgRating.TitleRatings;
import scala.Tuple2;

public class FilmoviDirectorsAvgRatingSpark {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("DRS").setMaster("local");
		JavaSparkContext context = new JavaSparkContext(conf);

		JavaRDD<String> fileTitleCrew = context.textFile("title_crew.tsv");
		JavaRDD<String> filetitleRatings = context.textFile("title_ratings.tsv");

		JavaPairRDD<String, String> titleDirectors = fileTitleCrew
				.map(tc -> new TitleCrew(tc))
				.map(tc -> tc.getFilmDirectors())
				.flatMap(tc -> tc.iterator())
				.mapToPair(tc -> new Tuple2<String, String>(tc.split("-")[0], tc.split("-")[1]));

		JavaPairRDD<String, Double> titleRatings = filetitleRatings
				.map(tr -> new TitleRatings(tr))
				.mapToPair(tr -> new Tuple2<String, Double>(tr.tconst, tr.rating));
		
		JavaPairRDD<String, Tuple2<Double, String>> titleDirectorRatings = titleRatings
				.join(titleDirectors);
		
		JavaRDD<DirectorRating> titleDirectorRatingsObj = titleDirectorRatings
				.map(dr -> dr._1() + "-" + dr._2()._2() + "-" + dr._2()._1())
				.map(dr -> new DirectorRating(dr));
		
		JavaPairRDD<String, Double> directorRatings = titleDirectorRatingsObj
				.map(dr -> new Tuple2<String, Double>(dr.director, dr.rating))
				.mapValues(rating -> new Tuple2<Double, Integer>(rating, 1))
				.reduceByKey((dr1, dr2) ->  new Tuple2<Double, Integer>(dr1._1 + dr2._1, dr1._2 + dr2._2))
				.mapToPair(dr -> {
				     int rating = dr._2._1;
				     int cnt = dr._2._2;
				     Tuple2<String, Double> averageDirectorRating = new Tuple2<String, Double>(dr._1, rating / cnt);
				     return averageDirectorRating;
				})
				.collect()
				.forEach(System.out::println);		
	}	
}
