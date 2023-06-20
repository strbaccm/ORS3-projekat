package shell;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import assembler.Operations;
import fileSystem.FileSystem;
import kernel.Process;
import kernel.ProcessScheduler;
import memory.Disk;
import memory.Memory;
import memory.RAM;

public class Commands {
	private static String command;

	public static void getCommand() {
		command = command.toLowerCase();
		String cp[] = command.split(" ");
		String c = cp[0];
		
		if (c.equals("dir")) {
			if (cp.length == 1) 
				FileSystem.listFiles();
			else
				System.out.println("Incorrect parameters!\n");	
			}
		
		else if (c.equals("cd")) {
			if (cp.length == 2) {
				String p = cp[1];
				FileSystem.changeDirectory(p);
			}
			else
				System.out.println("Incorrect parameters!\n");	
			}
		
		else if (c.equals("mkdir")) {
			if (cp.length == 2) {
				String p = cp[1];
				FileSystem.makeDirectory(p);
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("deldir")) {
			if (cp.length == 2) {
				String p = cp[1];
				FileSystem.deleteDirectory(p);
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("delf")) {
			if (cp.length == 2) {
				String p = cp[1];
				FileSystem.deleteFile(p);
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("ren")) {
			if (cp.length == 3) {
				String p1 = cp[1];
				String p2 = cp[2];
				FileSystem.renameDirectory(p1, p2);
			}
			else
				System.out.println("Incorrect parameters!\n");	
			}
		
		else if (c.equals("mem")) {
			if (cp.length == 1) 
				Memory.printMemory();
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("memram")) {
			if (cp.length == 1) 
				RAM.printRAM();
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("memreg")) {
			if (cp.length == 1) 
				Operations.printReg();
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("memdisk")) {
			if (cp.length == 1) 
				Disk.printDisk();
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("load")) {
			if (cp.length == 2) {
				String p = cp[1];
				new Process(p);
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("execute")) {
			new ProcessScheduler().start();
		}
		
		else if (c.equals("proc")) {
			if (cp.length == 1) 
				ProcessScheduler.printProcesses();
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("term")) {
			if (cp.length == 2) {
				String p = cp[1];
				ProcessScheduler.terminateProcess(Integer.parseInt(p));
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("block")) {
			if (cp.length == 2) {
				String p = cp[1];
				ProcessScheduler.blockProcess(Integer.parseInt(p));
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("unblock")) {
			if (cp.length == 2) {
				String p = cp[1];
				ProcessScheduler.unblockProcess(Integer.parseInt(p));
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("date")) {
			if (cp.length == 1) 
				System.out.println(java.time.LocalDate.now());    
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("time")) {
			if (cp.length == 1) 
				System.out.println(java.time.LocalTime.now()); 
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("cls")) {
			if (cp.length == 1) 
				GUI.clear();
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("exit")) {
			if (cp.length == 1) 
				System.exit(1);
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else if (c.equals("help")) {
			if (cp.length == 1) {
				String help = "";
				help += "DIR\t\tLists files and subdirectories in directory.\n";
				help += "CD\t\tChanges directories.\n";
				help += "MKDIR\t\tCreates a directory.\n";
				help += "DELDIR\t\tDeletes directories.\n";
				help += "DELF\t\tDeletes files.\n";
				help += "REN\t\tRenames directories.\n";
				help += "MEM\t\tShows RAM, registers and disk.\n";
				help += "MEMRAM\t\tShows RAM.\n";
				help += "MEMREG\t\tShows registers.\n";
				help += "MEMDISK\t\tShows disk.\n";
				help += "LOAD\t\tLoads process.\n";
				help += "EXECUTE\t\tStarts executing processes.\n";
				help += "PROC\t\tLists processes.\n";
				help += "TERM\t\tTerminates process.\n";
				help += "BLOCK\t\tBlocks process.\n";
				help += "UBLOCK\t\tUnblocks process.\n";
				help += "DATE\t\tDisplays date.\n";
				help += "TIME\t\tDisplays time.\n";
				help += "CLS\t\tClears terminal.\n";
				help += "EXIT\t\tCloses program.";
				System.out.println(help);
			}
			else
				System.out.println("Incorrect parameters!\n");		
			}
		
		else
			System.out.println("Wrong entry!");
	}

	public static void readCommand(PipedInputStream inp, int length) {
		command = "";
		char c;
		for (int i = 0; i < length; i++) {
			try {
				c = (char) inp.read();
				command += c;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error occurred while reading the command!\n");
			}
		}
	}

	public static void setOut(OutputStream out) {
		System.setOut(new PrintStream(out, true));
	}
}
