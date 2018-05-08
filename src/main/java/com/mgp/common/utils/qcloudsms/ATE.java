package com.yishenxiao.commons.utils.qcloudsms;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ATE {
	public static void main(String[] args) { 
		 String[] arrays = new String[] { "Q@", "[", "{", "大同", "收到", "地方", "三等分", "的人", "反对高铁", "泛代数", "上的投入", "和国家" };
		 for (int i = 0; i < arrays.length; i++) {
		  String str = arrays[i];
		  if (str.length() == 0)
		  return;
		  String alphabet = str.substring(0, 1);
		  /*判断首字符是否为中文，如果是中文便将首字符拼音的首字母和&符号加在字符串前面*/
		  if (alphabet.matches("[\\u4e00-\\u9fa5]+")) {
		  str = getAlphabet(str) + "&" + str;
		  arrays[i] = str;
		  }
		 }
		 /*设置排序语言环境*/
		 Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
		 Arrays.sort(arrays, com);
		 /*遍历数组，去除标识符&及首字母*/
		 for (int i=0;i<arrays.length;i++) {
		  String str=arrays[i];
		  if(str.contains("&")&&str.indexOf("&")==1){
		  arrays[i]=str.split("&")[1];
		  }
		  System.out.println(arrays[i]);
		 }
		 }
		 public static String getAlphabet(String str) {
		 HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		 // 输出拼音全部小写
		 defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		 // 不带声调
		 defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		 String pinyin = null;
		 try {
		  pinyin = (String) PinyinHelper.toHanyuPinyinStringArray(str.charAt(0), defaultFormat)[0];
		 } catch (BadHanyuPinyinOutputFormatCombination e) {
		  e.printStackTrace();
		 }
		 return pinyin.substring(0, 1);
		}
}
