package Utils;
import java.util.Date;
import java.util.Calendar;
/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 22.01.13
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */
public class DateCalc {
    public static long calculateDays(String startDate, String endDate)
    {
        Date sDate = new Date(startDate);
        Date eDate = new Date(endDate);
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(sDate);
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(eDate);
        return daysBetween(cal3, cal4);
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
}
