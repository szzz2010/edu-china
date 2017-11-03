package com.zichan.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.zichan.dao.SpringBaseDao;

@Repository("springBaseDao")
public class SpringBaseDaoImpl implements SpringBaseDao {

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
	public int[] batchInsert(String sql, Object[]... args) {
		List<Object[]> batchArgs = Arrays.asList(args);
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
		System.out.println(sql.toString());
		return insertOrUpdate(sql.toString(), list.toArray());
	}

}
