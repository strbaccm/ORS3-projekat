package memory;

import kernel.Process;

public class Partition {
	private static int index;
	private final int size;
	private int occupied;
	private Process process;
	
	public Partition(int sizeP) {
		size = sizeP;
		occupied = 0;
	}
	
	public Partition() {
		size = 128;
		occupied = 0;
	}
	
	public Partition(Partition partition1, Partition partition2) {
		size = partition1.size + partition2.size;
		occupied = 0;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int ind) {
		index = ind;
	}
	
	public int getSize() {
		return size;
	}
	
	public Process getProcess() {
		return process;
	}
	
	public Partition occupyMemory(Process proc) {
		freeMemory();
		if(process.getSize() > size)
			return null;
		else {
			occupied = proc.getSize();
			this.process = proc;
			return this;
		}
	}
	
	public void freeMemory() {
		if(this.process != null) {
			this.process.freeMemory();
			this.process = null;
			this.occupied = 0;
		}
	}
	
	public int getOccupied() {
		return occupied;
	}
	
	public int getFree() {
		return getSize() - getOccupied();
	}
	
	@Override
	public String toString() {
		int k, z, s;
		k = (size%10 < 5) ? size/10 : size/10+1;
		z = (occupied%10 < 5) ? occupied/10 : occupied/10+1;
		s = k - z;
		
		String res = "|";
		for(int i = 0; i<z; i++)
			res += "x";
		for(int i = 0; i<s; i++)
			res += " ";
		res += "|";
		return res;
	}
	
	public String info() {
		String nameP = (process==null) ? "N.P." : process.getName();
		return String.format("%s: %d (%d : %d)", nameP, size, occupied, size - occupied);
	}
}
