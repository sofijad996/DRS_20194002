package rs.ac.bg.etf.drs.conc;

public class Main {

	public static void main(String[] args) {
		final int C = 5;
		final String titleCrewFile = "title_crew.tsv";
		final String titleRatingsFile = "title_ratings.tsv";

		Buffer<String> buffer1 = new Buffer<String>();
		Buffer<String> buffer2 = new Buffer<String>();
		Buffer<String> buffer3 = new Buffer<String>();
		Buffer<String> buffer4 = new Buffer<String>();
		Buffer<String> buffer5 = new Buffer<String>();
		Buffer<String> buffer6 = new Buffer<String>();

		Barrier barrier1 = new Barrier(C);
		Barrier barrier2 = new Barrier(C);
		Barrier barrier3 = new Barrier(C);

		long start = System.currentTimeMillis();

		Producer producerTitleCrew = new Producer(titleCrewFile, buffer1);
		producerTitleCrew.start();
		Producer producerTitleRatings = new Producer(titleRatingsFile, buffer2);
		producerTitleRatings.start();

		for (int i = 1; i <= C; i++) {
			ConsumerTitleCrew consumerTitleCrew = new ConsumerTitleCrew(buffer1, barrier1, buffer3);
			consumerTitleCrew.start();
		}

		for (int i = 1; i <= C; i++) {
			ConsumerTitleRatings consumerTitleRatings = new ConsumerTitleRatings(buffer2, barrier2, buffer4);
			consumerTitleRatings.start();
		}

		for (int i = 1; i <= C; i++) {
			Consumer c = new Consumer(buffer3, buffer4, barrier3, buffer5);
			c.start();
		}

		Combiner combiner = new Combiner(buffer5, buffer6);
		combiner.start();

		Printer printer = new Printer(buffer6);
		printer.start();

		try {
			printer.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		long duration = end - start;

		System.out.println("Vreme izvrsavanja je: " + duration);

	}

}
