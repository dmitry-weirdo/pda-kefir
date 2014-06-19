/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.enumField.EnumFieldValue;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.ColumnGroups;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.json.ColumnModelExclude;
import su.opencode.kefir.srv.json.ViewConfig;

import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.srv.renderer.RenderersFactory.*;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.StringUtils.*;

public class ExtEntityFieldsUtils
{
	/**
	 * @param field поле
	 * @return <code>true</code>, если поле помечено как поле для формы, <br/>
	 * <code>false</code> — в противном случае.
	 */
	public static boolean hasFieldAnnotation(Field field) {
		Annotation[] annotations = field.getDeclaredAnnotations();
		if (annotations == null || annotations.length == 0)
			return false;

		for (Annotation annotation : annotations)
		{
			if (
				annotation instanceof IdField
				|| annotation instanceof TextField || annotation instanceof TextAreaField || annotation instanceof IntegerField || annotation instanceof SpinnerField || annotation instanceof NumberField
				|| annotation instanceof DateField || annotation instanceof CheckboxField
				|| annotation instanceof OgrnTextField || annotation instanceof KppTextField || annotation instanceof InnJuridicalTextField
				|| annotation instanceof LocalComboBoxField || annotation instanceof ComboBoxField
				|| annotation instanceof ChooseField || annotation instanceof AttachmentsField || annotation instanceof AddressField
			)
				return true;
		}

		return false;
	}

	public static boolean hasIdFieldAnnotation(Field field) {
		return field.getAnnotation(IdField.class) != null;
	}

	public static Field getIdField(Class entityClass) {
		for (Field field : entityClass.getDeclaredFields())
		{
			if ( hasIdFieldAnnotation(field) )
				return field;
		}

		return null;
	}

	public static boolean hasSqlColumnAnnotation(Field field) {
		return field.getAnnotation(SqlColumn.class) != null;
	}
	public static SqlColumn getSqlColumnAnnotation(Field field) {
		return field.getAnnotation(SqlColumn.class);
	}

	public static boolean hasTextFieldAnnotation(Field field) {
		return field.getAnnotation(TextField.class) != null;
	}
	public static TextField getTextFieldAnnotation(Field field) {
		return field.getAnnotation(TextField.class);
	}

	public static boolean hasTextAreaFieldAnnotation(Field field){
		return field.getAnnotation(TextAreaField.class)!= null;
	}
	public  static TextAreaField getTextAreaFieldAnnotation(Field field){
		return  field.getAnnotation(TextAreaField.class);
	}

	public static boolean hasOgrnTextFieldAnnotation(Field field) {
		return field.getAnnotation(OgrnTextField.class) != null;
	}
	public static OgrnTextField getOgrnTextFieldAnnotation(Field field) {
		return field.getAnnotation(OgrnTextField.class);
	}
	public static boolean hasKppTextFieldAnnotation(Field field) {
		return field.getAnnotation(KppTextField.class) != null;
	}
	public static KppTextField getKppTextFieldAnnotation(Field field) {
		return field.getAnnotation(KppTextField.class);
	}
	public static boolean hasInnJuridicalTextFieldAnnotation(Field field) {
		return field.getAnnotation(InnJuridicalTextField.class) != null;
	}
	public static InnJuridicalTextField getInnJuridicalTextFieldAnnotation(Field field) {
		return field.getAnnotation(InnJuridicalTextField.class);
	}

	public static boolean hasIntegerFieldAnnotation(Field field) {
		return field.getAnnotation(IntegerField.class) != null;
	}
	public static IntegerField getIntegerFieldAnnotation(Field field) {
		return field.getAnnotation(IntegerField.class);
	}

	public static boolean hasSpinnerFieldAnnotation(Field field) {
		return field.getAnnotation(SpinnerField.class) != null;
	}
	public static SpinnerField getSpinnerFieldAnnotation(Field field) {
		return field.getAnnotation(SpinnerField.class);
	}

	public static boolean hasNumberFieldAnnotation(Field field) {
		return field.getAnnotation(NumberField.class) != null;
	}
	public static NumberField getNumberFieldAnnotation(Field field) {
		return field.getAnnotation(NumberField.class);
	}

	public static boolean hasDateFieldAnnotation(Field field) {
		return field.getAnnotation(DateField.class) != null;
	}
	public static DateField getDateFieldAnnotation(Field field) {
		return field.getAnnotation(DateField.class);
	}

	public static boolean hasCheckboxFieldAnnotation(Field field) {
		return field.getAnnotation(CheckboxField.class) != null;
	}
	public static CheckboxField getCheckboxFieldAnnotation(Field field) {
		return field.getAnnotation(CheckboxField.class);
	}
	public static boolean hasCheckboxFields(Class entityClass) {
		for (Field field : entityClass.getDeclaredFields())
			if ( hasCheckboxFieldAnnotation(field) )
				return true;

		return false;
	}
	public static List<Field> getCheckboxFields(Class entityClass) {
		List<Field> fields = new ArrayList<Field>();

		for (Field field : entityClass.getDeclaredFields())
			if ( hasCheckboxFieldAnnotation(field) )
				fields.add(field);

		return fields;
	}

	public static boolean hasLocalComboBoxFieldAnnotation(Field field) {
		return field.getAnnotation(LocalComboBoxField.class) != null;
	}
	public static LocalComboBoxField getLocalComboBoxFieldAnnotation(Field field) {
		return field.getAnnotation(LocalComboBoxField.class);
	}

	public static boolean hasComboBoxFieldAnnotation(Field field) {
		return field.getAnnotation(ComboBoxField.class) != null;
	}
	public static ComboBoxField getComboBoxFieldAnnotation(Field field) {
		return field.getAnnotation(ComboBoxField.class);
	}

	public static boolean hasChooseFieldAnnotation(Field field) {
		return field.getAnnotation(ChooseField.class) != null;
	}
	public static ChooseField getChooseFieldAnnotation(Field field) {
		return field.getAnnotation(ChooseField.class);
	}

	@SuppressWarnings("unchecked")
	public static boolean hasColumnGroupsAnnotation(Class entityClass) {
		return entityClass.getAnnotation(ColumnGroups.class) != null;
	}
	@SuppressWarnings("unchecked")
	public static ColumnGroups getColumnGroupsAnnotation(Class entityClass) {
		return (ColumnGroups) entityClass.getAnnotation(ColumnGroups.class);
	}

	@SuppressWarnings("unchecked")
	public static boolean hasFieldSetsAnnotation(Class entityClass) {
		return entityClass.getAnnotation(FieldSets.class) != null;
	}
	@SuppressWarnings("unchecked")
	public static FieldSets getFieldSetsAnnotation(Class entityClass) {
		return (FieldSets) entityClass.getAnnotation(FieldSets.class);
	}

