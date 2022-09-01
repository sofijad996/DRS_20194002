package rs.ac.bg.etf.drs.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rs.ac.bg.etf.drs.conc.Barrier;

public class BarrierRMI extends Barrier {

	RemoteBarrier stub;

	public BarrierRMI(String host, int port) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			Registry registry = LocateRegistry.getRegistry(host, port);

			stub = (RemoteBarrier) registry.lookup("/barrier");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sync() {
		try {
			stub.sync();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
