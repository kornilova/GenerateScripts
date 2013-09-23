package FBS;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class BankAccountMessage {

    private static String encoding866 = "cp866";
    private static String pathToNewDirectory = "C:\\Users\\nkornilova\\Desktop\\Info\\Testing\\FBS2\\AccountMessages_2mln\\";

    public static void generateSBCFile(String startPartMsgNumber,
                                       String startPartAccountNumber,
                                       String sourceFileName,
                                       String typeMessage,
                                       int count) throws IOException {

        File file = new File(new File(".").getCanonicalPath() + "\\src\\FBS\\Files\\SBC\\", sourceFileName);
        String partFileName = sourceFileName.substring(0, 26);
        String secondPartMsgNumber = sourceFileName.substring(43, 46);
        String newMessNumberInFile, newMessNumberInFileName, newAccountNumber, newFileName;
        try {

            for (int i = 1; i <= count; i++) {
                newMessNumberInFileName = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "_" + secondPartMsgNumber;
                newAccountNumber = startPartAccountNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 10));
                newMessNumberInFile = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "," + secondPartMsgNumber;
                newFileName = partFileName + newMessNumberInFileName + ".TXT";
                File newFile = new File(pathToNewDirectory + "SBC"+typeMessage + "\\" + newFileName);
                replaceSubstringInFile(newMessNumberInFile, newAccountNumber, file, newFile, typeMessage);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateSBFFile(String startPartMsgNumber, String sourceFileName,
                                       String typeMessage,
                                       int count) throws IOException {

        File file = new File(new File(".").getCanonicalPath() + "\\src\\FBS\\Files\\SBF\\", "SBF.txt");
        String partFileName = sourceFileName.substring(0, 26);
        String secondPartMsgNumber = sourceFileName.substring(43, 46);
        String newMessNumberInFile, newMessNumberInFileName, newFileName;
        try {

            for (int i = 1; i <= count; i++) {
                newMessNumberInFileName = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "_" + secondPartMsgNumber;
                newMessNumberInFile = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "," + secondPartMsgNumber;
                newFileName = partFileName + newMessNumberInFileName + ".TXT";
                File newFile = new File(pathToNewDirectory + "SBF"+typeMessage + "\\" + newFileName);
                replaceSubstringInFile(newMessNumberInFile, null, file, newFile, null);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void replaceSubstringInFile(String mewMsgNumber, String newAccountNumber, File input,
                                              File output, String typeMessage)
            throws IOException {

        for (String line : FileUtils.readLines(input, encoding866)) {
            String wordAccountNumber = "НомСч:";
            if(typeMessage!=null && (typeMessage=="11"||typeMessage=="12"||typeMessage=="13"))
                wordAccountNumber = "НомИзмСч:";
            FileUtils.write(output,
                    (mewMsgNumber!=null && line.contains("НомСооб:") ? ("НомСооб:" + mewMsgNumber) :
                    newAccountNumber!=null && line.contains(wordAccountNumber) ? wordAccountNumber + newAccountNumber : line) + "\r\n",
                    encoding866, true);
        }

    }

    /*Составление строки. Пример: 123 (длина 5) => 00123 */
    public static String stringWithLength(String inputString, char symbol, int fullStringLength) {

        String outputString = inputString;
        for (int i = inputString.length(); i < fullStringLength; i++) {
            outputString = symbol + outputString;
        }
        return outputString;
    }
}
