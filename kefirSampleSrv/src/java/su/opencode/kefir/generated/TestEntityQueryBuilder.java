/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.generated;

import su.opencode.kefir.sampleSrv.TestEntity;
import su.opencode.kefir.srv.EntityQueryBuilder;

public class TestEntityQueryBuilder extends EntityQueryBuilder
{
	public Class getEntityClass() {
		return TestEntity.class;
	}
	public String getSqlEntityName() {
		return "TestEntity";
	}
}