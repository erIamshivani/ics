import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class difS {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		//connect
		try {
			ServerSocket ss = new ServerSocket(8088);
			Socket socket = ss.accept();
			System.out.println("Connected!");
			
			//accept p,q,R
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			int p = Integer.parseInt(in.readUTF());
			int q = Integer.parseInt(in.readUTF());
			double R = Double.parseDouble(in.readUTF());
								
			System.out.println("Recieved:\n p:"+p+"\n q:"+q+"\n R:"+R);

			//accept b from user
			System.out.println("Enter large integer 'b': ");
			int b = sc.nextInt();
			
			//calc S using p,q,b
			double S= (Math.pow(q, b)) % p;
			
			System.out.println("Calc S: "+S);
			
			//send S
			OutputStream os = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(os);
			
			String Sstr =Double.toString(S);	
			out.writeUTF(Sstr);
			
			//calc sk= R^b mod p
			double Sk = (Math.pow(R, b)) % p;
			System.out.println("Calc Sk: "+Sk);
			
			//accept rk 
			double Rk = Double.parseDouble(in.readUTF());
			
			//compare sk and rk
			if(Sk == Rk)
			{
				System.out.println("Agreed on future communication.");
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}

}
