package com.tcp.action.yao;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SCMUtil {

//	public static void main(String[] args) {
//		String [] strings=SCMUtil.getArrayFromSCM("1/2016-12-17 00:00:00/11.1/11.1;2/2016-12-17 00:00:00/22.2/22.2;3/2016-12-17 00:00:00/33.3/33.3;");
//		String [] strings2=SCMUtil.getParamterFromArray(strings[0]);
//		System.out.println("1111");
//	}

	/**
	 * 获取单片机传入字符串进行分割成数组 按;号分割
	 * 
	 * @param str
	 * @return
	 */
	public static String[] getArrayFromSCM(String str) {
		if (str == null || str.length() <= 0) {
			System.out.println("getArrayListFromSCM is null");
			return null;
		}
		return str.split(";");

	}
	
	/**
	 * 解析字符串参数成数组。按/分割
	 * @param str
	 * @return
	 */
	public static String[] getParamterFromArray(String str){
		if (str == null || str.length() <= 0) {
			System.out.println("getParamterFromArray is null");
			return null;
		}
		return str.split(":");
	}
	
	/**
	 * 获取当前时间 如2000-01-01 00:00:00
	 * 
	 * @return
	 */
	public static String getSimpledDateTime() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	
	/**
	 * 通过开辟缓冲池存放输入流后转换成字符串
	 * 
	 * 
	 * @param inputStream
	 * @return
	 */
	public String inputStream2String(InputStream inputStream){
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			//Create a Buffer Space to save the inputStream
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(System.getProperty("line.separator"));
				stringBuilder.append(line);
			}
			System.out.println(stringBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	
	
	
	
	

}
