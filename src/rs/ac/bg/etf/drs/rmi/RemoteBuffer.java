package rs.ac.bg.etf.drs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteBuffer<T> extends Remote {
	
	public void put(T data) throws RemoteException;
	
	public T get() throws RemoteException;

}
