package components;

import java.text.SimpleDateFormat;

/**
 * @author ali
 * @created_on 7/17/20
 */
public class Helpers {
    public static String getTime(){
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String current_time_str = time_formatter.format(System.currentTimeMillis());
        return current_time_str;
    }
}
