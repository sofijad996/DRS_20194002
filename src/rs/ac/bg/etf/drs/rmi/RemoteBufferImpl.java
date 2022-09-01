package rs.ac.bg.etf.drs.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteBufferImpl<T> implements RemoteBuffer<T> {
	
	protected T[] buffer;
	protected int head, tail, size;
	boolean theEnd;
	int capacity;
	
	public RemoteBufferImpl(int capacity) {
		this.capacity = capacity;
		this.buffer = (T[]) new Object[capacity];
		this.theEnd = false;
	}
	
	@Override
	public synchronized void put(T data) throws RemoteException {
		if (data == null) {
			theEnd = true;
			notifyAll();
			return;
		}
		
		while (size == capacity && !theEnd) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buffer[head] = data;
		head = (head + 1) % capacity;
		size++;
		
		notifyAll();
	}

	@Override
	public synchronized T get() throws RemoteException {
		while (size == 0 && !theEnd) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (theEnd && size == 0) {
			return null;
		}
		
		T result = buffer[tail];
		buffer[tail] = null;
		tail = (tail + 1) % capacity;
		size--;
		
		notifyAll();
		return result;
	}

}
