package CompareDB;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 08.02.13
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */
public class MetaColumn {

    public String name;
    public String dataType;
    public int length;
    public int isNull;
    public int precision;
    public int scale;
                                                           //Oracle
    public boolean dataTypeLengthEquals(String dataTypeIn, int dataLengthIn, DataBaseType source, DataBaseType receiver) {
        boolean res = false;
        if (dataTypeIn.equals(dataType) && dataLengthIn>=length) res = true;
        else
            //Будем считать, что источник сравнения БД MYSQL
            if (source == DataBaseType.MYSQL && receiver == DataBaseType.ORACLE) {
            if (dataType.equals(ColumnDataTypeMySQL.VARCHAR.toString()) && dataTypeIn.equals(ColumnDataTypeOracle.VARCHAR2.toString())
                    && length<=dataLengthIn)
                res = true;
            else if (dataType.equals(ColumnDataTypeMySQL.VARCHAR.toString()) &&  dataTypeIn.equals(ColumnDataTypeOracle.CHAR.toString())
                    &&length==dataLengthIn && length==1)
                res = true;
            else if (dataType.equals(ColumnDataTypeMySQL.CHAR.toString()) &&  dataTypeIn.equals(ColumnDataTypeOracle.CHAR.toString())
                    &&length<=dataLengthIn)
                res = true;
            else if ((dataType.equals(ColumnDataTypeMySQL.INT.toString()) || dataType.equals(ColumnDataTypeMySQL.INTEGER.toString()))
                    && dataTypeIn.equals(ColumnDataTypeOracle.NUMBER.toString())
                    && dataLengthIn>=10)
                res = true;    //MAX_LENGTH(2147483647)=10
            else if ((dataType.equals(ColumnDataTypeMySQL.YEAR.toString()))
                    && dataTypeIn.equals(ColumnDataTypeOracle.NUMBER.toString())
                    && dataLengthIn>=3)
                res = true;
            else if (dataType.equals(ColumnDataTypeMySQL.BIGINT.toString()) && dataTypeIn.equals(ColumnDataTypeOracle.NUMBER.toString())
                    && dataLengthIn>=20)
                res = true;   //MAX_LENGTH(18446744073709551615)=20
            else if (dataType.equals(ColumnDataTypeMySQL.TINYINT.toString()) && dataTypeIn.equals(ColumnDataTypeOracle.NUMBER.toString()))
                res = true;
            else if ((dataType.equals(ColumnDataTypeMySQL.TEXT.toString())
                            || dataType.equals(ColumnDataTypeMySQL.VARCHAR.toString()))
                    && (dataTypeIn.equals(ColumnDataTypeOracle.CLOB.toString())
                            || dataTypeIn.equals(ColumnDataTypeOracle.NCLOB.toString())) )
                res = true;

            else if ((dataType.equals(ColumnDataTypeMySQL.DATE.toString())
                    || dataType.equals(ColumnDataTypeMySQL.DATETIME.toString())
                    || dataType.equals(ColumnDataTypeMySQL.TIMESTAMP.toString()))
                    && dataTypeIn.equals(ColumnDataTypeOracle.DATE.toString()))
                res = true;
            else if (dataType.equals(ColumnDataTypeMySQL.DOUBLE.toString()) && dataTypeIn.equals(ColumnDataTypeOracle.BINARY_DOUBLE.toString()))
                res = true;
            else if (dataType.equals(ColumnDataTypeMySQL.FLOAT.toString()) && dataTypeIn.equals(ColumnDataTypeOracle.BINARY_FLOAT.toString()))
                res = true;
        }
        return res;
    }
}
