package memory;

public class BlockFile {
	private Block block;
	private BlockFile next;
	
	public BlockFile(Block block) {
		this.block = block;
		this.next = null;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public BlockFile getNext() {
		return next;
	}
	
	public void setNext(BlockFile next) {
		this.next = next;
	}
	
	public Block getNextBlock() {
		return next.getBlock();
	}

	public String toString() {
		return this.getBlock().toString();
	}
}
