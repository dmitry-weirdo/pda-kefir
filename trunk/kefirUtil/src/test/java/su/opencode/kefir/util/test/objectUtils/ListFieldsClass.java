/**
 Copyright 2013 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util.test.objectUtils;

import java.util.Date;

import static su.opencode.kefir.util.StringUtils.concat;

public class ListFieldsClass
{
	@Override
	public String toString() {
		return concat(
			  "ListFieldsClass{"
				,  "privateInt=", privateInt
				,", protectedDouble=", protectedDouble
				,", publicString=", publicString
				,", noModifierParent=", noModifierParent
				,", privateSampleEnum=", privateSampleEnum
	 		, '}'
		);
	}

	public int getPrivateInt() {
		return privateInt;
	}
	public void setPrivateInt(int privateInt) {
		this.privateInt = privateInt;
	}
	public double getProtectedDouble() {
		return protectedDouble;
	}
	public void setProtectedDouble(double protectedDouble) {
		this.protectedDouble = protectedDouble;
	}
	public String getPublicString() {
		return publicString;
	}
	public void setPublicString(String publicString) {
		this.publicString = publicString;
	}
	public ListFieldsClass getNoModifierParent() {
		return noModifierParent;
	}
	public void setNoModifierParent(ListFieldsClass noModifierParent) {
		this.noModifierParent = noModifierParent;
	}
	public SampleEnum getPrivateSampleEnum() {
		return privateSampleEnum;
	}
	public void setPrivateSampleEnum(SampleEnum privateSampleEnum) {
		this.privateSampleEnum = privateSampleEnum;
	}
	public Date getPrivateDate() {
		return privateDate;
	}
	public void setPrivateDate(Date privateDate) {
		this.privateDate = privateDate;
	}

	private int privateInt = 0;
	protected double protectedDouble;
	public String publicString;

	ListFieldsClass noModifierParent;

	private SampleEnum privateSampleEnum;
	private Date privateDate;

	public static final String publicStaticFinalString = "some value";
}