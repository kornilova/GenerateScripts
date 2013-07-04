package FBS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 27.05.13
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
 */
public class CreateFileForFirstLoad {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        File fileExport = new File("C:\\Users\\nkornilova\\Desktop\\Info\\Testing\\FBS2\\Accounts_11111.xml");

        if(!fileExport.exists()) {
            fileExport.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(fileExport);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<?xml version=\"1.0\" encoding=\"Windows-1251\"?>\n");
        stringBuilder.append("<BankAccounts>\n");

        String str;
        String startPartMsgNumber="3000000012";
        String msgNumber;
        String startPartAccountNumber="60101810400000";
        String accountNumber;

        for(int i=1; i<=10001;i++)
        {
            msgNumber=startPartMsgNumber + BankAccountMessageFile_SBC.stringWithLength(String.valueOf(i), '7', 6);
            accountNumber=startPartAccountNumber + BankAccountMessageFile_SBC.stringWithLength(String.valueOf(i), '0', 6);
            str="<res insOgrn=\"5500000001506\" insInn=\"4444444605\" insKpp=\"444444448\" insName=\"ÎÎÎ Èäåíòèôèêàöèÿ506\" resNum=\""+ accountNumber +"\" bRegNum=\"481\" bBic=\"046027283\" bOgrn=\"1022300003021\" bName= \"ÐÎÑÒÔÈÍÀÍÑ\" messNum=\""+ msgNumber +",100\" messType=\"1\" openDate=\"27.05.2013\"/>";
            stringBuilder.append(str+"\n");
        }

        stringBuilder.append("</BankAccounts>");

        fileWriter.write(stringBuilder.toString());

        fileWriter.close();

    }
}
