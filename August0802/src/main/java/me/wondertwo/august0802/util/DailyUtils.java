package me.wondertwo.august0802.util;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.wondertwo.august0802.R;

/**
 * Created by Eric on 15/6/9.
 */
public class DailyUtils {

    private DailyUtils() {

    }

    public static String getDisplayDate(Context context, int datetime) {
        String[] weekTitle = context.getResources().getStringArray(R.array.weeks);

        int year = datetime / 10000;
        int month = (datetime % 10000) / 100 - 1;
        int day = datetime % 100;
        Date date = new GregorianCalendar(year, month, day).getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(calendar.DAY_OF_WEEK);

        return TimeUtils.convertByFormatter(date, "MM月dd日") + " " + weekTitle[week - 1];
    }

}
