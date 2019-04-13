import java.util.Scanner;

public class Sdes 
{
	static int[] k1 = new int[8];
	static int[] k2 = new int[8];
	static int[] L;
	static int[] R;
	static int[] LR;
	static Scanner scan = new Scanner(System.in);

	static int[] P4 = {2, 4, 3, 1};
	static int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
	static int[] P10 = {3,5,2,7,4,10,1,9,8,6};
	static int[] P8 = {6,3,7,4,8,5,10,9};
	static int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
	static int[] IPInv = {4, 1, 3, 5, 7, 2, 8, 6};

	static int[][] Sbox1 = {{ 1, 0, 3, 2},{ 3, 2, 1, 0},{ 0, 2, 1, 3},{ 3, 1, 3, 2}};
	static int[][] Sbox2 = {{ 0, 1, 2, 3},{ 2, 0, 1, 3},{ 3, 0, 1, 0},{ 2, 1, 0, 3}};
	
//------------------------------- MAIN FUNCTION ------------------------- 
	
	public static void main(String args[])
	{
		int[] key = new int[10];
		int[] pt = new int[8];
		int[] ct = new int[8];
		int[] dt = new int[8];
		
		System.out.println("Welcome to SDES !!!!");
		System.out.println("\n Enter 10-bit key:");
		key = accept(10);
		System.out.println("Entered 10-bit key:");
		display(key);
		
		System.out.println("\n Enter 8-bit plaintext:");
		pt = accept(8);
		System.out.println("Entered 8-bit plaintext:");
		display(pt);
		
		System.out.println("\nGeneration of subkey..");
		generateKey(key);
		System.out.print("Subkey K1: "); 
		display(k1);
		System.out.print("Subkey K2: "); 
		display(k2);
		System.out.println();
		
		ct = encrypt(pt);
		System.out.print("Encrypted Text: ");
		display(ct);
		
		dt = decrypt(ct);
		System.out.print("Decrypted Text: ");
		display(dt);
	}

//--------------------------- ACCEPT & DISPLAY ----------------------------------
	
	private static void display(int[] arr) {
		for(int i=0;i<arr.length;i++)
			System.out.print(arr[i]+" ");
		System.out.println();
	}

	private static int[] accept(int n) {
		int[] arr = new int[n];
		for(int i=0;i<n;i++)
			arr[i] = scan.nextInt();
		return arr;
	}
	
//---------------------------- GENERATE SUBKEY -------------------------------------

	private static void generateKey(int[] key) 
	{
		/*
		 	1. Permute 10bit
		 	2. divide in half and LS by 1 and join
		 	3. k1 = permute 8 bit
		 	4. LS 2. by 2 bit each half and join
		 	5. k2 = permute 8 bit
		 */
		int[] ap10 = permute(P10,key);
		
		half(ap10,5);
		L = rls(L,1);
		R = rls(R,1);
		join(L,R,5);
		
		k1 = permute(P8,LR);
		
		half(LR,5);		
		L = rls(L,2);
		R = rls(R,2);
		join(L,R,5);
		
		k2 = permute(P8,LR);
	}
	
//----------------------------- ENCRYPTION -----------------------------
	
	private static int[] encrypt(int[] ptext) 
	{
		/*
		 * 1) IP
		 * 2) fk1
		 * 3) Switch
		 * 4) fk2
		 * 5) IPInv
		 */
		int[] ip = permute(IP,ptext);		
		
		int[] fk1 = FK(ip,k1);
		
		swap(fk1); // output in LR;
		
		int[] fk2 = FK(LR,k2);	
		
		int[] ctxt = permute(IPInv,fk2);		
		
		return ctxt;
	}


//---------------------------- DECRYPTION -------------------------------

