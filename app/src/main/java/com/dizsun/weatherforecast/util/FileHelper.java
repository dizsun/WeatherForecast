package com.dizsun.weatherforecast.util;

import android.content.Context;
import android.util.Log;

import com.dizsun.weatherforecast.MainActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by sundiz on 16/12/14.
 */

public class FileHelper {
    //城市信息存储的文件名
    public static final String CITIES_FILE="cities.json";

    /**
     * 向村粗文件中写入城市信息
     * @param context
     * @param fileName
     * @param Data
     * @throws IOException
     */
    public static void writeData(Context context, String fileName, String Data) throws IOException {
        if(existFile(context,CITIES_FILE)) {
            context.deleteFile(CITIES_FILE);
        }
        FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
        PrintStream ps = new PrintStream(fos);
        ps.print(Data);
        ps.close();
        fos.close();
    }

    /**
     * 从存储文件中读取城市信息
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readData(Context context, String fileName) throws IOException {
        FileInputStream fis = context.openFileInput(fileName);
        byte buffer[] = new byte[200000];
        int len;
        String str="";
        while ((len=fis.read(buffer,0,200000))!=-1){
            str+=new String(buffer,0,len);
        }
        fis.close();
        return str;
    }

    /**
     * 判断存储文件是否存在
     * @param context
     * @param fileName
     * @return
     */
    public static boolean existFile(Context context,String fileName){
        String files[] = context.fileList();
        for (String file : files) {
            if (file.equals(fileName))return true;
        }
        return false;
    }
}
