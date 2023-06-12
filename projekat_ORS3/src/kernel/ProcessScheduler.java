package kernel;

import java.util.ArrayList;

import memory.Memory;
import memory.Partition;

public interface ProcessScheduler {
		
		public Partition loadProcess(Process proc);
		public void execute();
		
		default public ArrayList<Partition> getFreePartitions(Process proc){
			ArrayList<Partition> freePartitions = new ArrayList<>();
			for(Partition par : Memory.getPartitions())
				if(par.getProcess() == null && par.getSize() >= proc.getSize())
					freePartitions.add(par);
			return freePartitions;			
		}
		
		default public ArrayList<Partition> getSuitablePartitions(Process proc){
			ArrayList<Partition> suitablePartitions = new ArrayList<>();
			for(Partition par : Memory.getPartitions())
				if(par.getSize() >= proc.getSize())
					suitablePartitions.add(par);
			return suitablePartitions;
		}
		
		default public ArrayList<Partition> getOccupiedPartitons() {
			ArrayList<Partition> occupiedPartitions = new ArrayList<>();
			for(Partition par : Memory.getPartitions())
				if(par.getProcess() != null)
					occupiedPartitions.add(par);
			return occupiedPartitions;
		}
		
		
		
		

}
