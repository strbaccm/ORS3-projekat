package kernel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Process implements Comparable<Process> {
	private int processID;
	private String name;
	private String path;
	private int arrivalTime;
	private int size;
	ProcessControlBlock pcb;
	
	private ArrayList<String> instructions = new ArrayList<String>();
	
	
	public Process(int processID, String name, String path, int arrivalTime, int size) {
		this.pcb = new ProcessControlBlock();
		this.processID = processID;
		this.name = name;
		this.path = path;
		this.arrivalTime = arrivalTime;
		this.size = size;
		readFile();
		
	}
	
	public void readFile() {
		try {
		File file = new File(this.path);
		Scanner reader = new Scanner(file);
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			instructions.add(line);
		reader.close();
		}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		pcb.setProcessState(ProcessState.TERMINATED);
	}
	
	public void block() {
		pcb.setProcessState(ProcessState.BLOCKED);
	}
	
	public void run() {
		pcb.setProcessState(ProcessState.RUNNING);
	}
	
	public void ready() {
		pcb.setProcessState(ProcessState.READY);
	}
	
	public String getPath() {
		return path;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public void setArrivaleTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public boolean isProcessAlive() {
		if(pcb.getProcessState().compareTo(ProcessState.RUNNING) == 0)
			return true;
		else
			return false;
	}
	
	public int getProcessID() {
		return processID;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Process : [pId = " + this.getProcessID() + ", name = " + name + ", path = " + path + ", state = "
				+ pcb.getProcessState() + "]";
	}
	
	//If the current object is less than the object being compared (other), compareTo returns a negative integer.
	//If the current object is greater than the object being compared, compareTo returns a positive integer.
	@Override
	public int compareTo(Process p) {
		// TODO Auto-generated method stub
		return this.arrivalTime - p.getArrivalTime();
	}
	

}