	@SuppressWarnings("unchecked")
	public static boolean hasViewConfigAnnotation(Class entityClass) {
		return entityClass.getAnnotation(ViewConfig.class) != null;
	}
	@SuppressWarnings("unchecked")
	public static ViewConfig getViewConfigAnnotation(Class entityClass) {
		return (ViewConfig) entityClass.getAnnotation(ViewConfig.class);
	}

	public static FieldSet getFieldSet(Class entityClass, String id) {
		FieldSets fieldSets = getFieldSetsAnnotation(entityClass);
		if (fieldSets == null)
			throw new IllegalArgumentException(concat("Class ", entityClass.getName(), " has no ", FieldSets.class.getName(), " annotation"));

		for (FieldSet fieldSet : fieldSets.fieldSets())
			if (fieldSet.id().equals(id))
				return fieldSet;

		throw new IllegalArgumentException(concat("Class ", entityClass.getName(), " has no ", FieldSet.class.getName(), " with id \"", id, "\""));
	}
	@SuppressWarnings("unchecked")
	public static boolean isTransient(Class entityClass, Field field) {
		if (hasTransientAnnotation(field)) // check @Transient annotation on field
			return true;

		try
		{  // check @Transient annotation on field getter method
			Method getterMethod = entityClass.getMethod( getGetterName(field.getName()) );
			return hasTransientAnnotation(getterMethod);
		}
		catch (NoSuchMethodException e)
		{
			return false; // no getter method -> field is not transient
		}
	}
	public static boolean hasTransientAnnotation(Field field) {
		return field.getAnnotation(Transient.class) != null;
	}
	public static boolean hasTransientAnnotation(Method method) {
		return method.getAnnotation(Transient.class) != null;
	}

