package FBS;

import java.io.*;
import java.lang.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 19.12.12
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountMessageFiles_SBFE {

    public static void main(String[] args) throws IOException {
        int startMsgNumber = 683;
        int newMsgNumber = startMsgNumber + 1;
        String startPartMsgNumber="1000000012000";
        String startFileName = "SBF011012762_011120121217_"+String.valueOf(startPartMsgNumber);
        String oldFileName = startFileName + String.valueOf(startMsgNumber) + "_100.txt";
        String newFileName = startFileName + String.valueOf(newMsgNumber) + "_100.txt";
        String filePath = "D:\\FBS-2\\trunk\\Testing\\Test Data\\Банковские счета\\Packages\\FLK\\SBF\\Test cases 20121106\\";

        FileInputStream fis2 = null;
        DataInputStream input = null;
        FileOutputStream fos2 = null;
        DataOutputStream output = null;
        for (int i = 0; i < 5; i++) {
            try {
                newMsgNumber += i;
                fis2 = new FileInputStream(filePath + oldFileName);
                input = new DataInputStream(fis2);
                fos2 = new FileOutputStream(filePath + newFileName);
                output = new DataOutputStream(fos2);

                String line = null;
                String s2 = startPartMsgNumber + String.valueOf(startMsgNumber);
                String s3 = startPartMsgNumber + String.valueOf(newMsgNumber);

                int x = 0;
                int y = 0;
                String result = "";


                while (null != ((line = input.readLine()))) {
                    x = 0;
                    y = 0;
                    result = "";
                    while ((x = line.indexOf(s2, y)) > -1) {
                        result += line.substring(y, x);
                        result += s3;
                        y = x + s2.length();
                    }
                    result += line.substring(y);
                    line = result;

                    if (line.indexOf("'',") != -1) {
                        continue;
                    } else {
                        line = line + "\r\n";
                        output.writeBytes(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
                fos2.close();
                input.close();
                fis2.close();
            }
        }

    }
}
