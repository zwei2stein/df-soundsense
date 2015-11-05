package cz.zweistein.df.soundsense.util.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.fusesource.jansi.Ansi;

public class SingleLineFormatter extends Formatter {

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        
        builder.append(Ansi.ansi().reset());
        builder.append(df.format(new Date(record.getMillis()))).append(": ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}
