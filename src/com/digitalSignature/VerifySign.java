package com.digitalSignature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class VerifySign {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out
					.println("Usage:VerifySign <PublicKey> <dataFile> <SignatureFile>");
			System.out.println("<Public Key >The filename of the public key.");
			System.out.println("<DataFile>The filename that want to signature");
			System.out
					.println("<SignatureFile>:The file name containing signature data.");
			System.exit(0);
		}

		// 从<PublicKey>文件中读取公钥
		FileInputStream fileIn = null;
		byte[] encodepubKey = null;
		try {
			fileIn = new FileInputStream(args[0]);
			encodepubKey = new byte[fileIn.available()];
			fileIn.read(encodepubKey);
		} catch (FileNotFoundException e) {
			System.out.println("The Public Key File does not exist");
		} catch (IOException e) {
			System.out.println("Read Public Key File Error");
		} finally {
			if (fileIn != null) {
				try {
					fileIn.close();
				} catch (IOException e) {
					fileIn = null;
				}
			}
		}

		// 如果公钥没有没有读取出来，那以，提示并退出程序
		if (encodepubKey == null) {
			System.out.println("Read Public Key File failed");
			System.exit(0);
		}

		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodepubKey);
		FileInputStream sigStream = null;
		FileInputStream sigFileStream = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
			
			//从签名文件中获取文件的签名
			sigStream = new FileInputStream(args[2]);
			byte[] signature = new byte[sigStream.available()];
			sigStream.read(signature);
			Signature sigObj = Signature.getInstance("SHA/DSA");
			sigObj.initVerify(pubKey);
			
			//获取要加密的文件
			sigFileStream = new FileInputStream(args[1]);
			byte b;
			while (sigFileStream.available() != 0){
				b = (byte) sigFileStream.read();
				sigObj.update(b);
			}
			
			//验证是否一致
			boolean verifies = sigObj.verify(signature);
			System.out.println("Signature verifies:" + verifies);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("no DSA algorithm");
		} catch (InvalidKeySpecException e) {
			System.out.println("Invalid Key");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} finally{
			if (sigStream != null) {
				try {
					sigStream.close();
				} catch (IOException e) {
					sigStream = null;
				}
			}
			if (sigFileStream != null) {
				try {
					sigFileStream.close();
				} catch (IOException e) {
					sigFileStream = null;
				}
			}
		}
	}

}
