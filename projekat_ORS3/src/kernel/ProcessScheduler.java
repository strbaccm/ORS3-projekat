package kernel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;
import assembler.Operations;
import memory.Memory;
import memory.Disk;
import memory.RAM;
import shell.Shell;


public class ProcessScheduler extends Thread{
	private static int timeQuantum = 1;
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
	
	@Override
	public void run() {
		while(!processQueue.isEmpty()) {
			Process next = processQueue.poll();
			executeProcess(next);
			if(next.getPCB().getProcessState() != ProcessState.BLOCKED && 
					next.getPCB().getProcessState() != ProcessState.TERMINATED &&
					next.getPCB().getProcessState() != ProcessState.DONE) {
				next.getPCB().setProcessState(ProcessState.READY);
				processQueue.add(next);
			}
		}
		System.out.println("There are no processes left to be executed!");
		
	}
	
	private static void executeProcess(Process p) {
		Shell.currentlyExecuting = p;
		if( p.getPCValue() == -1 ) {
			System.out.println("Process " + p.getName() + " started executiong!");
			int startAdress = Shell.memory.loadProcess(p);
			p.setStartAdress(startAdress);
			Shell.base = startAdress;
			Shell.limit = p.getInstructions().size();
			Shell.PG = 0;
			p.getPCB().setProcessState(ProcessState.RUNNING);
			execute(p, System.currentTimeMillis());
		}
		else {
			System.out.println("Process " + p.getName() + " is executing again");
			int startAdress = Shell.memory.loadProcess(p);
			p.setStartAdress(startAdress);
			Shell.base = startAdress;
			Shell.limit = p.getInstructions().size();
			Shell.loadValues();
			p.getPCB().setProcessState(ProcessState.RUNNING);
			execute(p, System.currentTimeMillis());
		}
	}

	private static void execute(Process p, long startTime) {	
		while(p.getPCB().getProcessState() == ProcessState.RUNNING &&
				System.currentTimeMillis() - startTime < timeQuantum) {
			int t = RAM.get(Shell.PG + Shell.base);
			String instruction = Shell.fromIntToInstruction(t);
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
		} else if (p.getPCB().getProcessState() == ProcessState.TERMINATED) {
			System.out.println("Process " + p.getName() + " is terminated!");
			Memory.removeProcess(p);
		} else if (p.getPCB().getProcessState() == ProcessState.DONE) {
			System.out.println("Process " + p.getName() + " is done!");
			Memory.removeProcess(p);
		} else { // process is switched by process scheduler
			Shell.saveValues();
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
	
	public PriorityQueue<Process> getProcessQueue(){
		return processQueue;
	}
	

}
