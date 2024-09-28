package nl._99th_dutchies.halloween_heist.util;

public class TimeHelper {
    public static String secondsToTime(int seconds) {
        String time = "";
        if(seconds > 60 * 60) {
            int h = (int) Math.floor(seconds / (60*60));
            time += h + (h == 1 ? " hour, " : " hours, ");
        }
        if(seconds > 60) {
            int m = ((int) Math.floor(seconds / 60)) % (60*60);
            time += m + (m == 1 ? " minute, " : " minutes, ");
        }
        int s = seconds % 60;
        time += s + (s == 1 ? " second" : " seconds");
        return time;
    }
}
