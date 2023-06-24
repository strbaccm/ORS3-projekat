package memory;

public class StoredFile {
	private String name;
	private int size;
	private BlockFile startBlockFile;
	private int length;
	private static byte[] elements = new byte[0];

	public StoredFile(String name, byte[] bytes) {
		this.name = name;
		elements = bytes;
		size = elements.length;
	}

	public static byte[] takePartForBlock(int blockIndex) {
		byte[] bytes = new byte[Block.getSize()];
		int number = 0;
		for (int i = blockIndex * Block.getSize(); i < elements.length; i++) {
			bytes[number] = elements[i];
			if (number == Block.getSize() - 1)
				break;
			number += 1;
		}
		while (number < Block.getSize() - 1) {
			byte[] by = " ".getBytes();
			bytes[number] = by[0];
			number += 1;
		}
		return bytes;
	}

	public BlockFile getStartBlockFile() {
		return startBlockFile;
	}

	public void setStartBlockFile(BlockFile startBlockFile) {
		this.startBlockFile = startBlockFile;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public byte[] getElements() {
		return elements;
	}

	public void setElements(byte[] bytes) {
		elements = bytes;
	}
}
