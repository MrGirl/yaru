/**
 * 
 */
package com.netease.yaru.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author hzzhangyuandao,Yaru
 *
 */
public class FileUtils {

	public static String getPwd(){
		File directory = new File("");//参数为空 
		String pwd;
		try {
			pwd = directory.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
		return pwd;
	}
	
	public static String readFileContent(String filename) throws IOException{
		String inputStr = "";  
		BufferedReader reader = null;
		try{
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
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
		return inputStr;
	}
}
