package memory;

import java.util.ArrayList;

public class Memory {
	private static int size = 1024;
	private static ArrayList<Partition> partitions = new ArrayList<>();
	private static int occupied;
	
	public static void init() {
		occupied = 0;
	}
	
	public static ArrayList<Partition> getPartitions(){
		return partitions;
	}
	
	public static boolean addPartition() {
		if(size - occupied < size)
			return false;
		
		occupied += size;
		Partition newP = new Partition(size);
		partitions.add(newP);
		return true;
	}

	public static boolean addPartition(int sizeP) {
		if(size - occupied < sizeP)
			return false;
		
		occupied += sizeP;
		Partition newP = new Partition(sizeP);
		partitions.add(newP);
		return true;
	}
	
	public static boolean mergeFreePartitions(int i) {
		if(i>0 && partitions.get(i-1).getProcess() == null)
			i--;
		if(i >= partitions.size()-1 || partitions.get(i+1).getProcess() != null)
			return false;
		
		Partition newP = new Partition(partitions.get(i), partitions.get(i+1));
		partitions.set(i, newP);
		partitions.remove(i+1);

		boolean condition = true;
		while(condition) {
			condition = mergeFreePartitions(i);
		}
		
		return true;
	}
	
	public static boolean mergeFreePartitions(Partition part) {
		int indexP = partitions.indexOf(part);
		return mergeFreePartitions(indexP);
	}
	
	public static boolean separatePartitions(int i) {
		Partition part = partitions.get(i);
		if(part.getSize() == part.getOccupied())
			return false;
		Partition newP1 = new Partition(part.getOccupied());
		newP1.occupyMemory(part.getProcess());
		Partition newP2 = new Partition(part.getFree());
		partitions.set(i, newP1);
		partitions.add(i+1, newP2);
		
		mergeFreePartitions(i+1);
		
		return true;
	}
	
	public static boolean separatePartitions(Partition part) {
		int indexP = partitions.indexOf(part);
		return separatePartitions(indexP);
	}
	
	public static Partition occupyPartition(int index, Process proc) {
		return partitions.get(index).occupyMemory(proc);
	}
	
	public static Partition occupyPartition(Partition part, Process proc) {
		if (!partitions.contains(part))
			return null;
		return part.occupyMemory(proc);
	}
	
	public static String info() {
		String s = "";
		for(Partition part: partitions)
			s += part.info() + "| ";
		return s;
	}
	
	public static String showMemory() {
		String res = "";
		int d = 0;
		int ind = 0;
		for(Partition part: partitions) {
			part.setIndex(ind);
			ind += part.getSize();
			res += part;
			d += part.toString().length();
			if(d>90) {
				d = 0;
				res += "\n";
			}
		}
		return res;
	}
}
