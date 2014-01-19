/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 11.08.2011 12:52:52$
*/
package su.opencode.kefir.util.test;

import junit.framework.TestCase;
import org.junit.Test;
import su.opencode.kefir.util.RestartJBoss;

import java.sql.SQLException;

public class RestartJBossTest extends TestCase
{
	@Test
	public void testRestartJBoss() throws SQLException {
		RestartJBoss.restart();
	}
}
