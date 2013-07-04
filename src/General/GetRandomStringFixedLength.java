package General;

import Utils.CustomClipboard;
import java.io.IOException;
import java.sql.SQLException;

import static Utils.RandomGenerate.getRandomString;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 09.04.13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class GetRandomStringFixedLength {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        String res = getRandomString(250);
        System.out.println(res);
        System.out.println(res.length());
        CustomClipboard.pushInClipboard(res);
    }

}
