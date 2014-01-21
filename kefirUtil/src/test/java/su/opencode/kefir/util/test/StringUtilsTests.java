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
import su.opencode.kefir.util.StringUtils;

import static su.opencode.kefir.util.StringUtils.*;


public class StringUtilsTests extends TestCase
{
	public void test866ProblemLetter() {
		final String problemStr = "шШ";
		System.out.println(getConcatenation("     str: ", problemStr));

		final String problemStrTo866 = getStringTo866(problemStr);
		System.out.println(getConcatenation("  to 866: ", problemStrTo866));


		final String problemStrFrom866 = getStringFrom866(problemStrTo866);
		System.out.println(getConcatenation("from 866: ", problemStrFrom866));
	}
	public void test866() {
		System.out.println(getConcatenation("     str: ", str));

		final String strTo866 = getStringTo866(str);
		System.out.println(getConcatenation("  to 866: ", strTo866));

		final String strFrom866 = getStringFrom866(strTo866);
		System.out.println(getConcatenation("from 866: ", strFrom866));
	}
	public void testUTF8ProblemLetter() {
		final String problemStr = "иИ";
		System.out.println(getConcatenation("      str: ", problemStr));

		final String problemStrToUTF8 = getStringToUTF8(problemStr);
		System.out.println(getConcatenation("  to utf8: ", problemStrToUTF8));

		final String problemStrFromUTF8 = getStringFromUTF8(problemStrToUTF8);
		System.out.println(getConcatenation("from utf8: ", problemStrFromUTF8));
	}
	public void testUTF8() {
		System.out.println(getConcatenation("      str: ", str));

		final String strToUTF8 = getStringToUTF8(str);
		System.out.println(getConcatenation("  to utf8: ", strToUTF8));

		final String strFromUTF8 = getStringFromUTF8(strToUTF8);
		System.out.println(getConcatenation("from utf8: ", strFromUTF8));
	}

	public void testRemoveLineBreaks() {
		String line = "string one.\r\nstring two.\r\nstring three.";
		String lineWithNoBreaks = line.replace("\n", "").replace("\r", "");

		System.out.println( concat("line with line breaks:\n", line) );
		System.out.println( concat("line with no line breaks:\n", lineWithNoBreaks) );
	}

	public void testBytesArrayToString() {
		byte[] bytes = { -128, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 127 };
		System.out.println( concat("bytes array to str: ", StringUtils.getHexString(bytes)) );
	}

	private static final String fileName = "d:\\11.txt";
	private static final String str = "аАбБвВдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ";
}