/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 19:21:31$
*/
package su.opencode.kefir.gen.project;

import org.apache.log4j.BasicConfigurator;
import org.xml.sax.SAXException;
import su.opencode.kefir.gen.helper.ObjectFiller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static su.opencode.kefir.util.StringUtils.concat;

public class ProjectGeneratorRunner
{
	public static void main(String[] args) throws IOException, SAXException {
		BasicConfigurator.configure();

		ProjectGenerator generator = new ProjectGenerator(getProjectConfig(args));
		generator.generateProject();
	}
	public static ProjectConfig getProjectConfig(String[] args) throws FileNotFoundException {
		final String fileName = getPropertiesFileName(args);
		final InputStream in = new FileInputStream(fileName);

		return ObjectFiller.createObject(in, ProjectConfig.class);
	}
	private static String getPropertiesFileName(String[] args) {
		if (args.length != 1 || !args[0].startsWith(concat(PROPS, SEPARATOR)))
			throw new IllegalArgumentException(concat("'", PROPS, "' not defined"));

		return args[0].substring(PROPS.length() + SEPARATOR.length());
	}

	private static final String PROPS = "props";
	private static final String SEPARATOR = "=";
}