import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class difC {

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);

		///connect
		try {
			Socket socket= new Socket("localhost", 8088);
			System.out.println("Connected!");
			
			//accept p,q,a
			System.out.println("Enter p, q: ");
			int p = sc.nextInt();
			int q = sc.nextInt();
			
			System.out.println("Enter large random no. 'a': ");
			int a = sc.nextInt();
			
			//calc R using p,q,a
			double R = (Math.pow(q, a)) % p;
			System.out.println("Calc R:"+R);
			
			//send p,q,R
			String ps = Integer.toString(p);
			String qs = Integer.toString(q);
			String Rs = Double.toString(R);
			
			OutputStream os = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(os);
			
			out.writeUTF(ps);
			out.writeUTF(qs);
			out.writeUTF(Rs);
			
			//accept S
			DataInputStream in= new DataInputStream(socket.getInputStream());
			double S = Double.parseDouble(in.readUTF());
			System.out.println("Recieved S:"+S);
			
			//calc rk = S ^a mod p
			double Rk = (Math.pow(S, a)) % p;
			System.out.println("Calc Rk:"+ Rk);
			
			//send rk
			String Rkstr= Double.toString(Rk);
			out.writeUTF(Rkstr);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
	}

}
