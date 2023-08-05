package org.example.DBA1;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;

//Reference : https://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash-in-java
//Reference : https://www.geeksforgeeks.org/md5-hash-in-java/
public class Utils {

    public static List<String> getFileContentList(BufferedReader file) {
        List<String> fileContentList = new ArrayList<String>();
        if(file == null) return null;
        String line;
        while(true) {
            try {
                if((line = file.readLine()) == null) {
                    break;
                }
                fileContentList.add(line);
            } catch (IOException e) {
                return null;
            }
        }
        return fileContentList;
    }

    public static List<String> getListFromCommaSeperatedString(String value) {
        return List.of(value.split(","));
    }

    public static String encryptUsingMD5(String input) {
        try {
            byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            BigInteger no = new BigInteger(1, theMD5digest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTableFileName(String tableName){
        return "db/tables/" + tableName + ".txt";
    }

    public static String getTableMetadataFileName(String tableName){
        return "db/tables-metadata/" + tableName + "-metadata.txt";
    }

    public static Integer tryParseInt(String str) {
        Integer retVal;
        try {
            retVal = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            retVal = null;
        }
        return retVal;
    }

    public static Double tryParseDouble(String str) {
        Double retVal;
        try {
            retVal = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            retVal = null;
        }
        return retVal;
    }

    /**
     * @param columnType: columntype contains data type like int, string and double.
     * @return boolean : it will return boolean values like true or false.
     * */
    public static boolean validateType(String columnType, String value) {
        switch (columnType) {
            case "string":
                return true;
            case "int":
                if(tryParseInt(value) != null) return true;
                return false;
            case "double":
                if(tryParseDouble(value) != null) return true;
                return false;
            default:
                return false;
        }
    }
}
