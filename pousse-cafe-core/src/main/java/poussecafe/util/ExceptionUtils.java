package poussecafe.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import poussecafe.exception.SevereException;

public class ExceptionUtils {

    public static String getStackTrace(Exception e) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        e.printStackTrace(printStream);
        printStream.flush();
        try {
            return outputStream.toString(Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e1) {
            throw new SevereException("Unable to encode stack trace", e1);
        }
    }
}
