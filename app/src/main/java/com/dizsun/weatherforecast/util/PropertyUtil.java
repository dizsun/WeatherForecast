package com.dizsun.weatherforecast.util;

import java.io.*;
import java.util.Properties;

/**
 * @author dizsun
 */
public class PropertyUtil {

    private String propertyFile;

    public String getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public PropertyUtil(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public PropertyUtil() {
        this.propertyFile ="pro.properties";
    }



    /**
     * 根据Key 读取Value
     *
     * @param key
     * @return
     *
     */
    public String readData(String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    propertyFile));
            props.load(in);
            in.close();
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 修改或添加键值对 如果key存在，修改 反之，添加。
     *
     * @param key
     * @param value
     *
     */
    public void writeData(String key, String value) {
        Properties prop = new Properties();
        try {
            File file = new File(propertyFile);
            if (!file.exists())
                file.createNewFile();
            InputStream fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();//一定要在修改值之前关闭fis
            OutputStream fos = new FileOutputStream(propertyFile);
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");
            prop.store(fos, "just for test");

            fos.close();
        } catch (IOException e) {
            System.err.println("Visit " + propertyFile + " for updating "
                    + value + " value error");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PropertyUtil test = new PropertyUtil();
        test.setPropertyFile("test.properties");
    }

}