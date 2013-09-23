package FBS;

import java.io.IOException;
import static FBS.BankAccountMessage.generateSBCFile;
import static FBS.BankAccountMessage.generateSBFFile;

public class GenerateBankAccountMessage {

    private static String startPartMsgNumber = "5500000055";
    private static String startPartAccountNumber="5432101234";
    private static String constPartOfMessage = "1012762_011120130604_";

    private static String[] typeMessage = new String[]{"01","02","03","11","12","13","21","22","23"};


    public static void main(String[] args) throws ClassNotFoundException{
        int countSBCF  = 220000;
        try {
            //Сообщения
            //Об открытии
            generateSBC(typeMessage[0],countSBCF);
            generateSBC(typeMessage[1],countSBCF);
            generateSBC(typeMessage[2],countSBCF);
            //Об изменении
            generateSBC(typeMessage[3],countSBCF);
            generateSBC(typeMessage[4],countSBCF);
            generateSBC(typeMessage[5],countSBCF);
            //О закрытии
            generateSBC(typeMessage[6],countSBCF);
            generateSBC(typeMessage[7],countSBCF);
            generateSBC(typeMessage[8],countSBCF);

            //Квитанции о принятии
            generateSBF(typeMessage[0],countSBCF);

            generateSBF(typeMessage[1],countSBCF);
            generateSBF(typeMessage[2],countSBCF);

            generateSBF(typeMessage[3],countSBCF);
            generateSBF(typeMessage[4],countSBCF);
            generateSBF(typeMessage[5],countSBCF);

            generateSBF(typeMessage[6],countSBCF);
            generateSBF(typeMessage[7],countSBCF);
            generateSBF(typeMessage[8],countSBCF);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateSBC(String messType, int count) throws IOException {
        generateSBCFile(startPartMsgNumber, startPartAccountNumber,getSBCFFileName(messType, "SBC"),messType, count);
        System.out.print("Already is generated SBC files for types " + messType + ". Count "+count);
    }

    private static void generateSBF(String messType, int count) throws IOException {
        generateSBFFile(startPartMsgNumber, getSBCFFileName(messType, "SBF"), messType, count);
        System.out.print("Already is generated SBF files for types " + messType + ". Count "+count);
    }


    private static String getSBCFFileName(String messType, String fileType)
    {
        String fileName = fileType + messType + constPartOfMessage +"0000000000000000_";
        return  (messType ==typeMessage[0]? fileName + "100":
                 messType ==typeMessage[1]? fileName + "101":
                 messType ==typeMessage[2]? fileName + "177":
                 messType ==typeMessage[3]? fileName + "500":
                 messType ==typeMessage[4]? fileName + "501":
                 messType ==typeMessage[5]? fileName + "177":
                 messType ==typeMessage[6]? fileName + "100":
                 messType ==typeMessage[7]? fileName + "101":
                 messType ==typeMessage[8]? fileName + "177":
                         null)
                !=null?".txt":null;


    }

}
