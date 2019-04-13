package rsa;

import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES {
	public Cipher cipher;
	Scanner sc = new Scanner(System.in);
	AES() 
	{
		try {
			cipher = Cipher.getInstance("AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void aes() {
		try {
			//generate 128 bit key
			KeyGenerator k = KeyGenerator.getInstance("AES");
			k.init(128);
			SecretKey sk = k.generateKey();
			
			//display key
			String skStr = keytoString(sk);
			
			System.out.println("Enter Plaintext to be encrypted : ");
			String pt = sc.nextLine();
			
			String encryptedPT = encrypt(pt, sk);
			String decryptedCT = decrypt(encryptedPT, sk);
			
			System.out.println("Input PlainText 	 : "+pt);
			System.out.println("Generated 128 bit key : "+skStr);
			System.out.println("Encrypted PlainText  : "+encryptedPT);
			System.out.println("Dencrypted PlainText : "+decryptedCT);
		}
		catch(Exception e) {
			System.out.println(e);
		}		
	}
	String keytoString(SecretKey sk) 
	{
		//byte encoded[] = sk.getEncoded();
		//String keyStr = Base64.getEncoder().encodeToString(encoded);
		//return keyStr;
		return Base64.getEncoder().encodeToString(sk.getEncoded());
	}
	
	String encrypt(String pt, SecretKey sk)throws Exception 
	{
		cipher.init(Cipher.ENCRYPT_MODE, sk);

		byte[] ctByte = cipher.doFinal(pt.getBytes());
		Base64.Encoder encoder = Base64.getEncoder();		
	
		return encoder.encodeToString(ctByte);		
	}
	
	String decrypt(String ct, SecretKey sk)throws Exception 
	{
		cipher.init(Cipher.DECRYPT_MODE, sk);
		
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] ptByte = cipher.doFinal(decoder.decode(ct));
		
		return new String(ptByte);	
	}
	public static void main(String args[]) {
		AES aes = new AES();
		aes.aes();
	}
}