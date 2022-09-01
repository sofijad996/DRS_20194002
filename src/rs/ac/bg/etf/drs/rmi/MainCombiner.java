package rs.ac.bg.etf.drs.rmi;

import rs.ac.bg.etf.drs.conc.Combiner;
import rs.ac.bg.etf.drs.conc.Buffer;

public class MainCombiner {

	public static void main(String[] args) {
		Buffer<String> buffer5 = new BufferRMI<>("localhost", 4003, 5);
		Buffer<String> buffer6 = new BufferRMI<>("localhost", 4003, 6);
		
		Combiner combiner = new Combiner(buffer5, buffer6);
		combiner.start();

	}

}
