package kernel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class ProcessControlBlock {
	private ProcessState state;
	private int priority;

	public ProcessControlBlock () {
		this.state = ProcessState.READY;	
	}

	public ProcessState getProcessState() {
		return state;
	}
	
	public void setProcessState(ProcessState state) {
		this.state = state;
	}
	
	public int getPriority() {
		return priority;
	}
}
