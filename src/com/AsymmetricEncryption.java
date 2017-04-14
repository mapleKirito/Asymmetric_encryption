package com;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 * 公钥加密，私钥解密(非对称加密)
 * 
 * @author long
 * 
 */
public class AsymmetricEncryption {

    public static void main(String[] args) throws Exception {
        publicEnrypy();
        privateEncode();
    }

    /**
     * 加密的方法,使用公钥进行加密
     * @throws Exception
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

        // 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 对数据进行加密
        byte[] result = cipher.doFinal("aaa".getBytes());

        //把私钥保存到硬盘上
        saveKey(privateKey);

        //把加密后的数据保存到硬盘上
        saveData(result);
    }

    /**
     * 解密的方法，使用私钥进行解密
     * @throws Exception
     */
    public static void privateEncode() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");

        // 从硬盘中读取私钥
        Key privateKey = loadKey();

        //设置为解密模式，用私钥解密
         cipher.init(Cipher.DECRYPT_MODE, privateKey);

         FileInputStream fileInputStream = new FileInputStream(new File("E://data.data"));

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
         }

        //cipherInputStream.
        // 从硬盘中读取加密后的数据
        //byte[] data = loadData();

        //对加密后的数据进行解密，返回解密后的结果
        //byte[] result = cipher.doFinal(data);

        //System.out.println(new String(result));
    }

    /**
     * 从硬盘中加载加密后的文件
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] loadData() throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(
                "E://data.data"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;

        while ((len = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        fileInputStream.close();

        return outputStream.toByteArray();
    }

    /**
     * 从硬盘中加载私钥
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    private static Key loadKey() throws IOException, FileNotFoundException,
            ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream(new File("E://private_key")));
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
                "E://data.data"));
        fileOutputStream.write(result);
    }

    /**
     * 把私钥保存到硬盘上
     * @param privateKey
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static void saveKey(Key privateKey) throws IOException,
            FileNotFoundException {
        ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(new File("E://private_key")));
        outputStream.writeObject(privateKey);
    }

}
