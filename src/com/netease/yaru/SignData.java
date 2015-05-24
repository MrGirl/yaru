/**
 * 
 */
package com.netease.yaru;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyd,cyr
 * @since 2015/5/14
 * 
 * http://blog.csdn.net/wangqiuyun/article/details/42143957
 * 
 * 
 * 
 */
public class SignData {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//**************************提示用户选择算法**********************************************
		promptChoseAttri();
		
		//create the input stream to listen cmd input
		BufferedReader cmd = new BufferedReader(new InputStreamReader(System.in)); 
		String line = cmd.readLine();

		int num = Integer.parseInt(line);
		
		String algorithm = "DSA";
		
		if(num % 2 == 0){
			algorithm = "RSA";
		}
		
		
		//**************************提示用户输入要签名的文件名和 私钥名**********************************************
		promptSignInput();
	
		line = cmd.readLine();
		
		String input[] = line.split("[ ]+");
		String filename = input[0];
		String privateKey = input[1];
		
		//**************************提示用户输入要验证的签名串和 公钥名**********************************************
		promptVerifyInput();
		line = cmd.readLine();
		input = line.split("[ ]+");
		String signature = input[0];
		String publickey = input[1];
		
//		String seedStr = DSACoder.generateSeed(input);
//		String md5 = new String(DSACoder.encryptMD5(seedStr.getBytes()));
//		String inputStr = "";  
//		BufferedReader reader = null;
//		try{
//		reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input.get("source")))));
//		String line = "";
//		
//		String lineSep = System.getProperty("line.separator");
//		while((line=reader.readLine()) != null){
//			inputStr+=line+lineSep;
//		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			if(reader != null)
//				reader.close();
//		}
//		
//		
//        byte[] data = inputStr.getBytes("UTF-8");  
//  
//        // 构建密钥  
//        Map<String, Object> keyMap = DSACoder.initKey(md5); 
//  
//        // 获得密钥  
//        String publicKey = DSACoder.getPublicKey(keyMap);  
//        String privateKey = DSACoder.getPrivateKey(keyMap);  
//  
//        System.err.println("公钥:\r" + publicKey);  
//        System.err.println("私钥:\r" + privateKey);  
//  
//        // 产生签名  
//        String sign = DSACoder.sign(data, privateKey);  
//        System.err.println("签名:\r" + sign);  
//        
//       //***********************************获取原始数据-start*************************************************************
//        /**
//         * 获取原始数据
//         */
//        byte[] decodedSign = DSACoder.decryptBASE64(sign);
//        
//        
//        //***********************************获取原始数据-end*************************************************************
//
//        // 验证签名  
//        boolean status = DSACoder.verify(data, publicKey, sign);  
//        System.err.println("状态:\r" + status);  
//		
		
	}
	
	public static void prompt(){
		System.out.println("This is a demo from Yaru.");
		System.out.println("U can type 'exit' to escape the program.");
		return ;
	}
	
	public static void promptChoseAttri(){
		prompt();
		System.out.println("please enter the number below:");
		System.out.println("1,represent ur job.");
		System.out.println("2,represent ur age.");
		System.out.println("3,represent ur id.");
		System.out.println("4,represent ur hometown.");

		return ;
	}
	
	public static void promptSignInput(){
		prompt();
		System.out.println("please enter the filename u wanna signature and the filename of the private key which u use to signature.");
		System.out.println("eg:");
		System.out.println("mydata.txt myprivate.key");
		return ;
	}
	
	public static void promptVerifyInput(){
		prompt();
		System.out.println("please enter the signature and the filename of the public key which u use to verify.");
		System.out.println("eg:");
		System.out.println("mysignature mypublic.key");
		return ;
	}
	
	public static Map<String,String> parseInput(String[] args){
		
		Map<String,String> result = new HashMap<String,String>();
		
		if(args.length % 2 != 0){
			result.put("error", "param count is wrong!");
			return result;
		}
		
		for(int i=0;i<args.length-1;i++){
			if(args[i].trim().equals("-u")||args[i].trim().equals("--username")){
				result.put("username", args[i+1].trim());
			}
			if(args[i].trim().equals("-p")||args[i].trim().equals("--password")){
				result.put("password", args[i+1].trim());
			}
			if(args[i].trim().equals("-c")||args[i].trim().equals("--code")){
				result.put("code", args[i+1].trim());
			}
			if(args[i].trim().equals("-t")||args[i].trim().equals("--tel")){
				result.put("tel", args[i+1].trim());
			}
		}
		
		//get source and dest
		result.put("source", args[args.length-2].trim());
		result.put("dest", args[args.length-1].trim());
		return result;
	}

}
