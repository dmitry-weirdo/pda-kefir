/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 04.04.2012 15:50:58$
*/
package su.opencode.kefir.gen.replacer;

import org.apache.log4j.BasicConfigurator;

public class TestReplacer extends KefirReplacer
{
	public TestReplacer(String templateFilePath, String filePath) {
		super(templateFilePath, filePath);
	}
	@Override
	protected String getEncoding() {
		return null;
	}

	@Override
	protected void fillValues() {
		values.put("surname", "\\$\\{Popov\\}");
		values.put("name", "\\$\\{Dmitriy\\}");
//		values.put("surname", "Popov");
//		values.put("name", "Dmitriy");
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();

		Replacer replacer = new TestReplacer(null, null);
		replacer.fillValues();

		String str = "Hello. My surname is ${surname}. My name is ${name}. Greetings, ${surname} ${name}.";
		System.out.println("String before replacement: " + str);

		String result = replacer.replace(str);
		System.out.println("String after replacement: " + result);
	}
}