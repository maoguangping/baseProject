package com.yishenxiao.commons.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class InternationalMobilePhoneNumberUtils {
    public static String[] getPhoneInfo(String[] phones){
    	for(int i=0;i<phones.length;i++){
    	  String temp=getPhoneInfo(phones[i]);
    	  phones[i]=temp;
    	}
    	return phones;
    }
    
    public static String getPhoneInfo(String phone){
    	String localphone="";
    	try {
    		if(phone.contains("#")){
	    		String[] localtemp = phone.split("#");
				PhoneNumber pn = PhoneNumberUtil.getInstance().parse(localtemp[1], localtemp[0]);
				localphone="00"+String.valueOf(pn.getCountryCode())+String.valueOf(pn.getNationalNumber());
			}else{
				return phone;
			}
		} catch (NumberParseException e) {
			System.out.println("phone: "+phone);
			return phone;
		}
    	return localphone;
    }
}
