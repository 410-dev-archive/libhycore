package me.libhycore;

public class CoreLogger {

    private static boolean printDebug = false;

    public static enum EventType {
        INFO, WARNING, ERROR
    }

    public static void print(EventType eventType, String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String callerClass = ste.getClassName();
        String time = CoreDate.timestamp();
        String s = "[" + time + "] [" + callerClass + "] [" + eventType.toString() + "] " + message;
        if (eventType.equals(EventType.ERROR)) {
            System.err.println(s);
        }else{
            System.out.println(s);    
        }
    }

    public static void debug(EventType eventType, String message) {
        if (printDebug) print(eventType, message);
    }

    public static void setDebugPrinting(boolean printDebug) {
        CoreLogger.printDebug = printDebug;
    }
}
