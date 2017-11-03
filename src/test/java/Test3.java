import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.zichan.utils.HttpClientUtil;

/** 
 * 测试基类 
 *  
 * 
 */  

@WebAppConfiguration  
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)  
@Transactional  
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"classpath*:xml/application-context.xml","classpath*:xml/application-servlet.xml"}) 

public class Test3 {

	 /** 
     * 日志打印 
     */  
    protected Logger log = LoggerFactory.getLogger(getClass());   
    
    
    @Test
	public void doTest(){
    	String method = "borrow.account.search";
		Map<String,Object> content = new HashMap<String,Object>();
		content.put("bankUserId", "");
    	
		String result = doQueryXS(content, method);
		
		log.info(result);
    	
	}
	
	public String doQueryXS(Map<String,Object>content_para,String method_para){
		String url = "http://10.40.1.31:19200/las/agency/api/kv";
		String appId = "124321513153213";//待协商
		String version = "1.0";
		String sign = "32651321";
		String method = method_para;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String content = new Gson().toJson(content_para);
		Map<String,Object>inParams = new HashMap<String,Object>();
		inParams.put("appId", appId);
		inParams.put("version", version);
		inParams.put("sign", sign);
		inParams.put("method", method);
		inParams.put("timestamp", timestamp);
		inParams.put("content", content);
		return HttpClientUtil.exec(url, "1", "1", inParams);
	}
	
	
}
