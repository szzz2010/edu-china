package com.zichan.dao.springJdbc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.zichan.dao.springJdbc.MysqlSpringJdbcDao;
import com.zichan.dao.springJdbc.helper.DaoHelper;

@Repository("mysqlSpringJdbcDao")
public class MysqlSpringJdbcDaoImpl implements MysqlSpringJdbcDao {

	@Autowired
	DruidDataSource dataSource;

	private JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	private SimpleJdbcInsert simpleJdbcInsert() {
		return new SimpleJdbcInsert(jdbcTemplate());
	}

	@Override
	public void runSql(String sql) {
		jdbcTemplate().execute(sql);
	}

	@Override
	public int insertOrUpdate(String sql, Object... args) {
		return jdbcTemplate().update(sql, args);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) {
		return jdbcTemplate().queryForMap(sql, args);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return jdbcTemplate().queryForList(sql, args);
	}

	@Override
	public int[] batchInsertOrUpdate(String sql,List<Object[]> batchArgs) {
		return jdbcTemplate().batchUpdate(sql, batchArgs);
	}

	@Override
	public <T> Object queryForObject(String sql, Class<T> clazz, Object... args) {
		return jdbcTemplate().queryForObject(sql, clazz, args);
	}

	@Override
	public Number addAndGetId(String tableName, Map<String, Object> args) {
		return simpleJdbcInsert().withTableName(tableName).usingGeneratedKeyColumns("id").executeAndReturnKey(args);
	}

	@Override
	public Map<String, Object> addAndReturn(String tableName, Map<String, Object> args) {
		return jdbcTemplate().queryForMap(" select * from `"+tableName+"` where `id` = ? ", new Object[]{simpleJdbcInsert().withTableName(tableName).usingGeneratedKeyColumns("id").executeAndReturnKey(args)});
	}

	@Override
	public int Update(Map<String, Object> para, String tableName, Map<String, Object> where) {
		StringBuilder sql = new StringBuilder(" UPDATE `" + tableName + "` SET ");
		List<Object> list = new ArrayList<Object>();
		Set<String> keySet = para.keySet();
		for (String string : keySet) {
			sql.append("`" + string + "`=?,");
			list.add(para.get(string));
		}
		sql.deleteCharAt(sql.length() - 1);
		if (where != null && !where.isEmpty()) {
			sql.append(" WHERE ");
			keySet = where.keySet();
			for (String string : keySet) {
				sql.append(" `" + string + "` =? ");
				sql.append(" and ");
				list.add(where.get(string));
			}
		}
		sql.delete(sql.length() - 4,sql.length());
		return insertOrUpdate(sql.toString(), list.toArray());
	}

	@Override
	public Map<String,Object> commonData(Map<String,Object>params){
		String sql = (String) params.get("sql");
		String command = (String) params.get("command");
		Object data = null;
		if("execute".equals(command)){
			DaoHelper.getMysqlSpringJdbcDao().runSql(sql);
		}
		if("insertOrUpdate".equals(command)){
			data = DaoHelper.getMysqlSpringJdbcDao().insertOrUpdate(sql);
		}
		if("queryForMap".equals(command)){
			data = DaoHelper.getMysqlSpringJdbcDao().queryForMap(sql);
		}
		if("queryForList".equals(command)){
			data = DaoHelper.getMysqlSpringJdbcDao().queryForList(sql);
		}
		params.put("data", data);
		return params;
	}
	
}
