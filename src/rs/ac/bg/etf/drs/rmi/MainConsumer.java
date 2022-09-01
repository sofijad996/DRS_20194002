package rs.ac.bg.etf.drs.rmi;

import rs.ac.bg.etf.drs.conc.Barrier;
import rs.ac.bg.etf.drs.conc.Buffer;
import rs.ac.bg.etf.drs.conc.Consumer;

public class MainConsumer {

	public static void main(String[] args) {
		final int C = 5;

		Barrier barrier3 = new BarrierRMI("localhost", 4003, 3);

		Buffer<String> buffer3 = new BufferRMI<>("localhost", 4003, 3);
		Buffer<String> buffer4 = new BufferRMI<>("localhost", 4003, 4);
		Buffer<String> buffer5 = new BufferRMI<>("localhost", 4003, 5);

		for (int i = 1; i <= C; i++) {
			Consumer c = new Consumer(buffer3, buffer4, barrier3, buffer5);
			c.start();
		}

	}

}
