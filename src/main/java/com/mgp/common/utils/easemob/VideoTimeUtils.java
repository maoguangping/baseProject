package com.yishenxiao.commons.utils.easemob;

import java.io.File;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

public class VideoTimeUtils {
   public static long getTime(String fileName){
	   File source = new File(fileName);
       Encoder encoder = new Encoder();
       long sum =0;
       try {
            MultimediaInfo m = encoder.getInfo(source);
            long ls = m.getDuration()/1000;//ls是获取到的秒数
            sum += ls;
       } catch (Exception e) {
           return 10;
       }
	return sum;
   }
}