	private static int[] decrypt(int[] ctext) 
	{
		/*
		 * 1) IP
		 * 2) fk2
		 * 3) Switch
		 * 4) fk1
		 * 5) IPInv
		 */
		int[] ip = permute(IP,ctext);	
		
		int[] fk2 = FK(ip,k2);
		
		swap(fk2); // output in LR;
		
		int[] fk1 = FK(LR,k1);	
		
		int[] dtxt = permute(IPInv,fk1);		

		return dtxt;
	}

//-------------------------- REQUIRED FUNCTIONS ---------------------------
	
	private static int[] permute(int[] eq, int[] inp) {
		int[] per = new int[eq.length];
		for(int i=0;i<eq.length;i++)
			per[i] = inp[eq[i]-1];
		return per;
	}
	
	private static void half(int[] inp, int n) 
	{
		L = new int[n];
		R = new int[n];
		int j=n;
		for(int i=0;i<n;i++)
		{
			L[i] = inp[i];
			R[i] = inp[j++];
		}
	}
	
	private static int[] rls(int[] inp, int n) {
		int cnt=0;
		int[] shift = new int[inp.length];
		for(int i=0+n;i<inp.length;i++)
		{
			if(cnt == inp.length)
				break;
			shift[cnt++]=inp[i];
			if(i == inp.length-1)
				i=-1;
		}
		return shift;
	}
	
	private static void join(int[] l, int[] r, int n) 
	{
		LR = new int[n+n];
		for(int i=0;i<n;i++)
		{
			LR[i] = l[i];
			LR[n+i] = r[i];
		}
	}

	private static int[] FK(int[] inp, int[] subkey) 
	{
		/*
		   1) Divide into L and R
		   2) Compute Fk = [ (L XOR F(R,SK)) , R]
		   		2.1) Compute F(R,SK) first
		  			2.1.1) Expand R into 8 bits
		  			2.1.2) XOR with subkey
		  			2.1.3) Calculate R0,C0,R1,C1
		  			2.1.4) Generate 4 bits using sbox
		  			2.1.5) Permute 4 bits
		   		2.2) XOR with L the output of 2.1.5
		   		2.34) Append R to the output of 2.2
		*/
		half(inp,4); // L contains 1st 4 bits, R contains last 4 bits 
		
		int[] ep = permute(EP,R);	
		int[] xor = XOR(ep,subkey);
		int R0 = decimal(xor[0],xor[3]);
		int C0 = decimal(xor[1],xor[2]);
		int R1 = decimal(xor[4],xor[7]);
		int C1 = decimal(xor[5],xor[6]);
		Sbox(R0,C0,R1,C1); // final result in LR
		int[] ap4 = permute(P4,LR);

		int[] op = XOR(ap4,L);
		
		join(op,R,4);
		
		return LR;
	}

	private static int[] XOR(int[] inp, int[] sk) 
	{
		int[] doxor = new int[inp.length];
		for(int i=0;i<inp.length;i++)
		{
			if(inp[i] == sk[i])
				doxor[i] = 0;
			else
				doxor[i] = 1;
		}		
		return doxor;
	}
	
	private static int decimal(int i, int j) 
	{
		int r=0;
		if(i == 1)
			r = (int) Math.pow(2, 1);
		if(j == 1)
			r = r+1;
		return r;
	}
	
	private static void Sbox(int r0, int c0, int r1, int c1) 
	{
		int first = Sbox1[r0][c0];
		int second = Sbox2[r1][c1];
		int[] fbin = binary(first);
		int[] sbin = binary(second);
		join(fbin,sbin,2);
	}

	
	private static int[] binary(int i) {
		int[] conv = new int[4];
		if(i == 0)
		{
			conv[0] = 0;
			conv[1] = 0;
		}
		else if(i == 1)
		{
			conv[0] = 0;
			conv[1] = 1;
		}
		else if(i == 2)
		{
			conv[0] = 1;
			conv[1] = 0;
		}
		else 
		{
			conv[0] = 1;
			conv[1] = 1;
		}
		return conv;
	}
	
	private static void swap(int[] fk1) 
	{
		half(fk1,4);
		for(int i=0;i<4;i++)
		{
			LR[i] = R[i];
			LR[4+i] = L[i];
		}
	}
}
