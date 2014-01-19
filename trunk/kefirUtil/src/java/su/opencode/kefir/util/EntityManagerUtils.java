/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 21.09.2010 10:47:13$
*/
package su.opencode.kefir.util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class EntityManagerUtils
{
	public static void persist(EntityManager em, Object entity) {
		if (entity != null)
			em.persist(entity);
	}
	public static void merge(EntityManager em, Object existingEntity, Object newEntity) {
		if (existingEntity == null)
			em.persist(newEntity);
		else
			em.merge(newEntity);
	}
	public static void remove(EntityManager em, Object entity) {
		if (entity != null)
			em.remove(entity);
	}

	public static String getString(Object ob) {
		return ob == null ? null : ob.toString();
	}
	public static String getStringOrEmpty(Object ob) {
		return ob == null ? "" : ob.toString();
	}
	public static Integer getInteger(String ob) {
		return ob == null ? null : Integer.parseInt(ob);
	}
	public static int getInt(Object ob) {
		final Integer rv = getInteger(ob);
		return rv == null ? 0 : rv;
	}
	public static Integer getInteger(Object ob) {
		if (ob == null)
			return null;

		final String className = ob.getClass().getName();
		if (className.equals(Integer.class.getName()))
			return (Integer) ob;

		if (className.equals(Short.class.getName()))
			return ((Short) ob).intValue();

		if (className.equals(BigInteger.class.getName()))
			return ((BigInteger) ob).intValue();

		if (className.equals(Double.class.getName()))
			return ((Double) ob).intValue();

		if (className.equals(BigDecimal.class.getName()))
			return ((BigDecimal) ob).intValue();

		if (className.equals(String.class.getName()))
			return getInteger((String) ob);

		throw new RuntimeException(StringUtils.getConcatenation("Can't parse (", className, " ", ob, ") to Integer"));
	}
	public static Double getDouble(Object ob) {
		if (ob == null)
			return null;

		final String className = ob.getClass().getName();
		if (className.equals(Double.class.getName()))
			return (Double) ob;

		if (className.equals(BigDecimal.class.getName()))
			return ((BigDecimal) ob).doubleValue();

		if (className.equals(String.class.getName()))
			return Double.parseDouble(((String) ob));

		throw new RuntimeException(StringUtils.getConcatenation("Can't parse (", className, " ", ob, ") to Double"));
	}
	public static Float getFloat(Object ob) {
		if (ob == null)
			return null;

		final String className = ob.getClass().getName();
		if (className.equals(Float.class.getName()))
			return (Float) ob;

		if (className.equals(BigDecimal.class.getName()))
			return ((BigDecimal) ob).floatValue();

		throw new RuntimeException(StringUtils.getConcatenation("Can't parse (", className, " ", ob, ") to Float"));
	}
	public static Long getLong(Object ob) {
		if (ob == null)
			return null;

		final String className = ob.getClass().getName();
		if (className.equals(Double.class.getName()))
			return ((Double) ob).longValue();

		if (className.equals(BigDecimal.class.getName()))
			return ((BigDecimal) ob).longValue();

		throw new RuntimeException(StringUtils.getConcatenation("Can't parse (", className, " ", ob, ") to Long"));
	}
	public static Boolean getBoolean(Object ob) {
		if (ob == null)
			return null;

		final String className = ob.getClass().getName();
		if (className.equals(Boolean.class.getName()))
			return (Boolean) ob;

		if (className.equals(Integer.class.getName()))
			return ((Integer) ob) != 0;

		if (className.equals(Short.class.getName()))
			return ((Short) ob) != 0;

		throw new RuntimeException(StringUtils.getConcatenation("Can't parse (", className, " ", ob, ") to Boolean"));
	}
	public static Date getDate(Object ob) {
		return ob == null ? null : (Date) ob;
	}

	public static final String PERSIST_METHOD_NAME = "persist";
	public static final String MERGE_METHOD_NAME = "merge";
	public static final String REMOVE_METHOD_NAME = "remove";
}