/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.json;

import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;
import su.opencode.kefir.util.DateUtils;

import java.text.ParseException;
import java.util.*;

public class JsonObjectTests extends TestCase
{
	public void testSimpleClass() {
		final SimpleClass simpleClass = new SimpleClass();
		simpleClass.setFields();
		System.out.println(simpleClass.toJson());
	}
	public void testComplexClass() {
		final ComplexClass complexClass = new ComplexClass();
		System.out.println(complexClass.toJson());
	}

	@Test
	public void testFromJsonSimpleClass() {
		SimpleClass simpleClass = new SimpleClass();
		simpleClass.setFields();
		JSONObject jsonObject = simpleClass.toJson();
		System.out.println("SimpleClass jsonObject:");
		System.out.println(jsonObject.toString());

		SimpleClass fromJson = JsonObject.fromJson(jsonObject, SimpleClass.class);

		checkSimpleClass(fromJson);
	}
	private void checkSimpleClass(SimpleClass fromJson) {
		assertEquals( STRING_VALUE, fromJson.getS() );
		assertEquals( CHAR_VALUE, fromJson.getC() );
		assertEquals( CHARACTER_VALUE, fromJson.getC1() );
		assertEquals( BYTE_PRIMITIVE_VALUE, fromJson.getBt() );
		assertEquals( BYTE_VALUE, fromJson.getBt1() );
		assertEquals( SHORT_PRIMITIVE_VALUE, fromJson.getSh() );
		assertEquals( SHORT_VALUE, fromJson.getSh1() );
		assertEquals( INT_VALUE, fromJson.getI() );
		assertEquals( INTEGER_VALUE, fromJson.getI1() );
		assertEquals( LONG_PRIMITIVE_VALUE, fromJson.getL() );
		assertEquals( LONG_VALUE, fromJson.getL1() );
		assertEquals( DOUBLE_PRIMITIVE_VALUE, fromJson.getD() );
		assertEquals( DOUBLE_VALUE, fromJson.getD1() );
		assertEquals( FLOAT_PRIMITIVE_VALUE, fromJson.getF() );
		assertEquals( FLOAT_VALUE, fromJson.getF1() );
		assertEquals( BOOLEAN_PRIMITIVE_VALUE, fromJson.isB() );
		assertEquals( BOOLEAN_VALUE, fromJson.getB1() );

		assertEquals( getDateValue(), fromJson.getDate() );

		assertEquals( ENUM_VALUE, fromJson.getSimpleEnum() );

		// array
		byte[] array = fromJson.getArray();
		assertNotNull(array);
		assertEquals( ARRAY_VALUE.length, array.length );
		for (int i = 0; i < ARRAY_VALUE.length; i++)
		{
			assertEquals( ARRAY_VALUE[i], array[i] );
		}

		List<String> list = fromJson.getList();
		assertNotNull(list);
		assertEquals( list.size(), LIST_VALUE.size() );
		for (int i = 0; i < LIST_VALUE.size(); i++)
		{
			String s = LIST_VALUE.get(i);
			assertEquals(s, list.get(i) );
		}

		Set<Integer> set = fromJson.getSet();
		Set<Integer> setValue = getSetValue();
		assertNotNull(set);
		assertEquals( set.size(), setValue.size() );

		Iterator<Integer> setValueIterator = setValue.iterator();
		Iterator<Integer> setIterator = set.iterator();

		while ( setValueIterator.hasNext() )
		{
			assertEquals( setValueIterator.hasNext(), setIterator.hasNext() );

			Integer correctInt = setValueIterator.next();
			Integer intToCheck = setIterator.next();
			assertEquals(correctInt, intToCheck);

			setValueIterator.next();
			setIterator.next();
		}
	}

