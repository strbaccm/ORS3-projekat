package kernel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import memory.Memory;
import memory.Partition;

public class Process implements Comparable<Process> {
	private int processID;
	private String name;
	private String path;
	private Date arrivalTime;
	private int size;
	private boolean alive;
	ProcessControlBlock pcb;
	Partition partition;

	public static ArrayList<Process> listOfProcesses = new ArrayList<>();
	public static ArrayList<Process> processQueue = new ArrayList<>();
	
	private ArrayList<String[]> instructions;
	
	public Process(int processID, String name, String path, int size) {
		this.pcb = new ProcessControlBlock();
		this.processID = processID;
		this.name = name;
		this.path = path;
		this.arrivalTime = new Date();
		this.size = size;
		this.alive = true;
		this.partition = null;
		instructions = new ArrayList<>();
		readFile();
		listOfProcesses.add(this);
		processQueue.add(this);
	}
	
	public void readFile() {
		try {
		File file = new File(this.path);
		Scanner reader = new Scanner(file);
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			String [] instruction = line.split(" ");
			instructions.add(instruction);
		}
			reader.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getProcessPriority(Process p) {
		int priority = 0;
		Collections.sort(processQueue);
		for(int i=0; i < processQueue.size(); i++) {
			if(processQueue.get(i).getProcessID() == p.getProcessID() ) {
				priority = i + 1 ;
				break;
			}
		}
		return priority;
	}
	
	public void setProcessPriority() {
		Collections.sort(processQueue);
	}
	
	public void terminate() {
		pcb.setProcessState(ProcessState.TERMINATED);
		this.alive = false;
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

	public ArrayList<String[]> getInstrucstions(){
		return instructions;
	}
	
	public Partition getPartition() {
		if( this.alive == false)
			return null;
		else 
			return partition;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getSize() {
		return size;
	}
	
	public Date getArrivalTime() {
		return arrivalTime;
	}
	
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public boolean isProcessAlive() {
		return this.alive;
	}
	
	public int getProcessID() {
		return processID;
	}
	
	public String getName() {
		return name;
	}
	
	public void freeMemory() {
		partition = null;
	}
	
	public boolean load(int indexP) {
		this.partition = Memory.occupyPartition(indexP, this);
		if(partition == null)
			return false;
		return true;
	}
	
	public boolean load(Partition part) {
		this.partition = Memory.occupyPartition(part, this);
		if(this.partition == null)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Process : [pId = " + this.getProcessID() + ", name = " + name + ", path = " + path + ", state = "
				+ pcb.getProcessState() + "]";
	}
	
	//If the current object came before the object being compared (other), compareTo returns a negative integer.
	//If the current object came after the object being compared (other), compareTo returns a positive integer.
	@Override
	public int compareTo(Process p) {
		return this.arrivalTime.compareTo(p.getArrivalTime());
	}
}
