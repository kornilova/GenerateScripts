package FBS;

import Database.DB2Connection;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class GeneratorUser {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = null;
        Statement statementSelectRoles = null;
        ResultSet resultSetRoles = null;
        PreparedStatement statementInsertUser = null;
        PreparedStatement statementInsertUserRoles = null;

        connection = DB2Connection.setConnection("jdbc:db2://192.168.1.238:50000/FBS", "db2inst", "db2inst");

        String insertCommandUser = "INSERT INTO DB2INST.FBS_USER(USER_ID, OPFR_ID, USER_LOGIN, USER_FULL_NAME, USER_POSITION, USER_PASSWORD_HASH, USER_FAILED_ATTEMPTS, USER_PASSWORD_EXPIRED, USER_DISABLED, USER_LAST_LOGIN_TIME, USER_RESPONSIBLE_FOR_EXCHANGE, USER_PHONE_NUMBER)" +
                " VALUES(DEFAULT, NULL, ?, ?, NULL, 'agrIpW2/wWTjI', 0, 0, 0, NULL, 0, ?)";

        String insertCommandRole = "INSERT INTO DB2INST.FBS_USER_ROLE(USER_ROLE_ID, USER_ID, ROLE_ID)" +
                "  VALUES(DEFAULT, ?, ?)";
        try {
            statementSelectRoles = connection.createStatement();

            resultSetRoles = statementSelectRoles.executeQuery("SELECT ROLE_ID, count(*) over (partition by 1) as CNT FROM FBS_ROLE");

            int[] roles = null;
            int i = 0;
            while (resultSetRoles.next()) {
                if (roles == null) roles = new int[resultSetRoles.getInt("CNT")];
                roles[i] = resultSetRoles.getInt("ROLE_ID");
                i++;
            }

            statementInsertUser = null;
            Random random = new Random();
            String userNamePost;
            for (i = 0; i < 15; i++) {
                statementInsertUser = connection.prepareStatement(insertCommandUser, Statement.RETURN_GENERATED_KEYS);
                userNamePost = "autest" + String.valueOf(i);
                statementInsertUser.setString(1, userNamePost);
                statementInsertUser.setString(2, userNamePost);
                statementInsertUser.setString(3, "8(495)999-12-23");
                statementInsertUser.executeUpdate();
                ResultSet rs = statementInsertUser.getGeneratedKeys();
                rs.next();
                statementInsertUserRoles = connection.prepareStatement(insertCommandRole);
                statementInsertUserRoles.setInt(1, rs.getInt(1));
                statementInsertUserRoles.setInt(2, roles[random.nextInt(roles.length - 1)]);
                statementInsertUserRoles.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statementSelectRoles != null) statementSelectRoles.close();
            if (resultSetRoles != null) resultSetRoles.close();
            if (statementInsertUser != null) statementInsertUser.close();
            if (statementInsertUserRoles != null) statementInsertUserRoles.close();
            connection.close();
        }
    }
}
