package assembler;

import kernel.ProcessState;
import shell.Shell;

public class Operations {
	
	public static final String hlt="0000";
	public static final String mov ="0001";
	public static final String add ="0010";
	public static final String sub ="0011";
	public static final String mul ="0100";
	public static final String jmp ="0101";
	public static final String dec ="0110";
	public static final String inc ="0111";
	public static final String div ="1000";
	
	public static Register R1=new Register("R1",Constants.R1,0);
	public static Register R2=new Register("R2",Constants.R2,0);
	public static Register R3=new Register("R3",Constants.R3,0);
	public static Register R4=new Register("R4",Constants.R4,0);
	
	
	public static void mov(String reg, String val) {
		Register r=getRegister(reg);
		if(val.length()==8) { //val je vrijednost
			if(r!=null)
				r.value=Integer.parseInt(val, 2);
	}
	else if(val.length()==4) { //val je registar
		Register r2=getRegister(val);
		if(r!=null && r2!=null)
			r.value=r2.value;
	}
	}
	
	
	//Sabira vrijednost prvog i drugog registra i cuva na prvom registru
	public static void add(String reg,String val) {
		Register r=getRegister(reg);
		if(val.length()==8) { 
			if(r!=null)
				r.value=r.value+Integer.parseInt(val,2);
		}
		else if (val.length()==4) {
			Register r2=getRegister(val);
			if(r!=null && r2!=null)
				r.value=r.value + r2.value;
		}
	}
	
	
	//Oduzima vrijednosti prvog i drugog registra i cuva na prvom registru
	public static void sub(String reg,String val) {
		Register r=getRegister(reg);
		if(val.length()==8) { 
			if(r!=null)
				r.value=r.value-Integer.parseInt(val,2);
		}
		else if (val.length()==4) {
			Register r2=getRegister(val);
			if(r!=null && r2!=null)
				r.value=r.value - r2.value;
		}
	}
	
	
	//Mnozi vrijednosti prvog i drugog registra i cuva na prvom registru
		public static void mul(String reg,String val) {
			Register r=getRegister(reg);
			if(val.length()==8) { 
				if(r!=null)
					r.value=r.value*Integer.parseInt(val,2);
			}
			else if (val.length()==4) {
				Register r2=getRegister(val);
				if(r!=null && r2!=null)
					r.value=r.value * r2.value;
			}
		}
		
		
		//Dijeli vrijednost prvog registra sa vrijednoscu drugog registra i cuva na prvom registru
				public static void div(String reg,String val) {
					Register r=getRegister(reg);
					if(val.length()==8) { 
						if(r!=null && !val.equals("00000000"))
							r.value=r.value/Integer.parseInt(val,2);
					}
					else if (val.length()==4) {
						Register r2=getRegister(val);
						if(r!=null && r2!=null && r2.value!=0)
							r.value=r.value / r2.value;
					}
				}
				
				
				//Povecava vrijednost registra za 1
				public static void inc(String reg) {
					Register r=getRegister(reg);
					r.value=r.value+1;
				}
				
				
				//Smanjuje vrijednost registra za 1
				public static void dec(String reg) {
					Register r=getRegister(reg);
					r.value=r.value-1;
				}
				
				//Prekida izvrsavanje
			     public static void hlt() {
				Shell.currentlyExecuting.getPCB().setProcessState(ProcessState.DONE);
				}
				
			     public static void jmp (String adr) {
				int temp=Integer.parseInt(adr,2);
			         if(temp>=Shell.limit) {
			          Shell.currentlyExecuting.getPCB().setProcessState(ProcessState.TERMINATED);
			           System.out.println("Error with address in process: "+Shell.currentlyExecuting.getName());
			          return;
				 }
				  Shell.currentlyExecuting.setPGCounter(temp);
				 }
				
	private static Register getRegister(String address) {
		switch (address) {
		case Constants.R1:
		return R1;
		case Constants.R2:
		return R2;
		case Constants.R3:
		return R3;
		case Constants.R4:
		return R4;
		default:
			return null;
		
		}
	}
	//Vrijednosti registara postavlja na 0(brise ih)
	public static void clearReg() {
		R1.value=0;
		R2.value=0;
		R3.value=0;
		R4.value=0;
	}
	
	public static void printReg() {
		System.out.println("~~~~~~~~~~ REGISTERS ~~~~~~~~~~");
		System.out.println("R1 value: ["+R1.value+"]");
		System.out.println("R2 value: ["+R2.value+"]");
		System.out.println("R3 value: ["+R3.value+"]");
		System.out.println("R4 value: ["+R4.value+"]");
	}
	
	
	
	


	

}
