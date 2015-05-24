/**
 * 
 */
package com.netease.yaru;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author hzzhangyuandao
 *
 */
public class ZydTest {

	public static void main(String args[]) throws IOException{
		
		String test = "1   2";
		String tests[] = test.split("[ ]+");
		System.out.println(tests.length);
		
		

		File f = new File(ZydTest.class.getResource("/").getPath()); 
		System.out.println(f); 

		File directory = new File("");//参数为空 
		String courseFile = directory.getCanonicalPath() ; 
		System.out.println(courseFile); 

//		URL xmlpath = ZydTest.class.getClassLoader().getResource("selected.txt"); 
//		System.out.println(xmlpath); 

		System.out.println(System.getProperty("user.dir")); 

		System.out.println( System.getProperty("java.class.path")); 

		
		
	}
}
