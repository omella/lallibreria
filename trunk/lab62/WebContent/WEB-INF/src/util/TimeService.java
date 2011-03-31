package util;

import java.text.DateFormat;
import java.util.Locale;

public class TimeService {
	private static Locale currentLocale = Locale.ENGLISH;
	private static DateFormat hourFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
	private static DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);

	public static String getCurrentDate()
	{
		return dateFormatter.format(new java.util.Date());
	}

	public static String getHour(java.util.Date time)
	{
		return hourFormatter.format(time);
	}

	public static String getDate(java.util.Date time)
	{
		return dateFormatter.format(time);
	}
}
