package common;

public class Log {

    private final static boolean DEBUG = true;

    public static void i(Object o) {
        if (DEBUG) {
            System.out.println("I: " + o.toString());
        }
    }
}
