package com.eslamhossam23bichoymessiha.projetelim.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by bichoymessiha on 03-Jan-17.
 */

public class TimeFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
//        return mValues[(int) value];
        Calendar c = Calendar.getInstance();
        c.setTime(new Date((long) value));
        String hour = c.get(Calendar.HOUR_OF_DAY) + "";
        String minute = c.get(Calendar.MINUTE) + "";
        return hour + "h" + minute;
    }

    /** this is only needed if numbers are returned, else return 0 */

}
