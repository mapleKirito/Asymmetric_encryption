package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.lang.ArrayUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Encryption {
	
	/**
     * 获取私匙
     * @throws FileNotFoundException,ClassNotFoundException
     */
	private static Key loadKey() throws IOException, FileNotFoundException,ClassNotFoundException {
		ObjectInputStream inputStream = 
			new ObjectInputStream(new FileInputStream(new File("E:/key/服务器key/privateKey")));
		Key privateKey = (Key) inputStream.readObject();
		return privateKey;
	}
	
	/**
     * 把加密后的就过保存到硬盘上
     * @param result
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveData(byte[] result) throws FileNotFoundException,
            IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(
                "E:/key/ke/EnrypyData"));
        fileOutputStream.write(result);
    }
	
	public static void toEnrypy() throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		// 得到私钥
        Key privateKey = loadKey();
		// 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // 对数据进行加密
        byte[] result = cipher.doFinal("aaa".getBytes());
       /* System.out.println("加密完成");
        //把加密后的数据保存到硬盘上
          saveData(result);*/
        
      //经过base64加密
        String toBase64=new BASE64Encoder().encode(result);
        System.out.println("加密完成");
      //把加密后的数据保存到硬盘上
        saveData(toBase64.getBytes());
	}
	
	public static void toEnrypy(InputStream is) throws Exception{
		File f=new File("E:/key/Enrypy/framework.properties");
		if(!f.exists()){
			f.createNewFile();
		}
		FileOutputStream fos=new FileOutputStream(f);
		int i=0;
		//117是RSA最大处理数
		byte[] data=new byte[117];
		Cipher cipher = Cipher.getInstance("RSA");
		Cipher cipher2 = Cipher.getInstance("RSA");
		// 得到私钥
        Key privateKey = loadKey();
        Key publicKey = Decrypt.loadKey();
		// 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        cipher2.init(Cipher.DECRYPT_MODE, publicKey);

        // 对数据进行加密
        byte[] result=null;
        int l=1;
        int now_length=0;
        while((i=is.read(data))>0){
        	result=data;
        	if(i<117){
        		result=ArrayUtils.subarray(data, 0,i);
        	}
        	System.out.println(new String(result,"utf-8"));
        	System.out.println(l+"before:"+result.length);
        	result = cipher.doFinal(result);
        	//经过base64加密
        	String toBase64=new BASE64Encoder().encode(result);
        	result=new BASE64Decoder().decodeBuffer(toBase64);
        	
        	byte[] doFinal = cipher2.doFinal(result);  
        	//System.out.println(l+"  after:"+doFinal.length);
        	//System.out.println(new String(doFinal,"utf-8"));
        	fos.write(toBase64.getBytes());
        	l++;
        	//System.out.println();
        	data=new byte[117];
        }
        is.close();
        fos.close();
	}
	
	public static void toEnrypy(File fileIn,File fileOut) throws Exception{
		InputStream is=new FileInputStream(fileIn);
		FileOutputStream fos=new FileOutputStream(fileOut);
		int i=0;
		//117是RSA最大处理数
		byte[] data=new byte[117];
		Cipher cipher = Cipher.getInstance("RSA");
		Cipher cipher2 = Cipher.getInstance("RSA");
		// 得到私钥
        Key privateKey = loadKey();
        Key publicKey = Decrypt.loadKey();
		// 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        cipher2.init(Cipher.DECRYPT_MODE, publicKey);

        // 对数据进行加密
        byte[] result=null;
        int l=1;
        BigDecimal now_length=new BigDecimal(0);
        BigDecimal max_length=new BigDecimal(fileIn.length());
        while((i=is.read(data))>0){
        	//======加密进度开始
        	//计算进度
        	System.out.print("==>("+now_length.multiply(new BigDecimal(100)).divide(max_length,BigDecimal.ROUND_HALF_EVEN)+"%)");
        	System.out.println(now_length+"/"+fileIn.length());
        	now_length=now_length.add(new BigDecimal(i));
        	
        	//加密进度结束
        	result=data;
        	if(i<117){
        		result=ArrayUtils.subarray(data, 0,i);
        	}
        	//System.out.println(new String(result,"utf-8"));
        	//System.out.println(l+"before:"+result.length);
        	result = cipher.doFinal(result);
        	//经过base64加密
        	String toBase64=new BASE64Encoder().encode(result);
        	result=new BASE64Decoder().decodeBuffer(toBase64);
        	
        	byte[] doFinal = cipher2.doFinal(result);  
        	//System.out.println(l+"  after:"+doFinal.length);
        	//System.out.println(new String(doFinal,"utf-8"));
        	fos.write(toBase64.getBytes());
        	l++;
        	//System.out.println();
        	data=new byte[117];
        }
        is.close();
        fos.close();
	}
	
	public static String bytesToString(byte[] encrytpByte) {
	       String result = "";
	       for (Byte bytes : encrytpByte) {
	           result += bytes.toString() + " ";
	       }
	       return result;
	   }
	
	
	/**
	 * 入口方法
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fis=new FileInputStream(new File("E:/key/Decrypt/framework.properties"));
		toEnrypy(fis);
		
		/*File in=new File("e:/key/00000001.wmv");
		File out=new File("e:/key/00000001_code.wmv");
		toEnrypy(in, out);*/
	}
}
