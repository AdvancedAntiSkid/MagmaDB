package net.bluenight.magmadb.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * @author AdvancedAntiSkid
 * High level java programming. Do not worry about it.
 */
public class Logger
{
    public static void write(PrintStream stream, String prefix, String content)
    {
        LocalDateTime now = LocalDateTime.now();
        stream.println("[" + (now.getHour() < 10 ? "0" + now.getHour() : now.getHour()) + ":"
            + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0"
            + now.getSecond() : now.getSecond()) + " " + prefix + "] " + content);
    }

    public static void info(String message)
    {
        write(System.out, "INFO", message);
    }

    public static void warning(String message)
    {
        write(System.out, "WARNING", message);
    }

    public static void error(String message)
    {
        write(System.err, "ERROR", message);
    }

    public static void error(String message, Throwable throwable)
    {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        error(message + ": " + writer.toString());
    }
}
