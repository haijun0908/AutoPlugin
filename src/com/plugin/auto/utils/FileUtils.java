package com.plugin.auto.utils;

import java.io.*;

public class FileUtils {
    public static String readToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void writeString(File file, String content) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
