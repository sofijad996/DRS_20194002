package rs.ac.bg.etf.drs.rmi;

import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

public class RemoteBarrierImpl implements RemoteBarrier {

	int n;
	int cnt;
	Semaphore doorIn, doorOut;

	public RemoteBarrierImpl() {
	}
	
	public RemoteBarrierImpl(int n) {
		this.n = n;
		this.cnt = 0;
		doorIn = new Semaphore(1);
		doorOut = new Semaphore(0);
	}
	
	public void sync() throws RemoteException {
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
