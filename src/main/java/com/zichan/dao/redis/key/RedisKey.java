package com.zichan.dao.redis.key;

/**
 * 
 * @ClassName: RedisKey
 * @Description: rediskey配置
 * @author liyanchao
 * @date 2017年8月10日 上午10:39:50
 */
public class RedisKey {

	/**第三方请求token**/
	public static final String HELLO_SAAS_TOKEN = "hello:saas:token";
	/**用户信息**/
	public static final String HELLO_USER = "hello:user:index:%1$s";
	/**用户辅助信息**/
	public static final String HELLO_USER_INFO = "hello:user:info:%1$s";
	/**用户登录之后 获取的用户账户信息**/
	public static final String HELLO_USER_ACCOUNTINFO = "hello:user:useraccountinfo:%1$s";
	/**用户登录之后 获取的用户账户信息**/
	public static final String HELLO_USER_ACCINFO = "hello:user:useraccinfo:%1$s";
	/**用户注册下发短信时的sessiongid(注册接口需要回传)**/
	public static final String HELLO_USER_REG_SESSIONGID = "hello:user:reg:sessiongid:%1$s";
	/**用户更改密码下发短信时的sessiongid(修改密码接口需要回传)**/
	public static final String HELLO_USER_Resetpwd_SESSIONGID = "hello:user:resetpwd:sessiongid:%1$s";
	/**用户设置交易密码发短信时的sessiongid(设置交易密码接口需要回传)**/
	public static final String HELLO_USER_Settradepwd_SESSIONGID = "hello:user:settradepwd:sessiongid:%1$s";
	/**用户登录之后 获取的用户信息**/
	public static final String HELLO_USER_USERINFO = "hello:user:userinfo:%1$s";
	/**放款成功之后 获取订单信息**/
	public static final String HELLO_ORDER_ORDERINFO = "hello:order:orderinfo:%1$s";
	/**用户查看支持银行卡列表**/
	public static final String HELL0_BANK_SUPPORTBANKS = "hello:bank:supportbanks";
	/**费率信息**/
	public static final String HELL0_LOAN_RATE = "hello:loan:rate:%1$s:%2$s";
	/**借款id 的两个key**/
	public static final String HELL0_LOAN_ID_KEY1 = "hello:loan:key1:%1$s";
	public static final String HELL0_LOAN_ID_KEY2 = "hello:loan:key2:%1$s";
	/**用户历史账单列表**/
	public static final String HELLO_LOAN_LOANHISTORYLIST = "hello:loan:loanhistorylist:%1$s";
	/**用户在借账单列表**/
	public static final String HELLO_LOAN_LOANLIST = "hello:loan:loanlist:%1$s";
	/**用户全部账单列表**/
	public static final String HELLO_LOAN_ALLLOANLIST = "hello:loan:allloanlist:%1$s";
	/**第三方请求token**/
	public static final String HELLO_SAAS_ONLINENUM = "hello:saas:onlinenum";
	public static final String HELLO_APPLY_LOAN_SUMLOANAMOUT_LIMIT = "hello:loan:limit";
	/**用户登录之后 获取的用户登陆信息**/
	public static final String HELLO_USER_USERLOGIN = "hello:user:userlogin:%1$s";
	/**用户是否为理财人 **/
	public static final String HELLO_USER_MOBILEXS = "hello:user:mobilexs:%1$s";
	/**用户优惠券列表**/
	public static final String HELLO_USER_VOUCHER = "hello:user:voucher:%1$s";
	/**系统配置表 hash key**/
	public static final String HELLO_SYSTEM_CONFIG = "hello:system:config";
	/**防止重复绑卡token**/
	public static final String HELLO_BANKCARD_VERIFY = "hello:bankcard:verify:%1$s";
	
	/**
	 * 
	 * @Title: keyFormatter 
	 * @Description:  格式化rediskey
	 * @param key 被格式化key
	 * @param element 格式化value值
	 * @return  String 格式化后key
	 * @date 2017年8月8日 下午5:55:23
	 * @author liyanchao
	 */
	public static String keyFormatter(String key, String element) {
		String regex = "(\\$\\{).*(\\})";
		return key.replaceAll(regex, element);
	}

	/**
	 * 
	 * @Title: formatter 
	 * @Description:  格式化rediskey
	 * @param key 被格式化key
	 * @param element 格式化value值
	 * @return String
	 * @date 2017年8月8日 下午5:56:08
	 * @author liyanchao
	 */
	public static String formatter(String key, String... element) {
		if (0 != element.length && null != element) {
			return String.format(key, (Object[]) element);
		}
		return key;
	}
}
