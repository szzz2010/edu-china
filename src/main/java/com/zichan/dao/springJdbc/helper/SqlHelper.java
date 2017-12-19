package com.zichan.dao.springJdbc.helper;

import java.util.Map;

//import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.druid.sql.PagerUtils;

public class SqlHelper {

	public static String getCountSql(String sql){
		return PagerUtils.count(sql, "mysql");
	}
	
	public static String getLimitSql(String sql, int pageIndex, int pageSize){
		return PagerUtils.limit(sql, "mysql",(pageIndex-1)*pageSize,pageSize);
	}
	
	/**
	 * 查询 某个表的记录 根据参数
	 * @param tableName
	 * @param name_valueMap
	 * @param names
	 * @return select * from tableName where 1=1 and  name = name_valueMap.get(name) and ...
	 */
	public static String getSelectSqlByParams(String tableName,Map<String,Object>name_valueMap,String ... names){
		StringBuilder sb = new StringBuilder();
		sb.append("select * from "+tableName+" where 1=1 ");
		if(!ObjectUtils.isEmpty(names)){
			for(String name :names){
				Object value = name_valueMap.get(name);
				try {
					if((value instanceof String)&&StringUtils.isNotEmpty((String) value)){
						sb.append(" and "+name+" = '"+value+"' ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 *  仅仅给sql语句构建查询参数
	 * @param name_valueMap
	 * @param names
	 * @return select ... where 1=1 and  name = name_valueMap.get(name) and ...
	 */
	public static String getWhereSqlByParams(Map<String,Object>name_valueMap,String ... names){
		StringBuilder sb = new StringBuilder();
		sb.append(" where 1=1 ");
		if(!ObjectUtils.isEmpty(names)){
			for(String name :names){
				Object value = name_valueMap.get(name);
				try {
					if((value instanceof String)&&StringUtils.isNotEmpty((String) value)){
						sb.append(" and "+name+" = '"+value+"' ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
}
