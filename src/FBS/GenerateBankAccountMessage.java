package FBS;

import java.io.IOException;

import static FBS.BankAccountMessage.generateSBCFile;
import static FBS.BankAccountMessage.generateSBFFile;

public class GenerateBankAccountMessage {
    private static String startPartMsgNumber = "5500000055";
    private static String startPartAccountNumber="5432101234";
    private static String constPartOfMessage = "1012762_011120130604_";

    private static String fileNameSBC01 = "SBC01"+ constPartOfMessage +"0000000000000000_100.txt";
    private static String fileNameSBC02 = "SBC02"+ constPartOfMessage +"0000000000000000_101.txt";
    private static String fileNameSBC03 = "SBC03"+ constPartOfMessage +"0000000000000000_177.txt";
    private static String fileNameSBC11 = "SBC11"+ constPartOfMessage +"0000000000000000_500.txt";
    private static String fileNameSBC12 = "SBC12"+ constPartOfMessage +"0000000000000000_501.txt";
    private static String fileNameSBC13 = "SBC13"+ constPartOfMessage +"0000000000000000_177.txt";
    private static String fileNameSBC21 = "SBC21"+ constPartOfMessage +"0000000000000000_100.txt";
    private static String fileNameSBC22 = "SBC22"+ constPartOfMessage +"0000000000000000_101.txt";
    private static String fileNameSBC23 = "SBC23"+ constPartOfMessage +"0000000000000000_177.txt";

    public static void main(String[] args) throws ClassNotFoundException{
        int count  = 2;
        try {
            //Сообщения
            generateSBC01(count);
            generateSBC02(count);
            generateSBC03(count);
            generateSBC11(count);
            generateSBC12(count);
            generateSBC13(count);
            generateSBC21(count);
            generateSBC22(count);
            generateSBC23(count);

            //Квитанции о принятии
            generateSBF01(count);
            generateSBF02(count);
            generateSBF03(count);
            generateSBF11(count);
            generateSBF12(count);
            generateSBF13(count);
            generateSBF21(count);
            generateSBF22(count);
            generateSBF23(count);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void generateSBC01(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC01, "01", count);
    }

    private static void generateSBC02(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC02, "02", count);
    }

    private static void generateSBC03(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC03, "03", count);
    }

    private static void generateSBC11(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC11, "11", count);
    }

    private static void generateSBC12(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC12, "12", count);
    }

    private static void generateSBC13(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC13, "13", count);
    }

    private static void generateSBC21(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC21, "21", count);
    }

    private static void generateSBC22(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC22, "22", count);
    }

    private static void generateSBC23(int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber, fileNameSBC23, "23", count);
    }

    private static void generateSBF01(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "01", constPartOfMessage, count);
    }

    private static void generateSBF02(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "02", constPartOfMessage, count);
    }

    private static void generateSBF03(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "03", constPartOfMessage, count);
    }

    private static void generateSBF11(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "11", constPartOfMessage, count);
    }

    private static void generateSBF12(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "12", constPartOfMessage, count);
    }

    private static void generateSBF13(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "13", constPartOfMessage, count);
    }

    private static void generateSBF21(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "21", constPartOfMessage, count);
    }

    private static void generateSBF22(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "22", constPartOfMessage, count);
    }

    private static void generateSBF23(int count) throws IOException {
        generateSBFFile(startPartMsgNumber, "23", constPartOfMessage, count);
    }

}
