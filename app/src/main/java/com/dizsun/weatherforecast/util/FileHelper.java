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
    public static final String CITIES_FILE="cities.json";

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
    public static boolean existFile(Context context,String fileName){
        String files[] = context.fileList();
        for (String file : files) {
            if (file.equals(fileName))return true;
        }
        return false;
    }
}
