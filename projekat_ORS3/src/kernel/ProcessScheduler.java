package kernel;

import java.util.ArrayList;
import java.util.PriorityQueue;
import assembler.Operations;
import fileSystem.FileSystem;
import memory.Memory;
import memory.RAM;
import shell.Shell;

public class ProcessScheduler{
	public static ArrayList<Process> listOfProcesses = new ArrayList<>();
	public static PriorityQueue<Process> processQueue = new PriorityQueue<>();
	
	public ProcessScheduler() {
	}
	
	public int getProcessPriority(Process p) {
		int priority = 0;
		ArrayList<Process> pQ = new ArrayList<>(processQueue);
		for(int i=0; i < pQ.size(); i++) {
			if(pQ.get(i).getProcessID() == p.getProcessID() ) {
				priority = i + 1 ;
				ProcessControlBlock pcb = p.getPCB();
				pcb.savingPriority(priority);
				break;
			}
		}
		return priority;
	}
	
	public void runProgram() {
		while(!processQueue.isEmpty()) {
			Process next = processQueue.poll();
			executeProcess(next);
		}	
		System.out.println("There are no processes left to be executed!");
	}
	
	private static void executeProcess(Process p) {
		Shell.currentlyExecuting = p;
		System.out.println("Process " + p.getName() + " started executiong!");
		int startAdress = Shell.memory.loadProcess(p);
		p.setStartAdress(startAdress);
		Shell.base = startAdress;
		Shell.limit = p.getInstructions().size();
		p.getPCB().setProcessState(ProcessState.RUNNING);
		executeP(p);
	}

	private static void executeP(Process p) {	
		while(p.getPCB().getProcessState() == ProcessState.RUNNING) {
			int ramValue = RAM.get(Shell.base + Shell.PC);
			String instruction = Shell.fromIntToInstruction(ramValue);
			Shell.IR = instruction;
			Shell.executeMachineInstruction();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println("Error with thread");
		}
		if (p.getPCB().getProcessState() == ProcessState.BLOCKED) {
			System.out.println("Process " + p.getName() + " is blocked!");
			Shell.saveValues();
		}
		else if (p.getPCB().getProcessState() == ProcessState.TERMINATED) {
			System.out.println("Process " + p.getName() + " is terminated!");
			Memory.removeProcess(p);
		} 
		else if (p.getPCB().getProcessState() == ProcessState.DONE) {
			System.out.println("Process " + p.getName() + " is done!");
			Memory.removeProcess(p);
			FileSystem.createFile(p); 
		} 
		Operations.clearReg();
	}
	
	public static void blockProcess(int pID) {
		if(pID < listOfProcesses.size()) {
			listOfProcesses.get(pID).block();
			return;
		}
		System.out.println("Process with this processID " + pID + " doesn't exist");
	}
	
	public static void unblockProcess(int pID) {
		if(pID < listOfProcesses.size()) {
			listOfProcesses.get(pID).unblock();
			return;
		}
		System.out.println("Process with this processID " + pID + " doesn't exist");
	}
	
	public static void terminateProcess(int pID) {
		if(pID < listOfProcesses.size()) {
			listOfProcesses.get(pID).terminate();
			return;
		}
		System.out.println("Process with this processID " + pID + " doesn't exist");
	}

	public static void printProcesses() {
		System.out.println("PID\tProgram\t\tSize\tState\t\tCurrent occupation of memory");
		for (Process process : listOfProcesses)
			System.out.println(process.getProcessID() + "\t" + process.getName() + "\t  " + process.getSize() + "\t"
					+ process.getPCB().getProcessState() + "\t " +
					+ Memory.memoryOccupiedByProcessSize(process));
	}
	
	public PriorityQueue<Process> getProcessQueue(){
		return processQueue;
	}
}
