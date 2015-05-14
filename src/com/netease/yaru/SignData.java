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
 * 输入参数，参数为 用户名，密码，身份证号码，以及电话号码，其中，用户名和密码为必填参数，身份证号码以及电话号码为可选参数。
 * 
 * eg:
 * SignData -u zyd -p cyr  mydata  mysignature
 * 
 * or
 * 
 * SignData -u zyd -p cyr -c 888888199009096666 -t 13282015032  mydata  mysignature
 * 
 * or
 * 
 * SignData -u zyd -p cyr -c 888888199009096666  mydata  mysignature
 * 
 * or
 * 
 * SignData -u zyd -p cyr -t 13282015032   mydata  mysignature
 */
public class SignData {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		if(args.length < 6){
			help();
			return;
		}

		//parse the input
		Map<String,String> input = parseInput(args);
		
		//check whether error occured
		String error = input.get("error");
		if(error!=null){
			help();
			return;
		}
		
		//check input , params 'username' and 'password' are necessary
		String username = input.get("username");
		String password = input.get("password");
		if(username == null || username.equals("")||password == null || password.equals("")){
			help();
			return;
		}
		
		String seedStr = DSACoder.generateSeed(input);
		String md5 = new String(DSACoder.encryptMD5(seedStr.getBytes()));
		String inputStr = "";  
		BufferedReader reader = null;
		try{
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input.get("source")))));
		String line = "";
		
		String lineSep = System.getProperty("line.separator");
		while((line=reader.readLine()) != null){
			inputStr+=line+lineSep;
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null)
				reader.close();
		}
		
		
        byte[] data = inputStr.getBytes("UTF-8");  
  
        // 构建密钥  
        Map<String, Object> keyMap = DSACoder.initKey(md5); 
  
        // 获得密钥  
        String publicKey = DSACoder.getPublicKey(keyMap);  
        String privateKey = DSACoder.getPrivateKey(keyMap);  
  
        System.err.println("公钥:\r" + publicKey);  
        System.err.println("私钥:\r" + privateKey);  
  
        // 产生签名  
        String sign = DSACoder.sign(data, privateKey);  
        System.err.println("签名:\r" + sign);  
        
       //***********************************获取原始数据-start*************************************************************
        /**
         * 获取原始数据
         */
        byte[] decodedSign = DSACoder.decryptBASE64(sign);
        
        
        //***********************************获取原始数据-end*************************************************************

        // 验证签名  
        boolean status = DSACoder.verify(data, publicKey, sign);  
        System.err.println("状态:\r" + status);  
		
		
	}
	
	public static void help(){
		System.out.println("Usage:SignData [OPTION]...SOURCE...DEST\nOR:Usage:SignData [OPTION]...SOURCE...DIRECTORY");
		System.out.println("Generate a signatrue using the option on a source file to the destination.");
		System.out.println("-u,--username		the user's username");
		System.out.println("-p,--password		the user's password");
		System.out.println("-c,--code		the user's identity");
		System.out.println("-t,--tel		the user's telephone");
		
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
