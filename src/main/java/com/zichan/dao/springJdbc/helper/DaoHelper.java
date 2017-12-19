package com.zichan.dao.springJdbc.helper;

import com.zichan.dao.springJdbc.MysqlSpringJdbcDao;

/**
 * powered by tianxinyang
 * async or static   in use
 */
public class DaoHelper {
	
	private static MysqlSpringJdbcDao mysqlSpringJdbcDao;
	
	public static MysqlSpringJdbcDao getMysqlSpringJdbcDao(){
		if(mysqlSpringJdbcDao == null ){
			mysqlSpringJdbcDao = (MysqlSpringJdbcDao) SpringApplicationContextHolder.getBean("mysqlSpringJdbcDao");
		}
		return mysqlSpringJdbcDao;
	}
	
}
