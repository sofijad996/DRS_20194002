package rs.ac.bg.etf.drs.conc;

import java.util.concurrent.Semaphore;

public class Barrier {

	int n;
	int cnt;
	Semaphore doorIn, doorOut;

	public Barrier() {
	}

	public Barrier(int n) {
		this.n = n;
		this.cnt = 0;
		doorIn = new Semaphore(1);
		doorOut = new Semaphore(0);
	}

	public void sync() {
		doorIn.acquireUninterruptibly();
		cnt++;
		if (cnt == n) {
			doorOut.release();
		} else {
			doorIn.release();
		}

		doorOut.acquireUninterruptibly();
		cnt--;
		if (cnt == 0) {
			doorIn.release();
		} else {
			doorOut.release();
		}

	}
}
