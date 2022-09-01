package rs.ac.bg.etf.drs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteBarrier extends Remote {
	
	public void sync() throws RemoteException;
}
