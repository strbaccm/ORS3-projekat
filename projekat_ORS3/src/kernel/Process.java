package kernel;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import memory.Memory;
import memory.Partition;

public class Process implements Comparable<Process> {
	private int processID;
	private String name;
	private Path path;
	private Date arrivalTime;
	private int size;
	private ProcessControlBlock pcb;
	private Partition partition;
	private int startAdress;
	private int[] valuesOfRegister;
	private ArrayList<String> instructions;
	private int pcValue = -1;
	
	public Process(String name, Path path) {
		this.pcb = new ProcessControlBlock();
		this.processID = ProcessScheduler.listOfProcesses.size();
		this.name = name;
		this.path = path;
		this.arrivalTime = new Date();
		valuesOfRegister = new int[4];
		instructions = new ArrayList<>();
		readFile();
		this.size = instructions.size();
		this.partition = null;
		ProcessScheduler.listOfProcesses.add(this);
		ProcessScheduler.processQueue.add(this);

	}
	
	public void readFile() {
	
	}
	
	public void block() {
		if (this.getPCB().getProcessState() == ProcessState.RUNNING) {
			this.getPCB().setProcessState(ProcessState.BLOCKED);
			if (ProcessScheduler.processQueue.contains(this))
				ProcessScheduler.processQueue.remove(this);
		}
	}

	public void unblock() {
		if (this.getPCB().getProcessState() == ProcessState.BLOCKED) {
			this.getPCB().setProcessState(ProcessState.READY);
			System.out.println("Process " + this.getName() + " is unblocked");
			ProcessScheduler.processQueue.add(this);
		}
	}

	public void terminate() {
		if (this.getPCB().getProcessState() == ProcessState.READY || this.getPCB().getProcessState() == ProcessState.RUNNING) {
			this.getPCB().setProcessState(ProcessState.TERMINATED);
			if (ProcessScheduler.processQueue.contains(this))
				ProcessScheduler.processQueue.remove(this);
		} else if (this.getPCB().getProcessState() == ProcessState.BLOCKED) {
			this.getPCB().setProcessState(ProcessState.TERMINATED);
		}
	}
	
	public ArrayList<String> getInstrucstions(){
		return instructions;
	}
	
	public Partition getPartition() {
		return partition;
	}
	
	public ProcessControlBlock getPCB() {
		return pcb;
	}
	
	public Path getPath() {
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
	
	public int getProcessID() {
		return processID;
	}

	public int getPCValue() {
		return pcValue;
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
