package memory;

import java.util.ArrayList;
import kernel.Process;

public class Partition {
	private int index = -1;
	private int[] data;
	private int size;
	private Process process;
	private static ArrayList<Partition> partitions;
	
	public Partition(Process p) {  //dobro
		process = p;
		size = process.getInstructions().size();
		data = new int[size];
		for (int i = 0; i < size; i++) {
			String temp = p.getInstructions().get(i);
			data[i] = Integer.parseInt(temp, 2);
		}
		partitions.add(this);
	}

	public Partition(int[] data) {   //dobro
		this.data = data;
	}
	
	public static void initialize() {   //dobro
		partitions = new ArrayList<>();
	}

	/*public Partition(int sizeP) {
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
	}*/
	
	public int getIndex() {   //dobro
		return index;
	}
	
	public void setIndex(int ind) {   //dobro
		index = ind;
	}
	
	public int[] getData() {   //dobro
		return data;
	}
	
	public int getSize() {   //dobro
		return size;
	}
	
	public Process getProcess() {   //dobro
		return process;
	}
	
	/*public Partition occupyMemory(Process proc) {
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
	}*/

	public static Partition getPartition(int ind) {  //dobro
		for (Partition part: partitions) {
			if (part.getIndex() == ind)
				return part;
		}
		return null;
	}

	public static Partition getPartition(Process p) {  //dobro
		for (Partition part: partitions) {
			if (part.getProcess().equals(p))
				return part;
		}
		return null;
	}
}
