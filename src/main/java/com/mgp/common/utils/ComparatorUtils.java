package com.yishenxiao.commons.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ComparatorUtils {

	/**
	 * 将字符串数字按首字母先后进行排序
	 * 
	 * Java原生排序为 数字->英文->中文 为了将英文和中文首字母相同的排列到一起 先将字符串首字符为汉字的改为该汉字的首字母加上该字符串
	 * 为了以示区分中间再加一个分割符& 然后使用Java原生排序算法 再将包含&字符的字符串中的&和首字母去除从而达到排序目的
	 */
	public static Map<String, List<String>> chineseAndEnglishSort(String[] arrays) {
		for (int i = 0; i < arrays.length; i++) {
			String str = arrays[i];
			if (str.length() == 0){
				return null;
			}
			String alphabet = str.substring(0, 1);
			/* 判断首字符是否为中文，如果是中文便将首字符拼音的首字母和&符号加在字符串前面 */
			if (alphabet.matches("[\\u4e00-\\u9fa5]+")) {
				str = getAlphabet(str) + "&" + str;
				arrays[i] = str;
			}
		}
		/* 设置排序语言环境 */
		Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
		Arrays.sort(arrays, com);
		//放入不同的集合
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String[] letter = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
		for(int i=0;i<arrays.length;i++){
			String temp = arrays[i];
			for(int z=0;z<letter.length;z++){
			  if(getAlphabet(temp).equals(letter[z])){
				if(map.containsKey(letter[z])){
					//存在这个map
					map.get(letter[z]).add(temp);
				}else{
					//不存在这个map
					List<String> tempList = new ArrayList<String>();
					tempList.add(temp);
					map.put(letter[z], tempList);
				}
				
			  }
			}
		}
		/* 遍历数组，去除标识符&及首字母 */
		/*for (int i = 0; i < arrays.length; i++) {
			String str = arrays[i];
			if (str.contains("&") && str.indexOf("&") == 1) {
				arrays[i] = str.split("&")[1];
			}
		}*/
		for(List<String> list: map.values()){
			for(int i=0;i<list.size();i++){
				String str = list.get(i);
				if (str.contains("&") && str.indexOf("&") == 1) {
					list.set(i, str.split("&")[1]);
				}
			}
		}
		return map;
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
