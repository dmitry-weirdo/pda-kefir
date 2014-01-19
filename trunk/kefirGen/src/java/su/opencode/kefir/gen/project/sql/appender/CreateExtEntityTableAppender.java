/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 17.04.2012 11:54:55$
*/
package su.opencode.kefir.gen.project.sql.appender;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.project.sql.SqlTable;
import su.opencode.kefir.gen.project.sql.SqlTableField;
import su.opencode.kefir.gen.project.sql.SqlTableForeignKey;
import su.opencode.kefir.gen.project.xml.orm.EntityMappingAppender;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.project.sql.SqlTable.getCreateTableString;
import static su.opencode.kefir.gen.project.sql.SqlTableField.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class CreateExtEntityTableAppender extends CreateTablesSqlAppender
{
	public CreateExtEntityTableAppender(String filePath, ExtEntity extEntity, Class entityClass) {
		super(filePath, null);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	@Override
	protected void appendCreateTable(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		// проверка, что создания таблицы еще нет
		String tableName = getSqlTableName(extEntity, entityClass);
		String createTableStr = getCreateTableString(tableName);

		for (String line : allFileLines)
		{
			if (line.endsWith(createTableStr))
			{
				logger.info( concat(sb, entityClass.getName(), " class create table (table name: \"", tableName, "\") is already present in createTables.sql."));
				return;
			}
		}

		SqlTable table = new SqlTable(tableName, extEntity.sqlTableComment() );

		for (Field field : entityClass.getDeclaredFields())
		{
			if ( isTransient(entityClass, field) ) // transient fields do not map to sql
			{
				logger.info( concat(sb, "field \"", field.getName(), "\" is transient and would not be appended to sql table for class ", entityClass.getName()) );
				continue;
			}

			if ( isStatic(field) )  // static fields do not map to sql
			{
				logger.info( concat(sb, "field \"", field.getName(), "\" is static and would not be appended to sql table for class ", entityClass.getName()) );
				continue;
			}

			if ( hasIdFieldAnnotation(field) )
			{
				table.addIdField(field.getName()); // todo: возможность в IdField тоже указывать имя sql-столбца
				continue;
			}

			if ( hasChooseFieldAnnotation(field) )
			{
				ChooseField chooseField = getChooseFieldAnnotation(field);
				if (chooseField.addManyToOneMapping())
				{
					Class<?> fieldClass = field.getType();
					if ( !hasExtEntityAnnotation(fieldClass) )
						throw new IllegalArgumentException( concat(sb, ExtEntity.class.getSimpleName(), " annotation not present for class ", fieldClass.getName()) );

					String columnName = getJoinColumnName(chooseField, field);
					table.addField( getIntegerField(columnName, !chooseField.allowBlank()) );
					table.addForeignKey( new SqlTableForeignKey(columnName, getSqlTableName( getExtEntityAnnotation(fieldClass), fieldClass) ) );
				}

				continue;
			}

			if ( hasComboBoxFieldAnnotation(field) )
			{
				ComboBoxField comboBoxField = getComboBoxFieldAnnotation(field);
				if (comboBoxField.addManyToOneMapping())
				{
					Class<?> fieldClass = field.getType();
					if ( !hasExtEntityAnnotation(fieldClass) )
						throw new IllegalArgumentException( concat(sb, ExtEntity.class.getSimpleName(), " annotation not present for class ", fieldClass.getName()) );

					String columnName = getJoinColumnName(comboBoxField, field);
					table.addField( getIntegerField(columnName, !comboBoxField.allowBlank()) );
					table.addForeignKey( new SqlTableForeignKey(columnName, getSqlTableName( getExtEntityAnnotation(fieldClass), fieldClass) ) );
				}

				continue;
			}

			if ( hasAddressFieldAnnotation(field) )
			{
				AddressField addressField = getAddressFieldAnnotation(field);
				if (addressField.addOneToOneMapping())
				{
					Class<?> fieldClass = field.getType();
					if ( !hasExtEntityAnnotation(fieldClass) )
						throw new IllegalArgumentException( concat(sb, ExtEntity.class.getSimpleName(), " annotation not present for class ", fieldClass.getName()) );

					String columnName = getJoinColumnName(addressField, field);
					table.addField( getIntegerField(columnName, !addressField.allowBlank()) );
					table.addForeignKey( new SqlTableForeignKey(columnName, getSqlTableName( getExtEntityAnnotation(fieldClass), fieldClass) ) );
				}

				continue;
			}

			if ( hasEnumFieldAnnotation(field) )
			{
				EnumField enumField = getEnumFieldAnnotation(field);
				if ( !hasLocalComboBoxFieldAnnotation(field) )
					throw new IllegalArgumentException( concat(sb, LocalComboBoxField.class.getSimpleName(), " annotation not present for field \"", field.getName(), "\" in class ", field.getType().getName()) );

				LocalComboBoxField localComboBoxField = getLocalComboBoxFieldAnnotation(field);

				table.addField( getVarcharField( getSqlColumnName(enumField, field), localComboBoxField.maxLength(), !localComboBoxField.allowBlank()) );
				continue;
			}

			if ( hasTextFieldAnnotation(field) )
			{
				TextField textField = getTextFieldAnnotation(field);
				table.addField( getVarcharField( EntityMappingAppender.getSqlColumnName(field), textField.maxLength(), !textField.allowBlank()) );
				continue;
			}

			if ( hasTextAreaFieldAnnotation(field) )
			{
				TextAreaField textAreaField = getTextAreaFieldAnnotation(field);

				if (textAreaField.blob())
					table.addField( getBlobField(EntityMappingAppender.getSqlColumnName(field)) );
				else
					table.addField( getVarcharField(EntityMappingAppender.getSqlColumnName(field), textAreaField.maxLength(), !textAreaField.allowBlank()) );

				continue;
			}

			if ( hasIntegerFieldAnnotation(field) )
			{
				IntegerField integerField = getIntegerFieldAnnotation(field);
				table.addField( getIntegerField(EntityMappingAppender.getSqlColumnName(field), !integerField.allowBlank()) );
				continue;
			}

			if ( hasSpinnerFieldAnnotation(field) )
			{
				SpinnerField spinnerField = getSpinnerFieldAnnotation(field);
				table.addField( getIntegerField(EntityMappingAppender.getSqlColumnName(field), !spinnerField.allowBlank()) );
				continue;
			}

			if ( hasNumberFieldAnnotation(field) )
			{
				NumberField numberField = getNumberFieldAnnotation(field);
				table.addField( getNumericField(EntityMappingAppender.getSqlColumnName(field), !numberField.allowBlank(), numberField.digits(), numberField.precision()) );
				continue;
			}

			if ( hasDateFieldAnnotation(field) )
			{
				DateField dateField = getDateFieldAnnotation(field);
				table.addField( getDateField(EntityMappingAppender.getSqlColumnName(field), !dateField.allowBlank()) );
				continue;
			}

			if ( hasCheckboxFieldAnnotation(field) )
			{
				CheckboxField checkboxField = getCheckboxFieldAnnotation(field);
				table.addField( getSmallintField(EntityMappingAppender.getSqlColumnName(field), !checkboxField.allowBlank()) );
				continue;
			}

			if ( hasLocalComboBoxFieldAnnotation(field) )
			{
				LocalComboBoxField localComboBoxField = getLocalComboBoxFieldAnnotation(field);
				table.addField( getVarcharField( EntityMappingAppender.getSqlColumnName(field), localComboBoxField.maxLength(), !localComboBoxField.allowBlank()) );
				continue;
			}

			if ( hasOgrnTextFieldAnnotation(field) )
			{
				OgrnTextField ogrnTextField = getOgrnTextFieldAnnotation(field);
				table.addField( getNumericField(EntityMappingAppender.getSqlColumnName(field), !ogrnTextField.allowBlank(), OgrnTextField.OGRN_FIELD_LENGTH, 0) );
				continue;
			}

			if ( hasKppTextFieldAnnotation(field) )
			{
				KppTextField kppTextField = getKppTextFieldAnnotation(field);
				table.addField( getNumericField(EntityMappingAppender.getSqlColumnName(field), !kppTextField.allowBlank(), KppTextField.KPP_FIELD_LENGTH, 0) );
				continue;
			}

			if ( hasInnJuridicalTextFieldAnnotation(field) )
			{
				InnJuridicalTextField innJuridicalTextField = getInnJuridicalTextFieldAnnotation(field);
				table.addField( getNumericField(EntityMappingAppender.getSqlColumnName(field), !innJuridicalTextField.allowBlank(), InnJuridicalTextField.INN_JURIDICAL_FIELD_LENGTH, 0) );
				continue;
			}

			if ( hasSqlColumnAnnotation(field) )
			{
				SqlColumn sqlColumn = getSqlColumnAnnotation(field);
				table.addField( new SqlTableField(sqlColumn, field) );
				continue;
			}

			logger.info( concat(sb, "Cannot define sql field type for field \"", field.getName(),"\" in class ", entityClass.getName()) );
		}

		table.setGeneratorName( getSqlSequenceName(extEntity, entityClass) );
		table.writeCreateTable(fileLinesToAppend);
	}

	private ExtEntity extEntity;
	private Class entityClass;
}