	public static boolean isStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	public static String getFieldId(ExtEntity extEntity, Class entityClass, Field field) {
		IdField idField = field.getAnnotation(IdField.class);
		if (idField != null)
			if ( !idField.id().isEmpty() )
				return idField.id();

		TextField textField = field.getAnnotation(TextField.class);
		if (textField != null)
			if ( !textField.id().isEmpty() )
				return textField.id();

		OgrnTextField ogrnTextField = field.getAnnotation(OgrnTextField.class);
		if (ogrnTextField != null)
			if ( !ogrnTextField.id().isEmpty() )
				return ogrnTextField.id();

		KppTextField kppTextField = field.getAnnotation(KppTextField.class);
		if (kppTextField != null)
			if ( !kppTextField.id().isEmpty() )
				return kppTextField.id();

		InnJuridicalTextField innJuridicalTextField = field.getAnnotation(InnJuridicalTextField.class);
		if (innJuridicalTextField != null)
			if ( !innJuridicalTextField.id().isEmpty() )
				return innJuridicalTextField.id();

		IntegerField integerField = field.getAnnotation(IntegerField.class);
		if (integerField != null)
			if ( !integerField.id().isEmpty() )
				return integerField.id();

		SpinnerField spinnerField = field.getAnnotation(SpinnerField.class);
		if (spinnerField != null)
			if ( !spinnerField.id().isEmpty() )
				return spinnerField.id();

		NumberField numberField = field.getAnnotation(NumberField.class);
		if (numberField != null)
			if ( !numberField.id().isEmpty() )
				return numberField.id();

		DateField dateField = field.getAnnotation(DateField.class);
		if (dateField != null)
			if ( !dateField.id().isEmpty() )
				return dateField.id();

		CheckboxField checkboxField = field.getAnnotation(CheckboxField.class);
		if (checkboxField != null)
			if ( !checkboxField.id().isEmpty() )
				return checkboxField.id();

		LocalComboBoxField localComboBoxField = field.getAnnotation(LocalComboBoxField.class);
		if (localComboBoxField != null)
			if ( !localComboBoxField.id().isEmpty() )
				return localComboBoxField.id();

		ComboBoxField comboBoxField = field.getAnnotation(ComboBoxField.class);
		if (comboBoxField != null)
			if ( !comboBoxField.id().isEmpty() )
				return comboBoxField.id();

		ChooseField chooseField = field.getAnnotation(ChooseField.class);
		if (chooseField != null)
			if ( !chooseField.idFieldId().isEmpty() )
				return chooseField.idFieldId();

		// default value
		return concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, field.getName() );
	}

	public static String getFieldName(Field field) {
		IdField idField = field.getAnnotation(IdField.class);
		if (idField != null)
			if ( !idField.name().isEmpty() )
				return idField.name();

		TextField textField = getTextFieldAnnotation(field);
		if (textField != null)
			if ( !textField.name().isEmpty() )
				return textField.name();

		TextAreaField textAreaField = getTextAreaFieldAnnotation(field);
		if (textAreaField != null)
			if ( !textAreaField.name().isEmpty() )
				return textAreaField.name();

		OgrnTextField ogrnTextField = field.getAnnotation(OgrnTextField.class);
		if (ogrnTextField != null)
			if ( !ogrnTextField.name().isEmpty() )
				return ogrnTextField.name();

		KppTextField kppTextField = field.getAnnotation(KppTextField.class);
		if (kppTextField != null)
			if ( !kppTextField.name().isEmpty() )
				return kppTextField.name();

		InnJuridicalTextField innJuridicalTextField = field.getAnnotation(InnJuridicalTextField.class);
		if (innJuridicalTextField != null)
			if ( !innJuridicalTextField.name().isEmpty() )
				return innJuridicalTextField.name();

		IntegerField integerField = field.getAnnotation(IntegerField.class);
		if (integerField != null)
			if ( !integerField.name().isEmpty() )
				return integerField.name();

		SpinnerField spinnerField = field.getAnnotation(SpinnerField.class);
		if (spinnerField != null)
			if ( !spinnerField.name().isEmpty() )
				return spinnerField.name();

		NumberField numberField = field.getAnnotation(NumberField.class);
		if (numberField != null)
			if ( !numberField.name().isEmpty() )
				return numberField.name();

		DateField dateField = field.getAnnotation(DateField.class);
		if (dateField != null)
			if ( !dateField.name().isEmpty() )
				return dateField.name();

		CheckboxField checkboxField = field.getAnnotation(CheckboxField.class);
		if (checkboxField != null)
			if ( !checkboxField.name().isEmpty() )
				return checkboxField.name();

		LocalComboBoxField localComboBoxField = field.getAnnotation(LocalComboBoxField.class);
		if (localComboBoxField != null)
			if ( !localComboBoxField.name().isEmpty() )
				return localComboBoxField.name();

		ComboBoxField comboBoxField = field.getAnnotation(ComboBoxField.class);
		if (comboBoxField != null)
			if ( !comboBoxField.name().isEmpty() )
				return comboBoxField.name();

		ChooseField chooseField = field.getAnnotation(ChooseField.class);
		if (chooseField != null)
			if ( !chooseField.name().isEmpty() )
				return chooseField.name();

		AttachmentsField attachmentsField = field.getAnnotation(AttachmentsField.class);
		if (attachmentsField != null)
			if ( !attachmentsField.name().isEmpty() )
				return attachmentsField.name();

		// default value
		return field.getName();
	}
	public static String getFieldFieldSetId(Field field) {
		TextField textField = getTextFieldAnnotation(field);
		if (textField != null)
			if ( !textField.fieldSetId().isEmpty() )
				return textField.fieldSetId();

		TextAreaField textAreaField = getTextAreaFieldAnnotation(field);
		if (textAreaField != null)
			if ( !textAreaField.fieldSetId().isEmpty() )
				return textAreaField.fieldSetId();

		OgrnTextField ogrnTextField = field.getAnnotation(OgrnTextField.class);
		if (ogrnTextField != null)
			if ( !ogrnTextField.fieldSetId().isEmpty() )
				return ogrnTextField.fieldSetId();

		KppTextField kppTextField = field.getAnnotation(KppTextField.class);
		if (kppTextField != null)
			if ( !kppTextField.fieldSetId().isEmpty() )
				return kppTextField.fieldSetId();

		InnJuridicalTextField innJuridicalTextField = field.getAnnotation(InnJuridicalTextField.class);
		if (innJuridicalTextField != null)
			if ( !innJuridicalTextField.fieldSetId().isEmpty() )
				return innJuridicalTextField.fieldSetId();

		IntegerField integerField = field.getAnnotation(IntegerField.class);
		if (integerField != null)
			if ( !integerField.fieldSetId().isEmpty() )
				return integerField.fieldSetId();

		SpinnerField spinnerField = field.getAnnotation(SpinnerField.class);
		if (spinnerField != null)
			if ( !spinnerField.fieldSetId().isEmpty() )
				return spinnerField.fieldSetId();

		NumberField numberField = field.getAnnotation(NumberField.class);
		if (numberField != null)
			if ( !numberField.fieldSetId().isEmpty() )
				return numberField.fieldSetId();

		DateField dateField = field.getAnnotation(DateField.class);
		if (dateField != null)
			if ( !dateField.fieldSetId().isEmpty() )
				return dateField.fieldSetId();

		CheckboxField checkboxField = field.getAnnotation(CheckboxField.class);
		if (checkboxField != null)
			if ( !checkboxField.fieldSetId().isEmpty() )
				return checkboxField.fieldSetId();

		LocalComboBoxField localComboBoxField = field.getAnnotation(LocalComboBoxField.class);
		if (localComboBoxField != null)
			if ( !localComboBoxField.fieldSetId().isEmpty() )
				return localComboBoxField.fieldSetId();

		ComboBoxField comboBoxField = field.getAnnotation(ComboBoxField.class);
		if (comboBoxField != null)
			if ( !comboBoxField.fieldSetId().isEmpty() )
				return comboBoxField.fieldSetId();

		ChooseField chooseField = field.getAnnotation(ChooseField.class);
		if (chooseField != null)
			if ( !chooseField.fieldSetId().isEmpty() )
				return chooseField.fieldSetId();

		AttachmentsField attachmentsField = field.getAnnotation(AttachmentsField.class);
		if (attachmentsField != null)
			if ( !attachmentsField.fieldSetId().isEmpty() )
				return attachmentsField.fieldSetId();

		AddressField addressField = getAddressFieldAnnotation(field);
		if (addressField != null)
			if ( !addressField.fieldSetId().isEmpty() )
				return addressField.fieldSetId();

		// default value
		return null;
	}

	public static int getFieldMaxLength(Field field) {
		TextField textField = field.getAnnotation(TextField.class);
		if (textField != null)
			return textField.maxLength();

		TextAreaField textAreaField = getTextAreaFieldAnnotation(field);
		if (textAreaField != null)
			return textAreaField.maxLength();

		IntegerField integerField = field.getAnnotation(IntegerField.class);
		if (integerField != null)
			return integerField.maxLength();

		SpinnerField spinnerField = field.getAnnotation(SpinnerField.class);
		if (spinnerField != null)
			return spinnerField.maxLength();

		NumberField numberField = field.getAnnotation(NumberField.class);
		if (numberField != null)
			return numberField.maxLength();

		LocalComboBoxField localComboBoxField = field.getAnnotation(LocalComboBoxField.class);
		if (localComboBoxField != null)
			return localComboBoxField.maxLength();

		ComboBoxField comboBoxField = field.getAnnotation(ComboBoxField.class);
		if (comboBoxField != null)
			return comboBoxField.maxLength();

		// default value
		throw new IllegalArgumentException( concat("Cannot define maxLength for field \"", field.getName(), "\" in class ", field.getDeclaringClass().getName()) );
	}
	public static String getFieldLabel(Field field) {
		TextField textField = getTextFieldAnnotation(field);
		if (textField != null)
			return textField.label();

		TextAreaField textAreaField = getTextAreaFieldAnnotation(field);
		if (textAreaField != null)
			return textAreaField.label();

		OgrnTextField ogrnTextField = getOgrnTextFieldAnnotation(field);
		if (ogrnTextField != null)
			return ogrnTextField.label();

		KppTextField kppTextField = getKppTextFieldAnnotation(field);
		if (kppTextField != null)
			return kppTextField.label();

		InnJuridicalTextField innJuridicalTextField = getInnJuridicalTextFieldAnnotation(field);
		if (innJuridicalTextField != null)
			return innJuridicalTextField.label();

		IntegerField integerField = getIntegerFieldAnnotation(field);
		if (integerField != null)
			return integerField.label();

		SpinnerField spinnerField = getSpinnerFieldAnnotation(field);
		if (spinnerField != null)
			return spinnerField.label();

		NumberField numberField = getNumberFieldAnnotation(field);
		if (numberField != null)
			return numberField.label();

		DateField dateField = getDateFieldAnnotation(field);
		if (dateField != null)
			return dateField.label();

		CheckboxField checkboxField = getCheckboxFieldAnnotation(field);
		if (checkboxField != null)
			return checkboxField.label();

		LocalComboBoxField localComboBoxField = getLocalComboBoxFieldAnnotation(field);
		if (localComboBoxField != null)
			return localComboBoxField.label();

		ComboBoxField comboBoxField = getComboBoxFieldAnnotation(field);
		if (comboBoxField != null)
			return comboBoxField.label();

		AddressField addressField = getAddressFieldAnnotation(field);
		if (addressField != null)
			return addressField.textFieldLabel();

		// default value
		throw new IllegalArgumentException( concat("Cannot define label for field \"", field.getName(), "\" in class ", field.getDeclaringClass().getName()) );
	}
	public static String getFieldColumnHeader(Field field) {
		ColumnModel columnModel = getColumnModelAnnotation(field);
		if (columnModel != null && notEmpty(columnModel.header()))
			return columnModel.header();

		return getFieldLabel(field);
	}

	public static String getHeader(Method method) {
		if (!hasColumnModelAnnotation(method))
			throw new IllegalArgumentException( concat("method \"", method.getName(), "\" in class ",  method.getDeclaringClass().getName()," does not have ", ColumnModel.class.getName(), " annotation") );

		ColumnModel columnModel = getColumnModelAnnotation(method);
		return columnModel.header();
	}
	public static String getRenderer(Method method) {
		if (!hasColumnModelAnnotation(method))
			throw new IllegalArgumentException( concat("method \"", method.getName(), "\" in class ",  method.getDeclaringClass().getName()," does not have ", ColumnModel.class.getName(), " annotation") );

		ColumnModel columnModel = getColumnModelAnnotation(method);
		return columnModel.renderer();
	}
	public static Boolean getSortable(Method method) {
		if (!hasColumnModelAnnotation(method))
			throw new IllegalArgumentException( concat("method \"", method.getName(), "\" in class ",  method.getDeclaringClass().getName()," does not have ", ColumnModel.class.getName(), " annotation") );

		ColumnModel columnModel = getColumnModelAnnotation(method);
		return columnModel.sortable();
	}
	public static String getTooltip(Method method) {
		if (!hasColumnModelAnnotation(method))
			throw new IllegalArgumentException(concat("method \"", method.getName(), "\" in class ", method.getDeclaringClass().getName(), " does not have ", ColumnModel.class.getName(), " annotation"));

		ColumnModel columnModel = getColumnModelAnnotation(method);
		return columnModel.tooltip();
	}

	public static ColumnModel getColumnModelAnnotation(Field field) {
		return field.getAnnotation(ColumnModel.class);
	}
	public static boolean hasColumnModelAnnotation(Field field) {
		return getColumnModelAnnotation(field) != null;
	}
	public static ColumnModel getColumnModelAnnotation(Method method) {
		return method.getAnnotation(ColumnModel.class);
	}
	public static boolean hasColumnModelAnnotation(Method method) {
		return getColumnModelAnnotation(method) != null;
	}
	private static ColumnModelExclude getColumnModelExcludeAnnotation(Field field) {
		return field.getAnnotation(ColumnModelExclude.class);
	}
	public static boolean hasColumnModelExcludeAnnotation(Field field) {
		return getColumnModelExcludeAnnotation(field) != null;
	}

	public static String getFieldRenderer(Field field) {
		IdField idField = field.getAnnotation(IdField.class);
		if (idField != null)
			return null; // id field has no renderer

		TextField textField = field.getAnnotation(TextField.class);
		if (textField != null)
			if ( !textField.renderer().isEmpty() )
				return textField.renderer();

		IntegerField integerField = field.getAnnotation(IntegerField.class);
		if (integerField != null)
			if ( !integerField.renderer().isEmpty() )
				return integerField.renderer();

		SpinnerField spinnerField = field.getAnnotation(SpinnerField.class);
		if (spinnerField != null)
			if ( !spinnerField.renderer().isEmpty() )
				return spinnerField.renderer();

		NumberField numberField = field.getAnnotation(NumberField.class);
		if (numberField != null)
			if ( !numberField.renderer().isEmpty() )
				return numberField.renderer();

		DateField dateField = field.getAnnotation(DateField.class);
		if (dateField != null)
			if ( !dateField.renderer().isEmpty() )
				return dateField.renderer();

		CheckboxField checkboxField = field.getAnnotation(CheckboxField.class);
		if (checkboxField != null)
			return null; // checkbox field has no renderer

		LocalComboBoxField localComboBoxField = field.getAnnotation(LocalComboBoxField.class);
		if (localComboBoxField != null)
			if ( !localComboBoxField.renderer().isEmpty() )
				return localComboBoxField.renderer();

		ComboBoxField comboBoxField = field.getAnnotation(ComboBoxField.class);
		if (comboBoxField != null)
			if ( !comboBoxField.renderer().isEmpty() )
				return comboBoxField.renderer();

		// todo: ChooseField if needed
		// todo: implement other branches

		// default value
		return null;
	}
	public static String getFieldColumnRenderer(Field field) {
		ColumnModel columnModel = getColumnModelAnnotation(field);
		if (columnModel != null && notEmpty(columnModel.renderer()))
			return getRendererConstantName(columnModel.renderer());

		IdField idField = field.getAnnotation(IdField.class);
		if (idField != null)
			return null; // id field has no renderer

		TextField textField = getTextFieldAnnotation(field);
		if (textField != null)
			if ( !textField.renderer().isEmpty() )
				return getRendererConstantName(textField.renderer());

		OgrnTextField ogrnTextField = getOgrnTextFieldAnnotation(field);
		if (ogrnTextField != null)
			return DONT_SHOW_NIL_RENDERER_CONSTANT_NAME;

		KppTextField kppTextField = getKppTextFieldAnnotation(field);
		if (kppTextField != null)
			return DONT_SHOW_NIL_RENDERER_CONSTANT_NAME;

		InnJuridicalTextField innJuridicalTextField = getInnJuridicalTextFieldAnnotation(field);
		if (innJuridicalTextField != null)
			return DONT_SHOW_NIL_RENDERER_CONSTANT_NAME;

		IntegerField integerField = getIntegerFieldAnnotation(field);
		if (integerField != null)
			if ( !integerField.renderer().isEmpty() )
				return getRendererConstantName(integerField.renderer());

		SpinnerField spinnerField = getSpinnerFieldAnnotation(field);
		if (spinnerField != null)
			if ( !spinnerField.renderer().isEmpty() )
				return getRendererConstantName(spinnerField.renderer());

		NumberField numberField = getNumberFieldAnnotation(field);
		if (numberField != null)
		{
			return getValue(numberField.renderer(), getRendererConstantName(numberField.renderer()), FLOAT_RENDERER_CONSTANT_NAME);
		}

		DateField dateField = getDateFieldAnnotation(field);
		if (dateField != null)
		{
			return getValue( !dateField.renderer().equals(DateField.DEFAULT_FORM_RENDERER), getRendererConstantName(dateField.renderer()), DATE_RENDERER_CONSTANT_NAME );
		}

		CheckboxField checkboxField = getCheckboxFieldAnnotation(field);
		if (checkboxField != null)
		{
			return getValue(checkboxField.renderer(), getRendererConstantName(checkboxField.renderer()), BOOLEAN_RENDERER_CONSTANT_NAME);
		}

		LocalComboBoxField localComboBoxField = getLocalComboBoxFieldAnnotation(field);
		if (localComboBoxField != null)
			if ( !localComboBoxField.renderer().isEmpty() )
				return getRendererConstantName(localComboBoxField.renderer());

		ComboBoxField comboBoxField = getComboBoxFieldAnnotation(field);
		if (comboBoxField != null)
			if ( !comboBoxField.renderer().isEmpty() )
				return getRendererConstantName(comboBoxField.renderer());

		// todo: ChooseField if needed
		// todo: implement other branches

		// default value
		return null;
	}
	public static Boolean getFieldSortable(Field field) {
		ColumnModel columnModel = getColumnModelAnnotation(field);
		if (columnModel != null)
			return columnModel.sortable();

		return null;
	}
	public static String getFieldSortParam(Field field) {
		ColumnModel columnModel = getColumnModelAnnotation(field);
		if (columnModel != null && notEmpty(columnModel.sortParam()))
			return columnModel.sortParam();

		return null;
	}
	public static String getFieldGroupId(Field field) {
		ColumnModel columnModel = getColumnModelAnnotation(field);
		if (columnModel != null && notEmpty(columnModel.groupId()))
			return columnModel.groupId();

		return null;
	}
	public static String getFieldTooltip(Field field) {
		ColumnModel columnModel = getColumnModelAnnotation(field);
		if (columnModel != null && notEmpty(columnModel.tooltip()))
			return columnModel.tooltip();

		return null;
	}

	public static String getChooseButtonId(ExtEntity extEntity, Class entityClass, ChooseField chooseField, Field field) {
		String buttonId;

		if ( chooseField.chooseButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, field.getName(), JS_FIELD_NAME_SEPARATOR, "choose" );
		else
			buttonId = chooseField.chooseButtonId();

		logger.info( concat(field.getName(), " field choose button id: ", buttonId) );
		return buttonId;
	}
	public static String getShowButtonId(ExtEntity extEntity, Class entityClass, ChooseField chooseField, Field field) {
		String buttonId;

		if ( chooseField.showButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, field.getName(), JS_FIELD_NAME_SEPARATOR, "show" );
		else
			buttonId = chooseField.showButtonId();

		logger.info( concat(field.getName(), " field show button id: ", buttonId) );
		return buttonId;
	}

	public static String getChooseFieldChooseFunctionName(Field field) {
		ChooseField chooseField = getChooseFieldAnnotation(field);
		if ( !chooseField.chooseFunctionName().isEmpty())
			return chooseField.chooseFunctionName();

		Class<?> fieldClass = field.getType();
		ExtEntity fieldExtEntity = getExtEntityAnnotation(fieldClass);

		return concat( getChooseJsFullNamespace(fieldExtEntity, fieldClass), ".", getChooseInitFunctionName(fieldExtEntity) );
	}
	public static String getChooseFieldChooseFunctionSuccessHandlerParamName(Field field) {
		ChooseField chooseField = getChooseFieldAnnotation(field);
		if ( !chooseField.chooseFunctionSuccessHandlerParamName().isEmpty())
			return chooseField.chooseFunctionSuccessHandlerParamName();

		Class<?> fieldClass = field.getType();
		ExtEntity fieldExtEntity = getExtEntityAnnotation(fieldClass);

	 return getChooseInitConfigSuccessHandlerParamName(fieldExtEntity);
	}

	public static String getChooseFieldShowFunctionName(Field field) {
		ChooseField chooseField = getChooseFieldAnnotation(field);
		if ( !chooseField.showFunctionName().isEmpty())
			return chooseField.showFunctionName();

		Class<?> fieldClass = field.getType();
		ExtEntity fieldExtEntity = getExtEntityAnnotation(fieldClass);

		return concat( getFormJsFullNamespace(fieldExtEntity, fieldClass), ".", getFormShowFunctionName(fieldExtEntity) );
	}
	public static String getChooseFieldShowFunctionIdParamName(Field field) {
		ChooseField chooseField = getChooseFieldAnnotation(field);
		if ( !chooseField.showFunctionIdParamName().isEmpty())
			return chooseField.showFunctionIdParamName();

		Class<?> fieldClass = field.getType();
		ExtEntity fieldExtEntity = getExtEntityAnnotation(fieldClass);

	 return getFormConfigEntityIdParamName(fieldExtEntity);
	}


	public static String getLocalComboBoxStore(LocalComboBoxField localComboBoxField, Field field) {
		String store;

		if ( localComboBoxField.store().isEmpty() )
		{
			Class<?> fieldClass = field.getType();
			if (hasEnumFieldAnnotation(fieldClass))
			{ // получить store из enumField
				EnumField enumField = getEnumFieldAnnotation(fieldClass);
				store = getEnumStoreFullName(enumField, fieldClass);
			}
			else
			{ // по умолчанию - полное имя класса поля + Store
				store = concat( fieldClass.getName(), "Store" );
			}
		}
		else
		{
			store = localComboBoxField.store();
		}

		logger.info( concat(field.getName(), " local combobox store: ", store) );
		return store;
	}
	public static String getLocalComboBoxHiddenName(LocalComboBoxField localComboBoxField, Field field) {
		String hiddenName;

		if ( localComboBoxField.hiddenName().isEmpty() )
			hiddenName = getFieldName(field); // по умолчанию равно имени поля
		else
			hiddenName = localComboBoxField.hiddenName();

		logger.info( concat(field.getName(), " local combobox hiddenName: ", hiddenName) );
		return hiddenName;
	}
	public static int getLocalComboBoxListWidth(LocalComboBoxField localComboBoxField, Field field) {
		int listWidth;

		if ( localComboBoxField.listWidth() == LocalComboBoxField.DEFAULT_LIST_WIDTH )
			listWidth = localComboBoxField.width(); // по умолчанию ширина списка равна ширине комбобокса
		else
			listWidth = localComboBoxField.listWidth();

		logger.info( concat(field.getName(), " local combobox listWidth: ", listWidth) );
		return listWidth;
	}

	public static String getComboBoxHiddenName(ComboBoxField comboBoxField, Field field) {
		String hiddenName;

		if ( comboBoxField.hiddenName().isEmpty() )
			hiddenName = getFieldName(field); // по умолчанию равно имени поля
		else
			hiddenName = comboBoxField.hiddenName();

		logger.info( concat(field.getName(), " combobox hiddenName: ", hiddenName) );
		return hiddenName;
	}
	public static int getComboBoxListWidth(ComboBoxField comboBoxField, Field field) {
		int listWidth;

		if ( comboBoxField.listWidth() == LocalComboBoxField.DEFAULT_LIST_WIDTH )
			listWidth = comboBoxField.width(); // по умолчанию ширина списка равна ширине комбобокса
		else
			listWidth = comboBoxField.listWidth();

		logger.info( concat(field.getName(), " combobox listWidth: ", listWidth) );
		return listWidth;
	}


