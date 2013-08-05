package General;

import Utils.CustomClipboard;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 02.08.13
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class GetGuid {
    public static void main(String[] args) throws ClassNotFoundException {
        String res = UUID.randomUUID().toString();
        System.out.print(res);
        CustomClipboard.pushInClipboard(res);
    }

}
