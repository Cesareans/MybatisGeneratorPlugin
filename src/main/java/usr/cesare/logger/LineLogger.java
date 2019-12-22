package usr.cesare.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Cesare
 */
public class LineLogger {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String toJsonString(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }

    public static void log(String message) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]:" + message);
    }

    public static void log(Object object) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]:" + object.toString());
    }

    public static void log(long i) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]:" + i);
    }

    public static void log(int i) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]:" + i);
    }

    public static void log(boolean b) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]:" + (b ? "True" : "False"));
    }

    public static void logJson(Object object) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        if (object == null) {
            System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]: null");
            return;
        }
        try {
            System.out.println(ste.getMethodName() + "() at [" + ste.getClassName() + ":" + ste.getLineNumber() + "]:" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}