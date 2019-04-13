import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class rsaC 
{

	public static void main(String[] args) 
	{
		int port=8088;
		String server= "localhost";
		Scanner sc= new Scanner(System.in);
		
		try 
		{
			//Establish connection
			Socket socket = new Socket(server, port);
			System.out.println("connected");
			
			//get prime no.s
			System.out.println("Enter 2 prime no.s: ");
			int p=sc.nextInt();
			int q=sc.nextInt();
			
			//compute n, phi, e, 
			int n =p*q;
			int f= (p-1)*(q-1);
			int e;
			
			//public key
			for(e=2;e<f;e++)
			{
				if(gcd(e,f)==1)
				{
					break;
				}
			}
			System.out.println("Public key (e, n) is ("+e+", "+n+").");
			
			//private key
			int x,d = 0;
			for(int i=0;i<=9;i++)
			{
				x= 1+ (i*f);
				
				if(x%e ==0)
				{
					d= x/e;
					break;
				}
			}
			System.out.println("\nPrivate key (d, n) is ("+d+", "+n+").");
			
			//send e,n to server
			OutputStream os = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(os);
			
			String E= Integer.toString(e);
			String Nstr= Integer.toString(n);
			
			out.writeUTF(E); 	//send e
			out.writeUTF(Nstr);  	//send n
			
			///accept ecrypted msg
			DataInputStream in = new DataInputStream(socket.getInputStream());
			Double CT =Double.parseDouble(in.readUTF());
			System.out.println("Encrypted msg recieved is: "+ CT);
			
			//convert n,c to BigInt
			BigInteger N= BigInteger.valueOf(n);
			BigInteger C = BigDecimal.valueOf(CT).toBigInteger();
			
			//decrypt
			BigInteger PT = (C.pow(d)).mod(N);
			System.out.println("Decrypted msg is: "+ PT);

		} 

		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static int gcd(int e, int f)
	{
		if(e==0)
			return f;
		else
			return gcd(f%e, e);
	}

}
