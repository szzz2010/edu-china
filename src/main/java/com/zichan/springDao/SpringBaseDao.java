package com.zichan.dao;

import java.util.List;
import java.util.Map;

public interface SpringBaseDao {
	void runSql(String sql);

	int insertOrUpdate(String sql, Object... args);

	int[] batchInsert(String sql, Object[]... args);

	Map<String, Object> queryForMap(String sql, Object... args);

	List<Map<String, Object>> queryForList(String sql, Object... args);

	<T> Object queryForObject(String sql, Class<T> clazz, Object... args);

	Number addAndGetId(String tableName, Map<String, Object> args);

	Map<String, Object> addAndReturn(String tableName, Map<String, Object> args);

	int Update(Map<String, Object> para, String tableName, Map<String, Object> where);

}
