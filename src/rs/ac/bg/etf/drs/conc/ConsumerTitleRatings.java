package rs.ac.bg.etf.drs.conc;

import java.util.ArrayList;
import java.util.List;

public class ConsumerTitleRatings extends Thread {

	Buffer<String> bufferIn, bufferOut;
	Barrier barrier;
	List<String> titleRatings;

	public ConsumerTitleRatings(Buffer bufferIn, Barrier barrier, Buffer bufferOut) {
		this.bufferIn = bufferIn;
		this.barrier = barrier;
		this.bufferOut = bufferOut;
		this.titleRatings = new ArrayList<>();
	}

	@Override
	public void run() {
		String line = null;
		while ((line = bufferIn.get()) != null) {
			String tr = parseLine(line);
			titleRatings.add(tr);
		}
		barrier.sync();

		for (String titleRating : titleRatings) {
			bufferOut.put(titleRating);
		}

		barrier.sync();
		

		System.out.println("Consumer TitleRatings done");

		bufferOut.put(null);
		

	}

	private String parseLine(String line) {
		String[] data = line.split("\t");
		String nconst = data[0];
		String rating = data[1];

		String result = nconst + "-" + rating;
		return result;
	}

}
