package poussecafe.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import poussecafe.exception.PousseCafeException;

public class ExceptionUtils {

    private ExceptionUtils() {

    }

    public static String getStackTrace(Exception e) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        e.printStackTrace(printStream);
        printStream.flush();
        try {
            return outputStream.toString(Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e1) {
            throw new PousseCafeException("Unable to encode stack trace", e1);
        }
    }
}
