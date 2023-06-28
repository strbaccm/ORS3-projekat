package kernel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import memory.Partition;
import shell.Shell;

public class Process implements Comparable<Process> {
	private int processID;
	private String name;
	private Path path;
	private Date arrivalTime;
	private int size;
	private ProcessControlBlock pcb;
	private Partition partition;
	private int startAdress;
	private int[] valuesOfRegisters;
	private ArrayList<String> instructions;
	private int pcValue = -1;
	
	public Process(String name) {
		if (new File(Shell.tree.getCurrentFolder().getAbsolutePath() + "\\" + name).exists()){
		this.pcb = new ProcessControlBlock();
		this.processID = ProcessScheduler.listOfProcesses.size();
		this.name = name;
		this.path = Paths.get(Shell.tree.getCurrentFolder().getAbsolutePath() + "\\" + name);
		this.arrivalTime = new Date();
		valuesOfRegisters = new int[4];
		instructions = new ArrayList<>();
		readFile();
		this.size = instructions.size();
		this.partition = null;
		ProcessScheduler.listOfProcesses.add(this);
		ProcessScheduler.processQueue.add(this);
		System.out.println("Program " + name + " is loaded!");
		}
		else{
			System.out.println("Program " + name + " doesn't exist in this directory!");
		}

	}
	
	public void readFile() {
		String data = Shell.disk.readFile(Shell.disk.getFile(name));
		String [] commands = data.split("\\n");
		for(String command : commands) {
			if( !command.equals(commands[commands.length-1]) ) {
				command = command.substring(0, command.length() -1);
			}
			else {
				if( command.length() > 3 )
					command = command.substring(0,3);
			}
		String machineIstruction = Shell.assemblerToMachineInstruction(command);
		instructions.add(machineIstruction);
		}
	
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
			this.setArrivalTime(new Date());
			ProcessScheduler.processQueue.add(this);
			System.out.println("Process " + this.getName() + " is unblocked!");
			
		}
	}

	public void terminate() {
		if (this.getPCB().getProcessState() == ProcessState.READY || this.getPCB().getProcessState() == ProcessState.RUNNING ||
				this.getPCB().getProcessState() == ProcessState.BLOCKED) {
			    this.getPCB().setProcessState(ProcessState.TERMINATED);
			    if (ProcessScheduler.processQueue.contains(this))
					ProcessScheduler.processQueue.remove(this);
		}
	}

	public int[] getValuesOfRegisters() {
		return valuesOfRegisters;
	}

	public void setValuesOfRegisters(int[] valuesOfRegisters) {
		for (int i = 0; i < valuesOfRegisters.length; i++)
			this.valuesOfRegisters[i] = valuesOfRegisters[i];
	}

	public int getPcValue() {
		return pcValue;
	}

	public void setPcValue(int pcValue) {
		this.pcValue = pcValue;
	}
	
	public int getStartAdress() {
		return startAdress;
	}

	public void setStartAdress(int startAdress) {
		this.startAdress = startAdress;
	}
	
	public ArrayList<String> getInstructions(){
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
	
	public String getName() {
		return name;
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
