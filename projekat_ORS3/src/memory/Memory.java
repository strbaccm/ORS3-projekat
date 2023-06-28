package memory;

import java.util.ArrayList;
import assembler.Operations;
import kernel.Process;
import kernel.ProcessState;

public class Memory {
	public static int indexLast;
	public static int lengthLast;
	public static ArrayList<Partition> partitionsRAM;

	public Memory() {   
		RAM.initialize();
		Partition.initialize();
		indexLast = 0;
		lengthLast = 1;
		partitionsRAM = new ArrayList<>();
	}

	public static boolean removePartition(Partition part) {   
		if (partitionsRAM.contains(part)) {
			RAM.clear(part.getIndex(), part.getSize());
			part.setIndex(-1);
			partitionsRAM.remove(part);
			return true;
		}
		return false;
	}
	
	public static boolean removeProcess(Process p) {   
		return removePartition(Partition.getPartition(p));
	}
	
	public int loadPartition(Partition part) {   
		int position = load(part.getData());
		if (position != -1) {
			part.setIndex(position);
			partitionsRAM.add(part);
		}
		return position;
	}
	
	public int loadProcess(Process p) {   
		Partition part = Partition.getPartition(p);
		if (!partitionsRAM.contains(part)) {
			return loadPartition(new Partition(p));
		} else {
			return p.getStartAdress();
		}
	}
	
	public int[] readRAM(int start, int size) {   
		int[] data = new int[size];
		for (int i = 0; i < data.length; i++) {
			if (RAM.isOccupied(i + start)) {
				data[i] = RAM.get(i + start);
			}
		}
		return data;
	}

	public int[] readPartiton(Partition part) {   
		if (partitionsRAM.contains(part))
			return readRAM(part.getIndex(), part.getSize());
		return null;
	}
	
	public int[] readProcess(Process p) {   
		return readPartiton(Partition.getPartition(p));
	}
	
	private void makeSpace(int size) {   
		for (Partition partition : partitionsRAM) {
			if (partition.getProcess().getPCB().getProcessState() == ProcessState.BLOCKED) {
				removePartition(partition);
			}
			if (size < RAM.getFreeSize())
				break;
		}
		Partition lastAdded = partitionsRAM.get(partitionsRAM.size() - 1);
		while (size > RAM.getFreeSize()) {
			removePartition(lastAdded);
			if (!partitionsRAM.isEmpty())
				lastAdded = partitionsRAM.get(partitionsRAM.size() - 1);
		}
	}

	private void defragmentation() {  
		int freePosition = -1;
		boolean availablePosition = false;
		for (int i = 0; i < RAM.getSize(); i++) {
			if (!RAM.isOccupied(i) && !availablePosition)  {
				freePosition = i;
				availablePosition = true;
			} 
			else if (RAM.isOccupied(i) && availablePosition) {
				Partition partition = Partition.getPartition(i);																
				int size = partition.getSize();
				int j;
				for (j = freePosition; j < freePosition + size; j++) {
					RAM.set(j, RAM.get(i));
					RAM.clear(i); 
					i++;
				}
				partition.setIndex(freePosition);
				i = j - 1; 
				availablePosition = false;
			}
		}
	}

	private int load(int[] data) { 
		int size = data.length;
		if (size > RAM.getFreeSize()) { 
			makeSpace(size);
		}
		int newIndex = -1; 
		int currentIndex = -1; 
		int currentSize = 0;
		for (int i = indexLast + lengthLast - 1; i < RAM.getSize(); i++) {
			if (RAM.isOccupied(i) && currentSize != 0) { 
				if (currentSize >= size) {  
					newIndex = currentIndex;
					break;
				}
				currentIndex = -1;
				currentSize = 0;
			} 
			else if (!RAM.isOccupied(i) && currentSize == 0) { 
				currentIndex = i;
				currentSize = 1;
			}
			else if (!RAM.isOccupied(i) && currentSize != 0) { 
				currentSize += 1;
			}
		}
		newIndex = currentIndex;
		if (newIndex == -1) { 
			defragmentation(); 
			newIndex = RAM.getOccupiedSize();
		}
		if (RAM.set(newIndex, data)) { 
			indexLast = newIndex;
			lengthLast = size;
			return newIndex;
	    }
		return -1;
	}

	public static int memoryOccupiedByProcessSize(Process p) {   
		for (Partition part: partitionsRAM)
			if (part.getProcess().getProcessID() == p.getProcessID())
				return part.getSize();
		return 0;
	}
	
	public static void printMemory() {  
		System.out.println("~~~~~~~~~~ MEMORY ~~~~~~~~~~");  
		RAM.printRAM();
		Operations.printReg();
		Disk.printDisk();
	}

	public static ArrayList<Partition> getPartitionsRAM() {   
		return partitionsRAM;
	}
}
