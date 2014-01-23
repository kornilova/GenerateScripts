package IntegrationFBSPTKS;

import Database.DB2Connection;

import java.io.IOException;
import java.sql.SQLException;

public class CompareIntegrationResultMain {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        CompareIntegrationData compareDataDB2DB = null;
        try {

            compareDataDB2DB = new CompareIntegrationData(DB2Connection.setConnection("jdbc:db2://192.168.1.13:50000/STRAH", "db2inst", "db2inst"),
                    DB2Connection.setConnection("jdbc:db2://192.168.1.70:50000/FBS", "db2inst", "db2inst"));
            compareDataDB2DB.compareCountData();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (compareDataDB2DB != null) compareDataDB2DB.closeConnection();
        }
    }
}
