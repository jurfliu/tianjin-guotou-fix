package com.bonc.tianjin.guotou.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class MD5Util {
	 public static String getMD5String(String value) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("md5");
	            byte[] e = md.digest(value.getBytes());
	            return toHexString(e);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return value;
	        }
	    }
 
	 
	    public static String getMD5String(byte[] bytes) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("md5");
	            byte[] e = md.digest(bytes);
	            return toHexString(e);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }
	 
	    private static String toHexString(byte bytes[]) {
	        StringBuilder hs = new StringBuilder();
	        String stmp = "";
	        for (int n = 0; n < bytes.length; n++) {
	            stmp = Integer.toHexString(bytes[n] & 0xff);
	            if (stmp.length() == 1)
	                hs.append("0").append(stmp);
	            else
	                hs.append(stmp);
	        }
	 
	        return hs.toString();
	    }
		public static String md5(String str){
			try {
				byte[] bs= MessageDigest.getInstance("md5").digest(str.getBytes());
				 StringBuilder sb = new StringBuilder(40);
			        for(byte x:bs) {
			            if((x & 0xff)>>4 == 0) {
			                sb.append("0").append(Integer.toHexString(x & 0xff));
			            } else {
			                sb.append(Integer.toHexString(x & 0xff));
			            }
			        }
			        return sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
	       return System.currentTimeMillis()+"";
	    }
	   public static void main(String[] args) {
		   String url="佰达昨日并网电量最大值_佰达昨日并网电量最大值";
		   System.out.println("url:"+url);
		System.out.println(MD5Util.getMD5String(url));
	}
}
