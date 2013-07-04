package Database;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 19.12.12
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
import java.sql.*;
import java.lang.*;
import static java.sql.DriverManager.*;

public class DB2Connection {

    public static Connection setConnection(String serverName, String userName, String userPassword)
            throws SQLException, ClassNotFoundException {

        Class.forName("com.ibm.db2.jcc.DB2Driver");
        return getConnection(serverName, userName, userPassword);
    }
 }
