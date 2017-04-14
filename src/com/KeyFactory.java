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
	 * 密匙工厂
	 * 生成公匙与私匙
	 */
	
	public static void publicEnrypy() throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        // 生成钥匙对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 得到公钥
        Key publicKey = keyPair.getPublic();

        // 得到私钥
        Key privateKey = keyPair.getPrivate();


        //把私钥保存到硬盘上
        saveKey(privateKey,"privateKey");
        saveKey(publicKey,"publicKey");
    }
	
	 /**
     * 把私钥保存到硬盘上
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
        System.out.println(keyType+"创建完成");
    }
    
    /**
     * 入口方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	publicEnrypy();
	}
}
