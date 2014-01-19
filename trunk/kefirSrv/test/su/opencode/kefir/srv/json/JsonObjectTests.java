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

import java.util.*;

public class JsonObjectTests extends TestCase
{
	public void testSimpleClass() {
		final SimpleClass simpleClass = new SimpleClass();
		System.out.println(simpleClass.toJson());
	}
	public void testComplexClass() {
		final ComplexClass complexClass = new ComplexClass();
		System.out.println(complexClass.toJson());
	}

	public class ComplexClass extends JsonObject {
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
		String s = "sss";
		SimpleClass sc = new SimpleClass();
		List<SimpleClass> list = new ArrayList<>(Arrays.asList(new SimpleClass()));
	}

	public class SimpleClass extends JsonObject {
		public SimpleClass() {
			Collections.addAll(set, 99, 88, 77, 66);
		}
		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
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
		String s = "a string";
		int i = 1;
		Integer i1 = 2;
		char c = 'q';
		Character c1 = 'c';
		double d = 11.11;
		Double d1 = (double) 22.22;
		float f = (float) 1.1;
		Float f1 = (float) 1.1;
		boolean b = true;
		Boolean b1 = false;
		byte bt = (byte) 1;
		Byte bt1 = new Byte("2");
		List<String> list = new ArrayList<>(Arrays.asList("zzz", "xxx", "cc"));
		Set<Integer> set = new HashSet<>();
	}
}