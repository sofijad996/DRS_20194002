package rs.ac.bg.etf.drs.conc;

import java.util.ArrayList;
import java.util.List;

public class ConsumerTitleCrew extends Thread {

	Buffer<String> bufferIn, bufferOut;
	Barrier barrier;
	List<String> titleDirectors;

	public ConsumerTitleCrew(Buffer bufferIn, Barrier barrier, Buffer bufferOut) {
		this.bufferIn = bufferIn;
		this.barrier = barrier;
		this.bufferOut = bufferOut;
		this.titleDirectors = new ArrayList<>();
	}

	@Override
	public void run() {
		String line = null;
		while ((line = bufferIn.get()) != null) {
			String[] titleCrew = parseLine(line);
			for (String tc : titleCrew) {
				titleDirectors.add(tc);
			}

		}
		
		barrier.sync();

		for (String titleDirector : titleDirectors) {
			bufferOut.put(titleDirector);
		}

		barrier.sync();

		bufferOut.put(null);
		
		System.out.println("Consumer TitleCrew done");
	

	}

	private String[] parseLine(String line) {
		String[] data = line.split("\t");
		String nconst = data[0];
		String[] directors = new String[0];
		
		if (!("\\N".equals(data[1]))) {
			directors = data[1].split(",");
		}

		String result[] = new String[directors.length];
		int i = 0;
		for (String director : directors) {
			result[i] = nconst + "-" + director;
			i++;
		}
		return result;
	}

}
