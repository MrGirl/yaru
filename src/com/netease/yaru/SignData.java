/**
 * 
 */
package com.netease.yaru;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import com.netease.yaru.utils.FileUtils;

/**
 * @author Yaru
 * @since 2015/5/14
 * 
 * 
 * 
 * 
 */
public class SignData {
	
	public static boolean checkInput(String line ,int num){
		if(line==null || line.trim().length() == 0){
			System.err.println("Wrong enter!Please contact Yaru!");
			return false;
		}
		
		String fields[] = line.split("[ ]+");
		if(fields.length == num)
			return true;
		else{
			System.err.println("Wrong enter!U must enter "+num+" parameters.");
			return false;
		}
	}
	
	public static boolean checkNum(String num){
		try{
			 Integer.parseInt(num);
			return true;
		} catch(Exception e){
			return false;
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String line = "";
		while (!line.equalsIgnoreCase("exit")) {
			// **************************提示用户选择算法**********************************************
			promptChoseAttri();

			// create the input stream to listen cmd input
			BufferedReader cmd = new BufferedReader(new InputStreamReader(
					System.in));
			line = cmd.readLine();
			if(line.equalsIgnoreCase("exit"))
				return;
			
			boolean isValid = checkInput(line,1);
			while(!isValid||!checkNum(line)){
				System.out.println("must enter a num.");
				promptChoseAttri();

				// create the input stream to listen cmd input
				line = cmd.readLine();
				if(line.equalsIgnoreCase("exit"))
					return;
				
				isValid = checkInput(line,1);
			}
			
			int num = Integer.parseInt(line);

			String algorithm = "DSA";

			if (num % 2 == 0) {
				algorithm = "RSA";
			}
			DSACoder.ALGORITHM = algorithm;

			String seedStr = DSACoder.generateSeed(num + "");
			String md5 = new String(DSACoder.encryptMD5(seedStr.getBytes()));
			// 构建密钥
			Map<String, Object> keyMap = DSACoder.initKey(md5);

			// 获得密钥
			String publicKey = DSACoder.getPublicKey(keyMap);
			String privateKey = DSACoder.getPrivateKey(keyMap);

			/**
			 * 保存密钥，保存在与 当前目录
			 */
			String pwd = FileUtils.getPwd();
			char filesep = File.separatorChar;
			String pubFile = pwd + filesep + num + ".public";
			String priFile = pwd + filesep + num + ".private";

			System.err.println("公钥:" + pubFile + "\r" + publicKey);
			Thread.sleep(30);

			System.err.println("私钥:" + priFile + "\r" + privateKey);

			PrintWriter printerpub = null;
			PrintWriter printerPri = null;
			try {
				printerpub = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(new File(pubFile))));
				printerpub.write(publicKey);
				printerPri = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(new File(priFile))));
				printerPri.write(privateKey);
			} finally {
				if (printerpub != null)
					printerpub.close();
				if (printerPri != null)
					printerPri.close();
			}
			// **************************提示用户输入要签名的文件名和 私钥名**********************************************
			Thread.sleep(30);
			zyd:
			promptSignInput();
			String inputStr = "";
			String filename = null;
			String privateKeyName = null;
			String input[] = null;
			byte[] data = null;
			while(inputStr.equals("")){
				if(filename !=null||privateKeyName != null){
					System.out.println("The file u enter is empty,please check!");
					promptSignInput();
				}
				line = cmd.readLine();
				if(line.equalsIgnoreCase("exit"))
					return;
				
				isValid = checkInput(line,2);
				while(!isValid){
					promptSignInput();
	
					// create the input stream to listen cmd input
					line = cmd.readLine();
					if(line.equalsIgnoreCase("exit"))
						return;
					
					isValid = checkInput(line,2);
				}
				
				
				input = line.split("[ ]+");
				filename = input[0];
				privateKeyName = input[1];
	
				inputStr =  FileUtils.readFileContent(filename);
				data = inputStr.getBytes("UTF-8");
				
				// 读取私钥内容
				inputStr = FileUtils.readFileContent(privateKeyName);
				privateKey = inputStr;
			}
			




			// 产生签名
			String sign = DSACoder.sign(data, privateKey);
			System.err.println("签名:\r" + sign);
			Thread.sleep(30);

			// **************************提示用户输入要验证的签名串和 公钥名**********************************************
			promptVerifyInput();
			
			inputStr = "";
			String signature = null;
			String publickeyName =  null;
			while(inputStr.equals("")){
				
				if(publickeyName !=null){
					System.out.println("the file u entered is empty.");
					promptVerifyInput();
				}
				
				line = cmd.readLine();
				if(line.equalsIgnoreCase("exit"))
					return;
				
				isValid = checkInput(line,2);
				while(!isValid){
					promptVerifyInput();
					line=cmd.readLine();
					isValid = checkInput(line,2);
				}
				
				
				input = line.split("[ ]+");
				signature = input[0];
				publickeyName = input[1];
	
				// 读取公钥内容
				inputStr = FileUtils.readFileContent(publickeyName);
				publicKey = inputStr;
			}
			sign = signature;

			// 验证签名
			boolean status = DSACoder.verify(data, publicKey, sign);
			System.err.println("状态:\r" + status);
		}

	}

	public static void prompt() {
		System.out.println("This is a demo from Yaru.");
		System.out.println("U can type 'exit' to escape the program.");
		return;
	}

	public static void promptChoseAttri() {
		prompt();
		System.out.println("please enter the number below:");
		System.out.println("1,represent ur job.");
		System.out.println("2,represent ur age.");
		System.out.println("3,represent ur id.");
		System.out.println("4,represent ur hometown.");

		return;
	}

	public static void promptSignInput() {
		prompt();
		System.out
				.println("please enter the filename u wanna signature and the filename of the private key which u use to signature.");
		System.out.println("eg:");
		System.out.println("mydata.txt myprivate.key");
		return;
	}

	public static void promptVerifyInput() {
		prompt();
		System.out
				.println("please enter the signature and the filename of the public key which u use to verify.");
		System.out.println("eg:");
		System.out.println("mysignature mypublic.key");
		return;
	}

}
