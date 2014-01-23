package HIMKI;

import CompareDB.CompareDataBaseMeta;
import CompareDB.DataBaseType;
import CompareDB.MetaDataDB;

import java.io.IOException;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 08.02.13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class CompareOracleMySQLDatabase {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        int schoolId = 343;

        String driverName = "com.mysql.jdbc.Driver";
        Class.forName(driverName);

        String serverNameMySQL = "192.168.1.98:3306";
        String dataBaseMySQL = "school" + schoolId;
        String urlMySQL = "jdbc:mysql://" + serverNameMySQL + "/" + dataBaseMySQL;
        String usernameMySQL = "inclass";
        String passwordMySQL = "inclass";

        /*String serverNameMySQL = "192.168.1.103:3306";
        String dataBaseMySQL = "school"+schoolId;
        String urlMySQL = "jdbc:mysql://" + serverNameMySQL + "/" + dataBaseMySQL;
        String usernameMySQL = "user";
        String passwordMySQL = "himki";*/

        MetaDataDB metaDataDBdMySQL = null;

        //Oracle
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        String usernameOracle = "HIMKI_MOU_TST";
        String passwordOracle = "HIMKI_MOU_TST";
        MetaDataDB metaDataDBOracle = null;

        try {
            metaDataDBdMySQL = new MetaDataDB(DriverManager.getConnection(urlMySQL, usernameMySQL, passwordMySQL), DataBaseType.MYSQL, String.valueOf(schoolId));
            metaDataDBdMySQL.getMetadata();


            metaDataDBOracle = new MetaDataDB(DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.98:1521:himki", usernameOracle, passwordOracle), DataBaseType.ORACLE, String.valueOf(schoolId));
            metaDataDBOracle.getMetadata();

            CompareDataBaseMeta compareDataBaseMeta = new CompareDataBaseMeta(metaDataDBdMySQL, metaDataDBOracle, String.valueOf(schoolId));
            compareDataBaseMeta.getCompare();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (metaDataDBOracle != null) metaDataDBOracle.closeConnection();
            if (metaDataDBdMySQL != null) metaDataDBdMySQL.closeConnection();
        }

    }
}
