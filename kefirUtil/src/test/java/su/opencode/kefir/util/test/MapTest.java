/**
 Copyright 2013 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util.test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import su.opencode.kefir.util.DateUtils;
import su.opencode.kefir.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapTest
{
	public static void main(String[] args) throws IOException {
		StringBuilder sb = new StringBuilder();
		BasicConfigurator.configure();
		DateFormat format = DateUtils.getYearMonthDayHourMinuteSecondMillisecondFormat();

		int elementsCount = 500000;

		Map<Integer, EntityClass> map = new HashMap<>();
//		Map<Integer, EntityClass> map = new TreeMap<>();


		log("Elements count: " + elementsCount, format);
		for (int i = 0; i < elementsCount; i++)
		{
			EntityClass value = new EntityClass();
			value.setId(i);
			value.setCreationDate( new Date() );
			value.setName("name " + Math.random() );

			map.put(i, value);
		}
		log("Successfully put " + elementsCount + " values to the map", format);
//		System.in.read();
		Date date1 = new Date();
		long time1 = date1.getTime();

		for (int i = 0; i < elementsCount; i++)
		{
			EntityClass value = map.get(i);
//			System.out.println( "i: " + value.toString() );
//			logger.info("i: " + value.toString());
			logger.info( StringUtils.concat(sb, "i: ", value.toString()) );

			if ( !value.getId().equals(i) )
				throw new RuntimeException("id (" + value.getId() + ") does not match i (" + i + ")");
		}
		log("Successfully got " + elementsCount + " values from the map", format);

		Date date2 = new Date();
		long time2 = date2.getTime();

		log("\nDate1: " + format.format(date1) + "\nDate2: " + format.format(date2), format);
		log( "Time between date1 and date2: " + (time2 - time1), format);
	}
	public static void log(String str, DateFormat format) {
		System.out.println( format.format(new Date()) + ". " + str);
	}

	public static class EntityClass {

		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Date getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		@Override
		public String toString() {
			return "EntityClass{" +
				  "id=" + id +
				", name='" + name + '\'' +
				", creationDate=" + creationDate +
				'}';
		}

		private Integer id;
		private String name;
		private Date creationDate;
	}

	private static final Logger logger = Logger.getLogger(MapTest.class);
}