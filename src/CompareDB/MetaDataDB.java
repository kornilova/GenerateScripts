package CompareDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 08.02.13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class MetaDataDB {

    private String queryShowTables;
    private String  schoolId;
    private Connection connection;
    private DataBaseType dataBaseType;
    public List<MetaTable> lTables = new ArrayList<MetaTable>();


    public MetaDataDB(Connection connection, DataBaseType dataBaseType, String schoolId) {
        this.connection = connection;
        this.dataBaseType=dataBaseType;
        this.schoolId=schoolId;
        switch(dataBaseType)
        {
            case MYSQL:    queryShowTables= "SHOW TABLES";  break;
            case ORACLE:   queryShowTables= "SELECT TABLE_NAME FROM USER_TABLES"; break;
        }

    }

    public void getMetadata() throws SQLException {
        try {
            getTables();
            getColumns();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getTables() throws SQLException {

        Statement stmtShowTables = null;
        ResultSet resultSetShowTables = null;
        try {
            stmtShowTables = connection.createStatement();
            resultSetShowTables = stmtShowTables.executeQuery(queryShowTables);
            while (resultSetShowTables.next()) {
                MetaTable table = new MetaTable();
                table.name =  (resultSetShowTables.getString(1)).trim().toUpperCase();
                lTables.add(table);
            }
        } finally {
            if (resultSetShowTables != null) resultSetShowTables.close();
            if (stmtShowTables != null) {
                stmtShowTables.close();
            }
        }
    }

    private void getColumns() throws SQLException {
        if (lTables != null) {
            Statement stmtColumns = null;
            ResultSet resultSetColumns = null;
            ResultSetMetaData rsMetaData = null;
            Statement stmtCountRecord = null;
            ResultSet resultRecordCount = null;
            String conditionWhere;
            try {
                stmtColumns = connection.createStatement();
                for (MetaTable metaTable : lTables) {
                    resultSetColumns = metaTable.name.equals("USER")&&dataBaseType==DataBaseType.ORACLE?
                            stmtColumns.executeQuery("select * from \"user\""):
                            stmtColumns.executeQuery("select * from " + metaTable.name);
                    rsMetaData = resultSetColumns.getMetaData();
                    metaTable.columnCount= rsMetaData.getColumnCount();
                    metaTable.listColumns = new ArrayList<MetaColumn>();
                    stmtCountRecord = connection.createStatement();
                    conditionWhere="";
                    if(dataBaseType==DataBaseType.ORACLE)
                    {
                        conditionWhere = " where EXT_SCHOOL_ID=(select EXT_SCHOOL_ID from SCHOOL where SCHOOL_ID="+ String.valueOf(schoolId) +" and ROWNUM<=1)";
                        if(metaTable.name.equals("USER"))
                            resultRecordCount = stmtCountRecord.executeQuery("select count(*) from \"user\""+conditionWhere);
                        else  resultRecordCount = stmtCountRecord.executeQuery("select count(*) from " + metaTable.name+conditionWhere);
                    }
                    else  resultRecordCount = stmtCountRecord.executeQuery("select count(*) from " + metaTable.name);

                    resultRecordCount.next();
                    metaTable.recordCount =  resultRecordCount.getInt(1);
                    for (int i = 1; i <= metaTable.columnCount; i++) {
                        MetaColumn metaColumn = new MetaColumn();
                        metaColumn.name = rsMetaData.getColumnName(i).toUpperCase();
                        metaColumn.dataType = rsMetaData.getColumnTypeName(i);
                        metaColumn.length = rsMetaData.getColumnDisplaySize(i);
                        metaColumn.precision = rsMetaData.getPrecision(i);
                        metaColumn.scale = rsMetaData.getScale(i);
                        metaColumn.isNull = rsMetaData.isNullable(i);

                        metaTable.listColumns.add(metaColumn);
                    }
                }
            }
             catch(SQLException ex)
                {ex.printStackTrace();}
             finally {
                if (resultSetColumns != null) resultSetColumns.close();
                if (stmtColumns != null) {
                    stmtColumns.close();
                }
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
