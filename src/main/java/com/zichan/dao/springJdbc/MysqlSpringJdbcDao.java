package com.zichan.dao.springJdbc;

import java.util.List;
import java.util.Map;

public interface MysqlSpringJdbcDao {

	void runSql(String sql);

	int insertOrUpdate(String sql, Object... args);

	Map<String, Object> queryForMap(String sql, Object... args);

	Number addAndGetId(String tableName, Map<String, Object> args);

	int[] batchInsertOrUpdate(String sql, List<Object[]> batchArgs);
	
	List<Map<String, Object>> queryForList(String sql, Object... args);

	<T> Object queryForObject(String sql, Class<T> clazz, Object... args);

	Map<String, Object> addAndReturn(String tableName, Map<String, Object> args);

	int Update(Map<String, Object> para, String tableName, Map<String, Object> where);

	Map<String, Object> commonData(Map<String, Object> params);

}
