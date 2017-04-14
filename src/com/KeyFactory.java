package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

public class KeyFactory {
	/**
	 * �ܳ׹���
	 * ���ɹ�����˽��
	 */
	
	public static void publicEnrypy() throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        // ����Կ�׶�
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // �õ���Կ
        Key publicKey = keyPair.getPublic();

        // �õ�˽Կ
        Key privateKey = keyPair.getPrivate();


        //��˽Կ���浽Ӳ����
        saveKey(privateKey,"privateKey");
        saveKey(publicKey,"publicKey");
    }
	
	 /**
     * ��˽Կ���浽Ӳ����
     * @param privateKey
     * @throws IOException
     * @throws FileNotFoundException
     * keyType=publicKey||privateKey
     */
    private static void saveKey(Key key,String keyType) throws IOException,
            FileNotFoundException {
        ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(new File("E:/key/"+keyType)));
        outputStream.writeObject(key);
        System.out.println(keyType+"�������");
    }
    
    /**
     * ��ڷ���
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	publicEnrypy();
	}
}
