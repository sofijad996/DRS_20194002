package rs.ac.bg.etf.drs.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {

	public static void main(String[] args) {		
		try {
			
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}			
			Registry registry = LocateRegistry.createRegistry(4003);
			
			RemoteBarrier barrier = new RemoteBarrierImpl(15);
			RemoteBarrier stubBarrier = (RemoteBarrier) UnicastRemoteObject.exportObject(barrier, 0);
			registry.rebind("/barrier", stubBarrier);
			
			RemoteBuffer buffer1 = new RemoteBufferImpl<String>(7);
			RemoteBuffer stubBuffer1 = (RemoteBuffer) UnicastRemoteObject.exportObject(buffer1, 0);
			registry.rebind("/buffer1", stubBuffer1);
			
			RemoteBuffer buffer2 = new RemoteBufferImpl<String>(7);
			RemoteBuffer stubBuffer2 = (RemoteBuffer) UnicastRemoteObject.exportObject(buffer2, 0);
			registry.rebind("/buffer2", stubBuffer2);
			
			RemoteBuffer buffer3 = new RemoteBufferImpl<String>(7);
			RemoteBuffer stubBuffer3 = (RemoteBuffer) UnicastRemoteObject.exportObject(buffer3, 0);
			registry.rebind("/buffer3", stubBuffer3);
			
			RemoteBuffer buffer4 = new RemoteBufferImpl<String>(7);
			RemoteBuffer stubBuffer4 = (RemoteBuffer) UnicastRemoteObject.exportObject(buffer4, 0);
			registry.rebind("/buffer4", stubBuffer4);
			
			RemoteBuffer buffer5 = new RemoteBufferImpl<String>(7);
			RemoteBuffer stubBuffer5 = (RemoteBuffer) UnicastRemoteObject.exportObject(buffer5, 0);
			registry.rebind("/buffer5", stubBuffer5);
			
			RemoteBuffer buffer6 = new RemoteBufferImpl<String>(7);
			RemoteBuffer stubBuffer6 = (RemoteBuffer) UnicastRemoteObject.exportObject(buffer6, 0);
			registry.rebind("/buffer6", stubBuffer6);
			
			System.out.println("Server RMI pokrenut na portu 4003...");
			for (String item : registry.list()) {
				System.out.println(item);
			}			
			
		} catch (RemoteException re) {
			re.printStackTrace();
		}

	}

}
