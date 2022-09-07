package rs.ac.bg.etf.drs.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {
	
	final static int port = 4003;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {		
		try {
			
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}			
			Registry registry = LocateRegistry.createRegistry(4003);
			
			RemoteBarrier barrier1 = new RemoteBarrierImpl(5);
			RemoteBarrier stubBarrier1 = (RemoteBarrier) UnicastRemoteObject.exportObject(barrier1, 0);
			registry.rebind("/barrier1", stubBarrier1);
			
			RemoteBarrier barrier2 = new RemoteBarrierImpl(5);
			RemoteBarrier stubBarrier2 = (RemoteBarrier) UnicastRemoteObject.exportObject(barrier2, 0);
			registry.rebind("/barrier2", stubBarrier2);
			
			RemoteBarrier barrier3 = new RemoteBarrierImpl(5);
			RemoteBarrier stubBarrier3 = (RemoteBarrier) UnicastRemoteObject.exportObject(barrier3, 0);
			registry.rebind("/barrier3", stubBarrier3);
			
			RemoteBuffer<String> buffer1 = new RemoteBufferImpl<String>(7);
			RemoteBuffer<String> stubBuffer1 = (RemoteBuffer<String>) UnicastRemoteObject.exportObject(buffer1, 0);
			registry.rebind("/buffer1", stubBuffer1);
			
			RemoteBuffer<String> buffer2 = new RemoteBufferImpl<String>(7);
			RemoteBuffer<String> stubBuffer2 = (RemoteBuffer<String>) UnicastRemoteObject.exportObject(buffer2, 0);
			registry.rebind("/buffer2", stubBuffer2);
			
			RemoteBuffer<String> buffer3 = new RemoteBufferImpl<String>(7);
			RemoteBuffer<String> stubBuffer3 = (RemoteBuffer<String>) UnicastRemoteObject.exportObject(buffer3, 0);
			registry.rebind("/buffer3", stubBuffer3);
			
			RemoteBuffer<String> buffer4 = new RemoteBufferImpl<String>(7);
			RemoteBuffer<String> stubBuffer4 = (RemoteBuffer<String>) UnicastRemoteObject.exportObject(buffer4, 0);
			registry.rebind("/buffer4", stubBuffer4);
			
			RemoteBuffer<String> buffer5 = new RemoteBufferImpl<String>(7);
			RemoteBuffer<String> stubBuffer5 = (RemoteBuffer<String>) UnicastRemoteObject.exportObject(buffer5, 0);
			registry.rebind("/buffer5", stubBuffer5);
			
			RemoteBuffer<String> buffer6 = new RemoteBufferImpl<String>(7);
			RemoteBuffer<String> stubBuffer6 = (RemoteBuffer<String>) UnicastRemoteObject.exportObject(buffer6, 0);
			registry.rebind("/buffer6", stubBuffer6);
			
			System.out.println("Server RMI pokrenut na portu: " + port);
			for (String item : registry.list()) {
				System.out.println(item);
			}			
			
		} catch (RemoteException re) {
			re.printStackTrace();
		}

	}

}
