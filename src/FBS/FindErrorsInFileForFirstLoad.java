package FBS;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 26.06.13
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class FindErrorsInFileForFirstLoad {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\FBS-2\\trunk\\Testing\\Test Data\\Банковские счета\\FirstLoad_RealFiles\\";

        FileInputStream fis2 = null;
        DataInputStream input = null;
                fis2 = new FileInputStream(filePath + "exchange_protocol_Accounts_00001.xml");
                input = new DataInputStream(fis2);
        String line = null;
        StringBuilder str = new StringBuilder();
        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();
        int count=0;
        while (null != ((line = input.readLine()))) {
            //if(!line.contains("InsurerName")) str.append(line).append("\n");
            //if(!line.contains(",") && !line.contains("insKpp=")) str1.append(line).append("\n");
            if(!line.contains("bRegNum")) str2.append(line).append("\n");
            }

        //System.out.print(str);
        //System.out.print(count);
       // System.out.print("ИП\n");
       // System.out.print(str1);
        System.out.print(str2);
        }


}