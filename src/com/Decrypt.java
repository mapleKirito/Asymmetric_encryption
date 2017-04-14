package com;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.ArrayUtils;

import com.test.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Decrypt {

	/**
     * 获取公匙
     * @throws FileNotFoundException,ClassNotFoundException
     */
	public static Key loadKey() throws IOException, FileNotFoundException,ClassNotFoundException {
		ObjectInputStream inputStream = 
			new ObjectInputStream(new FileInputStream(new File("E:/key/publicKey")));
		Key publicKey = (Key) inputStream.readObject();
		return publicKey;
	}
	
	/**
     * 从硬盘中加载加密后的文件
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] loadData() throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(
                "E:/key/ke/EnrypyData"));
        /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;

        while ((len = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        fileInputStream.close();

        return outputStream.toByteArray();*/
        
        //经过base64加密
        byte[] buffer=new BASE64Decoder().decodeBuffer(fileInputStream);
        return buffer;
    }
    
    /**
     * 公匙解密
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws InvalidKeyException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws Exception
     */
    public static void toDecrypt() throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException  {
        Cipher cipher = Cipher.getInstance("RSA");

        // 从硬盘中读取公匙
        Key publicKey=null;
		try {
			publicKey = loadKey();
			//publicKey = Test.loadKey();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.err.println("读取密匙失败");
		}

        //设置为解密模式，用公钥解密
         cipher.init(Cipher.DECRYPT_MODE, publicKey);

         /*FileInputStream fileInputStream = new FileInputStream(new File("E://data.data"));

         CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

         byte[] buffer = new byte[1024];
         int len = 0;
         while ((len = cipherInputStream.read(buffer)) != -1) {
             {
                 outputStream.write(buffer, 0, len);
             }
             cipherInputStream.close();

            System.out.println(outputStream.toString());
         }*/

        //cipherInputStream.
        // 从硬盘中读取加密后的数据
        byte[] data = loadData();

        //对加密后的数据进行解密，返回解密后的结果
        byte[] result=null;
		try {
			result = cipher.doFinal(data);
			System.out.print("解密完成，原始数据为:");
	        System.out.println(new String(result));
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("从服务端获取数据完整性破坏");
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("数据解密失败，解密密匙不正确");
		}
        
    }
    
    
    public static void toDecrypt(InputStream is) throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException  {
    	FileOutputStream fos=new FileOutputStream(new File("e:/key/Decrypt/framework.properties"));
    	
    	Cipher cipher = Cipher.getInstance("RSA");

        // 从硬盘中读取公匙
        Key publicKey=null;
		try {
			publicKey = loadKey();
			//publicKey = Test.loadKey();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.err.println("读取密匙失败");
		}
		byte[] data=new BASE64Decoder().decodeBuffer(is);
        //设置为解密模式，用公钥解密
         cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
         //StringBuilder sb = new StringBuilder();  
         int l=1;
         for (int i = 0; i < data.length; i += 128) {
        	 int m=i+128;
        	 if(m>data.length){
        		 m=data.length;
        	 }
             try{
            	 byte[] temp=ArrayUtils.subarray(data, i,  
	                     m);
            	 
	        	 byte[] doFinal = cipher.doFinal(temp);  
	        	 //System.out.println(l+"=="+doFinal.length);
	        	 //System.out.println(new String(doFinal,"utf-8"));
	        	 fos.write(doFinal);
	        	 l++;
	             //sb.append(new String(doFinal));  
             } catch (IllegalBlockSizeException e) {
      			// TODO Auto-generated catch block
      			//e.printStackTrace();
      			System.err.println("从服务端获取数据完整性破坏");
      		} catch (BadPaddingException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      			//System.err.println("数据解密失败，解密密匙不正确");
      		}
         }  
         //System.out.println(sb.toString());
         is.close();
         fos.close();
    }
    
    public static void toDecrypt(File fileIn,File fileOut) throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException  {
    	InputStream is=new FileInputStream(fileIn);
		FileOutputStream fos=new FileOutputStream(fileOut);
    	Cipher cipher = Cipher.getInstance("RSA");

        // 从硬盘中读取公匙
        Key publicKey=null;
		try {
			publicKey = loadKey();
			//publicKey = Test.loadKey();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.err.println("读取密匙失败");
		}
		byte[] data=new BASE64Decoder().decodeBuffer(is);
        //设置为解密模式，用公钥解密
         cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
         //StringBuilder sb = new StringBuilder();  
         int l=1;
         BigDecimal now_length=new BigDecimal(0);
         BigDecimal max_length=new BigDecimal(data.length);
         for (int i = 0; i < data.length; i += 128) {
        	 int m=i+128;
        	 if(m>data.length){
        		 m=data.length;
        	 }
        	//======加密进度开始
         	//计算进度
         	System.out.print("==>("+now_length.multiply(new BigDecimal(100)).divide(max_length,BigDecimal.ROUND_HALF_EVEN)+"%)");
         	System.out.println(now_length+"/"+data.length);
         	now_length=new BigDecimal(m);
         	
         	//加密进度结束
             try{
            	 byte[] temp=ArrayUtils.subarray(data, i,  
	                     m);
            	 
	        	 byte[] doFinal = cipher.doFinal(temp);  
	        	 //System.out.println(l+"=="+doFinal.length);
	        	 //System.out.println(new String(doFinal,"utf-8"));
	        	 fos.write(doFinal);
	        	 l++;
	             //sb.append(new String(doFinal));  
             } catch (IllegalBlockSizeException e) {
      			// TODO Auto-generated catch block
      			//e.printStackTrace();
      			System.err.println("从服务端获取数据完整性破坏");
      		} catch (BadPaddingException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      			//System.err.println("数据解密失败，解密密匙不正确");
      		}
         }  
         //System.out.println(sb.toString());
         is.close();
         fos.close();
    }
    
    /**
     * 入口方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	/*FileInputStream fis=new FileInputStream(new File("e:/key/Enrypy/framework.properties"));
		toDecrypt(fis);*/
    	
    	File in=new File("e:/key/00000001_code.wmv");
		File out=new File("e:/key/00000001_decode.wmv");
		toDecrypt(in, out);
	}
}
