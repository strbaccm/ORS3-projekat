package memory;

public class RAM {
	private static final int size = 1024;
	private static int[] ram = new int[size];
	private static int occupiedSize;

	public static void initialize() {
		for (int i = 0; i < size; i++) {
			ram[i] = -1;
		}
	}

	public static boolean isOccupied(int index) {
		if (ram[index] != -1)
			return true;
		return false;
	}
	
	public static int get(int index) {
		return ram[index];
	}

	public static boolean set(int index, int value) {
		if (!isOccupied(index)) {
			ram[index] = value;
			occupiedSize += 1;
			return true;
		}
		else if (isOccupied(index) && get(index) == value)
			return true;
		return false;
	}
	
	public static boolean clear(int index) {
		if (isOccupied(index)) {
			ram[index] = -1;
			occupiedSize -= 1;
			return true;
		}
		else if (!isOccupied(index))
			return true;
		return false;
	}
	
	public static boolean set(int start, int[] data) {
		if (start + data.length > size)
			return false;
		for (int i = start; i < data.length + start; i++) {
			if (!isOccupied(i)) 
				set(i, data[i - start]);
		    else
				return false;
		}
		return true;
	}

	public static boolean clear(int start, int length) {
		if (start + length > size)
			return false;
		for (int i = start; i < start + length; i++) {
			if (isOccupied(i)) 
				clear(i);
			 else 
				return false;
		}
		return true;
	}

	public static int getOccupiedSize() {
		return occupiedSize;
	}

	public static int getFreeSize() {
		return size - occupiedSize;
	}

	public static int getSize() {
		return size;
	}
	
	public static void printRAM() {
		System.out.println("~~~~~~~~~~ RAM ~~~~~~~~~~");
		System.out.print("| ");
		for (int i = 0; i < size; i++) {
			if (i % 14 == 0 && i != 0) {
				System.out.println();
				System.out.print("| ");
			}
			System.out.print(ram[i] + " | ");
		}
		System.out.println();
	}
}
