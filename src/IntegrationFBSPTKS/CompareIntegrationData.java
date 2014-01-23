package IntegrationFBSPTKS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nataliya.Gordeeva
 * Date: 28.10.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class CompareIntegrationData {

    final String delimiter = "/*=================================================*/\n";
    final String smallDelimiter = "*****\n";
    public Connection connectionPTKS;
    public Connection connectionFBS;
    private String opfr_code;
    public List<MetaPTKSTable> lPTKSTables;
    public List<MetaPTKSTable> lPTKSDictionaries;
    public List<MetaFBSTable> lFBSTables;
    public List<MetaExchangePTKS> lExchangePTKSData;
    public List<MetaExchangeFBS> lExchangeFBSData;
    String[] fbsTables;
    String[] fbsDictionary;
    String queryGetIsExportedPTKSTable = "SELECT TB.TABNAME, COL.COLNAME" +
            "    FROM syscat.TABLES TB" +
            "    INNER JOIN SYSCAT.COLUMNS COL ON COL.TABNAME=TB.TABNAME" +
            "    WHERE TB.TABSCHEMA = 'DB2INST'" +
            "    AND COL.COLNAME LIKE '%_IS_EXPORTED' order by TB.TABNAME";

    String queryGetDefectDataFromFBSPTKSTable = "SELECT TB.TABNAME, COL.COLNAME" +
            "    FROM syscat.TABLES TB" +
            "    INNER JOIN SYSCAT.COLUMNS COL ON COL.TABNAME=TB.TABNAME" +
            "    WHERE TB.TABSCHEMA = 'DB2INST'" +
            "    AND COL.COLNAME LIKE '%_DEFLECT_BY_FBS_DATE' order by TB.TABNAME";

    String queryGetActualFBSPTKSTable = "SELECT TB.TABNAME, COL.COLNAME" +
            "    FROM syscat.TABLES TB" +
            "    INNER JOIN SYSCAT.COLUMNS COL ON COL.TABNAME=TB.TABNAME" +
            "    WHERE TB.TABSCHEMA = 'DB2INST'" +
            "    AND COL.COLNAME LIKE '%_ACTUAL_FBS' order by TB.TABNAME";

    public FileWriter fileWriter;

    public CompareIntegrationData(Connection connectionPTKS, Connection connectionFBS) throws IOException {
        this.connectionFBS = connectionFBS;
        this.connectionPTKS = connectionPTKS;
        //Сформировать список таблиц в ФБС для заливки
        fbsTables = new String[]{"FBS_INSURER", "FBS_OPF", "FBS_PAYMENT", "FBS_OKVED",
                "FBS_OKATO", "FBS_REG_TYPE", "FBS_IFNS", "FBS_RO", "FBS_REG_ORGAN", "FBS_KLADR", "FBS_TO"};



        lExchangePTKSData = new ArrayList<MetaExchangePTKS>();
        lExchangeFBSData = new ArrayList<MetaExchangeFBS>();
        lPTKSTables = new ArrayList<MetaPTKSTable>();
        lFBSTables = new ArrayList<MetaFBSTable>();
        lPTKSDictionaries = new ArrayList<MetaPTKSTable>();

        for (String tb : fbsTables) {
            MetaFBSTable metaFBSTable = new MetaFBSTable();
            metaFBSTable.name = tb;
            lFBSTables.add(metaFBSTable);
        }
        //Сформировать файл для выгрузки данных по сравнению
        Date date = new Date();
        File fileExport = new File("C:\\Users\\Nataliya.Gordeeva\\Desktop\\Info\\Testing\\FBS2\\Integration\\Result_compare_" + date.getTime() + ".txt");

        if (!fileExport.exists()) {
            fileExport.createNewFile();
        }

        fileWriter = new FileWriter(fileExport);

    }

    private void setExchangedOPFRCodePTKS() throws Exception {
        Statement stmtRecord = null;
        ResultSet result = null;
        try {
            stmtRecord = connectionPTKS.createStatement();
            result = stmtRecord.executeQuery("SELECT MRDV_CODE FROM STH_MAIN_REG_DIV_PFR WHERE MRDV_CURRENT=1");

            result.next();
            opfr_code = result.getString(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (result != null) result.close();
            if (stmtRecord != null) stmtRecord.close();
        }

        if (opfr_code == null) throw new Exception("Code OPFR is not found. Analyse process is broken");
    }

    public void closeConnection() throws SQLException {
        if (connectionPTKS != null) {
            connectionPTKS.close();
        }
        if (connectionFBS != null) {
            connectionFBS.close();
        }
    }

    public void compareCountData() throws Exception {
        prepareCompareCountData();
        compare();
    }

    private void prepareCompareCountData() throws Exception {
        //Инициализировать код ОПФР для обмена
        setExchangedOPFRCodePTKS();
        getExportDefectDataFromFBSPTKSData();
        //количество записей, выгруженныхи и не выгруженных из ПТКС в ФБС
        getExchangedCountDataFromPTKS();
        //количество записей, импортировванные в ФБС
        getImportedCountFBS();

    }

    private void compare() throws IOException {
        StringBuilder builder = new StringBuilder();
        boolean isExp;
        try {
            int expCnt = 0;
            for (MetaExchangeFBS metaExchangeFBS : lExchangeFBSData) {
                isExp = false;
                for (MetaExchangePTKS metaExchangePTKS : lExchangePTKSData) {

                    if (metaExchangeFBS.metaTable.name.equals("FBS_INSURER") && metaExchangePTKS.metaTable.name.equals("STH_INSURER")
                            || metaExchangeFBS.metaTable.name.equals("FBS_OPF") && metaExchangePTKS.metaTable.name.equals("STH_OPF")
                            || metaExchangeFBS.metaTable.name.equals("FBS_OKVED") && metaExchangePTKS.metaTable.name.equals("STH_OKVED")
                            || metaExchangeFBS.metaTable.name.equals("FBS_OKATO") && metaExchangePTKS.metaTable.name.equals("STH_OKATO")
                            || metaExchangeFBS.metaTable.name.equals("FBS_REG_TYPE") && metaExchangePTKS.metaTable.name.equals("STH_REGISTER_KIND")
                            || metaExchangeFBS.metaTable.name.equals("FBS_IFNS") && metaExchangePTKS.metaTable.name.equals("STH_TERRITORY_DPT_FNS")
                            || metaExchangeFBS.metaTable.name.equals("FBS_RO") && metaExchangePTKS.metaTable.name.equals("STH_TERRITORY_DPT_PFR")
                            || metaExchangeFBS.metaTable.name.equals("FBS_REG_ORGAN") && metaExchangePTKS.metaTable.name.equals("STH_TERRITORY_DPT_REG")
                            || metaExchangeFBS.metaTable.name.equals("FBS_TO") && metaExchangePTKS.metaTable.name.equals("STH_TERRITORIAL_ORGAN")) {
                        builder.append(delimiter)
                                .append("Table FBS: " + metaExchangeFBS.metaTable.name + "\n")
                                .append("Table PTKS: " + metaExchangePTKS.metaTable.name + "\n")
                                .append("Exported record count from PTKS: " + metaExchangePTKS.expImpCount + "\n")
                                .append("Defect record count from PTKS: " + metaExchangePTKS.defectCountFromPTKS + "\n")
                                .append("Imported record count in FBS: " + metaExchangeFBS.expImpCount + "\n");
                        expCnt = metaExchangePTKS.expImpCount - metaExchangePTKS.defectCountFromPTKS;
                        if (expCnt != metaExchangeFBS.expImpCount) {
                            builder.append("ERROR: Imported " + metaExchangeFBS.expImpCount +
                                    " and exported record " + expCnt + " count are not equals in table " + metaExchangeFBS.metaTable.name + "\n");
                        }
                        isExp = true;
                    } else {
                        //обрабатываем справочник Адресов КЛАДР и группу таблиц на стороне ПТКС
                        if (metaExchangeFBS.metaTable.name.equals("FBS_KLADR")) {
                            for (MetaFBSTable inTable : metaExchangeFBS.metaTable.listTables) {
                                if (metaExchangePTKS.metaTable.name.equals("STH_REGION") && inTable.name.equals("REGION")
                                        || metaExchangePTKS.metaTable.name.equals("STH_TERRITORY") && inTable.name.equals("TERRITORY")
                                        || metaExchangePTKS.metaTable.name.equals("STH_CITY") && inTable.name.equals("CITY")
                                        || metaExchangePTKS.metaTable.name.equals("STH_TOWN") && inTable.name.equals("TOWN")
                                        || metaExchangePTKS.metaTable.name.equals("STH_STREET") && inTable.name.equals("STREET")
                                        ) {
                                    builder.append(delimiter)
                                            .append("Table FBS: " + metaExchangeFBS.metaTable.name + "\n")
                                            .append("Table PTKS: " + metaExchangePTKS.metaTable.name + "\n")
                                            .append("Exported record count from PTKS: " + metaExchangePTKS.expImpCount + "\n")
                                            .append("Defect record count from PTKS: " + metaExchangePTKS.defectCountFromPTKS + "\n")
                                            .append("Imported record count in FBS: " + inTable.recordCount + "\n");
                                    expCnt = metaExchangePTKS.expImpCount - metaExchangePTKS.defectCountFromPTKS;
                                    if (expCnt != inTable.recordCount) {
                                        builder.append("ERROR: Imported " + inTable.recordCount +
                                                " and exported record " + expCnt + " count are not equals in table " + metaExchangeFBS.metaTable.name + "\n");
                                    }
                                    builder.append(smallDelimiter);
                                    break;
                                }

                            }
                        }

                    }

                    if (isExp) break;
                }
            }

            fileWriter.write(builder.toString());

        } catch (Exception ex) {

            ex.printStackTrace();
        } finally {
            fileWriter.close();
        }
    }

    //Количество выгруженных записей из ПТКС в ФБС в разрезе таблиц
    private void getExchangedCountDataFromPTKS() throws SQLException {
        if (lPTKSTables != null) {
            Statement stmtCountRecord = null;
            ResultSet resultRecordCount = null;
            for (MetaPTKSTable metaPTKSTable : lPTKSTables) {
                try {

                    MetaExchangePTKS exchangeData = new MetaExchangePTKS();
                    stmtCountRecord = connectionPTKS.createStatement();
                    resultRecordCount = stmtCountRecord.executeQuery("select count(1) from " + metaPTKSTable.name + " where " + metaPTKSTable.columnNameIsExported + " = 1");

                    resultRecordCount.next();
                    exchangeData.expImpCount = resultRecordCount.getInt(1);

                    resultRecordCount = stmtCountRecord.executeQuery("select count(1) from " + metaPTKSTable.name + " where " + metaPTKSTable.columnNameDeflectDate + " IS NOT NULL");
                    resultRecordCount.next();
                    exchangeData.defectCountFromPTKS = resultRecordCount.getInt(1);
                    exchangeData.metaTable = metaPTKSTable;
                    lExchangePTKSData.add(exchangeData);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    if (resultRecordCount != null) resultRecordCount.close();
                    if (stmtCountRecord != null) stmtCountRecord.close();
                }
            }
        }
    }


    private void getExportDefectDataFromFBSPTKSData() throws SQLException {
        Statement stmtGetTablesExported = null;
        ResultSet resultGetTablesExported = null;
        Statement stmtGetTablesDefect = null;
        ResultSet resultGetTablesDefect = null;
        try {
            stmtGetTablesExported = connectionPTKS.createStatement();
            stmtGetTablesDefect = connectionPTKS.createStatement();
            resultGetTablesExported = stmtGetTablesExported.executeQuery(queryGetIsExportedPTKSTable);
            resultGetTablesDefect = stmtGetTablesDefect.executeQuery(queryGetDefectDataFromFBSPTKSTable);
            while (resultGetTablesExported.next()) {
                if (resultGetTablesExported.getString(1).equals("STH_PERSONAL_ACCOUNT") && resultGetTablesExported.getString(2).equals("INS_IS_EXPORTED"))
                    continue;
                    resultGetTablesDefect.next();
                    MetaPTKSTable table = new MetaPTKSTable();
                    table.name = resultGetTablesExported.getString(1);
                    table.columnNameIsExported = resultGetTablesExported.getString(2);
                    table.columnNameDeflectDate = resultGetTablesDefect.getString(2);
                    lPTKSTables.add(table);
            }
        } finally {
            if (resultGetTablesExported != null) resultGetTablesExported.close();
            if (stmtGetTablesExported != null) stmtGetTablesExported.close();
            if (resultGetTablesDefect != null) resultGetTablesDefect.close();
            if (stmtGetTablesDefect != null) stmtGetTablesDefect.close();
        }
    }


    private void getImportedCountFBS() throws SQLException {
        if (lFBSTables != null) {
            Statement stmtCountRecord = null;
            ResultSet resultRecordCount = null;
            for (MetaFBSTable metaFBSTable : lFBSTables)
                try {

                    MetaExchangeFBS exchangeData = new MetaExchangeFBS();
                    stmtCountRecord = connectionFBS.createStatement();
                    if (metaFBSTable.name.equals("FBS_INSURER")
                            || metaFBSTable.name.equals("FBS_OPF")
                            || metaFBSTable.name.equals("FBS_OKVED")
                            || metaFBSTable.name.equals("FBS_OKATO")
                            || metaFBSTable.name.equals("FBS_REG_TYPE")
                            || metaFBSTable.name.equals("FBS_IFNS")
                            || metaFBSTable.name.equals("FBS_REG_ORGAN")
                            || metaFBSTable.name.equals("FBS_TO")) {
                        resultRecordCount = stmtCountRecord.executeQuery("select count(*) from " + metaFBSTable.name + " t inner join FBS_OPFR opfr on opfr.OPFR_ID=t.OPFR_ID where opfr.OPFR_CODE='" + opfr_code + "'");

                        resultRecordCount.next();
                        exchangeData.expImpCount = resultRecordCount.getInt(1);
                    } else if (metaFBSTable.name.equals("FBS_RO")) {
                        resultRecordCount = stmtCountRecord.executeQuery("select count(ro.RO_ID) from " + metaFBSTable.name + " ro" +
                                " inner join FBS_TO t on t.TO_ID=ro.TO_ID" +
                                " inner join FBS_BO bo on bo.BO_ID=ro.BO_ID" +
                                " inner join FBS_OPFR opfr on opfr.OPFR_ID=t.OPFR_ID and bo.OPFR_ID=opfr.OPFR_ID where opfr.OPFR_CODE='" + opfr_code + "'");
                        resultRecordCount.next();
                        exchangeData.expImpCount = resultRecordCount.getInt(1);
                    } else
                        //В Справочнике КЛАДР в ФБС нужно считать общее количество записей по типу объектов
                        if (metaFBSTable.name.equals("FBS_KLADR")) {
                            resultRecordCount = stmtCountRecord.executeQuery("select t.cnt, t.KLADR_TYPE_ID, kladrType.KLADR_TYPE_NAME" +
                                    " from " +
                                    " (select count(KLADR_ID) as cnt, kladr.KLADR_TYPE_ID" +
                                    " from FBS_KLADR kladr" +
                                    " inner join FBS_OPFR opfr on opfr.OPFR_ID=kladr.OPFR_ID" +
                                    " where opfr.OPFR_CODE='" + opfr_code + "'" +
                                    " group by kladr.KLADR_TYPE_ID) t " +
                                    " inner join FBS_KLADR_TYPE kladrType on kladrType.KLADR_TYPE_ID = t.KLADR_TYPE_ID " +
                                    " order by t.KLADR_TYPE_ID");

                            metaFBSTable.listTables = new ArrayList<MetaFBSTable>();
                            while (resultRecordCount.next()) {
                                MetaFBSTable table = new MetaFBSTable();
                                if (resultRecordCount.getString(2).equals("1"))
                                    table.name = "REGION";  //Субъект
                                if (resultRecordCount.getString(2).equals("2"))
                                    table.name = "TERRITORY";  //Район
                                if (resultRecordCount.getString(2).equals("4"))
                                    table.name = "CITY";  //Город
                                if (resultRecordCount.getString(2).equals("3"))
                                    table.name = "TOWN";  //Населённый пункт
                                if (resultRecordCount.getString(2).equals("5"))
                                    table.name = "STREET";  //Улица
                                table.recordCount = resultRecordCount.getInt(1);
                                metaFBSTable.listTables.add(table);
                            }
                        }
                    //TODO: платежи остаются на потом
                    exchangeData.metaTable = metaFBSTable;
                    lExchangeFBSData.add(exchangeData);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    if (resultRecordCount != null) resultRecordCount.close();
                    if (stmtCountRecord != null) stmtCountRecord.close();
                }
        }
    }

    private void compareExportedDictionaryFromFBS()
    {

    }

    private void getExportedDictionaryToPTKS() throws SQLException {
        Statement stmtGetDictionaries= null;
        ResultSet resultGetDictionaries = null;
        try {
            stmtGetDictionaries = connectionPTKS.createStatement();
            resultGetDictionaries = stmtGetDictionaries.executeQuery(queryGetActualFBSPTKSTable);
            while (resultGetDictionaries.next()) {
                MetaPTKSTable table = new MetaPTKSTable();
                table.name = resultGetDictionaries.getString(1);
                table.columnNameIsActualFBS = resultGetDictionaries.getString(2);
                lPTKSDictionaries.add(table);
            }
        } finally {
            if (resultGetDictionaries != null) resultGetDictionaries.close();
            if (stmtGetDictionaries != null) stmtGetDictionaries.close();
        }
    }
}

