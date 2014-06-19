/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 16.04.2012 14:41:52$
*/
package su.opencode.kefir.gen.project.xml.orm;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.AddressField;
import su.opencode.kefir.gen.field.ChooseField;
import su.opencode.kefir.gen.field.ComboBoxField;
import su.opencode.kefir.gen.field.TextAreaField;
import su.opencode.kefir.gen.field.enumField.EnumField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getJoinColumnName;
import static su.opencode.kefir.gen.ExtEntityUtils.getOrmSequenceName;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class ExtEntityMappingAppender extends EntityMappingAppender
{
	public ExtEntityMappingAppender(String fileName, ExtEntity extEntity, Class entityClass) {
		super(fileName);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	@Override
	protected void appendEntityMapping(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		// check whether this entity's mapping is not yet appended to orm.xml
		String startEntityString = getStartEntityString(entityClass);
		for (String line : allFileLines)
		{
			if (line.contains(startEntityString))
			{
				logger.info( concat(sb, entityClass.getName(), " class orm mapping is already present in orm.xml."));
				return;
			}
		}

		startEntity(fileLinesToAppend, entityClass);
		appendTable(fileLinesToAppend);
		appendGenerator(fileLinesToAppend);
		appendAttributes(fileLinesToAppend);

		endEntity(fileLinesToAppend);

		appendEmptyLine(fileLinesToAppend);
	}
	private void appendTable(List<String> fileLines) {
		String tableName = ExtEntityUtils.getSqlTableName(extEntity, entityClass);
		if (tableName.equals(entityClass.getName())) // таблицы с аналогичными классу именами отдельно объявлять не надо
			return;

		appendTable(fileLines, tableName);
	}
	private void appendGenerator(List<String> fileLines) {
		appendSequenceGenerator(fileLines, getOrmSequenceName(extEntity, entityClass), ExtEntityUtils.getSqlSequenceName(extEntity, entityClass));
	}
	private void appendAttributes(List<String> fileLines) {
		List<Field> manyToOneFields = new ArrayList<Field>();
		List<Field> oneToOneFields = new ArrayList<Field>();

		startAttributes(fileLines);

		for (Field field : entityClass.getDeclaredFields())
		{
			if ( isTransient(entityClass, field) ) // transient fields do not map to sql
			{
				logger.info( concat(sb, "field \"", field.getName(), "\" is transient and would not be appended to orm mapping of class ", entityClass.getName()) );
				continue;
			}

			if ( isStatic(field) )
			{
				logger.info( concat(sb, "field \"", field.getName(), "\" is static and would not be appended to orm mapping of class ", entityClass.getName()) );
				continue;
			}

			if ( hasIdFieldAnnotation(field) )
			{
				appendId(fileLines, field.getName(), getOrmSequenceName(extEntity, entityClass)); // todo: возможность в IdField тоже указывать имя sql-столбца
				continue;
			}

			if ( hasChooseFieldAnnotation(field) )
			{
				ChooseField chooseField = getChooseFieldAnnotation(field);
				if (chooseField.addManyToOneMapping())
					manyToOneFields.add(field);

				continue;
			}

			if ( hasComboBoxFieldAnnotation(field) )
			{
				ComboBoxField comboBoxField = getComboBoxFieldAnnotation(field);
				if (comboBoxField.addManyToOneMapping())
					manyToOneFields.add(field);

				continue;
			}

			if ( hasAddressFieldAnnotation(field) )
			{
				AddressField addressField = getAddressFieldAnnotation(field);
				if (addressField.addOneToOneMapping())
					oneToOneFields.add(field);

				continue;
			}

			if ( hasEnumFieldAnnotation(field) )
			{
				EnumField enumField = getEnumFieldAnnotation(field);
				appendEnumeratedBasic(fileLines, field.getName(), ExtEntityUtils.getSqlColumnName(enumField, field));
				continue;
			}

			if ( hasTextAreaFieldAnnotation(field) )
			{
				TextAreaField textAreaField = getTextAreaFieldAnnotation(field);

				if (textAreaField.blob())
				{ // lob поле -> надо добавить еще указание <lob/>, вне зависимости от названия поля
					appendLobBasic(fileLines, field.getName());
				}
				else
				{
					appendBasic(fileLines, field.getName()); // simple field -> add basic column name, if needed // todo: возможность перегружать дефолтное имя sql-колонки во всех полях
				}

				continue;
			}

			// todo: маппинг List полей на one-to-many (если это указано в их аннотации)

			appendBasic(fileLines, field.getName()); // simple field -> add basic column name, if needed // todo: возможность перегружать дефолтное имя sql-колонки во всех полях
		}

		// append many to one fields
		if (!manyToOneFields.isEmpty())
			appendEmptyLine(fileLines);

		for (Field field : manyToOneFields)
		{
			if (hasChooseFieldAnnotation(field))
			{
				ChooseField chooseField = getChooseFieldAnnotation(field);
				appendManyToOne(fileLines, field.getName(), getJoinColumnName(chooseField, field));
			}
			else if ( hasComboBoxFieldAnnotation(field))
			{
				ComboBoxField chooseField = getComboBoxFieldAnnotation(field);
				appendManyToOne(fileLines, field.getName(), getJoinColumnName(chooseField, field));
			}
		}

		// append one to one fields
		if (!oneToOneFields.isEmpty())
			appendEmptyLine(fileLines);

		for (Field field : oneToOneFields)
		{
			AddressField addressField = getAddressFieldAnnotation(field);
			appendOneToOne(fileLines, field.getName(), getJoinColumnName(addressField, field));
		}

		endAttributes(fileLines);
	}

	private ExtEntity extEntity;
	private Class entityClass;
}