package kernel;

public class ProcessControlBlock {
	private int processID;
	private ProcessState state;
	private String programName;
	private String fileNamePath;
	
	public ProcessControlBlock (int processID, String programName, String fileNamePath) {
		this.processID = processID;
		this.fileNamePath = fileNamePath;
		this.programName = programName;
		this.state = ProcessState.NEW;
		
	}
	
	public int getProcessID() {
		return processID;
	}
	
	public String getProgramName() {
		return programName;
	}
	
	public ProcessState getProcessState() {
		return state;
	}
	public void setProcesState(ProcessState state) {
		this.state = state;
	}
	public String getFileNamePath() {
		return fileNamePath;
	}
	
	

}
