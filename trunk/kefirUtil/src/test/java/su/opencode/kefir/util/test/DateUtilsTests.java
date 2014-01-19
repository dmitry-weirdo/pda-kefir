/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 21.02.2011 10:42:18$
*/
package su.opencode.kefir.util.test;

import junit.framework.TestCase;
import static su.opencode.kefir.util.DateUtils.*;

import java.util.Date;

public class DateUtilsTests extends TestCase
{
	public void testDate() {
		System.out.println(getYear(past));
		System.out.println(getYear(null));
	}
	public void testDayOfYear() {
		System.out.println(getDayOfYear(past));
		System.out.println(getDayOfYear());
	}
	public void testFirstDayOfYear() {
		System.out.println(getFirstDayOfYear(past));
		System.out.println(getFirstDayOfCurrentYear());
	}
	public void testFirstDayOfMonth() {
		System.out.println(getFirstDayOfMonth(past));
		System.out.println(getFirstDayOfCurrentMonth());
	}
	public void testLastDayOfMonth() {
		System.out.println(getLastDayOfMonth(getFirstDayOfCurrentYear()));
		System.out.println(getLastDayOfMonth(past));
		System.out.println(getLastDayOfCurrentMonth());
	}

	private static final Date past = getDayMonthYear("12.12.2007");
}