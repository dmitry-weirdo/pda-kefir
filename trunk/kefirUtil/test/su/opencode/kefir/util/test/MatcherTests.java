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
import su.opencode.kefir.util.InnMatcher;
import su.opencode.kefir.util.OgrnMatcher;

public class MatcherTests extends TestCase
{
	public void testInn4Juristics() {
		System.out.println(InnMatcher.isValid("1111111117"));//true
		System.out.println(InnMatcher.isValid("1234567894"));//true
		System.out.println(InnMatcher.isValid("7830002293"));//true
		System.out.println(InnMatcher.isValid("7830002294"));
		System.out.println(InnMatcher.isValid("78ó0002293"));
		System.out.println(InnMatcher.isValid("780002293"));
		System.out.println(InnMatcher.isValid("78000229366"));
		System.out.println(InnMatcher.isValid("631066062"));
	}
	public void testInn4Physics() {
		System.out.println(InnMatcher.isValid("111111111130"));//true
		System.out.println(InnMatcher.isValid("123456789047"));//true
		System.out.println(InnMatcher.isValid("12345678904"));
		System.out.println(InnMatcher.isValid("1234567890470"));
		System.out.println(InnMatcher.isValid("123456789040"));
		System.out.println(InnMatcher.isValid("12345678904a"));
	}
	public void testOgrn() {
		System.out.println(OgrnMatcher.isValid("1111111111110"));//true
		System.out.println(OgrnMatcher.isValid("1026301154186"));//true
		System.out.println(OgrnMatcher.isValid("1026301154185"));
		System.out.println(OgrnMatcher.isValid("s1026301154186"));
		System.out.println(OgrnMatcher.isValid("s026301154186"));
		System.out.println(OgrnMatcher.isValid("026301154186"));
	}
}
