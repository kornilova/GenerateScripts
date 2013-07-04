package General; /**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 22.01.13
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
 */

import static Utils.DateCalc.calculateDays;

public class GetDiffBetweenDates {
    public static void main(String[] args) throws ClassNotFoundException {
        String dateBegin= "2012/11/25";
        String dateEnd=  "2012/12/09";
        System.out.println(calculateDays(dateBegin, dateEnd));
    }
}
