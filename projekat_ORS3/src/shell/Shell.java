package shell;

import asembler.Constants;
import asembler.Operations;
import fileSystem.FileSystem;
import memory.Disk;
import memory.Memory;
import kernel.Process;

public class Shell {
  public static FileSystem tree;
	public static Memory memory;
	public static Disk disk;
	public static Process currentlyExecuting = null;
	public static int PG; 
	public static String IR;
	public static int base;
	public static int limit;

  public static void executeMachineInstruction() {
		String operation = IR.substring(0,4);
		boolean programCounterChanged = false;
		
		if(operation.equals(Operations.hlt)) {
			Operations.hlt();
		}
		else if(operation.equals(Operations.mov)) {
			String r1 = IR.substring(4,8);
			String r2 = IR.substring(8,12);
			Operations.mov(r1,r2);
		}
		else if(operation.equals(Operations.add)) {
			String r1 = IR.substring(4,8);
			if( IR.length() == 12) {
				String r2 = IR.substring(8,12);
				Operations.add(r1,r2);
			}
			else if( IR.length() == 16) {
				String val2 = IR.substring(8,16);
				Operations.add(r1,val2);
			}
		}
		else if(operation.equals(Operations.sub)) {
			String r1 = IR.substring(4,8);
			if( IR.length() == 12) {
				String r2 = IR.substring(8,12);
				Operations.sub(r1,r2);
			}
			else if( IR.length() == 16) {
				String val2 = IR.substring(8,16);
				Operations.sub(r1,val2);
			}
		}
		else if(operation.equals(Operations.mul)) {
			String r1 = IR.substring(4,8);
			if( IR.length() == 12) {
				String r2 = IR.substring(8,12);
				Operations.mul(r1,r2);
			}
			else if( IR.length() == 16) {
				String val2 = IR.substring(8,16);
				Operations.mul(r1,val2);
			}
		}
		else if(operation.equals(Operations.div)) {
			String r1 = IR.substring(4,8);
			if( IR.length() == 12) {
				String r2 = IR.substring(8,12);
				Operations.div(r1,r2);
			}
			else if( IR.length() == 16) {
				String val2 = IR.substring(8,16);
				Operations.div(r1,val2);
			}
		}
		else if(operation.equals(Operations.jmp)) {
			String adr = IR.substring(4,12);
			Operations.jmp(adr);
		}
		else if(operation.equals(Operations.inc)) {
			String reg = IR.substring(4,8);
			Operations.inc(reg);
		}
		else if(operation.equals(Operations.dec)) {
			String reg = IR.substring(4,8);
			Operations.dec(reg);
		}
		if (!programCounterChanged)
			PG++;
	}

  public static String assemblerToMachineInstruction(String line) {
		String instruction = "";
		String []command = line.split("[ |, ]");
		
		switch(command[0]) {
		case "HLT":
			instruction += Operations.hlt;
			break;
		case "MOV":
			instruction += Operations.mov;
			break;
		case "ADD":
			instruction += Operations.add;
			break;
		case "SUB":
			instruction += Operations.sub;
			break;
		case "MUL":
			instruction += Operations.mul;
			break;
		case "JMP":
			instruction += Operations.jmp;
			break;
		case "DEC":
			instruction += Operations.dec;
			break;
		case "INC":
			instruction += Operations.inc;
			break;
		case "DIV":
			instruction += Operations.div;
			break;
		}
		
		if(command[0].equals("HLT")) {
			return instruction;
		}
		else if(command[0].equals("JMP")) {
			instruction += toBinary(command[1]);
			return instruction;
			
		}
		else if(command[0].equals("INC") || command[0].equals("DEC")) {
			switch(command[1]) {
			case "R1":
				instruction += Constants.R1;
				break;
			case "R2":
				instruction += Constants.R2;
				break;
			case "R3":
				instruction += Constants.R3;
				break;
			case "R4":
				instruction += Constants.R4;
				break;
			}
			return instruction;
		}
		
		else if(command[2].equals("R1") || command[2].equals("R2") || 
				command[2].equals("R3") || command[2].equals("R4")) {
			switch(command[1]) {
			case "R1":
				instruction += Constants.R1;
				break;
			case "R2":
				instruction += Constants.R2;
				break;
			case "R3":
				instruction += Constants.R3;
				break;
			case "R4":
				instruction += Constants.R4;
				break;	
			}
			switch(command[2]) {
			case "R1":
				instruction += Constants.R1;
				break;
			case "R2":
				instruction += Constants.R2;
				break;
			case "R3":
				instruction += Constants.R3;
				break;
			case "R4":
				instruction += Constants.R4;
				break;	
			}
			return instruction;
		}
		else {
			switch(command[1]) {
			case "R1":
				instruction += Constants.R1;
				break;
			case "R2":
				instruction += Constants.R2;
				break;
			case "R3":
				instruction += Constants.R3;
				break;
			case "R4":
				instruction += Constants.R4;
				break;	
			}
			instruction += toBinary(command[2]);
			return instruction;
		}	
	}

  private static String toBinary(String s) {
		int number = Integer.parseInt(s);
		int []binary = new int[10];
		int index = 0;
		int counter = 0;
		while(number > 0) {
			binary[index] = number % 2;
			index++;
			number = number / 2;
			counter++;
		}
		String bin = "";
		counter = 8 - counter;
		for(int i = 0 ; i < counter; i++)
			bin += "0";
		for(int i = index - 1; i >= 0; i--)
			bin += binary[i];
		return bin;
	}

  public static String fromIntToInstruction(int val) {
		String inst = Integer.toBinaryString(val);
		if( inst == "0")
			inst = "0000";
		else if( inst.length() == 0 )
			return inst;
		else if( inst.length() <= 12 ) {
			while ( inst.length() < 12)
				inst = "0" + inst;
		}
		else if( inst.length() <= 16 ) {
			while ( inst.length() < 16 )
				inst = "0" + inst;
		}
		else if ( inst.length() <= 20 ) {
			while ( inst.length() < 20)
				inst = "0" + inst;
		} 
		else if (inst.length() <= 24) {
			while ( inst.length() < 24)
				inst = "0" + inst;
		}
		return inst;
	}

  public static void saveValues() {
		int [] registers = {Operations.R1.value, Operations.R2.value, Operations.R3.value, Operations.R4.value};
		currentlyExecuting.setValuesOfRegisters(registers);
		currentlyExecuting.setPCValues(PG);
	}
  
  public static void loadValues() {
		int []registers = currentlyExecuting.getValuesOfRegisters();
		Operations.R1.value = registers[0];
		Operations.R2.value = registers[1];
		Operations.R3.value = registers[2];
		Operations.R4.value = registers[3];
		PG = currentlyExecuting.getPCValue();
	}
}
