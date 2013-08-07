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

    private static String pathToSBC01 = "SBC01";
    private static String pathToSBC02 = "SBC02";
    private static String pathToSBC03 = "SBC03";
    private static String pathToSBC11 = "SBC11";
    private static String pathToSBC12 = "SBC12";
    private static String pathToSBC13 = "SBC13";
    private static String pathToSBC21 = "SBC21";
    private static String pathToSBC22 = "SBC22";
    private static String pathToSBC23 = "SBC23";

    private static String pathToSBF01 = "SBF01";
    private static String pathToSBF02 = "SBF02";
    private static String pathToSBF03 = "SBF03";
    private static String pathToSBF11 = "SBF11";
    private static String pathToSBF12 = "SBF12";
    private static String pathToSBF13 = "SBF13";
    private static String pathToSBF21 = "SBF21";
    private static String pathToSBF22 = "SBF22";
    private static String pathToSBF23 = "SBF23";

    public static void main(String[] args) throws ClassNotFoundException{
        int countSBCF  = 10000;
        try {
            //Сообщения
            //Об открытии
            generateSBC("01",countSBCF);
            generateSBC("02",countSBCF);
            generateSBC("03",countSBCF);
            //Об изменении
            generateSBC("11",countSBCF);
            generateSBC("12",countSBCF);
            generateSBC("13",countSBCF);
            //О закрытии
            generateSBC("21",countSBCF);
            generateSBC("22",countSBCF);
            generateSBC("23",countSBCF);

            //Квитанции о принятии
            generateSBF("01",countSBCF);
            generateSBF("02",countSBCF);
            generateSBF("03",countSBCF);

            generateSBF("11",countSBCF);
            generateSBF("12",countSBCF);
            generateSBF("13",countSBCF);

            generateSBF("21",countSBCF);
            generateSBF("22",countSBCF);
            generateSBF("23",countSBCF);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void generateSBC(String messType, int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber,
                getSBCFileName(messType),
                messType, count, getSBCPartOfPath(messType));
    }

    private static void generateSBF(String messType, int count) throws IOException {
        generateSBFFile(startPartMsgNumber, messType, constPartOfMessage, count, getSBFPartOfPath(messType));
    }


    private static String getSBCFileName(String messType)
    {
        return  messType =="01"? fileNameSBC01:
                messType =="02"? fileNameSBC02:
                        messType =="03"? fileNameSBC03:
                                messType =="11"? fileNameSBC11:
                                        messType =="12"? fileNameSBC12:
                                                messType =="13"? fileNameSBC13:
                                                        messType =="21"? fileNameSBC21:
                                                                messType =="22"? fileNameSBC22:
                                                                        messType =="23"? fileNameSBC23:null;
    }

    private static String getSBCPartOfPath(String messType)
    {
         return messType =="01"? pathToSBC01:
                 messType =="02"? pathToSBC02:
                         messType =="03"? pathToSBC03:
                                 messType =="11"? pathToSBC11:
                                         messType =="12"? pathToSBC12:
                                                 messType =="13"? pathToSBC13:
                                                         messType =="21"? pathToSBC21:
                                                                 messType =="22"? pathToSBC22:
                                                                         messType =="23"? pathToSBC23:null;
    }


    private static String getSBFPartOfPath(String messType)
    {
        return messType =="01"? pathToSBF01:
                messType =="02"? pathToSBF02:
                        messType =="03"? pathToSBF03:
                                messType =="11"? pathToSBF11:
                                        messType =="12"? pathToSBF12:
                                                messType =="13"? pathToSBF13:
                                                        messType =="21"? pathToSBF21:
                                                                messType =="22"? pathToSBF22:
                                                                        messType =="23"? pathToSBF23:null;
    }
}
