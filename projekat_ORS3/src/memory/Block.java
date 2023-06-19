package memory;

public class Block {
	private final static int size = 4;
	private byte[] elements = new byte[size];
	private final int index;
	private boolean occupied;

	public Block(int index) {
		this.index = index;
		this.occupied = false;
	}

	public static int getSize() {
		return size;
	}

	public byte[] getElements() {
		return elements;
	}

	public void setElements(byte[] elements) {
		this.elements = elements;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public String toString() {
		return "Block " + index;
	}
}
