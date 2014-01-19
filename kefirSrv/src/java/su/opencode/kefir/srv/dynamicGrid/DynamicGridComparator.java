/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 07.09.2010 12:01:35$
*/
package su.opencode.kefir.srv.dynamicGrid;

import java.util.Comparator;

public class DynamicGridComparator implements Comparator<DynamicGrid>
{
	public int compare(DynamicGrid o1, DynamicGrid o2) {
		return o1.getOrder().compareTo(o2.getOrder());
	}
}