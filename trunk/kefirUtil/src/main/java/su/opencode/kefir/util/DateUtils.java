/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 24.09.2010 19:42:51$
*/
package su.opencode.kefir.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils
{
	public static Date getDateByFormat(String dateFormat, String date) {
		try
		{
			return new SimpleDateFormat(dateFormat).parse(date);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static String getDateByFormat(String dateFormat, Date date) {
		return new SimpleDateFormat(dateFormat).format(date);
	}
	public static Date getDateByFormat(DateFormat dateFormat, String date) {
		try
		{
			return dateFormat.parse(date);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static Date getDayMonthYear(String date) {
		try
		{
			return date == null || date.isEmpty() ? null : getDayMonthYearFormat().parse(date);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static String getDayMonthYearHourMinuteSecond(Date date) {
		return date == null ? "" : getDayMonthYearHourMinuteSecondFormat().format(date);
	}
	public static String getHourMinuteSecond(Date date) {
		return date == null ? "" : getHourMinuteSecondFormat().format(date);
	}
	public static String getDayMonthYear(Date date) {
		return date == null ? "" : getDayMonthYearFormat().format(date);
	}
	public static String getYearMonthDay(Date date) {
		return date == null ? "" : getYearMonthDayFormat().format(date);
	}
	public static DateFormat getDayMonthYearFormat() {
		return new SimpleDateFormat("dd.MM.yyyy");
	}
	public static DateFormat getYearMonthDayFormat() {
		return new SimpleDateFormat("yyyy.MM.dd");
	}
	public static DateFormat getHourMinuteSecondFormat() {
		return new SimpleDateFormat("HH:mm:ss");
	}
	public static DateFormat getHourMinuteFormat() {
		return new SimpleDateFormat("HH:mm");
	}
	public static DateFormat getDayMonthYearHourMinuteFormat() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}
	public static DateFormat getDayMonthYearHourMinuteSecondFormat() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	}
	public static DateFormat getYearMonthDayHourMinuteSecondMillisecondFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}
	public static DateFormat getTimestampDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public static DateFormat getJsDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}
	public static DateFormat getUtcDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}
	public static DateFormat getUtcDateFormatWithNoTimeZone() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	}
	public static DateFormat getFirebirdSqlDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	public static int getYear(Date date) {
		Calendar calendar = clearCalendar(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * @param calendar календарь, используемый для преобразования.
	 * @param date исходная дата.
	 * @return дата с выставленным временем 00:00:00:000.
	 */
	public static Date getDayMonthYearDate(Calendar calendar, Date date) {
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
	public static Date getDayMonthYearDate(Date date) {
		return getDayMonthYearDate(Calendar.getInstance(), date);
	}

	/**
	 * @param date исходная дата.
	 * @return дата с выставленным временем 03:00:00.
	 */
	public static Date getDate3AM(Date date) {
		Calendar calendar = clearCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 3);

		return calendar.getTime();
	}

	public static Date addYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 1);
		return calendar.getTime();
	}
	public static Date getMaxDate(Date... dates) {
		if (dates.length == 0)
			return null;

		Date maxDate = dates[0];

		for (Date date : dates)
			if (date.compareTo(maxDate) > 0)
				maxDate = date;

		return maxDate;
	}

	public static Date getDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day); // month is 0-based

		return calendar.getTime();
	}
	public static Date addDate(Date date, int calendarField, int amount) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendarField, amount);

		return calendar.getTime();
	}
	public static Date getLastDayOfMonthDate(int month, int year) {
		Calendar calendar = Calendar.getInstance();

		// get 1st day of next month
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month); // month is 0-based, this sets next month
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// subtract one day from 1st day of next month -> get last
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return calendar.getTime();
	}

	public static XMLGregorianCalendar getXmlGregorianCalendar(Date date) {
		try
		{
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		}
		catch (DatatypeConfigurationException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static Date getDate(XMLGregorianCalendar calendar) {
		return calendar.toGregorianCalendar().getTime();
	}

	public static Timestamp getTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	public static Date getDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	public static int getDayOfYear() {
		return getDayOfYear(new Date());
	}
	public static int getDayOfYear(Date date) {
		Calendar calendar = clearCalendar(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	private static Calendar clearCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	public static Date getFirstDayOfCurrentYear() {
		return getFirstDayOfYear(new Date());
	}
	public static Date getFirstDayOfYear(Date date) {
		Calendar calendar = clearCalendar(date);

		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
	public static Date getFirstDayOfCurrentMonth() {
		return getFirstDayOfMonth(new Date());
	}
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = clearCalendar(date);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
	public static Date getLastDayOfCurrentMonth() {
		return getLastDayOfMonth(new Date());
	}
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = clearCalendar(date);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return calendar.getTime();
	}
	public static Date getDayYearBeforeCurrentDay() {
		return getDayYearBefore(new Date());
	}
	public static Date getDayYearBefore(Date date) {
		Calendar calendar = clearCalendar(date);
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String getCalendarField(Date date, CalendarField field) {
		if (date == null)
			return "";

		Calendar calendar = clearCalendar(date);

		if (field == null)
			throw new IllegalArgumentException("Calendar Field cannot be null");

		switch (field)
		{
			case DAY_OF_MONTH:
				return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

			case MONTH:
				return DateUtils.MONTHS_GENITIVE[calendar.get(Calendar.MONTH)];

			case YEAR:
				return Integer.toString(calendar.get(Calendar.YEAR));

			default:
				throw new IllegalArgumentException(StringUtils.getConcatenation("Incorrect Calendar Field value: ", field));
		}
	}

	public static String getMonthNameGenitive(int num) {
		if (num < Calendar.JANUARY || num > Calendar.DECEMBER)
			throw new IllegalArgumentException("Неверный номер месяца. Номер месяца должен лежать в пределах от 0 до 11");

		return MONTHS_GENITIVE[num];
	}

	public static String getMonthNameNominative(int num) {
		if (num < Calendar.JANUARY || num > Calendar.DECEMBER)
			throw new IllegalArgumentException("Неверный номер месяца. Номер месяца должен лежать в пределах от 0 до 11");

		return MONTHS_NOMINATIVE[num];
	}

	public static String getCurrentMonthNameGenitive() {
		Calendar calendar = Calendar.getInstance();
		return getMonthNameGenitive(calendar.get(Calendar.MONTH));
	}

	public static String getCurrentMonthNameNominative() {
		Calendar calendar = Calendar.getInstance();
		return getMonthNameNominative(calendar.get(Calendar.MONTH));
	}

	public enum CalendarField
	{
		DAY_OF_MONTH, MONTH, YEAR
	}

	public static final String[] MONTHS_GENITIVE = {
		"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"
	};

	public static final String[] MONTHS_NOMINATIVE = {
		"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
	};
}