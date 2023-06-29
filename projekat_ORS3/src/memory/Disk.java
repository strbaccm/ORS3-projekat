package memory;

import java.util.ArrayList;

public class Disk {
	private static int size = 2048;
	private static Block[] blocks;
	private static int blocksNumber;
	public static ArrayList<StoredFile> files;

	public Disk() {
		blocksNumber = size / Block.getSize();
		blocks = new Block[blocksNumber];
		files = new ArrayList<>();
		for (int i = 0; i < blocksNumber; i++) {
			Block block = new Block(i);
			blocks[i] = block;
		}
	}

	public void storeFile(StoredFile file) {   
		int rest = file.getSize() % Block.getSize();
		int blNum;
		if (rest == 0)
			blNum = file.getSize() / Block.getSize();
		else
			blNum = (file.getSize() - rest + Block.getSize()) / Block.getSize();
		if (getFreeBlocksNumber() >= blNum) {
			int number = 0;
			BlockFile one = null;
			for (int i = 0; i < blocksNumber; i++)
				if (!blocks[i].isOccupied()) {
					blocks[i].setOccupied(true);
					blocks[i].setElements(StoredFile.takePartForBlock(number));
					if (number == 0) {
						one = new BlockFile(blocks[i]);
						file.setStartBlockFile(one);
						number += 1;
					} else {
						BlockFile two = new BlockFile(blocks[i]);
						one.setNext(two);
						one = two;
						number += 1;
						if (number == blNum) {
							file.setLength(number);
							files.add(file);
							return;
						}
					}
				}
		} else
			System.out.println("File can not be stored!");
	}

	public void deleteFile(StoredFile file) {   
		if (!files.contains(file))
			System.out.println("File is not stored on disk!");
		else {
			BlockFile bf = file.getStartBlockFile();
			file.setStartBlockFile(null);
			bf.getBlock().setOccupied(false);
			bf.getBlock().setElements(null);
			while (bf.getNext() != null) {
				BlockFile temp = bf;
				bf = bf.getNext();
				temp.setNext(null);
				bf.getBlock().setOccupied(false);
			}
		}
		files.remove(file);
	}

	public String readFile(StoredFile file) {    
		String content = "";
		BlockFile bf = file.getStartBlockFile();
		byte[] elements = bf.getBlock().getElements();
		for (byte b: elements)
			content += (char) b;
		while (bf.getNext() != null) {
			bf = bf.getNext();
			elements = bf.getBlock().getElements();
			for (byte b: elements)
				content += (char) b;
		}
		return content;
	}

	public static int getFreeBlocksNumber() {   
		int number = 0;
		for (int i = 0; i < blocksNumber; i++)
			if (!blocks[i].isOccupied())
				number += 1;
		return number;
	}

	public boolean contains(String fileName) {   
		for (StoredFile file: files)
			if (file.getName().equals(fileName))
				return true;
		return false;
	}

	public StoredFile getFile(String fileName) {   
		for (StoredFile file: files)
			if (file.getName().equals(fileName))
				return file;
		return null;
	}

	public static int getSize() {   
		return size;
	}

	public static Block[] getBlocks() {   
		return blocks;
	}

	public ArrayList<StoredFile> getStoredFiles() {   
		return files;
	}

	public static void printDisk() {   
		System.out.println("~~~~~~~~~~ DISK ~~~~~~~~~~");
		System.out.println("File\t\t\t\t\t\tStart block\t\tLength");
		for (StoredFile file: files) {
			String print = "";
			if (file.getName().length() <8)
				print += file.getName() + "\t\t\t\t\t";
			else if (file.getName().length() <12)
				print += file.getName() + "\t\t\t\t";
			else if (file.getName().length() < 16)
				print += file.getName() + "\t\t\t";
			else
				print += file.getName() + "\t\t";
			print += file.getStartBlockFile().getBlock().getIndex() + "\t\t\t\t";
			print += file.getLength();
			System.out.println(print);
		}
	}
}
