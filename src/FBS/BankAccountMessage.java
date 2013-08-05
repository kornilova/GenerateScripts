package FBS;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class BankAccountMessage {

    private static String encoding866 = "cp866";
    private static String pathToNewDirectory = "C:\\Users\\nkornilova\\Desktop\\Info\\Testing\\FBS2\\AccountMessages\\";

    public static void generateSBCFile(String startPartMsgNumber, String startPartAccountNumber,
                                       String fileName, String typeMessage, int count) throws IOException {

        File file = new File(new File(".").getCanonicalPath() + "\\src\\FBS\\Files\\SBC\\", fileName);
        String partFileName = fileName.substring(0, 26);
        String secondPartMsgNumber = fileName.substring(43, 46);
        String newMessNumberInFile, newMessNumberInFileName, newAccountNumber, newFileName;
        try {

            for (int i = 1; i <= count; i++) {
                newMessNumberInFileName = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "_" + secondPartMsgNumber;
                newAccountNumber = startPartAccountNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 10));
                newMessNumberInFile = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "," + secondPartMsgNumber;
                newFileName = partFileName + newMessNumberInFileName + ".TXT";
                File newFile = new File(pathToNewDirectory + newFileName);
                replaceSubstringInFile(newMessNumberInFile, newAccountNumber, file, newFile, typeMessage);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateSBFFile(String startPartMsgNumber, String typeMessage, String constPartOfMessage, int count) throws IOException {

        File file = new File(new File(".").getCanonicalPath() + "\\src\\FBS\\Files\\SBF\\", "SBF.txt");
        String partFileName = "SBF"+typeMessage+constPartOfMessage;
        String secondPartMsgNumber = getPartNumberFromMessTypeMessKind(typeMessage);
        String newMessNumberInFile, newMessNumberInFileName, newFileName;
        try {

            for (int i = 1; i <= count; i++) {
                newMessNumberInFileName = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "_" + secondPartMsgNumber;
                newMessNumberInFile = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "," + secondPartMsgNumber;
                newFileName = partFileName + newMessNumberInFileName + ".TXT";
                File newFile = new File(pathToNewDirectory + newFileName);
                replaceSubstringInFile(newMessNumberInFile, null, file, newFile, null);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateSBEFile(String startPartMsgNumber, String fileName, int count) throws IOException {
           //TODO:
    }

    public static void replaceSubstringInFile(String mewMsgNumber, String newAccountNumber, File input,
                                              File output, String typeMessage)
            throws IOException {

        for (String line : FileUtils.readLines(input, encoding866)) {
            String wordAccountNumber = "Ќом—ч:";
            if(typeMessage!=null && (typeMessage=="11"||typeMessage=="12"||typeMessage=="13"))
                wordAccountNumber = "Ќом»зм—ч:";
            FileUtils.write(output,
                    (mewMsgNumber!=null && line.contains("Ќом—ооб:") ? ("Ќом—ооб:" + mewMsgNumber) :
                    newAccountNumber!=null && line.contains(wordAccountNumber) ? wordAccountNumber + newAccountNumber : line) + "\r\n",
                    encoding866, true);
        }

    }

    /*—оставление строки. ѕример: 123 (длина 5) => 00123 */
    public static String stringWithLength(String inputString, char symbol, int fullStringLength) {

        String outputString = inputString;
        for (int i = inputString.length(); i < fullStringLength; i++) {
            outputString = symbol + outputString;
        }
        return outputString;
    }

    private static String getPartNumberFromMessTypeMessKind(String typeKind)
    {
         /*
                    01 об открытии первичное
                    02 об открытии корректирующее
                    03 об открытии отмен€ющее
                    11 об изменении счета первичное
                    12 об изменении корректирующее
                    13 об изменении отмен€ющее
                    21 о закрытии первичное
                    22 о закрытии корректирующее
                    23 о закрытии отмен€ющее
         */
        if(typeKind.equals("01") || typeKind.equals("21"))
        {
            return "100";
        }
        else
        if(typeKind.equals("02") || typeKind.equals("22"))
        {
            return "101";
        }
        else if(typeKind.equals("03") || typeKind.equals("23"))
        {
            return "177";
        }
        else if(typeKind.equals("11"))
        {
            return "500";
        }
        else if(typeKind.equals("12"))
        {
            return "501";
        }
        else if(typeKind.equals("13"))
        {
            return "177";
        } else return null;

    }

}
