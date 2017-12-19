package com.zichan.dao.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * redis 数据源接口
 */
public interface RedisDataSource {
	
	/**
	 * 获取redias连接
	 */
	public abstract ShardedJedis getRedisClient();

	/**
	 * 关闭redis连接
	 * @param shardedJedis
	 */
	public void close(ShardedJedis shardedJedis);

}