package memory;

public class NextFit extends DynamicPartitioning {
	private static int indexLast = 0; 
	
	public NextFit() {
		super();
	}
	
	@Override
	public Partition loadProcess(Process proc)	{
		int suitableIndex = -1;
	
		ArrayList<Partition> suitablePartitions = getFreePartitions(proc);
		for(Partition part: suitablePartitions)
			if(Memory.getPartitions().indexOf(part) > indexLast) {
				suitableIndex = Memory.getPartitions().indexOf(part);
				indexLast = suitableIndex;
				break;
			}
		
		if(suitableIndex != -1) {
			proc.load(Memory.getPartitions().get(suitableIndex));
			return Memory.getPartitions().get(suitableIndex);
		}
		
		if(suitablePartitions.size() > 0) {
			indexLast = Memory.getPartitions().indexOf(suitablePartitions.get(0));
			proc.load(Memory.getPartitions().get(indexLast));
			return Memory.getPartitions().get(indexLast);
		}
		
		suitablePartitions = getSuitablePartitions(proc);
		for(Partition part: suitablePartitions)
			if(Memory.getPartitions().indexOf(part) > indexLast) {
				suitableIndex = Memory.getPartitions().indexOf(part);
				indexLast = suitableIndex;
				break;
			}

		if(suitableIndex != -1) {
			proc.load(Memory.getPartitions().get(suitableIndex));
			return Memory.getPartitions().get(suitableIndex);
		}
		
		if(suitablePartitions.size() > 0) {
			indexLast = Memory.getPartitions().indexOf(suitablePartitions.get(0));
			proc.load(Memory.getPartitions().get(indexLast));
			return Memory.getPartitions().get(indexLast);
		}
		
		return null;
	}
}
