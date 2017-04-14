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
     * ��ȡ˽��
     * @throws FileNotFoundException,ClassNotFoundException
     */
	private static Key loadKey() throws IOException, FileNotFoundException,ClassNotFoundException {
		ObjectInputStream inputStream = 
			new ObjectInputStream(new FileInputStream(new File("E:/key/������key/privateKey")));
		Key privateKey = (Key) inputStream.readObject();
		return privateKey;
	}
	
	/**
     * �Ѽ��ܺ�ľ͹����浽Ӳ����
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
		// �õ�˽Կ
        Key privateKey = loadKey();
		// ����Ϊ����ģʽ
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // �����ݽ��м���
        byte[] result = cipher.doFinal("aaa".getBytes());
       /* System.out.println("�������");
        //�Ѽ��ܺ�����ݱ��浽Ӳ����
          saveData(result);*/
        
      //����base64����
        String toBase64=new BASE64Encoder().encode(result);
        System.out.println("�������");
      //�Ѽ��ܺ�����ݱ��浽Ӳ����
        saveData(toBase64.getBytes());
	}
	
	public static void toEnrypy(InputStream is) throws Exception{
		File f=new File("E:/key/Enrypy/framework.properties");
		if(!f.exists()){
			f.createNewFile();
		}
		FileOutputStream fos=new FileOutputStream(f);
		int i=0;
		//117��RSA�������
		byte[] data=new byte[117];
		Cipher cipher = Cipher.getInstance("RSA");
		Cipher cipher2 = Cipher.getInstance("RSA");
		// �õ�˽Կ
        Key privateKey = loadKey();
        Key publicKey = Decrypt.loadKey();
		// ����Ϊ����ģʽ
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        cipher2.init(Cipher.DECRYPT_MODE, publicKey);

        // �����ݽ��м���
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
        	//����base64����
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
		//117��RSA�������
		byte[] data=new byte[117];
		Cipher cipher = Cipher.getInstance("RSA");
		Cipher cipher2 = Cipher.getInstance("RSA");
		// �õ�˽Կ
        Key privateKey = loadKey();
        Key publicKey = Decrypt.loadKey();
		// ����Ϊ����ģʽ
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        cipher2.init(Cipher.DECRYPT_MODE, publicKey);

        // �����ݽ��м���
        byte[] result=null;
        int l=1;
        BigDecimal now_length=new BigDecimal(0);
        BigDecimal max_length=new BigDecimal(fileIn.length());
        while((i=is.read(data))>0){
        	//======���ܽ��ȿ�ʼ
        	//�������
        	System.out.print("==>("+now_length.multiply(new BigDecimal(100)).divide(max_length,BigDecimal.ROUND_HALF_EVEN)+"%)");
        	System.out.println(now_length+"/"+fileIn.length());
        	now_length=now_length.add(new BigDecimal(i));
        	
        	//���ܽ��Ƚ���
        	result=data;
        	if(i<117){
        		result=ArrayUtils.subarray(data, 0,i);
        	}
        	//System.out.println(new String(result,"utf-8"));
        	//System.out.println(l+"before:"+result.length);
        	result = cipher.doFinal(result);
        	//����base64����
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
	 * ��ڷ���
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
