package memory;

import java.util.ArrayList;
import kernel.Process;
import kernel.ProcessScheduler;

public abstract class DynamicPartitioning implements ProcessScheduler {
	private ArrayList<Process> processes = new ArrayList<>();
	
	public DynamicPartitioning() {
		Memory.init();
		Memory.addPartition();
		}
	
	@Override
	public void execute() {
	}

	@Override
	public abstract Partition loadProcess(Process proc);
}
