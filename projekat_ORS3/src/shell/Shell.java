package shell;

import java.io.File;
import assembler.Constants;
import assembler.Operations;
import fileSystem.FileSystem;
import memory.Disk;
import memory.Memory;
import kernel.Process;
import kernel.ProcessScheduler;

public class Shell {
  public static FileSystem tree;
	public static Memory memory;
	public static Disk disk;
	public static Process currentlyExecuting = null; 
	public static String IR;
	public static int base;
	public static int limit;
	
public static void booting() {
		new ProcessScheduler();
		memory = new Memory();
		disk = new Disk();
		tree = new FileSystem(new File("PROGRAMS"));
	}

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
		        programCounterChanged = true;
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
		  currentlyExecuting.incpgCOUNTER();
	}

  public static String assemblerToMachineInstruction(String line) {
		String instruction = "";
		String []command = line.split("[ |, ]");
		
		//PREVOD U OPERACIJU
		if (command[0].equals("HLT")) {
			instruction += Operations.hlt;
		}
		else if(command[0].equals("MOV")) {
			instruction += Operations.mov;
		}
		else if(command[0].equals("ADD")) {
			instruction += Operations.add;
		}
		else if(command[0].equals("SUB")) {
			instruction += Operations.sub;
		}
		else if(command[0].equals("MUL")) {
			instruction += Operations.mul;
		}
		else if(command[0].equals("JMP")) {
			instruction += Operations.jmp;
		}
		else if(command[0].equals("DEC")) {
			instruction += Operations.dec;
		}
		else if(command[0].equals("INC")) {
			instruction += Operations.inc;
		}
		else if(command[0].equals("DIV")) {
			instruction += Operations.div;
		}
		
		// OBRADA UKOLIKO NAM JE OPERACIJA HLT
		if(command[0].equals("HLT")) {
			return instruction;
		}
		// OBRADA UKOLIKO NAM JE OPERACIJA JMP
		else if(command[0].equals("JMP")) {
			instruction += toBinary(command[1]);
			return instruction;
		}
		//SLUCAJ KADA SU NA PRVOJ POZICIJI OPERACIJE DEC I INC
		else if(command[0].equals("INC") || command[0].equals("DEC")) {
			
			// OBRADA REGISTRA
			if(command[1].equals("R1")) {
				instruction += Constants.R1;
			}
			else if(command[1].equals("R2")) {
				instruction += Constants.R2;
			}
			else if(command[1].equals("R2")) {
				instruction += Constants.R2;
			}
			else if(command[1].equals("R2")) {
				instruction += Constants.R2;
			}
			return instruction;
		}
		
		//PROVJERA AKO NA OBJE POZICIJE IMAMO REGISTRE
		else if(command[2].equals("R1") || command[2].equals("R2") || 
				command[2].equals("R3") || command[2].equals("R4")) {
			
			if(command[1].equals("R1")) {
				instruction += Constants.R1;
			}
			else if(command[1].equals("R2")) {
				instruction += Constants.R2;
			}
			else if(command[1].equals("R3")) {
				instruction += Constants.R3;
			}
			else if(command[1].equals("R4")) {
				instruction += Constants.R4;
			}
			
			if(command[2].equals("R1")) {
				instruction += Constants.R1;
			}
			else if(command[2].equals("R2")) {
				instruction += Constants.R2;
			}
			else if(command[2].equals("R3")) {
				instruction += Constants.R3;
			}
			else if(command[2].equals("R4")) {
				instruction += Constants.R4;
			}
			return instruction;
		}
		//OBRADA U SLUCAJU DA NAM SE NA PRVOM MJESTU NALAZI REGISTAR A NA DRUGOME VRIJEDNOST
		else {
			if(command[1].equals("R1")) {
				instruction += Constants.R1;
			}
			else if(command[1].equals("R2")) {
				instruction += Constants.R2;
			}
			else if(command[1].equals("R3")) {
				instruction += Constants.R3;
			}
			else if(command[1].equals("R4")) {
				instruction += Constants.R4;
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
		return inst;
	}

  public static void saveValues() {
		int [] registers = {Operations.R1.value, Operations.R2.value, Operations.R3.value, Operations.R4.value};
		currentlyExecuting.setValuesOfRegisters(registers);
	}
  
  public static void loadValues() {
		int []registers = currentlyExecuting.getValuesOfRegisters();
		Operations.R1.value = registers[0];
		Operations.R2.value = registers[1];
		Operations.R3.value = registers[2];
		Operations.R4.value = registers[3];
	}
}
