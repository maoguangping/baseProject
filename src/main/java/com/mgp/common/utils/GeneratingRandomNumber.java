package com.yishenxiao.commons.utils;

import java.util.Random;

public class GeneratingRandomNumber {
	
	private static String getRandomSingleNumber(String baseStr,int number){
		StringBuffer stringBuffer = new StringBuffer();
		char[] str = baseStr.toCharArray();
		for(int i=0;i<number;i++){
			Random random = new Random();
			int tempnum=random.nextInt(str.length);
			stringBuffer.append(str[tempnum]);
		}
		return stringBuffer.toString();	
	}
	
	private static String getRandomNumber(String[] baseStr,int numberCount,int strCount){
		StringBuffer stringBuffer = new StringBuffer();
		
		
		char[] numstr = baseStr[0].toCharArray();
		for(int i=0;i<numberCount;i++){
			Random random = new Random();
			int tempnum=random.nextInt(numstr.length);
			stringBuffer.append(numstr[tempnum]);
		}
		char[] str = baseStr[1].toCharArray();
		for(int i=0;i<strCount;i++){
			Random random = new Random();
			int tempnum=random.nextInt(str.length);
			stringBuffer.append(str[tempnum]);
		}
		return stringBuffer.toString();	
	}

	public static String getRandomSingleNum(int num){
		return getRandomSingleNumber("0123456789",num);
	}
	
	public static String getRandomNumAndStr(int num){
		String[] base = {"0123456789","abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWSYZ"};
		Random random = new Random();
		int tempnum=random.nextInt(num/2)+2;
		return getRandomNumber(base,tempnum,num-tempnum);
	}
	
	/*public static void main(String[] args) {
		System.out.println(getRandomSingleNum(8));
		System.out.println(getRandomNumAndStr(8));
	}*/

}