	@Test
	public void testFromJsonComplexClass() {
		final ComplexClass complexClass = new ComplexClass();
		complexClass.setFields();

		JSONObject jsonObject = complexClass.toJson();
		System.out.println("ComplexClass jsonObject:");
		System.out.println(jsonObject.toString());

		ComplexClass fromJson = JsonObject.fromJson(jsonObject, ComplexClass.class);
		assertNotNull(fromJson);
		assertEquals(COMPLEX_CLASS_STRING_VALUE, fromJson.getS());

		SimpleClass simpleClass = fromJson.getSc();
		assertNotNull(simpleClass);
		checkSimpleClass(simpleClass);

		List<SimpleClass> correctList = getSimpleClassList();
		List<SimpleClass> list = fromJson.getList();
		assertNotNull(list);
		assertEquals( correctList.size(), list.size() );

		for (int i = 0; i < correctList.size(); i++)
		{
			// todo: validate equality of correctList and list
			SimpleClass aClass = correctList.get(i);

			SimpleClass simpleClassFromJsonList = list.get(i);
			checkSimpleClass(simpleClassFromJsonList);
//			list.get(i);
		}
	}

	public static class ComplexClass extends JsonObject {
		public void setFields() {
			this.s = COMPLEX_CLASS_STRING_VALUE;

			this.sc = new SimpleClass();
			this.sc.setFields();

			this.list = getSimpleClassList();
		}

		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
		public SimpleClass getSc() {
			return sc;
		}
		public void setSc(SimpleClass sc) {
			this.sc = sc;
		}
		public List<SimpleClass> getList() {
			return list;
		}
		public void setList(List<SimpleClass> list) {
			this.list = list;
		}

		private String s;
		private SimpleClass sc;
		private List<SimpleClass> list;
	}

	public static class SimpleClass extends JsonObject {
		public SimpleClass() {
		}

		public void setFields() {
			this.s = STRING_VALUE;
			this.c = CHAR_VALUE;
			this.c1 = CHARACTER_VALUE;
			this.bt = BYTE_PRIMITIVE_VALUE;
			this.bt1 = BYTE_VALUE;
			this.sh = SHORT_PRIMITIVE_VALUE;
			this.sh1 = SHORT_VALUE;
			this.i = INT_VALUE;
			this.i1 = INTEGER_VALUE;
			this.l = LONG_PRIMITIVE_VALUE;
			this.l1 = LONG_VALUE;
			this.d = DOUBLE_PRIMITIVE_VALUE;
			this.d1 = DOUBLE_VALUE;
			this.f = FLOAT_PRIMITIVE_VALUE;
			this.f1 = FLOAT_VALUE;
			this.b = BOOLEAN_PRIMITIVE_VALUE;
			this.b1 = BOOLEAN_VALUE;

			this.date = getDateValue();

			this.simpleEnum = ENUM_VALUE;

			this.array = ARRAY_VALUE;
			this.list = LIST_VALUE;
			this.set = getSetValue();
		}

		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
		public char getC() {
			return c;
		}
		public void setC(char c) {
			this.c = c;
		}
		public Character getC1() {
			return c1;
		}
		public void setC1(Character c1) {
			this.c1 = c1;
		}
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}
		public Integer getI1() {
			return i1;
		}
		public void setI1(Integer i1) {
			this.i1 = i1;
		}
		public long getL() {
			return l;
		}
		public void setL(long l) {
			this.l = l;
		}
		public Long getL1() {
			return l1;
		}
		public void setL1(Long l1) {
			this.l1 = l1;
		}
		public double getD() {
			return d;
		}
		public void setD(double d) {
			this.d = d;
		}
		public Double getD1() {
			return d1;
		}
		public void setD1(Double d1) {
			this.d1 = d1;
		}
		public float getF() {
			return f;
		}
		public void setF(float f) {
			this.f = f;
		}
		public Float getF1() {
			return f1;
		}
		public void setF1(Float f1) {
			this.f1 = f1;
		}
		public boolean isB() {
			return b;
		}
		public void setB(boolean b) {
			this.b = b;
		}
		public Boolean getB1() {
			return b1;
		}
		public void setB1(Boolean b1) {
			this.b1 = b1;
		}
		public byte getBt() {
			return bt;
		}
		public void setBt(byte bt) {
			this.bt = bt;
		}
		public Byte getBt1() {
			return bt1;
		}
		public void setBt1(Byte bt1) {
			this.bt1 = bt1;
		}
		public short getSh() {
			return sh;
		}
		public void setSh(short sh) {
			this.sh = sh;
		}
		public Short getSh1() {
			return sh1;
		}
		public void setSh1(Short sh1) {
			this.sh1 = sh1;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public SimpleEnum getSimpleEnum() {
			return simpleEnum;
		}
		public void setSimpleEnum(SimpleEnum simpleEnum) {
			this.simpleEnum = simpleEnum;
		}
		public byte[] getArray() {
			return array;
		}
		public void setArray(byte[] array) {
			this.array = array;
		}
		public List<String> getList() {
			return list;
		}
		public void setList(List<String> list) {
			this.list = list;
		}
		public Set<Integer> getSet() {
			return set;
		}
		public void setSet(Set<Integer> set) {
			this.set = set;
		}

		@Json(exclude = true)
		public String getTransientMethod2() {
			return "s";
		}
		public String getTransientMethod() {
			return "transient method";
		}

		private String s;
		private char c;
		private Character c1;
		private byte bt;
		private Byte bt1;
		private short sh;
		private Short sh1;
		private int i;
		private Integer i1;
		private long l;
		private Long l1;
		private double d;
		private Double d1;
		private float f;
		private Float f1;
		private boolean b;
		private Boolean b1;
		private Date date;
		private SimpleEnum simpleEnum;
		private byte[] array;
		private List<String> list;
		private Set<Integer> set;
	}

