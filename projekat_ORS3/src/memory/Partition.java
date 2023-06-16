package memory;

import java.util.ArrayList;
import kernel.Process;

public class Partition {
	private int index = -1;
	private int[] data;
	private int size;
	private Process process;
	private static ArrayList<Partition> partitions;
	
	public Partition(Process p) {  
		process = p;
		size = process.getInstructions().size();
		data = new int[size];
		for (int i = 0; i < size; i++) {
			String temp = p.getInstructions().get(i);
			data[i] = Integer.parseInt(temp, 2);
		}
		partitions.add(this);
	}

	public Partition(int[] data) {   
		this.data = data;
	}
	
	public static void initialize() {   
		partitions = new ArrayList<>();
	}
	
	public int getIndex() {  
		return index;
	}
	
	public void setIndex(int ind) {   
		index = ind;
	}
	
	public int[] getData() {   
		return data;
	}
	
	public int getSize() {   
		return size;
	}
	
	public Process getProcess() {   
		return process;
	}

	public static Partition getPartition(int ind) {  
		for (Partition part: partitions) {
			if (part.getIndex() == ind)
				return part;
		}
		return null;
	}

	public static Partition getPartition(Process p) {  
		for (Partition part: partitions) {
			if (part.getProcess().equals(p))
				return part;
		}
		return null;
	}
}
