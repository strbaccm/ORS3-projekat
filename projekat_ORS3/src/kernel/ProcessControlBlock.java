package kernel;

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

	public void savingPriority(int priority) {
		this.priority = priority;
	}
}
