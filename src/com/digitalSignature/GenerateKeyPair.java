package com.digitalSignature;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class GenerateKeyPair {

	public static void main(String[] args) {
		FileOutputStream fileOut; // 输出密钥到文件
		byte[] pubkey; // 存储已编码的公钥字节
		byte[] privKey; // 存储已编码的私钥字节

		if (args.length != 2) {
			System.out.println("Usage:GenerateKeyPair <directory> ");
			System.out.println("Option:");
			System.out
					.println("<directory>: The absolute path of the directory the file will be create");
			System.out
					.println("<keyname>：The keyname used to generate files to store key");
			System.exit(0);
		}

		System.out.println("Generating key pair...");
		try {
			// 生成公钥和私钥
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
			keyGen.initialize(1024, new SecureRandom());
			KeyPair pair = keyGen.generateKeyPair();
			PublicKey pub = pair.getPublic();
			PrivateKey priv = pair.getPrivate();
			pubkey = pub.getEncoded();
			privKey = priv.getEncoded();

			// 将公钥和私钥写入文件中
			fileOut = new FileOutputStream(args[0] + "/pubkey_" + args[1] + ".key");
			fileOut.write(pubkey);
			fileOut.flush();
			
			fileOut.close();
			
			fileOut = new FileOutputStream(args[0] + "/prikey_" + args[1] + ".key");
			fileOut.write(privKey);
			fileOut.flush();
			fileOut.close();
			System.out.println("OK!");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("生成密钥失败！");
		} catch (FileNotFoundException e) {
			System.out.println("输入的文件路径不对！");
		} catch (IOException e) {
			System.out.println("写入文件失败!");
		}

	}

}
