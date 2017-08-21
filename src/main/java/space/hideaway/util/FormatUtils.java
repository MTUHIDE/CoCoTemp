package space.hideaway.util;

import java.text.DecimalFormat;

/**
 * Created by dough
 * 2017-02-19
 *
 * Used to format numbers
 */
public class FormatUtils
{
    /**
     * Used to format doubles to two decimal places. (i.e. 2.23)
     *
     * @param d the number to format
     * @return the formatted number
     */
    public static String doubleToVisualString(double d)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(d);
    }
}
