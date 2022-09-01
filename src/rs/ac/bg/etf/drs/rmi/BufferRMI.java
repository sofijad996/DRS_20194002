package rs.ac.bg.etf.drs.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rs.ac.bg.etf.drs.conc.Buffer;


public class BufferRMI<T> extends Buffer<T> {
	RemoteBuffer<T> stub;

	@SuppressWarnings("unchecked")
	public BufferRMI(String host, int port, int bufferNumber) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			Registry registry = LocateRegistry.getRegistry(host, port);
			String name = "/buffer" + bufferNumber;
			
			stub = (RemoteBuffer<T>) registry.lookup(name);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void put(T data) {
		try {
			stub.put(data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public T get() {
		T data = null;
		
		try {
			data = stub.get();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}
