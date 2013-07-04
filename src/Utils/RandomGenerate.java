package Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 09.04.13
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class RandomGenerate {

    private static final String FULL_SIMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            // Math.random() returns greater than or equal to 0.0 and less than 1.0
            int ndx = (int) (Math.random() * FULL_SIMBOLS.length());
            if(i==0 || i==length-1)  sb.append('S');
            else sb.append(FULL_SIMBOLS.charAt(ndx));
        }

        return sb.toString();
    }
}
