package rs.ac.bg.etf.drs.conc;

public class Printer extends Thread {

	Buffer<String> buffer;

	public Printer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		String line = null;
		while ((line = buffer.get()) != null) {
			System.out.println(line);
		}
	}

}
