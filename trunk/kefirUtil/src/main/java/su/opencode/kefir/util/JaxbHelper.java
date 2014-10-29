/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.StringWriter;

import static su.opencode.kefir.util.StringUtils.concat;

public class JaxbHelper
{
	public static String jaxbObjectToString(Object jaxbObject) {
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(jaxbObject.getClass());
			Marshaller marshaller = createMarshaller(jaxbContext);

			StringWriter writer = new StringWriter();
			marshaller.marshal(jaxbObject, writer);

			return writer.toString();
		}
		catch (JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static String jaxbObjectToFile(Object jaxbObject, String fileName) {
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(jaxbObject.getClass());
			Marshaller marshaller = createMarshaller(jaxbContext);

			StringWriter writer = new StringWriter();
			marshaller.marshal(jaxbObject, writer);
			String xmlAsString = writer.toString();

			FileUtils.writeToFile(fileName, xmlAsString, XML_ENCODING);
			logger.info( concat("marshalled xml written to file \"", fileName, "\" with \"", XML_ENCODING, "\" encoding.") );

			return xmlAsString;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T jaxbObjectFromFile(Class<T> jaxbObjectClass, String fileName) {
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(jaxbObjectClass);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (T) unmarshaller.unmarshal(new FileReader(fileName));
		}
		catch (Exception  e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Marshaller createMarshaller(JAXBContext jaxbContext) {
		try
		{
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // pretty-print

			// correct XML declaration
//		marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false); // fails
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

//		marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", getNamespacePrefixMapper()); // does not work in JBoss runtime
			return marshaller;
		}
		catch (JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static final Logger logger = Logger.getLogger(JaxbHelper.class);
	public static final String XML_ENCODING = "UTF-8";
}