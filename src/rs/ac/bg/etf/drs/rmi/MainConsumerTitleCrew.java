package rs.ac.bg.etf.drs.rmi;

import rs.ac.bg.etf.drs.conc.Barrier;
import rs.ac.bg.etf.drs.conc.Buffer;
import rs.ac.bg.etf.drs.conc.ConsumerTitleCrew;

public class MainConsumerTitleCrew {
	
	public static void main(String[] args) {
		final int C = 5;	
		
		Barrier barrier1 = new BarrierRMI("localhost", 4003, 1);
		
		Buffer<String> buffer1 = new BufferRMI<>("localhost", 4003, 1);
		Buffer<String> buffer3 = new BufferRMI<>("localhost", 4003, 3);	
		
		for (int i = 1; i <= C; i++) {
			ConsumerTitleCrew consumerTitleCrew = new ConsumerTitleCrew(buffer1, barrier1, buffer3);
			consumerTitleCrew.start();
		}
	}
	

}
