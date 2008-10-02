package java.text;

import java.util.Date;

public abstract class DateFormat
{
    public abstract String format(Date date);

    public abstract Date parse(String source);
}