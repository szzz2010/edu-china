package com.zichan.dao.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Base64Utils;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

/**
 * redis-client 操作的封装 <br/>
 * 依赖 {@link com.hello.redis.RedisDataSource}
 */
public class RedisClientTemplate {

	private static Log log = LogFactory.getLog(RedisDataSourceImpl.class);

	private RedisDataSource redisDataSource;

	public RedisDataSource getRedisDataSource() {
		return redisDataSource;
	}

	public void setRedisDataSource(RedisDataSource redisDataSource) {
		this.redisDataSource = redisDataSource;
	}

	/**
	 * 获取单个值(String)
	 * 
	 * @param key
	 * @return stringValue
	 */
	public String get(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.get(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 设置单个值(String)
	 * 
	 * @param key
	 * @param value
	 */
	public String set(String key, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 设置对象(Serializable)
	 * 
	 * @param key
	 * @param value
	 *            Serializable 类型对象
	 * @return 成功返回OK
	 */
	public String setObj(String key, Serializable value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			byte[] s = SerializationUtils.serialize(value);
			result = shardedJedis.set(key, Base64Utils.encodeToString(s));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 获取对象(Serializable)
	 * 
	 * @param key
	 * @return Serializable类型对象
	 */
	public Serializable getObj(String key) {
		Serializable result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			String value = shardedJedis.get(key);
			if(StringUtils.isNotBlank(value)){
			   result = (Serializable) SerializationUtils.deserialize(Base64Utils.decodeFromString(value));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 检查给定 key 是否存在。
	 * 
	 * @param key
	 */
	public Boolean exists(String key) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回 key 所储存的值的类型
	 * 
	 * @param key
	 * @return none (key不存在)<br/>
	 *         string (字符串)<br/>
	 *         list (列表)<br/>
	 *         set (集合)<br/>
	 *         zset (有序集)<br/>
	 *         hash (哈希表)<br/>
	 */
	public String type(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.type(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * 
	 * @param key
	 * @param seconds
	 *            秒
	 * @return 设置成功返回 1 。<br/>
	 *         当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key
	 *         的生存时间)，返回 0 。
	 */
	public Long expire(String key, int seconds) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 使指定 key 在timeStemp时间点过期
	 * 
	 * @param key
	 * @param timeStemp
	 *            unix时间戳
	 * @return 如果生存时间设置成功，返回 1 。<br/>
	 *         当 key 不存在或没办法设置生存时间，返回 0 。
	 */
	public Long expireAt(String key, long timeStemp) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.expireAt(key, timeStemp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 * 
	 * @param key
	 */
	public Long ttl(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.ttl(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。<br/>
	 * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推。 如果redis版本低于2.0 请调用 @
	 * {@link RedisClientTemplate#substr(String, int, int)}
	 * 
	 * @param key
	 * @param startOffset
	 * @param endOffset
	 */
	public String getrange(String key, long startOffset, long endOffset) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。<br/>
	 * 
	 * 若给定的 key 已经存在，则 SETNX 不做任何动作。<br/>
	 * 
	 * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
	 * 
	 * @param key
	 * @param value
	 * @return 设置成功，返回 1 。<br/>
	 *         设置失败，返回 0 。
	 */
	public Long setnx(String key, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.setnx(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。<br/>
	 * 
	 * 如果 key 已经存在， SETEX 命令将覆写旧值。
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return 设置成功时返回 OK 。<br/>
	 *         当 seconds 参数不合法时，返回一个错误。
	 */
	public String setex(String key, int seconds, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。<br/>
	 * 
	 * 当 key 存在但不是字符串类型时，返回一个错误。
	 * 
	 * @param key
	 * @param value
	 * @return 返回给定 key 的旧值。<br/>
	 *         当 key 没有旧值时，也即是， key 不存在时，返回 nil 。
	 */
	public String getSet(String key, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.getSet(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将 key 所储存的值减去减量 integer 。<br/>
	 * 
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。<br/>
	 * 
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
	 * 
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @param integer
	 * @return 减去 integer 之后， key 的值。
	 */
	public Long decrBy(String key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decrBy(key, integer);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将 key 中储存的数字值减一。<br/>
	 * 
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。<br/>
	 * 
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
	 * 
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @return 执行 DECR 命令之后 key 的值。
	 */
	public Long decr(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decr(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将 key 所储存的值加上增量 integer 。<br/>
	 * 
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。<br/>
	 * 
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
	 * 
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * 
	 * @param key
	 * @param integer
	 * @return 加上 integer 之后， key 的值
	 */
	public Long incrBy(String key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incrBy(key, integer);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将 key 中储存的数字值增一。<br/>
	 * 
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。<br/>
	 * 
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
	 * 
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @return 执行 INCR 命令之后 key 的值。
	 */
	public Long incr(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。<br/>
	 * 
	 * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
	 * 
	 * @param key
	 * @param value
	 * @return 追加 value 之后， key 中字符串的长度。
	 */
	public Long append(String key, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.append(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。<br/>
	 * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public String substr(String key, int start, int end) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.substr(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。<br/>
	 * 
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。<br/>
	 * 
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。 <br/>
	 *         如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
	 * @version 201609
	 */
	public Long hset(String key, String field, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 设置obj
	 * 
	 * @param key
	 * @param field
	 * @param value
	 *            Serializable类型实体
	 */
	public Long hsetObj(String key, String field, Serializable value) {
		byte[] s = SerializationUtils.serialize(value);
		return hset(key, field, Base64Utils.encodeToString(s));
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 * 
	 * @param key
	 * @param field
	 * @return 给定域的值。 <br/>
	 *         当给定域不存在或是给定 key 不存在时，返回 nil 。
	 */
	public String hget(String key, String field) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hget(key, field);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 取出对象
	 * @param key
	 * @param field
	 */
	public Serializable hgetObj(String key, String field) {
		Serializable result = null;
		String value = hget(key, field);
		if(value != null)
			result = (Serializable) SerializationUtils.deserialize(Base64Utils.decodeFromString(value));
		return result;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。<br/>
	 * 
	 * 若域 field 已经存在，该操作无效。<br/>
	 * 
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 设置成功，返回 1 。<br/>
	 *         如果给定域已经存在且没有操作被执行，返回 0 。
	 */
	public Long hsetnx(String key, String field, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hsetnx(key, field, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将map设置到哈希表 key 中。 <br/>
	 * 
	 * 此命令会覆盖哈希表中已存在的域。<br/>
	 * 
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。<br/>
	 * 
	 * @param key
	 * @param hash
	 * @return 如果命令执行成功，返回 OK 。<br/>
	 *         当 key 不是哈希表(hash)类型时，返回一个错误。
	 */
	public String hmset(String key, Map<String, String> hash) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmset(key, hash);

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。<br/>
	 * 
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。<br/>
	 * 
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 * 
	 * @param key
	 * @param fields
	 *            多个field
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
	 */
	public List<String> hmget(String key, String... fields) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmget(key, fields);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 value 。<br/>
	 * 
	 * 增量也可以为负数，相当于对给定域进行减法操作。<br/>
	 * 
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。<br/>
	 * 
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。<br/>
	 * 
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
	 */
	public Long hincrBy(String key, String field, long value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hincrBy(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在
	 * 
	 * @param key
	 * @param field
	 */
	public Boolean hexists(String key, String field) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hexists(key, field);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 删除给定的一个 key 。<br/>
	 * 
	 * 不存在的 key 会被忽略。
	 * 
	 * @param key
	 * @return 被删除 key 的数量。
	 */
	public Long del(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.del(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。<br/>
	 * 
	 * @param key
	 * @param field
	 * @return 被成功移除的域的数量，不包括被忽略的域。
	 */
	public Long hdel(String key, String field) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回哈希表 key 中域的数量。
	 * 
	 * @param key
	 * @return 哈希表中域的数量。<br/>
	 *         当 key 不存在时，返回 0 。
	 */
	public Long hlen(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hlen(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 * 
	 * @param key
	 * @return 一个包含哈希表中所有域的表。<br/>
	 *         当 key 不存在时，返回一个空表。
	 */
	public Set<String> hkeys(String key) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hkeys(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 * 
	 * @param key
	 * @return 一个包含哈希表中所有值的表。<br/>
	 *         当 key 不存在时，返回一个空表。
	 */
	public List<String> hvals(String key) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hvals(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。<br/>
	 * 
	 * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 * 
	 * @param key
	 * @return 以列表形式返回哈希表的域和域的值。<br/>
	 *         若 key 不存在，返回空列表。
	 */
	public Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hgetAll(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	// ================list ====== l表示 list或 left, r表示right====================
	/**
	 * 将一个值插入到列表最右边(表尾)
	 * 
	 * @param key
	 * @param value
	 * @return 执行 RPUSH 操作后，表的长度。
	 */
	public Long rpush(String key, String value) { 
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将一个值插入到列表最左边(表头)
	 * 
	 * @param key
	 * @param string
	 */
	public Long lpush(String key, String string) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpush(key, string);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回列表 key 的长度。
	 * 
	 * @param key
	 */
	public Long llen(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.llen(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回列表key 中指定区间内的元素,区间以偏移量 start 和 stop 指定。<br/>
	 * 可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrange(key, start, end);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public String ltrim(String key, long start, long end) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ltrim(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。<br/>
	 * 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 
	 * @param key
	 * @param index
	 */
	public String lindex(String key, long index) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lindex(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public String lset(String key, long index, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lset(key, index, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。<br/>
	 * count 的值可以是以下几种：<br/>
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。<br/>
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。<br/>
	 * count = 0 : 移除表中所有与 value 相等的值。
	 * 
	 * @param key
	 * @param count
	 * @param value
	 */
	public Long lrem(String key, long count, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrem(key, count, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除并返回列表 key 的头元素。
	 * 
	 * @param key
	 */
	public String lpop(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpop(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除并返回列表 key 的尾元素。
	 * 
	 * @param key
	 */
	public String rpop(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpop(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将一个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。<br/>
	 * 
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。<br/>
	 * 
	 * 当 key 不是集合类型时，返回一个错误。<br/>
	 * 
	 * @param key
	 * @param member
	 */
	public Long sadd(String key, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sadd(key, member);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回集合 key 中的所有成员。
	 * 
	 * @param key
	 */
	public Set<String> smembers(String key) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.smembers(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除集合 key 中的一个 member 元素，不存在的 member 元素会被忽略。
	 * 
	 * @param key
	 * @param member
	 */
	public Long srem(String key, String member) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();

		Long result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 * 
	 * @param key
	 */
	public String spop(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.spop(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回集合 key 的基数(集合中元素的数量)
	 * 
	 * @param key
	 */
	public Long scard(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Long result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.scard(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员。
	 * 
	 * @param key
	 * @param member
	 */
	public Boolean sismember(String key, String member) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Boolean result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sismember(key, member);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回集合中的一个随机元素。(不移除)
	 * 
	 * @param key
	 */
	public String srandmember(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srandmember(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将一个 member 元素及其 score 值加入到有序集 key 当中
	 * 
	 * @param key
	 * @param score
	 * @param member
	 */
	public Long zadd(String key, double score, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。<br/>
	 * 
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除有序集 key 中的一个成员，不存在的成员将被忽略。
	 * 
	 * @param key
	 * @param member
	 */
	public Long zrem(String key, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 为有序集 key 的成员 member 的值加上增量 score
	 * 
	 * @param key
	 * @param score
	 * @param member
	 */
	public Double zincrby(String key, double score, String member) {
		Double result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zincrby(key, score, member);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。<br/>
	 * 
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 * 
	 * @param key
	 * @param member
	 */
	public Long zrank(String key, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrank(key, member);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。<br/>
	 * 
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 
	 * @param key
	 * @param member
	 */
	public Long zrevrank(String key, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrank(key, member);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。<br/>
	 * 
	 * 其中成员的位置按 score 值递减(从大到小)来排列。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrange(key, start, end);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于下标 start 和 end 之间的成员。有序集成员按 score 值递增(从小到大)次序排列。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于下标 start 和 end 之间的成员。有序集成员按 score 值递增(从大到小)次序排列。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrangeWithScores(key, start, end);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 的基数(count)。
	 * 
	 * @param key
	 */
	public Long zcard(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zcard(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。
	 * 
	 * @param key
	 * @param member
	 */
	public Double zscore(String key, String member) {
		Double result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zscore(key, member);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回或保存给定列表、集合、有序集合 key 中经过排序的元素。
	 * 
	 * @param key
	 */
	public List<String> sort(String key) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.sort(key);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.sort(key, sortingParameters);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 
	 * @param key
	 * @param min
	 * @param max
	 */
	public Long zcount(String key, double min, double max) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zcount(key, min, max);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递增(从大到小)次序排列。<br/>
	 * 
	 * @param key
	 * @param min
	 * @param max
	 */
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrangeByScore(key, max, min);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递增(从小到大)次序排列。<br/>
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset
	 * 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 */
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrangeByScore(key, min, max, offset, count);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score
	 * 值递减(从大到小)的次序排列。<br/>
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset
	 * 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
	 * 
	 * @param key
	 * @param max
	 * @param min
	 * @param offset
	 * @param count
	 */
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递增(从大到小)次序排列。<br/>
	 * 
	 * @param key
	 * @param min
	 * @param max
	 */
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrangeByScoreWithScores(key, min, max);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递减(从小到大)次序排列。<br/>
	 * 
	 * @param key
	 * @param max
	 * @param min
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递增(从小到大)次序排列。<br/>
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset
	 * 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 */
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score
	 * 值递减(从大到小)的次序排列。<br/>
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset
	 * 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
	 * 
	 * @param key
	 * @param max
	 * @param min
	 * @param offset
	 * @param count
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Long zremrangeByRank(String key, int start, int end) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zremrangeByRank(key, start, end);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 移除有序集 key 中，指定区间内的所有成员。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Long zremrangeByScore(String key, double start, double end) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.zremrangeByScore(key, start, end);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。<br/>
	 * 
	 * 当 pivot 不存在于列表 key 时，不执行任何操作。<br/>
	 * 
	 * 当 key 不存在时， key 被视为空列表，不执行任何操作。<br/>
	 * 
	 * @param key
	 * @param where
	 * @{@link LIST_POSITION}}
	 * @param pivot
	 * @param value
	 */
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {

			result = shardedJedis.linsert(key, where, pivot, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	public Jedis getShard(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Jedis result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getShard(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	public JedisShardInfo getShardInfo(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		JedisShardInfo result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getShardInfo(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	public String getKeyTag(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getKeyTag(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	public Collection<JedisShardInfo> getAllShardInfo() {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Collection<JedisShardInfo> result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getAllShardInfo();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

	public Collection<Jedis> getAllShards() {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Collection<Jedis> result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getAllShards();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);

		} finally {
			redisDataSource.close(shardedJedis);
		}
		return result;
	}

}
