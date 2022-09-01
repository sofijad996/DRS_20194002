package rs.ac.bg.etf.drs.rmi;

import rs.ac.bg.etf.drs.conc.Barrier;
import rs.ac.bg.etf.drs.conc.Buffer;
import rs.ac.bg.etf.drs.conc.ConsumerTitleRatings;

public class MainConsumerTitleRatings {
	
	public static void main(String[] args) {
		final int C = 5;	
		
		Barrier barrier = new BarrierRMI("localhost", 4003);
		
		Buffer<String> buffer2 = new BufferRMI<>("localhost", 4003, 2);
		Buffer<String> buffer4 = new BufferRMI<>("localhost", 4003, 4);	
		
		for (int i = 1; i <= C; i++) {
			ConsumerTitleRatings consumerTitleRatings = new ConsumerTitleRatings(buffer2, barrier, buffer4);
			consumerTitleRatings.start();
		}
	}

}
