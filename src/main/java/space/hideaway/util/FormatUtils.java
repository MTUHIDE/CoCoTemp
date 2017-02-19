package space.hideaway.util;

import java.text.DecimalFormat;

/**
 * Created by dough on 2017-02-19.
 */
public class FormatUtils
{
    public static String doubleToVisualString(double d)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(d);
    }
}
