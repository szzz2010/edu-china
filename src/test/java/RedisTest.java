import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zichan.dao.redis.RedisClientTemplate;
import com.zichan.dao.redis.key.RedisKey;
import com.zichan.dao.springJdbc.helper.DaoHelper;

@WebAppConfiguration  
@Rollback(value=false)
@Transactional(transactionManager="txManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:xml/application*.xml"}) 

public class RedisTest {

	
	 /** 
     * 日志打印 
     */  
    protected Logger log = LoggerFactory.getLogger(getClass());   
    
    
    @Autowired
	private RedisClientTemplate redisClientTemplate;
    
    @Test
    public void test() {
		log.info("===========begin==============");
		String result = redisClientTemplate.setex(RedisKey.HELLO_SAAS_TOKEN, 30 * 60, "haha,test!");
		log.info("===========end==============");
		Map<String,Object> order = DaoHelper.getMysqlSpringJdbcDao().queryForMap(" select * from order_info where id = 1 ");
		String order_json = JSON.toJSONString(order);
		String key = RedisKey.formatter(RedisKey.HELLO_ORDER_ORDERINFO,(String)order.get("name"));
		String redis_result = redisClientTemplate.setex(key,30*24*60*60, order_json==null?"":order_json);
		log.info(" redis-key :"+key+",redis-result:"+redis_result);
    
    }
    
    
    
}