	public static enum SimpleEnum {
		first_value, second_value, third_value
	}

	public static Date getDateValue() {
		try
		{
			return DateUtils.getJsDateFormat().parse("1987-03-10T12:34:56");
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Set<Integer> getSetValue() {
		Set<Integer> set = new TreeSet<>();
		Collections.addAll(set, 99, 88, 77, 66);
		return set;
	}

	public static List<SimpleClass> getSimpleClassList() {
		SimpleClass simpleClassForList = new SimpleClass();
		simpleClassForList.setFields();

		List<SimpleClass> list = new ArrayList<>();
		list.add(simpleClassForList);
		return list;
	}

	public static final String STRING_VALUE = "a string";
	public static final char CHAR_VALUE = 'q';
	public static final Character CHARACTER_VALUE = 'c';
	public static final byte BYTE_PRIMITIVE_VALUE = 10;
	public static final Byte BYTE_VALUE = 20;
	public static final short SHORT_PRIMITIVE_VALUE = 101;
	public static final Short SHORT_VALUE = 202;
	public static final int INT_VALUE = 1;
	public static final Integer INTEGER_VALUE = 2;
	public static final long LONG_PRIMITIVE_VALUE = 1000L;
	public static final Long LONG_VALUE = 2000L;
	public static final double DOUBLE_PRIMITIVE_VALUE = 11.11d;
	public static final Double DOUBLE_VALUE = 22.22d;
	public static final float FLOAT_PRIMITIVE_VALUE = 1.1f;
	public static final Float FLOAT_VALUE = 2.2f;
	public static final boolean BOOLEAN_PRIMITIVE_VALUE = true;
	public static final Boolean BOOLEAN_VALUE = Boolean.FALSE;

	public static final SimpleEnum ENUM_VALUE = SimpleEnum.second_value;

	public static final byte[] ARRAY_VALUE = { 11, 22, 33 };
	public static final List<String> LIST_VALUE = Arrays.asList("zzz", "xxx", "cc");

	public static final String COMPLEX_CLASS_STRING_VALUE = "sss";
}