// --------------------------- Enum functions ----------------------------
	@SuppressWarnings(value = "unchecked")
	public static boolean hasEnumFieldAnnotation(Class enumClass) {
		return enumClass.getAnnotation(EnumField.class) != null;
	}
	public static boolean hasEnumFieldAnnotation(Field field) {
		return hasEnumFieldAnnotation(field.getType());
	}
	@SuppressWarnings(value = "unchecked")
	public static EnumField getEnumFieldAnnotation(Class enumClass) {
		return (EnumField) enumClass.getAnnotation(EnumField.class);
	}
	public static EnumField getEnumFieldAnnotation(Field field) {
		return getEnumFieldAnnotation(field.getType());
	}
	public static boolean hasEnumFieldValueAnnotation(Field field) {
		return field.getAnnotation(EnumFieldValue.class) != null;
	}
	public static EnumFieldValue getEnumFieldValueAnnotation(Field field) {
		return field.getAnnotation(EnumFieldValue.class);
	}

	public static String getEnumHashNamespace(EnumField enumField, Class enumClass) {
		String namespace;

		if ( enumField.hashNamespace().isEmpty() )
			namespace = getPackageName(enumClass.getName()); // по умолчанию пакет хэша энума равен пакету класса энума
		else
			namespace = enumField.hashNamespace();

		logger.info( concat("enum hash namespace: ", namespace) );
		return namespace;
	}
	public static String getEnumHashName(EnumField enumField, Class enumClass) {
		String name;

		if ( enumField.hashName().isEmpty() )
			name = enumClass.getSimpleName(); // по умолчанию название хэша энума равно названию класса энума
		else
			name = enumField.hashName();

		logger.info( concat("enum hash name: ", name) );
		return name;
	}
	public static String getEnumHashFullName(EnumField enumField, Class enumClass) {
		return concat(getEnumHashNamespace(enumField, enumClass), PACKAGE_SEPARATOR, getEnumHashName(enumField, enumClass));
	}

	public static String getEnumStoreNamespace(EnumField enumField, Class enumClass) {
		String namespace;

		if ( enumField.storeNamespace().isEmpty() )
			namespace = getPackageName(enumClass.getName()); // по умолчанию пакет хэша энума равен пакету класса энума
		else
			namespace = enumField.storeNamespace();

		logger.info( concat("enum store namespace: ", namespace) );
		return namespace;
	}
	public static String getEnumStoreName(EnumField enumField, Class enumClass) {
		String name;

		if ( enumField.storeName().isEmpty() )
			name = concat(enumClass.getSimpleName(), "Store"); // по умолчанию равно названию класса энума + "Store"
		else
			name = enumField.storeName();

		logger.info( concat("enum store name: ", name) );
		return name;
	}
	public static String getEnumStoreFullName(EnumField enumField, Class enumClass) {
		return concat(getEnumStoreNamespace(enumField, enumClass), PACKAGE_SEPARATOR, getEnumStoreName(enumField, enumClass));
	}

	public static String getEnumRendererName(EnumField enumField, Class enumClass) {
		String name;

		if ( enumField.rendererName().isEmpty() )
			name = concat(enumClass.getSimpleName(), "Renderer"); // по умолчанию равно названию класса энума + "Renderer"
		else
			name = enumField.rendererName();

		logger.info( concat("enum renderer name: ", name) );
		return name;
	}
	public static String getEnumRendererFullName(EnumField enumField, Class enumClass) {
		return concat(enumField.rendererNamespace(), PACKAGE_SEPARATOR, getEnumRendererName(enumField, enumClass));
	}

	public static String getEnumFieldValueHashName(EnumFieldValue enumFieldValue, Field field) {
		String name;

		if ( enumFieldValue.hashName().isEmpty() )
			name = field.getName().toUpperCase(); // по умолчанию — имя поля энума большими буквами
		else
			name = enumFieldValue.hashName();

		logger.info( concat("enum field value hash name: ", name) );
		return name;
	}
	public static String getEnumFieldValueHashValue(EnumFieldValue enumFieldValue, Field field) {
		String value;

		if ( enumFieldValue.hashValue().isEmpty() )
			value = field.getName(); // по умолчанию — имя поля энума
		else
			value = enumFieldValue.hashValue();

		logger.info( concat("enum field value hash value: ", value) );
		return value;
	}
	public static String getEnumFieldValueRendererValue(EnumFieldValue enumFieldValue) {
		String value;

		if ( enumFieldValue.rendererValue().isEmpty() )
			value = enumFieldValue.storeValue(); // по умолчанию — равно значению, используемому в store
		else
			value = enumFieldValue.rendererValue();

		logger.info( concat("enum field value renderer value: ", value) );
		return value;
	}

	public static String getEnumFieldRendererClassPackage(EnumField enumField, Class enumClass) {
		String packageName = getValue(enumField.rendererClassPackage(), enumClass.getPackage().getName());
		logger.info( concat("enum field renderer class package: ", packageName) );
		return packageName;
	}
	public static String getEnumFieldRendererClassSimpleName(EnumField enumField, Class enumClass) {
		String className = getValue(enumField.rendererClassName(), concat(enumClass.getSimpleName(), "CellRenderer"));
		logger.info( concat("enum field renderer class name: ", className) );
		return className;
	}
	public static String getEnumFieldRendererClassFullName(EnumField enumField, Class enumClass) {
		return getFullName(getEnumFieldRendererClassPackage(enumField, enumClass), getEnumFieldRendererClassSimpleName(enumField, enumClass));
	}

	public static String getEnumFieldRenderersConstantName(EnumField enumField, Class enumClass) {
		String constantName = getValue( enumField.rendererConstantName(), concat( getConstantName(enumClass.getSimpleName()), EnumField.RENDERER_CONSTANT_NAME_POSTFIX) );
		logger.info( concat("enum field renderer constant name (in Renderers class): ", constantName) );
		return constantName;
	}

	// --------------------------- Search field functions ----------------------------
	public static boolean hasSearchFieldAnnotation(Field field) {
		return field.getAnnotation(SearchField.class) != null;
	}
	public static SearchField getSearchFieldAnnotation(Field field) {
		return field.getAnnotation(SearchField.class);
	}
	public static int getSearchFieldRowNum(Field field) {
		if ( !hasSearchFieldAnnotation(field) )
			throw new IllegalArgumentException(concat("Field \"", field.getName(), "\" does not have ", SearchField.class.getName(), " annotation"));

		return getSearchFieldAnnotation(field).row();
	}

	public static List<Field> getSearchFields(Class entityClass) {
		List<Field> searchFields = new ArrayList<Field>();

		for (Field field : entityClass.getDeclaredFields())
		{
			// todo: также учитывать аннотации и других полей поиска
			if (hasSearchFieldAnnotation(field))
				searchFields.add(field);
		}

		return searchFields;
	}

	public static String getSearchFieldParamName(SearchField searchField, Field field) {
		String paramName;

		if ( searchField.paramName().isEmpty() )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				paramName = concat( field.getName(), capitalize(searchField.chooseFieldFieldName()) ); // по умолчанию — имя параметра = (имя поля с выбираемой сущностью + имя поля выбираемой сущности с большой буквы)
			}
			else
			{ // обычное поле
				paramName = field.getName(); // по умолчанию — имя параметра равно имени поля
			}
		}
		else
		{
			paramName = searchField.paramName();
		}

		logger.info( concat(field.getName(), " search field param name: ", paramName) );
		return paramName;
	}

	public static String getSearchFieldId(SearchField searchField, Field field) {
		String id;

		if ( searchField.id().isEmpty() )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				id = concat( field.getName(), capitalize(searchField.chooseFieldFieldName()), "SearchField" ); // по умолчанию — имя параметра = (имя поля с выбираемой сущностью + имя поля выбираемой сущности с большой буквы) + "SearchField"
			}
			else
			{ // обычное поле
				id = concat(field.getName(), "SearchField"); // по умолчанию — имя поля + "SearchField"
			}
		}
		else
		{
			id = searchField.id();
		}

		logger.info( concat(field.getName(), " search field id: ", id) );
		return id;
	}

	public static int getSearchFieldMaxLength(SearchField searchField, Field field) {
		int maxLength;

		if ( searchField.maxLength() == SearchField.DEFAULT_MAX_LENGTH_VALUE )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				Class<?> chooseFieldClass = field.getType();

				try
				{
					Field chooseFieldField = chooseFieldClass.getDeclaredField(searchField.chooseFieldFieldName());
					maxLength = getFieldMaxLength(chooseFieldField);
				}
				catch (NoSuchFieldException e)
				{
					throw new IllegalArgumentException( concat("Cannot define maxLength of field \"", field.getName(), "\". Field \"", searchField.chooseFieldFieldName(), "\" not found in class ", chooseFieldClass.getName()) );
				}
			}
			else
			{ // обычное поле
				maxLength = getFieldMaxLength(field); // по умолчанию — имя параметра равно имени поля
			}
		}
		else
		{
			maxLength = searchField.maxLength();
		}

		logger.info( concat(field.getName(), " search field maxLength: ", maxLength) );
		return maxLength;
	}

	public static String getSearchFieldSqlParamName(SearchField searchField, Field field) {
		String paramName;

		if ( searchField.sqlParamName().isEmpty() )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				paramName = concat( field.getName(), ".", searchField.chooseFieldFieldName() ); // по умолчанию — имя параметра = (имя поля с выбираемой сущностью + "." + имя поля выбираемой сущности)
			}
			else
			{ // обычное поле
				paramName = field.getName(); // по умолчанию — имя поля
			}
		}
		else
		{
			paramName = searchField.sqlParamName();
		}

		logger.info( concat(field.getName(), " search field sql param name: ", paramName) );
		return paramName;
	}

	// --------------------------- FilterConfig field functions ----------------------------
	public static boolean hasFilterConfigFieldAnnotation(Field field) {
		return field.getAnnotation(FilterConfigField.class) != null;
	}
	public static FilterConfigField getFilterConfigFieldAnnotation(Field field) {
		return field.getAnnotation(FilterConfigField.class);
	}

	public static List<Field> getFilterConfigFields(Class entityClass) {
		List<Field> filterConfigFields = new ArrayList<Field>();

		for (Field field : entityClass.getDeclaredFields())
		{
			if (hasFilterConfigFieldAnnotation(field))
				filterConfigFields.add(field);
		}

		return filterConfigFields;
	}
	public static boolean hasFilterConfigNotUppercaseFields(Class entityClass) {
		List<Field> filterConfigFields = getFilterConfigFields(entityClass);
		for (Field field : filterConfigFields)
		{
			if ( !getFilterConfigFieldAnnotation(field).uppercase() )
				return true;
		}

		return false;
	}
	public static boolean hasFilterConfigListFilterFields(Class entityClass) {
		for (Field field : getFilterConfigFields(entityClass))
		{
			if ( getFilterConfigFieldAnnotation(field).listFilterParam() )
				return true;
		}

		return false;
	}
	public static List<Field> getFilterConfigListFilterFields(Class entityClass) {
		List<Field> filterConfigFields = new ArrayList<Field>();

		for (Field field : getFilterConfigFields(entityClass))
		{
			if ( getFilterConfigFieldAnnotation(field).listFilterParam() )
				filterConfigFields.add(field);
		}

		return filterConfigFields;
	}
	public static String getFilterVarName(Field field) { // используется и в List, и в Choose, и в Form
//		return getSimpleName(field.getName());
		return decapitalize(field.getName());
	}
	public static String getFilterConfigListInitFunctionParamName(FilterConfigField filterConfigField, Field field) {
		String paramName;

		if ( filterConfigField.listInitFunctionParamName().isEmpty() )
			paramName = decapitalize(field.getName()); //  по умолчанию равно имени поля с маленькой буквы
		else
			paramName = filterConfigField.listInitFunctionParamName();

		logger.info( concat(field.getName(), " filter config field list init function param name: ", paramName) );
		return paramName;
	}


	public static boolean hasFilterConfigFormFilterFields(Class entityClass) {
		for (Field field : getFilterConfigFields(entityClass))
		{
			if ( getFilterConfigFieldAnnotation(field).formFilterParam() )
				return true;
		}

		return false;
	}
	public static List<Field> getFilterConfigFormFilterFields(Class entityClass) {
		List<Field> filterConfigFields = new ArrayList<Field>();

		for (Field field : getFilterConfigFields(entityClass))
		{
			if ( getFilterConfigFieldAnnotation(field).formFilterParam() )
				filterConfigFields.add(field);
		}

		return filterConfigFields;
	}
	public static List<Field> getFilterConfigFormFilterChooseFields(Class entityClass) { // только те параметры фильтрации, которые являются выбираемыми сущнсотями в crud-форме. Они будут жестко подставлены при указании
		List<Field> filterConfigFields = new ArrayList<Field>();

		for (Field field : getFilterConfigFields(entityClass))
		{
			if ( hasChooseFieldAnnotation(field) && getFilterConfigFieldAnnotation(field).formFilterParam() )
				filterConfigFields.add(field);
		}

		return filterConfigFields;
	}
	public static String getFilterConfigFormInitFunctionsParamName(FilterConfigField filterConfigField, Field field) {
		String paramName;

		if ( filterConfigField.formInitFunctionsParamName().isEmpty() )
			paramName = decapitalize(field.getName()); //  по умолчанию равно имени поля с маленькой буквы
		else
			paramName = filterConfigField.formInitFunctionsParamName();

		logger.info( concat(field.getName(), " filter config from init function config param name: ", paramName) );
		return paramName;
	}

	public static String getFilterConfigFieldName(FilterConfigField filterConfigField, Field field) {
		String name;

		if ( filterConfigField.name().isEmpty() )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				name = concat( field.getName(), capitalize(filterConfigField.chooseFieldFieldName()) ); // по умолчанию — имя параметра = (имя поля с выбираемой сущностью + имя поля выбираемой сущности с большой буквы)
			}
			else
			{ // обычное поле
				name = field.getName(); // по умолчанию — имя параметра равно имени поля
			}
		}
		else
		{
			name = filterConfigField.name();
		}

		logger.info( concat(field.getName(), " filter config field name: ", name) );
		return name;
	}
	public static String getFilterConfigFilterFieldName(FilterConfigField filterConfigField, Field field) {
		String name;

		if ( filterConfigField.filterFieldName().isEmpty() )
		{
			name = getFilterConfigFieldName(filterConfigField, field);
		}
		else
		{
			name = filterConfigField.filterFieldName();
		}

		logger.info( concat(field.getName(), " filter config filter field name (to get from entity): ", name) );
		return name;
	}
	public static String getFilterConfigFieldType(FilterConfigField filterConfigField, Field field) {
		String type;

		if ( filterConfigField.type().isEmpty() )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				type = filterConfigField.chooseFieldFieldType(); // берется указанный тип для поля связанной сущности
				// todo: если не указан тип chooseFieldFieldType, брать его по возможности из связанной сущности
			}
			else
			{ // обычное поле
				type = field.getType().getSimpleName(); // по умолчанию — имя параметра равно имени поля
			}
		}
		else
		{
			type = filterConfigField.type();
		}

		logger.info( concat(field.getName(), " filter config field type: ", type) );
		return type;
	}
	public static String getFilterConfigFieldSqlParamName(FilterConfigField filterConfigField, Field field) {
		String paramName;

		if ( filterConfigField.sqlParamName().isEmpty() )
		{
			if (hasChooseFieldAnnotation(field))
			{ // связанная сущность
				paramName = concat( field.getName(), ".", filterConfigField.chooseFieldFieldName() ); // по умолчанию — имя параметра = (имя поля с выбираемой сущностью + "." + имя поля выбираемой сущности)
			}
			else
			{ // обычное поле
				paramName = field.getName(); // по умолчанию — имя поля
			}
		}
		else
		{
			paramName = filterConfigField.sqlParamName();
		}

		logger.info( concat(field.getName(), " filter config field sql param name: ", paramName) );
		return paramName;
	}


	// attachment field
	public static boolean hasAttachmentsFieldAnnotation(Field field) {
		return field.getAnnotation(AttachmentsField.class) != null;
	}
	public static AttachmentsField getAttachmentsFieldAnnotation(Field field) {
		return field.getAnnotation(AttachmentsField.class);
	}
	public static boolean hasAttachmentsFields(Class entityClass) {
		for (Field field : entityClass.getDeclaredFields())
			if ( hasAttachmentsFieldAnnotation(field) )
				return true;

		return false;
	}
	public static List<Field> getAttachmentsFields(Class entityClass) {
		List<Field> fields = new ArrayList<Field>();

		for (Field field : entityClass.getDeclaredFields())
			if ( hasAttachmentsFieldAnnotation(field) )
				fields.add(field);

		return fields;
	}
	public static String getAttachmentsFieldIdsParamName(AttachmentsField attachmentsField, Field field) {
		String paramName;

		if ( attachmentsField.idsParamName().isEmpty() )
			paramName = concat(getFieldName(field), "Ids"); //  по умолчанию равно имени поля + "Ids"
		else
			paramName = attachmentsField.idsParamName();

		logger.info( concat(field.getName(), " attachments field ids paramName: ", paramName) );
		return paramName;
	}
	public static String getAttachmentsFieldEntityFieldName(AttachmentsField attachmentsField, Field field) {
		String entityFieldName;

		if ( attachmentsField.entityFieldName().isEmpty() )
			entityFieldName = field.getName(); //  по умолчанию равно имени поля
		else
			entityFieldName = attachmentsField.entityFieldName();

		logger.info( concat(field.getName(), " attachments field entityFieldName: ", entityFieldName) );
		return entityFieldName;
	}
	public static String getAttachmentsFieldPanelId(AttachmentsField attachmentsField, Field field, ExtEntity extEntity, Class entityClass) {
		String entityFieldName;

		if ( attachmentsField.panelId().isEmpty() )
			entityFieldName = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, field.getName() ); //  по умолчанию равно имени поля
		else
			entityFieldName = attachmentsField.panelId();

		logger.info( concat(field.getName(), " attachments field entityFieldName: ", entityFieldName) );
		return entityFieldName;
	}

	// --------------------------------------- AddressField ------------------------------
	public static boolean hasAddressFieldAnnotation(Field field) {
		return field.getAnnotation(AddressField.class) != null;
	}
	public static AddressField getAddressFieldAnnotation(Field field) {
		return field.getAnnotation(AddressField.class);
	}
	public static boolean hasAddressFields(Class entityClass) {
		for (Field field : entityClass.getDeclaredFields())
			if ( hasAddressFieldAnnotation(field) )
				return true;

		return false;
	}
	public static List<Field> getAddressFields(Class entityClass) {
		List<Field> fields = new ArrayList<Field>();

		for (Field field : entityClass.getDeclaredFields())
			if ( hasAddressFieldAnnotation(field) )
				fields.add(field);

		return fields;
	}

	public static String getGetAddressStrFunctionName(AddressField addressField, Field field) {
		return concat(getAddressNamespace(field), ".", addressField.getAddressStrFunctionName() );
	}
	public static String getAddressInitFunctionName(AddressField addressField, Field field) {
		return concat(getAddressNamespace(field), ".", addressField.initFunctionName() );
	}
	public static String getAddressNamespace(Field field) {
		Class<?> fieldClass = field.getType();

		if ( !hasExtEntityAnnotation(fieldClass) )
			throw new IllegalStateException( concat("Address class ", field.getType().getName(), " does not have @", ExtEntity.class.getName(), " annotation") );

		return getFormJsFullNamespace( getExtEntityAnnotation(fieldClass), fieldClass );
	}

	public static String getTextFieldId(ExtEntity extEntity, Class entityClass, AddressField addressField, Field field) {
		String buttonId;

		if ( addressField.textFieldId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, field.getName() );
		else
			buttonId = addressField.textFieldId();

		logger.info( concat(field.getName(), " address field text field id: ", buttonId) );
		return buttonId;
	}
	public static String getTextFieldName(AddressField addressField, Field field) {
		String buttonId;

		if ( addressField.textFieldName().isEmpty() )
			buttonId = concat( field.getName(), "Full" ); // по умолчанию (имя поля + full)
		else
			buttonId = addressField.textFieldName();

		logger.info( concat(field.getName(), " address field text field name: ", buttonId) );
		return buttonId;
	}
	public static String getUpdateButtonId(ExtEntity extEntity, Class entityClass, AddressField addressField, Field field) {
		String buttonId;

		if ( addressField.updateButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, field.getName(), JS_FIELD_NAME_SEPARATOR, "update" );
		else
			buttonId = addressField.updateButtonId();

		logger.info( concat(field.getName(), " address field update button id: ", buttonId) );
		return buttonId;
	}
	public static String getVoFieldName(AddressField addressField, Field field) {
		String fieldName;

		if ( addressField.voFieldName().isEmpty() )
			fieldName = field.getName(); // по умолчанию равно имени поля
		else
			fieldName = addressField.voFieldName();

		logger.info( concat(field.getName(), " address field vo field name: ", fieldName) );
		return fieldName;
	}

	private static final Logger logger = Logger.getLogger(ExtEntityFieldsUtils.class);
}