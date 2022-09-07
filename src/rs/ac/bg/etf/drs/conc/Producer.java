package rs.ac.bg.etf.drs.conc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Producer extends Thread {

	String fileName;
	Buffer<String> buffer;

	public Producer(String fileName, Buffer<String> buffer) {
		setName("Producer");
		this.fileName = fileName;
		this.buffer = buffer;
	}

	@Override
	public void run() {
		File file = new File(fileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				buffer.put(line);
			}
			buffer.put(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Producer done");

	}

}
