package FBS;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class BankAccountMessage {

    private static String encoding866 = "cp866";
    private static String pathToNewDirectory =  "C:\\Users\\Nataliya.Gordeeva\\Desktop\\Info\\Testing\\FBS2\\Messages_test\\";
    public static void generateSBCFile(String startPartMsgNumber,
                                       String startPartAccountNumber,
                                       String sourceFileName,
                                       String typeMessage,
                                       int count) throws IOException {

        File file = new File(new File(".").getCanonicalPath() + "\\src\\FBS\\Files\\SBC\\", sourceFileName);
        String partFileName = sourceFileName.substring(0, 26);
        String secondPartMsgNumber = sourceFileName.substring(43, 46);
        String newMessNumberInFile, newMessNumberInFileName, newAccountNumber, oldAccountNumber, newFileName;
        try {

            for (int i = 1; i <= count; i++) {
                newMessNumberInFileName = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "_" + secondPartMsgNumber;
                newAccountNumber = startPartAccountNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 10));
                oldAccountNumber = startPartAccountNumber + String.valueOf(stringWithLength(String.valueOf(i-1), '0', 10));
                newMessNumberInFile = startPartMsgNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6)) + "," + secondPartMsgNumber;
                newFileName = partFileName + newMessNumberInFileName + ".TXT";
                File newFile = new File(pathToNewDirectory + "SBC"+typeMessage + "\\" + newFileName);
                replaceSubstringInFile(newMessNumberInFile, newAccountNumber, oldAccountNumber, file, newFile, typeMessage);
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
                replaceSubstringInFile(newMessNumberInFile, null, null, file, newFile, null);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void replaceSubstringInFile(String mewMsgNumber, String newAccountNumber, String oldAccountNumber, File input,
                                              File output, String typeMessage)
            throws IOException {

        String wordAccountNumber = "НомСч:";
        String wordNewAccountNumber = "НомИзмСч:";
        String wordOldAccountNumber="НомСчСтар:";

        boolean isChangedMess= typeMessage!=null && (typeMessage=="11"||typeMessage=="12"||typeMessage=="13");
        for (String line : FileUtils.readLines(input, encoding866)) {

            FileUtils.write(output,
                    (mewMsgNumber!=null && line.contains("НомСооб:") ? ("НомСооб:" + mewMsgNumber) :
                            !isChangedMess && newAccountNumber!=null && line.contains(wordAccountNumber) ? (wordAccountNumber + newAccountNumber):
                            isChangedMess && newAccountNumber!=null && line.contains(wordNewAccountNumber) ? (wordNewAccountNumber + newAccountNumber)
                            : isChangedMess && oldAccountNumber!=null && line.contains(wordOldAccountNumber) ? (wordOldAccountNumber + oldAccountNumber) : line) + "\r\n",
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
