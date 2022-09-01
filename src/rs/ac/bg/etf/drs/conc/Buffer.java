package rs.ac.bg.etf.drs.conc;

import java.util.ArrayList;
import java.util.List;

public class Buffer<T> {
	List<T> buffer;
	boolean end;
	int capacity;
	
	public Buffer() {
		this.buffer = new ArrayList<T>();
		this.capacity = 50;
		this.end = false;
	}
	public synchronized void put(T data) {
		if (data == null) {
			end = true;
			notifyAll();
			return;
		}
		while (!end && (buffer.size()) == capacity) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.add(data);
		notifyAll();
	}
	
	public synchronized T get() {
		while (buffer.size() == 0 && !end) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (buffer.size() == 0 && end) {
			return null;
		}
		
		T result = buffer.remove(0);
		notifyAll();
		
		return result;
	}
}
