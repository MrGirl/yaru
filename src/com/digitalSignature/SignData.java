package com.digitalSignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignData {

	public static void main(String[] args) {
		FileOutputStream fileOut = null;
		byte b;
		if (args.length != 3) {
			System.out.println("Usage:SignData <PrivateKey> <DataFile> <destination>");
			System.out.println("Option:");
			System.out
					.println("<PrivateKey>:The file name of the private key.");
			System.out.println("<DataFile>:The filename that want to sign.");
			System.out.println("<destination>:The file name that will be signed");
			System.exit(0);
		}

		System.out.println("Generating digital signature...");

		FileInputStream fis = null;
		byte[] encodedPrivateKey = null;

		// 从文件中获取私钥
		try {
			fis = new FileInputStream(args[0]);
			encodedPrivateKey = new byte[fis.available()];
			fis.read(encodedPrivateKey);
		} catch (FileNotFoundException e) {
			System.out.println("The PrivateKey File dose not exist");
		} catch (IOException e) {
			System.out.println("The PrivateKey file breaked");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					fis = null;
				}
			}
		}
		
		if (encodedPrivateKey == null){
			System.out.println("Failed to get the Private Key");
			System.exit(0);
		}

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		FileInputStream fiss = null;
		try {
			KeyFactory keyF = KeyFactory.getInstance("DSA");
			PrivateKey privateKey = keyF.generatePrivate(privateKeySpec);
			Signature dsa = Signature.getInstance("SHA/DSA");
			dsa.initSign(privateKey);
			//获取要加指纹的文件进行加指纹
			fiss = new FileInputStream(new File(args[1]));
			while(fiss.available() != 0){
				b = (byte)fiss.read();
				dsa.update(b);
			}
			
			byte[] signature = dsa.sign();
			
			//将生成的指纹存储到文件中
			fileOut = new FileOutputStream(args[2]);
			fileOut.write(signature);
			fileOut.flush();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("no DSA algorithm");
		} catch (InvalidKeySpecException e) {
			System.out.println("Invalid Key");
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Key");
		} catch (FileNotFoundException e) {
			System.out.println("Date file not exist");
		} catch (IOException e) {
			System.out.println("Failed to read the Data File");
		} catch (SignatureException e) {
			System.out.println("Filed to sign the file");
		} finally{
			if (fiss != null) {
				try {
					fiss.close();
				} catch (IOException e) {
					fiss = null;
				}
			}
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					fileOut = null;
				}
			}
		}
	}

}
