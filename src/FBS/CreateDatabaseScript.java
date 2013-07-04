package FBS;

import Database.DB2Connection;
import sun.misc.IOUtils;

import java.sql.*;
import java.io.*;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 19.12.12
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class CreateDatabaseScript {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try
        {
            connection = DB2Connection.setConnection("jdbc:db2://192.168.1.238:50000/FBS", "db2inst", "db2inst");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM FBS_INSURER");

            Date date= new Date();

            File fileExport = new File("C:\\Users\\nkornilova\\Desktop\\Info\\Testing\\FBS2\\SCRIPT_INSERT_"+ date.getTime()+".txt");

            if(!fileExport.exists()) {
                fileExport.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(fileExport);

            try
            {
                ResultSetMetaData resultSetMData = resultSet.getMetaData();
                /*index 53   (always)
                SELECT NAME, GENERATED from SYSIBM.SYSCOLUMNS
                where TBNAME='FBS_INSURER'
                and NAME='INSURER_COMP_NAME'*/

                int columnCount = resultSetMData.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 0; i < columnCount; i++ )
                {
                    columnNames[i] = resultSetMData.getColumnName(i+1);
                }
                String resScript="";
                while (resultSet.next())
                {
                        for(int i=0;i<columnCount;i++)
                            resScript+=columnNames[i]+":"+resultSet.getString(columnNames[i])+((i!=columnCount-1)?  ", ":  ";");

                    fileWriter.write(resScript + "\n");
                    resScript="";
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                fileWriter.close();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(resultSet!=null) resultSet.close();
            if(statement!=null) statement.close();
            if(connection!=null) connection.close();
        }


    }
}
