package icstar.kbdsi.apps.util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class Convert {

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

//            SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy"); "31/12/1998"
//            SimpleDateFormat formatter2=new SimpleDateFormat("dd-MMM-yyyy"); "31-Dec-1998";
//            SimpleDateFormat formatter3=new SimpleDateFormat("MM dd, yyyy"); "12 31, 1998";
//            SimpleDateFormat formatter4=new SimpleDateFormat("E, MMM dd yyyy"); "Thu, Dec 31 1998";
//            SimpleDateFormat formatter5=new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss"); "Thu, Dec 31 1998 23:37:50";
//            SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"); "31-Dec-1998 23:37:50";

    public Date ConvertStringToDate(String strDate){
        try {
            Date convDate = dateFormat.parse(strDate);
            return convDate;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String ConvertDateToString(Date date){
            String strDate = dateFormat.format(date);
            return strDate;
    }

    public Date ConvertLocalDateToDate(LocalDate date){
        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date resDate = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
        return resDate;
    }

    public Date getLastDateOfCurrentMonth(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // set day to maximum
        calendar.set(Calendar.DAY_OF_MONTH,
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }

    public Date getFirstDateOfCurrentMonth(){
        //1st Way
//        LocalDate currDate = LocalDate.now();
//        ZoneId defaultZoneId = ZoneId.systemDefault();
//
//        Date firstDate = Date.from(currDate.withDayOfMonth(1).atStartOfDay(defaultZoneId).toInstant());
//        System.out.println(firstDate);

        //2nd Way
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // set day to minimum
        calendar.set(Calendar.DAY_OF_MONTH,
        calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }

    public Date getFirstDateOfYear(Integer year){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // set day to minimum
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getLastDateOfYear(Integer year){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // set day to maximum
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public int getLastDayOfMonthUsingCalendar(Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public int getFirstDayOfMonthUsingCalendar(Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        return cal.getActualMinimum(Calendar.DAY_OF_MONTH);
    }

    public static String ConvertToLocalTime(String dt, String timezone){
        SimpleDateFormat sdfOriginal = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdfOriginal.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = null;
        try {
            date1 = sdfOriginal.parse(dt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf.format(calendar.getTime());
    }
}
