package rs.ac.bg.etf.drs.rmi;

import rs.ac.bg.etf.drs.conc.Buffer;
import rs.ac.bg.etf.drs.conc.Printer;
import rs.ac.bg.etf.drs.conc.Producer;

public class MainProducerPrinter {

	public static void main(String[] args) {
		final String titleCrewFile = "title_crew.tsv";
		final String titleRatingsFile = "title_ratings.tsv";
		
		Buffer<String> buffer1 = new BufferRMI<String>("localhost", 4003, 1);
		Buffer<String> buffer2 = new BufferRMI<String>("localhost", 4003, 2);
		Buffer<String> buffer6 = new BufferRMI<String>("localhost", 4003, 6);
		
		long start = System.currentTimeMillis();
		
		Producer producerTitleCrew = new Producer(titleCrewFile, buffer1);
		producerTitleCrew.start();
		Producer producerTitleRatings = new Producer(titleRatingsFile, buffer2);
		producerTitleRatings.start();
		
		Printer print = new Printer(buffer6);
		print.start();
		
		try {
			print.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		long duration = end - start;
		
		System.out.println("Vreme izvrsavanja je: " + duration);
	}

}
