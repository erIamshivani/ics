import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class rsaS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int port =8088;
		try 
		{
			ServerSocket ss = new ServerSocket(port);
			Socket socket= ss.accept();
			System.out.println("Connected!");
			Scanner sc= new Scanner(System.in);
			//accept e, n,
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			int e = Integer.parseInt(in.readUTF());
			int n = Integer.parseInt(in.readUTF());
			
			//accept msg and encrypt it
			System.out.println("Enter msg to be encrypted: ");
			int msg = sc.nextInt();
			
			double CT = (Math.pow(msg,e)) % (n);
			System.out.println("Encrypted msg is : "+ CT);
			
			String c = Double.toString(CT);
			
			
			//send ct to client in string format
			OutputStream os = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(os);
			
			out.writeUTF(c);
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
