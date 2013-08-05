package FBS;

import Database.DB2Connection;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import FBS.BankAccountMessage;

import static FBS.BankAccountMessage.stringWithLength;

/**
 * User: nkornilova
 * Date: 23.01.13
 * Time: 17:59
 */
public class BankAccountMessageFile_SBC {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = null;
        //
        int countInsertedMessage = 1;
        //
        try {
            connection = DB2Connection.setConnection("jdbc:db2://192.168.1.238:50000/FBS", "db2inst", "db2inst");
            //Номер сообщения: часть реквизита до текущего года YY
            String messNumber = "1000000012";
            //Номер сообщения: порядковая часть реквизита
            String messPartNumber = "100";
            //Наименование файла: до даты сообщения YYYYMMDD
            String fileName = "SBC011012762_011120121015_";
            //Номер счета: до изменяемой части
            String messNumAccount = "30101810400000000";
            String insertCommand =
                    "INSERT INTO DB2INST.BSS_ACCOUNT_MSG" +
                            "(ACCOUNT_MSG_ID, ACCOUNT_MSG_STATUS_ID, ACCOUNT_MSG_FILE_ID, ACCOUNT_MSG_INFO_TYPE, ACCOUNT_MSG_PROGRAM_VERSION, ACCOUNT_MSG_SENDER_PHONE, ACCOUNT_MSG_SENDER_POSITION, ACCOUNT_MSG_SENDER_LAST_NAME, ACCOUNT_MSG_NUMBER_OF_DOCS, ACCOUNT_MSG_FORMAT_VERSION, ACCOUNT_MSG_DOC_ID, ACCOUNT_MSG_IDENTIFIER, ACCOUNT_MSG_TAX_AUTH_BANK, ACCOUNT_MSG_BANK_SHORT_NAME, ACCOUNT_MSG_BANK_ADDRESS, ACCOUNT_MSG_BANK_REG_RUM, ACCOUNT_MSG_BRANCH_ORDER_NUMBER, ACCOUNT_MSG_BANK_BIC, ACCOUNT_MSG_BANK_INN, ACCOUNT_MSG_BANK_KPP, ACCOUNT_MSG_BANK_OGRN, ACCOUNT_MSG_ORG_FULL_NAME, ACCOUNT_MSG_PERS_FULL_NAME, ACCOUNT_MSG_STATE_REG_CERT, ACCOUNT_MSG_TAX_REG_CERT, ACCOUNT_MSG_CLIENT_INN, ACCOUNT_MSG_CLIENT_KPP, ACCOUNT_MSG_CLIENT_OGRN, ACCOUNT_MSG_CONTRACT_STATUS, ACCOUNT_MSG_CONTRACT_CREATION, ACCOUNT_MSG_CONTRACT_TERMINATION, ACCOUNT_MSG_CONTRACT_NUMBER, ACCOUNT_MSG_ACC_CREATION_CODE, ACCOUNT_MSG_ACC_CURR_CODE, ACCOUNT_MSG_ACC_TYPE, ACCOUNT_MSG_ACC_OPEN_DATE, ACCOUNT_MSG_ACC_CLOSE_DATE, ACCOUNT_MSG_ACC_NUMBER, ACCOUNT_MSG_ACC_CHANGE, ACCOUNT_MSG_ACC_NEW_NUMBER, ACCOUNT_MSG_ACC_CHANGE_DATE, ACCOUNT_MSG_ACC_CHANGE_TYPE, ACCOUNT_MSG_BANK_CHANGE_TYPE, ACCOUNT_MSG_BANK_OLD_SHORT_NAME, ACCOUNT_MSG_BANK_OLD_ADDRESS, ACCOUNT_MSG_BANK_OLD_REG_NUM, ACCOUNT_MSG_BR_OLD_ORDER_NUMBER, ACCOUNT_MSG_BANK_OLD_BIC, ACCOUNT_MSG_BANK_OLD_INN, ACCOUNT_MSG_BANK_OLD_KPP, ACCOUNT_MSG_BANK_OLD_OGRN, ACCOUNT_MSG_BANK_CHANGE_DATE, ACCOUNT_MSG_BANK_REP_POSITION, ACCOUNT_MSG_BANK_REP_FULL_NAME, ACCOUNT_MSG_DATE, ACCOUNT_MSG_BANK_PHONE, ACCOUNT_MSG_TYPE, ACCOUNT_MSG_KIND, ACCOUNT_MSG_IS_BANK_EMESSAGE, ACCOUNT_MSG_BANK_EMESSAGE_ENTER_DATE, ACCOUNT_MSG_IS_BANK_PMESSAGE, ACCOUNT_MSG_BANK_PMESSAGE_ENTER_DATE, ACCOUNT_MSG_IS_PAYER_PMESSAGE, ACCOUNT_MSG_PAYER_PMESSAGE_ENTER_DATE, ACCOUNT_MSG_FILE_NAME, ACCOUNT_MSG_INS_REG_NUM, ACCOUNT_MSG_INS_OPFR_CODE, ACCOUNT_MSG_DEADLINE_CHECKED) " +
                            "  VALUES(DEFAULT, ?, '7702070140**99795000220121015000001', 'СООБЩБАНКА', 'ручной_набор 1.00', '(495)777-77-78', 'Директор разработки', 'Сидоров', 1, '6.02', '7702070140**99795000220121015000001000001', ?, '9944', 'ФИЛИАЛ ОАО БАНК ВТБ В Г.БЛАГОВЕЩЕНСКЕ', '675005,77,,БЛАГОВЕЩЕНСК,,Центральная,46,,', 1000, 11, '041012762', '7702070140', '997950002', '1027739609391', 'ООО \"Идентификация506\"', NULL, '60,034436789', '60,258679899', '4444444605', '444444448', '5500000001506', 0, '2012-10-11 00:00:00.0', NULL, '566/10', 1, 0, 'расчетный', '2012-10-15 00:00:00.0', '2012-10-09 00:00:00.0', ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'нач отдела разработки', 'Петров,Иван,Петрович', '2012-10-15 00:00:00.0', '(495)125-45-73', ?, ?, 1, '2013-01-23 00:00:00.0', 0, NULL, 0, NULL, ?, '550000001506', '001', 1)";
            String number;
            //Идентификаторы статусов сообщения
            int[] messStatus = {1, 2, 3, 5, 6, 7, 10, 11};
            //Идентификаторы типов сообщения: Открытие, Закрытие, Изменение
            int[] messType = {0, 1, 2};
            //Идентификаторы видов сообщений: Корректирующее, Первичное. Отменяющее
            int[] messKind = {1, 2, 3};
            int messKindRand, messTypeRand;
            String messKindTypeRand;
            PreparedStatement statement1 = null;
            Random random = new Random();
            for (int i = 1; i <= countInsertedMessage; i++) {
                try {
                    number = messNumber + String.valueOf(stringWithLength(String.valueOf(i), '0', 6));
                    messTypeRand= messType[random.nextInt(messType.length - 1)];
                    messKindRand=messKind[random.nextInt(messKind.length - 1)];
                    messKindTypeRand= String.valueOf(messTypeRand)+String.valueOf(messKindRand);
                    statement1 = connection.prepareStatement(insertCommand);
                    statement1.setInt(1, messStatus[random.nextInt(messStatus.length - 1)]);
                    messPartNumber = getPartNumberFromMessTypeMessKind(messKindTypeRand);
                    statement1.setString(2, number + "," + messPartNumber);
                    statement1.setString(3, messNumAccount + String.valueOf(random.nextInt(999 - 900 + 1) + 900));
                    statement1.setInt(4, messTypeRand);
                    statement1.setInt(5, messKindRand);
                    statement1.setString(6, fileName.replaceFirst("01",  messKindTypeRand)+ number + "_" + messPartNumber + ".txt");
                    statement1.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (statement1 != null) statement1.close();
                }
            }
        } finally {
            if (connection != null) connection.close();
        }
    }


    private static String getPartNumberFromMessTypeMessKind(String typeKind)
    {
         /*
                    01 об открытии первичное
                    02 об открытии корректирующее
                    03 об открытии отменяющее
                    11 об изменении счета первичное
                    12 об изменении корректирующее
                    13 об изменении отменяющее
                    21 о закрытии первичное
                    22 о закрытии корректирующее
                    23 о закрытии отменяющее
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
            return "577";
        } else return null;

    }
}