/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 21.02.2011 10:42:18$
*/
package su.opencode.kefir.util.test;

import org.junit.Test;
import su.opencode.kefir.util.InnMatcher;
import su.opencode.kefir.util.OgrnMatcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatcherTest
{
	@Test
	public void testInnJuridical() {
		assertTrue( InnMatcher.isValid("1111111117") );
		assertTrue( InnMatcher.isValid("1234567894") );
		assertTrue( InnMatcher.isValid("7830002293") );
		assertFalse( InnMatcher.isValid("7830002294") );
		assertFalse( InnMatcher.isValid("78у0002293") );
		assertFalse( InnMatcher.isValid("780002293") );
		assertFalse( InnMatcher.isValid("78000229366") );
		assertFalse( InnMatcher.isValid("631066062") );
	}

	@Test
	public void testInnPhysical() {
		assertTrue( InnMatcher.isValid("111111111130") );
		assertTrue( InnMatcher.isValid("123456789047") );
		assertFalse( InnMatcher.isValid("12345678904") );
		assertFalse( InnMatcher.isValid("1234567890470") );
		assertFalse( InnMatcher.isValid("123456789040") );
		assertFalse( InnMatcher.isValid("12345678904a") );
	}

	@Test
	public void testOgrn() {
		assertTrue( OgrnMatcher.isValid("1111111111110") );
		assertTrue( OgrnMatcher.isValid("1026301154186") );
		assertFalse( OgrnMatcher.isValid("1026301154185") );
		assertFalse( OgrnMatcher.isValid("s1026301154186") );
		assertFalse( OgrnMatcher.isValid("s026301154186") );
		assertFalse( OgrnMatcher.isValid("026301154186") );
	}
}