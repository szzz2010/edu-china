import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zichan.dao.SpringBaseDao;

  
/** 
 * 测试基类 
 */  


@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)  
@Transactional  
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"classpath*:xml/application-context.xml","classpath*:xml/application-servlet.xml"}) 
public class Test2 {  
  
    /** 
     * 日志打印 
     */  
    protected Logger log = LoggerFactory.getLogger(getClass());   
    
   @Autowired 
   SpringBaseDao springBaseDao;
    
    @Test
    public void doTest(){
    	int a = (int) springBaseDao.queryForObject(" select count(*) from order_info ",int.class);
    	Map<String,Object> orderMap = springBaseDao.queryForMap(" select * from order_info where id = 1 ");
    	List<Map<String,Object>> orderList = springBaseDao.queryForList(" select * from order_info  ");
    
     	log.info(a+"");
    	log.info(orderMap.toString());
    	log.info(orderList.toString());
    	
    	orderMap.put("name", "test2");
    	orderMap.remove("id");
    	Number n = springBaseDao.addAndGetId("order_info", orderMap);
    	log.info(n.toString());
    	Map<String, Object> addAndReturn = springBaseDao.addAndReturn("order_info", orderMap);
    	log.info(addAndReturn.toString());
    	Map<String,Object> where = new HashMap<String,Object>();
    	Map<String,Object> para = new HashMap<String,Object>();
    	para.put("money", 10085.33);
    	where.put("name", "test2");
    	int i = springBaseDao.Update(para, "order_info", where);
    	log.info(i+"");
    	
    	
    }
}  
