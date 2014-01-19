/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv;

/**
 * Возможное отношение элемента части where sql-запроса.
 */
public enum Relation
{
	equal, not_equal, more, more_equal, less, less_equal, like
}